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
 * Some standard menu parts that can be reused by plugins (framework should
 * create them from xml files or manually).
 * <p>
 * Note to developer: since parts are nothing more but a Separator separated
 * part of a JMenu, thereby there is nothing to localize here, nor pictures to
 * be set. Containers should be localized and actions added to parts.
 * 
 * @since May 28. 2008.
 * @version 1.0
 * @author Srecko Toroman (Срећко Тороман) <a
 *         href="mailto:sreckotoroman@gmail.com">sreckotoroman@gmail.com</a>
 */
public interface StandardMenuParts {
	/** All "creating" actions/containers like New, Open, Import etc */
	String FILE_CREATION_PART = "p_file_creation";

	/** All "persisting" actions/containers like Save, Save as, Export etc */
	String FILE_PERSISTING_PART = "p_file_persisting";

	String FILE_EXIT_PART = "p_file_exit";

	/**
	 * Part that contains all Actions that create Diagrams.
	 */
	String NEW_DIAGRAM_PART = "p_new_diagram";

	/**
	 * Most recently used list.
	 * <p>
	 * This part may be different than others (with limited size). Suitable for
	 * implementation in MainFrame.
	 */
	String MRU_PART = "p_mru";

	/** All Window operations - cascade, maximize etc */
	String WINDOW_ARRANGEMENT_PART = "p_window_arrangement";

	String ABOUT = "p_about";

	String HELP_CONTENTS = "p_help_contents";

	String WINDOW_LOOK_AND_FEEL = "p_look_and_feel";

	String PLUGIN_MANAGER = "p_plugin_manager";

}
