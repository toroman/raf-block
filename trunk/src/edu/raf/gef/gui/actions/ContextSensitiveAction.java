package edu.raf.gef.gui.actions;

public interface ContextSensitiveAction {
	/**
	 * These actions know on which context they can be applied.
	 * 
	 * @param plugin
	 *            Ask about the plugin
	 * @return false if this action can't be applied while this plugin is active
	 */
	public boolean worksOn(Object focused);
}
