package edu.raf.gef.plugin;

import javax.swing.Action;

/**
 * All plugins should implement this interface if they want to be able to
 * controll which actions can be applied to them or not.
 */
public interface ActionSensitivePlugin {
	/**
	 * Tells whether this plugin works with the action or not.
	 * 
	 * @param action
	 * @return
	 */
	public boolean worksWith(Action action);
}
