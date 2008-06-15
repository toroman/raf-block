package edu.raf.gef.editor.model.events;

import java.util.EventObject;

import edu.raf.gef.editor.model.object.Drawable;

/**
 * Or removed
 * 
 */
public class DrawableAddedEvent extends EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5513110266068721363L;
	private boolean added;

	public DrawableAddedEvent(Drawable source, boolean added) {
		super(source);
		this.added = true;
	}

	public boolean isAdded() {
		return added;
	}

	@Override
	public Drawable getSource() {
		return (Drawable) super.getSource();
	}
}