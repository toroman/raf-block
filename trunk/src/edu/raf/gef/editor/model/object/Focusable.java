package edu.raf.gef.editor.model.object;


public interface Focusable extends Drawable {
	public boolean isFocused();
	public boolean setFocused(boolean focused);
	public static final String FOCUSED_PROPERTY = "focused";
}
