package edu.raf.gef.plugin;

import java.util.Collection;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.model.object.DrawableElement;

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

	/**
	 * Which objects can be added to this diagram.
	 * 
	 * @return Classes for objects that can be created.
	 */
	public Collection<Class<? extends DrawableElement>> getDrawableClasses();

	public String[] getSupportedFileExtensions();
}
