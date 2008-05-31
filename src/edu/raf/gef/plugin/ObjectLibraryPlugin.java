package edu.raf.gef.plugin;

import java.util.Collection;

import edu.raf.gef.editor.model.object.Drawable;

/**
 * This plugin contributes with a Diagram editor.
 */
public interface ObjectLibraryPlugin extends AbstractPlugin {
	/**
	 * Which objects this library contains.
	 * 
	 * @return Classes for objects that can be created.
	 */
	public Collection<Class<? extends Drawable>> getDrawableClasses();
}
