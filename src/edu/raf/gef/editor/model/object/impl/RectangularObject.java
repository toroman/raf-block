package edu.raf.gef.editor.model.object.impl;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;

import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.AnchorPointContainer;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.editor.model.object.constraint.ControlPointConstraint;
import edu.raf.gef.util.GeomHelper;
import edu.raf.gef.util.MathHelper;

public abstract class RectangularObject extends DraggableDiagramObject implements
		AnchorPointContainer {

	private Dimension2D minDimension, maxDimension;
	private double x, y, width, height;

	private Point2D draggingOffset;

	/*
	 * 0-1-2 | | 7 3 | | 6-5-4
	 */
	protected Vector<ResizeControlPoint> resizeControlPoints;

	protected static int NORTH_MASK = 1;
	protected static int EAST_MASK = 2;
	protected static int SOUTH_MASK = 4;
	protected static int WEST_MASK = 8;

	protected int getRoleOfIndex(int index) {
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

		sourceAnchors = new LinkedList<SourceAnchorPoint>();
		destinationAnchors = new LinkedList<DestinationAnchorPoint>();

		minDimension = getMinDimension();
		maxDimension = getMaxDimension();
		Dimension2D prefDimension = getPrefferedDimension();
		draggingOffset = null;
		if (prefDimension == null)
			prefDimension = minDimension;
		if (prefDimension == null)
			prefDimension = new Dimension(100, 100);

		width = prefDimension.getWidth();
		height = prefDimension.getHeight();
		initControlPoints();
		updateControlPointLocations();
		setControlPointConstraints();
	}

	@Override
	public Rectangle2D getBoundingRectangle() {
		return new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
	}

	protected void initControlPoints() {
		resizeControlPoints = new Vector<ResizeControlPoint>();
		for (int i = 0; i < 8; i++)
			resizeControlPoints.add(new ResizeControlPoint(this, null));
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
			ResizeControlPoint cp = resizeControlPoints.get(index);
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
		draggingOffset = GeomHelper.substractPoints(point, getLocation());
		setChanged();
		notifyObservers();
		clearChanged();
	}

	protected boolean isPointOverObject(Point2D point) {
		return (MathHelper.isBetween(point.getX(), getX(), getX() + getWidth()) && MathHelper
				.isBetween(point.getY(), getY(), getY() + getHeight()));
	}

	@Override
	public Drawable getDrawableUnderLocation(Point2D point) {
		Drawable drawable = null;
		for (AnchorPoint a : getAllAnchors()) {
			drawable = a.getDrawableUnderLocation(point);
			if (drawable != null)
				return drawable;
		}
		for (int i = 0; i < 8; i++) {
			drawable = resizeControlPoints.get(i).getDrawableUnderLocation(point);
			if (drawable != null)
				return drawable;
		}
		if (isPointOverObject(point))
			return this;
		return null;
	}

	private void resizePointDragged(ResizeControlPoint controlPoint) {
		int index = resizeControlPoints.indexOf(controlPoint);
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
			resizePointDragged((ResizeControlPoint) controlPoint);
		updateControlPointLocations();
		return controlPoint.getLocation();
	}

	@Override
	public Point2D onControlPointDragStarted(ControlPoint controlPoint, Point2D location) {
		controlPoint.setLocation(location);
		if (controlPoint instanceof ResizeControlPoint)
			resizePointDragged((ResizeControlPoint) controlPoint);
		updateControlPointLocations();
		return controlPoint.getLocation();
	}

	@Override
	public Point2D onControlPointDragged(ControlPoint controlPoint, Point2D location) {
		controlPoint.setLocation(location);
		if (controlPoint instanceof ResizeControlPoint)
			resizePointDragged((ResizeControlPoint) controlPoint);
		updateControlPointLocations();
		return controlPoint.getLocation();
	}

	private void updateResizePoint(int index) {
		boolean north = (getRoleOfIndex(index) & NORTH_MASK) != 0;
		boolean east = (getRoleOfIndex(index) & EAST_MASK) != 0;
		boolean south = (getRoleOfIndex(index) & SOUTH_MASK) != 0;
		boolean west = (getRoleOfIndex(index) & WEST_MASK) != 0;
		ResizeControlPoint cp = resizeControlPoints.get(index);
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
		for (AnchorPoint point : getAllAnchors())			
			point.setLocation(point.afterAllConstraints(point.getLocation()));
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

	public Dimension2D getPrefferedDimension () {
		return null;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double newX) {
		for (AnchorPoint anchor: getAllAnchors()) {
			anchor.setX(anchor.getX() + newX - getX());
		}
		this.x = newX;
		setChanged();
		notifyObservers();
		clearChanged();
	}

	public double getY() {
		return y;
	}

	public void setY(double newY) {
		for (AnchorPoint anchor: getAllAnchors()) {
			anchor.setY(anchor.getY() + newY - getY());
		}
		this.y = newY;
		setChanged();
		notifyObservers();
		clearChanged();
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double newWidth) {
		for (AnchorPoint point: getAllAnchors())
			point.setX(getX() + (point.getX()-getX())*(newWidth/width));
		this.width = newWidth;
		setChanged();
		notifyObservers();
		clearChanged();
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double newHeight) {
		for (AnchorPoint point: getAllAnchors())
			point.setY(getY() + (point.getY()-getY())*(newHeight/height));
		this.height = newHeight;
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
			resizeControlPoints.get(i).paint(g);
		for (AnchorPoint point : getAllAnchors())
			point.paint(g);
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
	public void onControlPointClicked(ControlPoint controlPoint, MouseEvent e,
			Point2D userSpaceLocation) {

	}

	// AnchorPoints

	protected LinkedList<SourceAnchorPoint> sourceAnchors;
	protected LinkedList<DestinationAnchorPoint> destinationAnchors;

	public Collection <AnchorPoint> getAllAnchors() {
		Vector <AnchorPoint> vector = new Vector<AnchorPoint>(sourceAnchors.size() + destinationAnchors.size());
		for (AnchorPoint point: sourceAnchors)
			vector.add(point);
		for (AnchorPoint point: destinationAnchors)
			vector.add(point);
		return vector;
	}
	
	@Override
	public AnchorPoint getDestinationPointAt(Point2D location, Link link) {
		if (getDrawableUnderLocation(location) == null)
			return null;
		DestinationAnchorPoint result = null;
		for (DestinationAnchorPoint dap : destinationAnchors) {
			if (dap.getBoundingRectangle().contains(location))
				return dap;
			if (dap.willAcceptLinkAsDestination(link) && link.willAcceptAnchorAsDestination(dap))
				result = dap;
		}
		return result;
	}

	@Override
	public AnchorPoint getSourcePointAt(Point2D location, Link link) {
		if (getDrawableUnderLocation(location) == null)
			return null;
		SourceAnchorPoint result = null;
		for (SourceAnchorPoint sap : sourceAnchors) {
			if (sap.getBoundingRectangle().contains(location))
				return sap;
			if (sap.willAcceptLinkAsSource(link) && link.willAcceptAnchorAsSource(sap))
				result = sap;
		}
		return result;
	}

	@Override
	public Collection<Link> getLinks() {
		Set<Link> links = new HashSet<Link>();
		for (AnchorPoint point : getAllAnchors())
			if (point.getLink() != null)
				links.add(point.getLink());
		Link[] array = links.toArray(new Link[] {});
		Vector<Link> vector = new Vector<Link>(array.length);
		for (Link l : array)
			vector.add(l);
		return vector;
	}

	public AnchorPoint addAnchor(boolean asSource, ControlPointConstraint constraint,
			Point2D initLocation) {
		AnchorPoint newAnchor = null;
		if (asSource) {
			newAnchor = new SourceAnchorPoint(this, initLocation);
			sourceAnchors.add((SourceAnchorPoint) newAnchor);
		} else {
			newAnchor = new DestinationAnchorPoint(this, initLocation);
			destinationAnchors.add((DestinationAnchorPoint) newAnchor);
		}
		newAnchor.addConstraint(constraint);
		newAnchor.setLocation(newAnchor.afterAllConstraints(newAnchor.getLocation()));
		setChanged();
		notifyObservers();
		clearChanged();
		return newAnchor;
	}
}
