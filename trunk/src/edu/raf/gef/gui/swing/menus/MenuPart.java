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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JMenuItem;

/**
 * Usually what you see separated between two Separators in a menu.
 * 
 * @since May 28. 2008.
 * @version 1.0
 * @author Srecko Toroman (Срећко Тороман) <a
 *         href="mailto:sreckotoroman@gmail.com">sreckotoroman@gmail.com</a>
 */
public class MenuPart {
	private int count;
	private MenuPartContainer parent;
	private Map<Action, JMenuItem> actions;
	private String partId;

	public MenuPart(MenuPartContainer parent, String partId) {
		this.parent = parent;
		this.partId = partId;
		this.actions = new LinkedHashMap<Action, JMenuItem>();
	}

	public String getPartId() {
		return partId;
	}

	public void add(Action a) {
		JMenuItem item = parent.getMenu().insert(a, parent.getChildPosition(this) + count++);
		this.actions.put(a, item);
	}

	public void remove(Action a) {
		if (!this.actions.containsKey(a))
			throw new IllegalArgumentException("Action not present!");
		parent.getMenu().remove(this.actions.get(a));
		this.actions.remove(a);
		count--;
	}

	public void add(MenuPartContainer submenu) {
		parent.getMenu().insert(submenu.getMenu(), parent.getChildPosition(this) + count++);
	}

	public Collection<Action> getActions() {
		return Collections.unmodifiableCollection(this.actions.keySet());
	}

	public int getCount() {
		return count;
	}
}
