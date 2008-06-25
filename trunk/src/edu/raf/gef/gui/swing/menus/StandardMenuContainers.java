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

/**
 * There menus *should* be created by the framework and plugins are free to
 * reuse them.
 * 
 * @since May 28. 2008.
 * @version 1.0
 * @author Srecko Toroman (Срећко Тороман) <a
 *         href="mailto:sreckotoroman@gmail.com">sreckotoroman@gmail.com</a>
 */
public interface StandardMenuContainers {
	/**
	 * File menu.
	 * <p>
	 * Usually parts that reside here are FILE_CREATION_PART,
	 * FILE_PERSISTING_PART. These parts should be bound to the FILE container
	 * (because the name begins with FILE, but some other like DIAGRAM_MRU_PART
	 * shouldn't be bound because we aren't sure where it will be placed).
	 */
	public String FILE = "c_file";

	/**
	 * File->New menu (usually, but not necessarily contained in FILE_CREATION
	 * part, it will have parts like "DIAGRAM_CREATION_PART". This container
	 * will probably be a member of FILE_CREATION_PART, but that depends on the
	 * GUI configuration
	 */
	public String FILE_NEW = "c_file_new";

	/**
	 * View operations - like best fit
	 */
	public String VIEW = "c_view";
	/**
	 * Window standard menu. Contains Standard parts "WINDOW_ARRANGEMENT_PART"
	 * and maybe more.
	 */
	public String WINDOW = "c_window";
		
}
