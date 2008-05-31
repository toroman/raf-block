package edu.raf.gef.editor.model.object;

import java.awt.geom.Point2D;

public interface Draggable extends Drawable {
	public void dragStartedAt (Point2D point);
	public void dragTo (Point2D point);
	public void dragEndedAt (Point2D point);
	public void setLocation (Point2D point);	
	public Point2D getLocation ();
}
