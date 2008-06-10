package edu.raf.gef.gui.actions;

import javax.swing.Action;

/**
 * Action for which the working can be reconfigured.
 * 
 */
public interface OverridableAction {
	public void setOverridenBy(Action action);
}
