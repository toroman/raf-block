package edu.raf.gef.editor.model.object;

import java.awt.geom.Point2D;

public interface Focusable extends Drawable {
	public Drawable getDrawableAt (Point2D point);
	public boolean isFocused();
	public boolean setFocused(boolean focused);
	
	public static final String FOCUSED_PROPERTY = "focused";
}
