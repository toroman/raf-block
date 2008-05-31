package edu.raf.gef.editor.model.object;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Drawable element.
 * 
 */
public interface Drawable {
	public void paint(Graphics2D g);
	public boolean isUnderLocation (Point2D location);
	public Rectangle2D getBoundingRectangle();
	public void onClick (MouseEvent e);
}
