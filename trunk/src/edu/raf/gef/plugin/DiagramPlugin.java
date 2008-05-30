package edu.raf.gef.plugin;

import edu.raf.gef.editor.GefDiagram;

/**
 * This plugin contributes with a Diagram editor.
 */
public interface DiagramPlugin extends AbstractPlugin {
	/**
	 * Create blank editor.
	 * 
	 * @return Editor to be displayed.
	 */
	public Class<? extends GefDiagram> getDiagramClass();

	public String[] getSupportedFileExtensions();
}
