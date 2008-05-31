package edu.raf.gef.editor.model.object.impl;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.Vector;

import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.ControlPoint;
import edu.raf.gef.editor.model.object.Drawable;

public abstract class RectangularObject extends DraggableDiagramObject {

	private Dimension2D minDimension, maxDimension, prefDimension;
	private double x, y, width, height;
	
	private Point2D draggingOffset;
	
	/*
	 * 0-1-2
	 * |   |
	 * 7   3
	 * |   |
	 * 6-5-4
	 */
	private Vector <ControlPoint> controlPoints;
	
	public RectangularObject(DiagramModel model) {
		super(model);
		minDimension = getMinDimension();
		maxDimension = getMaxDimension();
		prefDimension = getPrefDimension();
		draggingOffset = null;
		if (prefDimension == null) {
			width = 100;
			height = 100;
		} else {
			width = prefDimension.getWidth();
			height = prefDimension.getHeight();
		}
		controlPoints = new Vector<ControlPoint>();
		for (int i = 0; i < 8; i++)
			controlPoints.add(new ResizeControlPoint(this, null));
	}
	
	@Override
	public void dragEndedAt(Point2D point) {
		draggingOffset = null;
		setChanged();
		notifyObservers();
		clearChanged();
	}

	@Override
	public void dragTo(Point2D point) {
		setX(point.getX() - draggingOffset.getX());
		setY(point.getY() - draggingOffset.getY());
		updateControlPointLocations();
		setChanged();
		notifyObservers();
		clearChanged();
	}

	@Override
	public void dragStartedAt(Point2D point) {
		draggingOffset = new Point2D.Double (point.getX() - this.getX(), point.getY() - this.getY());
		setChanged();
		notifyObservers();
		clearChanged();
	}

	@Override
	public Drawable getDrawableAt(Point2D point) {
		return null;
	}

	@Override
	public Point2D onControlPointDragEnded(ControlPoint controlPoint, Point2D location) {
		controlPoint.setLocation(location);
		updateControlPointLocations();
		return controlPoint.getLocation();
	}

	@Override
	public Point2D onControlPointDragStarted(ControlPoint controlPoint, Point2D location) {
		controlPoint.setLocation(location);
		updateControlPointLocations();
		return controlPoint.getLocation();
	}

	@Override
	public Point2D onControlPointDragged(ControlPoint controlPoint, Point2D location) {
		controlPoint.setLocation(location);
		updateControlPointLocations();
		return controlPoint.getLocation();
	}
	
	private void updateResizePoint (int index) {
		double newX = getX();
		double newY = getY();
		switch (index){
		case 1:
		case 5:
			newX += getWidth()/2;
			break;
		case 2:
		case 3:
		case 4:
			newX += getWidth();
			break;			
		}
		switch (index){
		case 3:
		case 7:
			newY += getHeight()/2;
			break;
		case 4:
		case 5:
		case 6:
			newY += getHeight();
			break;			
		}
		ControlPoint cp = controlPoints.get(index);		
		cp.setX(newX);
		cp.setY(newY);
	}
	
	private void updateControlPointLocations() {
		for (int i = 0; i < 8; i++)
			updateResizePoint(i);
	}

	public Dimension2D getMinDimension() {
		return minDimension;
	}

	public void setMinDimension(Dimension2D minDimension) {
		this.minDimension = minDimension;
	}

	public Dimension2D getMaxDimension() {
		return maxDimension;
	}

	public void setMaxDimension(Dimension2D maxDimension) {
		this.maxDimension = maxDimension;
	}

	public Dimension2D getPrefDimension() {
		return prefDimension;
	}

	public void setPrefDimension(Dimension2D prefDimension) {
		this.prefDimension = prefDimension;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
		setChanged();
		notifyObservers();
		clearChanged();
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
		setChanged();
		notifyObservers();
		clearChanged();
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
		setChanged();
		notifyObservers();
		clearChanged();
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
		setChanged();
		notifyObservers();
		clearChanged();
	}
	
	@Override
	public void setLocation(Point2D point) {
		setX(point.getX());
		setY(point.getY());
		updateControlPointLocations();
		setChanged();
		notifyObservers();
		clearChanged();
	}
	
	@Override
	public final void paint(Graphics2D g) {
		paintRectangular(g);
		for (int i = 0; i < 8; i++)
			controlPoints.get(i).paint(g);
	}
	
	protected abstract void paintRectangular(Graphics2D g);

	@Override
	public Point2D getLocation() {
		return new Point2D.Double (x, y);
	}
}
