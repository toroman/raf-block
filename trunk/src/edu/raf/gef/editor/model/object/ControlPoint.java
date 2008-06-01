package edu.raf.gef.editor.model.object;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import edu.raf.gef.editor.model.object.constraint.ControlPointConstraint;

public abstract class ControlPoint implements Draggable {

	private ControlPointContainer parent;
	private Point2D location = new Point2D.Double(0, 0);
	private List <ControlPointConstraint> constraints;

	public ControlPoint(ControlPointContainer parent, Point2D initLocation) {
		this.parent = parent;
		setLocation(initLocation);
		constraints = new LinkedList <ControlPointConstraint>();
	}
	
	public void addConstraint (ControlPointConstraint constraint) {
		constraints.add(constraint);
	}
	
	public Point2D afterAllConstraints (Point2D oldLocation) {
		Point2D newLocation = (Point2D)oldLocation.clone();
		for (ControlPointConstraint con: constraints)
			newLocation = con.updateLocation(newLocation);
		return newLocation;
	}
	
	@Override
	public void dragEndedAt(Point2D point) {
		location = parent.onControlPointDragEnded(this, afterAllConstraints(point));
	}

	@Override
	public void dragTo(Point2D point) {
		location = parent.onControlPointDragged(this, afterAllConstraints(point));
	}

	@Override
	public void dragStartedAt(Point2D point) {
		location = parent.onControlPointDragStarted(this, afterAllConstraints(point));
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
