package edu.raf.gef.app.framework;

import java.awt.Component;

/**
 * This class represents the "editor frame", base of all "main" views.
 * <p>
 * Each plugin must have a "public static Resources getResources()" method.
 */
public interface EditorPlugin {
	public Component createEditor();
}
