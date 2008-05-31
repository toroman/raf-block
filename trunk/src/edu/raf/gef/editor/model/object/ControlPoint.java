package edu.raf.gef.editor.model.object;

import java.awt.geom.Point2D;

public abstract class ControlPoint implements Draggable {

	private ControlPointContainer parent;
	private Point2D location = new Point2D.Double(0, 0);

	public ControlPoint(ControlPointContainer parent, Point2D initLocation) {
		this.parent = parent;
		setLocation(initLocation);
	}
	
	@Override
	public void dragEndedAt(Point2D point) {
		location = parent.onControlPointDragEnded(this, point);
	}

	@Override
	public void dragTo(Point2D point) {
		location = parent.onControlPointDragged(this, point);
	}

	@Override
	public void dragStartedAt(Point2D point) {
		location = parent.onControlPointDragStarted(null, point);
	}

	public void setLocation(Point2D newLocation) {
		if (newLocation != null)
			this.location = newLocation;
		else
			location = new Point2D.Double (0, 0);
	}

	public Point2D getLocation() {
		return location;
	}
	
	public void setX(double x) {
		location.setLocation(x, location.getY());
	}
	
	public void setY(double y) {
		location.setLocation(location.getX(), y);
	}

	public ControlPointContainer getParent() {
		return parent;
	}
}
