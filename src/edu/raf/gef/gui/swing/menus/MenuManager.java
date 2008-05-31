/*
RAF GEF - Graphical Editor Framework
Copyright (C) <2007>  Srecko Toroman, Ivan Bocic

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.raf.gef.gui.swing.menus;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.swing.Action;
import javax.swing.JMenuBar;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import edu.raf.gef.app.exceptions.GefRuntimeException;

/**
 * Menu creator with support for standardized menus and menu parts.
 * 
 * @since May 28. 2008.
 * @version 1.0b
 * @author Srecko Toroman (Срећко Тороман) <a
 *         href="mailto:sreckotoroman@gmail.com">sreckotoroman@gmail.com</a>
 */
public class MenuManager {
	public static final String ROOT = "";

	private JMenuBar menubar;

	/**
	 * Parts hold actions and submenus (which are containers)
	 */
	private final Map<String, MenuPart> parts;

	/**
	 * Containers hold parts
	 */
	private final HashMap<String, MenuPartContainer> containers;

	/**
	 * Named actions, like standard actions (Cut copy paste)
	 */
	private final Map<String, Action> namedActions = new HashMap<String, Action>();

	public MenuManager() {
		menubar = new JMenuBar();
		parts = new HashMap<String, MenuPart>();
		containers = new HashMap<String, MenuPartContainer>();
	}

	/**
	 * 
	 * @param group
	 * @param action
	 */
	public void addAction(String partId, Action action) {
		if (!parts.containsKey(partId))
			throw new GefRuntimeException("Invalid menu part id!");
		MenuPart part = parts.get(partId);
		part.add(action);
	}

	public void addAction(String partId, Action action, String actionId) {
		if (!parts.containsKey(partId))
			throw new GefRuntimeException("Invalid menu part id!");
		MenuPart part = parts.get(partId);
		namedActions.put(actionId, action);
	}

	public void overrideAction(String actionId, ActionListener listener) {
		
	}

	public void removeAction(String partId, Action action) {
		if (!parts.containsKey(partId))
			throw new GefRuntimeException("Invalid menu part id!");
		MenuPart part = parts.get(partId);
		part.remove(action);
	}

	public MenuPart createPart(String parentContainerId, String childPartId) {
		if (!containers.containsKey(parentContainerId))
			throw new GefRuntimeException("Invalid menu part id!");
		if (parts.containsKey(childPartId))
			return parts.get(childPartId);
		MenuPartContainer container = containers.get(parentContainerId);
		MenuPart part = new MenuPart(container, childPartId);
		container.add(part);
		this.parts.put(childPartId, part);
		return part;
	}

	public MenuPart getPart(String partId) {
		return parts.get(partId);
	}

	public MenuPartContainer createContainer(String parentPartId, String childContainerId) {
		if (parentPartId == ROOT) {
			return createRootContainer(childContainerId);
		}
		MenuPart parent = parts.get(parentPartId);
		if (parent == null)
			throw new GefRuntimeException("No such parent part.");

		MenuPartContainer child = new MenuPartContainer(childContainerId);
		containers.put(childContainerId, child);
		parent.add(child);
		return child;
	}

	public MenuPartContainer createContainer(MenuPart parentPart, String childContainerId) {
		if (parentPart == null) {
			return createRootContainer(childContainerId);
		}
		MenuPartContainer child = new MenuPartContainer(childContainerId);
		containers.put(childContainerId, child);
		parentPart.add(child);
		return child;
	}

	private MenuPartContainer createRootContainer(String childContainerId) {
		MenuPartContainer container = new MenuPartContainer(childContainerId);
		this.containers.put(childContainerId, container);
		menubar.add(container.getMenu());
		return container;
	}

	public void removeMenu(MenuPartContainer menu) {
		throw new NotImplementedException();
	}

	public Iterator<Action> getActions() {
		return new ActionsIterator();
	}

	public JMenuBar getMenubar() {
		return menubar;
	}

	private class ActionsIterator implements Iterator<Action> {
		private Iterator<MenuPart> partIterator;
		private MenuPart currentPart;
		private Iterator<Action> actionIterator;

		private Action nextAction;

		public ActionsIterator() {
			partIterator = MenuManager.this.parts.values().iterator();
			if (partIterator.hasNext())
				currentPart = partIterator.next();
			findNext();
		}

		public boolean hasNext() {
			return nextAction != null;
		}

		public Action next() {
			if (nextAction == null)
				throw new NoSuchElementException();
			Action ret = nextAction;
			findNext();
			return ret;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		private void findNext() {
			if (currentPart == null) {
				nextAction = null;
				return;
			}

			if (actionIterator == null) {
				actionIterator = currentPart.getActions().iterator();
			}

			if (actionIterator.hasNext()) {
				nextAction = actionIterator.next();
			} else {
				currentPart = partIterator.hasNext() ? partIterator.next() : null;
				actionIterator = null;
				findNext();
			}
		}
	}

}
