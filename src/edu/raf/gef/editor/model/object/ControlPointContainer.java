package edu.raf.gef.editor.model.object;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import edu.raf.gef.editor.model.object.impl.ControlPoint;

/**
 * Whenever anything happens to a ContolPoint, its ControlPointContainer is
 * notified. It should, then, modify the location of the control point and
 * return a new one.
 * 
 * @author Bocete
 * 
 */
public interface ControlPointContainer {

	public Point2D onControlPointDragStarted(ControlPoint controlPoint, Point2D location);

	public Point2D onControlPointDragged(ControlPoint controlPoint, Point2D location);

	public Point2D onControlPointDragEnded(ControlPoint controlPoint, Point2D location);
	
	public void onControlPointClicked (ControlPoint controlPoint, MouseEvent e, Point2D userSpaceLocation);
}
