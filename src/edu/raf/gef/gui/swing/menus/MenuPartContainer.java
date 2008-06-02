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

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Action;
import javax.swing.JMenu;

/**
 * Wrapper for JMenu, has unique string id, contains MenuParts.
 * 
 * @since May 28. 2008.
 * @version 1.0
 * @author Srecko Toroman (Срећко Тороман) <a
 *         href="mailto:sreckotoroman@gmail.com">sreckotoroman@gmail.com</a>
 */
public class MenuPartContainer {
	private final JMenu menu;
	/**
	 * Maps parts to their positions
	 */
	private ArrayList<MenuPart> parts = new ArrayList<MenuPart>();
	private String containerId;

	public MenuPartContainer(String childContainerId) {
		this.menu = new JMenu();
		this.containerId = childContainerId;
	}

	public String getContainerId() {
		return containerId;
	}

	public JMenu getMenu() {
		return menu;
	}

	void createMenu() {
		menu.removeAll();
		int position = 0;
		for (int i = 0; i < parts.size(); i++) {
			if (i > 0) {
				menu.addSeparator();
				++position;
			}
			MenuPart separatedPart = parts.get(i);
			Collection<Action> actions = separatedPart.getActions();
			if (actions.size() > 0) {
				int insertPosition = getChildPosition(separatedPart);
				for (Action a : actions) {
					this.menu.insert(a, insertPosition++);
				}
			}
		}
	}

	void add(MenuPart separatedPart) {
		if (parts.size() > 0)
			menu.addSeparator();
		parts.add(separatedPart);
		Collection<Action> actions = separatedPart.getActions();
		if (actions.size() > 0) {
			int insertPosition = getChildPosition(separatedPart);
			for (Action a : actions) {
				this.menu.insert(a, insertPosition++);
			}
		}
	}

	int getChildPosition(MenuPart child) {
		int position = -1;		
		for (MenuPart part : parts) {
			++position; // because of separator

			if (part == child)
				return position;
			position += part.getCount();
		}
		return -1;
	}

	ArrayList<MenuPart> getParts() {
		return parts;
	}
}
