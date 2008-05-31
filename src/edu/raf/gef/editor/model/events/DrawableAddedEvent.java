package edu.raf.gef.editor.model.events;

import java.util.EventObject;

import edu.raf.gef.editor.model.object.Drawable;

public class DrawableAddedEvent extends EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5513110266068721363L;

	public DrawableAddedEvent(Drawable source) {
		super(source);
	}
}