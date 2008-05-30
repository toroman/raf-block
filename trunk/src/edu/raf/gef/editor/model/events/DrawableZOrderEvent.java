package edu.raf.gef.editor.model.events;

import java.util.EventObject;

import edu.raf.gef.editor.model.object.DrawableElement;

public class DrawableZOrderEvent extends EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3228215230833579941L;

	public DrawableZOrderEvent(DrawableElement element) {
		super(element);
	}
}