package edu.raf.gef.gui;

/**
 * All objects (plugins) that know how to dynamicaly change some actions, update
 * them (hide, show, disable) and stuff.
 */
public interface ActionContextController {
	/**
	 * Called after deactivating previous controller.
	 * 
	 * @param main
	 *            Get the actions, statusbar, workspace etc
	 */
	public void onActivated(MainFrame main, ActionContextController previousContext);

	/**
	 * Called prior to activating next action controller.
	 * 
	 * @param main
	 */
	public void onDeactivated(MainFrame main, ActionContextController nextContext);
}
