package edu.raf.gef.editor.model.object.impl;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.Vector;

import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.editor.model.object.constraint.ControlPointConstraint;
import edu.raf.gef.util.MathHelper;

public abstract class RectangularObject extends DraggableDiagramObject {

	private Dimension2D minDimension, maxDimension, prefDimension;
	private double x, y, width, height;

	private Point2D draggingOffset;

	/*
	 * 0-1-2 | | 7 3 | | 6-5-4
	 */
	private Vector<ControlPoint> controlPoints;

	protected static int NORTH_MASK = 1;
	protected static int EAST_MASK = 2;
	protected static int SOUTH_MASK = 4;
	protected static int WEST_MASK = 8;

	private int getRoleOfIndex(int index) {
		int res = 0;
		if (index <= 2)
			res = res | NORTH_MASK;
		if (index >= 2 && index <= 4)
			res = res | EAST_MASK;
		if (index >= 4 && index <= 6)
			res = res | SOUTH_MASK;
		if (index >= 6 || index == 0)
			res = res | WEST_MASK;
		return res;
	}

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
		initControlPoints();
		updateControlPointLocations();
		setControlPointConstraints();
	}

	protected void initControlPoints() {
		controlPoints = new Vector<ControlPoint>();
		for (int i = 0; i < 8; i++)
			controlPoints.add(new ResizeControlPoint(this, null));
	}

	private void setControlPointConstraints() {
		final RectangularObject rect = this;
		ControlPointConstraint widthMinEast = new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				double minx = rect.getX() + rect.getMinDimension().getWidth();
				if (oldLocation.getX() < minx)
					return new Point2D.Double(minx, oldLocation.getY());
				return oldLocation;
			}
		};
		ControlPointConstraint widthMinWest = new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				double maxx = rect.getX() + rect.getWidth() - rect.getMinDimension().getWidth();
				if (oldLocation.getX() > maxx)
					return new Point2D.Double(maxx, oldLocation.getY());
				return oldLocation;
			}
		};
		ControlPointConstraint heightMinSouth = new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				double miny = rect.getY() + rect.getMinDimension().getHeight();
				if (oldLocation.getY() < miny)
					return new Point2D.Double(oldLocation.getX(), miny);
				return oldLocation;
			}
		};
		ControlPointConstraint heightMinNorth = new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				double maxy = rect.getY() + rect.getHeight() - rect.getMinDimension().getHeight();
				if (oldLocation.getY() > maxy)
					return new Point2D.Double(oldLocation.getX(), maxy);
				return oldLocation;
			}
		};

		for (int index = 0; index < 8; index++) {
			boolean north = (getRoleOfIndex(index) & NORTH_MASK) != 0;
			boolean east = (getRoleOfIndex(index) & EAST_MASK) != 0;
			boolean south = (getRoleOfIndex(index) & SOUTH_MASK) != 0;
			boolean west = (getRoleOfIndex(index) & WEST_MASK) != 0;
			ControlPoint cp = controlPoints.get(index);
			if (north)
				cp.addConstraint(heightMinNorth);
			if (east)
				cp.addConstraint(widthMinEast);
			if (south)
				cp.addConstraint(heightMinSouth);
			if (west)
				cp.addConstraint(widthMinWest);
		}

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
		draggingOffset = new Point2D.Double(point.getX() - this.getX(), point.getY() - this.getY());
		setChanged();
		notifyObservers();
		clearChanged();
	}

	@Override
	public Drawable getDrawableUnderLocation(Point2D point) {
		Drawable drawable = null;
		for (int i = 0; i < 8; i++) {
			drawable = controlPoints.get(i).getDrawableUnderLocation(point);
			if (drawable != null)
				return drawable;
		}
		if (MathHelper.isBetween(point.getX(), getX(), getX() + getWidth())
				&& MathHelper.isBetween(point.getY(), getY(), getY() + getHeight()))
			return this;
		return null;
	}

	private void resizePointDragged(ControlPoint controlPoint) {
		int index = controlPoints.indexOf(controlPoint);
		boolean north = (getRoleOfIndex(index) & NORTH_MASK) != 0;
		boolean east = (getRoleOfIndex(index) & EAST_MASK) != 0;
		boolean south = (getRoleOfIndex(index) & SOUTH_MASK) != 0;
		boolean west = (getRoleOfIndex(index) & WEST_MASK) != 0;
		if (north) {
			setHeight(getY() + getHeight() - controlPoint.getLocation().getY());
			setY(controlPoint.getLocation().getY());
		}
		if (west) {
			setWidth(getX() + getWidth() - controlPoint.getLocation().getX());
			setX(controlPoint.getLocation().getX());
		}
		if (east)
			setWidth(controlPoint.getLocation().getX() - getX());
		if (south)
			setHeight(controlPoint.getLocation().getY() - getY());
	}

	@Override
	public Point2D onControlPointDragEnded(ControlPoint controlPoint, Point2D location) {
		controlPoint.setLocation(location);
		if (controlPoint instanceof ResizeControlPoint)
			resizePointDragged(controlPoint);
		updateControlPointLocations();
		return controlPoint.getLocation();
	}

	@Override
	public Point2D onControlPointDragStarted(ControlPoint controlPoint, Point2D location) {
		controlPoint.setLocation(location);
		if (controlPoint instanceof ResizeControlPoint)
			resizePointDragged(controlPoint);
		updateControlPointLocations();
		return controlPoint.getLocation();
	}

	@Override
	public Point2D onControlPointDragged(ControlPoint controlPoint, Point2D location) {
		controlPoint.setLocation(location);
		if (controlPoint instanceof ResizeControlPoint)
			resizePointDragged(controlPoint);
		updateControlPointLocations();
		return controlPoint.getLocation();
	}

	private void updateResizePoint(int index) {
		boolean north = (getRoleOfIndex(index) & NORTH_MASK) != 0;
		boolean east = (getRoleOfIndex(index) & EAST_MASK) != 0;
		boolean south = (getRoleOfIndex(index) & SOUTH_MASK) != 0;
		boolean west = (getRoleOfIndex(index) & WEST_MASK) != 0;
		ControlPoint cp = controlPoints.get(index);
		if (west)
			cp.setX(getX());
		else if (east)
			cp.setX(getX() + getWidth());
		else
			cp.setX(getX() + getWidth() / 2);
		if (north)
			cp.setY(getY());
		else if (south)
			cp.setY(getY() + getHeight());
		else
			cp.setY(getY() + getHeight() / 2);
	}

	protected void updateControlPointLocations() {
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
		paintChildren(g);
	}

	protected void paintChildren(Graphics2D g) {
		
	}

	protected abstract void paintRectangular(Graphics2D g);

	@Override
	public Point2D getLocation() {
		return new Point2D.Double(x, y);
	}
	
	@Override
	public void onClick(MouseEvent e, Point2D userSpaceLocation) {
		
	}
	
	@Override
	public void onControlPointClicked(ControlPoint controlPoint, MouseEvent e, Point2D userSpaceLocation) {
		
	}
}
