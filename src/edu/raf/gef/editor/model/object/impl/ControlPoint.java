package edu.raf.gef.editor.model.object.impl;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.ControlPointContainer;
import edu.raf.gef.editor.model.object.Draggable;
import edu.raf.gef.editor.model.object.constraint.ControlPointConstraint;
import edu.raf.gef.util.GeomHelper;

public abstract class ControlPoint implements Draggable, Serializable {

	private ControlPointContainer parent;
	private Point2D location = new Point2D.Double(0, 0);
	protected List<ControlPointConstraint> constraints;
	private Point2D dragOffset = null;

	public ControlPoint(ControlPointContainer parent, Point2D initLocation) {
		this.parent = parent;
		if (initLocation != null)
			setLocation(initLocation);
		else
			setLocation(new Point2D.Double(0, 0));
		constraints = new LinkedList<ControlPointConstraint>();
	}

	public void addConstraint(ControlPointConstraint constraint) {
		constraints.add(constraint);
	}

	public Point2D afterAllConstraints(Point2D oldLocation) {
		Point2D newLocation = (Point2D) oldLocation.clone();
		for (ControlPointConstraint con : constraints)
			newLocation = con.updateLocation(newLocation);
		return newLocation;
	}

	@Override
	public void setParent(DiagramModel diagramModel) {

	}

	@Override
	public void dragEndedAt(Point2D point) {
		Point2D shouldBeLocation = GeomHelper.substractPoints(point, dragOffset);
		location = parent.onControlPointDragEnded(this, afterAllConstraints(shouldBeLocation));
	}

	@Override
	public void dragTo(Point2D point) {
		Point2D shouldBeLocation = GeomHelper.substractPoints(point, dragOffset);
		location = parent.onControlPointDragged(this, afterAllConstraints(shouldBeLocation));
	}

	@Override
	public void dragStartedAt(Point2D point) {
		dragOffset = GeomHelper.substractPoints(point, location);
		Point2D shouldBeLocation = GeomHelper.substractPoints(point, dragOffset);
		location = parent.onControlPointDragStarted(this, afterAllConstraints(shouldBeLocation));
	}

	public void setLocation(Point2D newLocation) {
		if (newLocation != null)
			this.location = newLocation;
		else
			location = new Point2D.Double(0, 0);
	}

	public void setLocation(double x, double y) {
		location = new Point2D.Double(x, y);
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

	public double getX() {
		return location.getX();
	}

	public double getY() {
		return location.getY();
	}

	public ControlPointContainer getParent() {
		return parent;
	}

	@Override
	public void onClick(MouseEvent e, Point2D userSpaceLocation) {
		parent.onControlPointClicked(this, e, userSpaceLocation);
	}
	
	public void setDraggingOffset(int i, int j) {
		this.dragOffset = new Point2D.Double(i, j);
	}
}
