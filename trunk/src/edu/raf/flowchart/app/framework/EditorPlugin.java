package edu.raf.flowchart.app.framework;

import javax.swing.JPanel;

/**
 * This class represents the "editor frame", base of all "main" views.
 * <p>
 * Each plugin must have a "public static Resources getResources()" method.
 */
public interface EditorPlugin {
	public JPanel getPanel();
}
