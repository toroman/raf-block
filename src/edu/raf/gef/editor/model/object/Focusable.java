package edu.raf.gef.editor.model.object;


public interface Focusable extends Drawable {
	public boolean isFocused();
	public boolean setFocused(boolean focused);
}
