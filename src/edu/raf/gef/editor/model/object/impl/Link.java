package edu.raf.gef.editor.model.object.impl;

import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.ListIterator;

import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.util.GeomHelper;

public abstract class Link extends DiagramObject {

	private static final double selectionDistance = 4;
	private static final double snapDistance = 6; // will skip snap if 0 or
													// less

	public boolean willAcceptAnchorAsSource(AnchorPoint point) {
		return true;
	}

	public boolean willAcceptAnchorAsDestination(AnchorPoint point) {
		return true;
	}

	protected Stroke getStroke() {
		return null;
	}

	protected void paintStart(Graphics2D g) {

	}

	protected void paintEnd(Graphics2D g) {

	}

	public void onAnchorMoved() {
		if (getSourcePoint() != null)
			resizePoints.getFirst().setLocation(getSourcePoint().getLocation());
		if (getDestinationPoint() != null)
			resizePoints.getLast().setLocation(getDestinationPoint().getLocation());
		setChanged();
		notifyObservers();
		clearChanged();
	}

	/**
	 * This list will always have at least two elements: the source and
	 * destination resize point. Those are needed to support relinking
	 * (razvezivanje :P). The first represents source, the last represents
	 * destination
	 */
	private LinkedList<ResizeControlPoint> resizePoints;

	/**
	 * Self explanatory
	 */
	private AnchorPoint sourcePoint, destinationPoint;

	public Link(DiagramModel model) {
		super(model);
		resizePoints = new LinkedList<ResizeControlPoint>();
		resizePoints.add(new ResizeControlPoint(this, null));
		resizePoints.add(new ResizeControlPoint(this, null));
	}

	@Override
	public Rectangle2D getBoundingRectangle() {
		double minx = Double.MAX_VALUE, maxx = -1 * Double.MAX_VALUE;
		double miny = minx, maxy = maxx;

		ListIterator<ResizeControlPoint> iterator = resizePoints.listIterator();
		while (iterator.hasNext()) {
			Point2D location = iterator.next().getLocation();
			minx = Math.min(location.getX(), minx);
			miny = Math.min(location.getY(), miny);
			maxx = Math.max(location.getX(), maxx);
			maxy = Math.max(location.getY(), maxy);
		}

		return new Rectangle2D.Double(minx, miny, maxx - minx, maxy - miny);
	}

	@Override
	public Drawable getDrawableUnderLocation(Point2D location) {
		for (ResizeControlPoint resizePoint : resizePoints) {
			Drawable drawable = resizePoint.getDrawableUnderLocation(location);
			if (drawable != null)
				return drawable;
		}

		ListIterator<ResizeControlPoint> iterator = resizePoints.listIterator();

		Point2D location2 = iterator.next().getLocation();
		do {
			Point2D location1 = location2;
			location2 = iterator.next().getLocation();
			double r = GeomHelper.getProjectionr(location1, location2, location);
			if (r < 0 || r > 1)
				continue;
			Point2D projectionPoint = GeomHelper.getProjectionPoint(location1, location2, r);
			double distance = GeomHelper.pointDistance(projectionPoint, location);
			if (distance <= selectionDistance)
				return this;

		} while (iterator.hasNext());
		return null;
	}

	@Override
	public void paint(Graphics2D g) {
		Stroke tempStroke = null;
		Stroke newStroke = getStroke();
		if (newStroke != null) {
			tempStroke = g.getStroke();
			g.setStroke(newStroke);
		}
		ListIterator<ResizeControlPoint> iterator = resizePoints.listIterator();

		ResizeControlPoint p1 = iterator.next();
		ResizeControlPoint p2;

		do {
			p2 = iterator.next();

			int x1 = (int) p1.getLocation().getX(), y1 = (int) p1.getLocation().getY();
			int x2 = (int) p2.getLocation().getX(), y2 = (int) p2.getLocation().getY();
			g.drawLine(x1, y1, x2, y2);
			p1.paint(g);

			p1 = p2;
		} while (iterator.hasNext());

		p2.paint(g);

		if (tempStroke != null)
			g.setStroke(tempStroke);
	}

	@Override
	public Point2D onControlPointDragEnded(ControlPoint controlPoint, Point2D location) {
		if (getSourcePoint() != null && controlPoint == resizePoints.getFirst())
			return getSourcePoint().getLocation();
		if (getDestinationPoint() != null && controlPoint == resizePoints.getLast())
			return getDestinationPoint().getLocation();
		Point2D returnLocation = (Point2D) location.clone();
		if (snapDistance > 0) {
			double bestXDistance = Double.MAX_VALUE, bestYDistance = Double.MAX_VALUE;
			Double bestX = null, bestY = null;
			for (ResizeControlPoint rcp : resizePoints) {
				if (rcp == controlPoint)
					continue;
				Point2D pointLocation = rcp.getLocation();
				double distance = Math.abs(pointLocation.getX() - location.getX());
				if (distance <= snapDistance && distance < bestXDistance) {
					bestXDistance = distance;
					bestX = pointLocation.getX();
				}
				distance = Math.abs(pointLocation.getY() - location.getY());
				if (distance <= snapDistance && distance < bestYDistance) {
					bestYDistance = distance;
					bestY = pointLocation.getY();
				}
			}
			if (bestX == null)
				bestX = location.getX();
			if (bestY == null)
				bestY = location.getY();
			returnLocation.setLocation(bestX, bestY);
		}
		setChanged();
		notifyObservers();
		clearChanged();
		return returnLocation;
	}

	@Override
	public Point2D onControlPointDragStarted(ControlPoint controlPoint, Point2D location) {
		if (getSourcePoint() != null && controlPoint == resizePoints.getFirst())
			return getSourcePoint().getLocation();
		if (getDestinationPoint() != null && controlPoint == resizePoints.getLast())
			return getDestinationPoint().getLocation();
		Point2D returnLocation = (Point2D) location.clone();
		if (snapDistance > 0) {
			double bestXDistance = Double.MAX_VALUE, bestYDistance = Double.MAX_VALUE;
			Double bestX = null, bestY = null;
			for (ResizeControlPoint rcp : resizePoints) {
				if (rcp == controlPoint)
					continue;
				Point2D pointLocation = rcp.getLocation();
				double distance = Math.abs(pointLocation.getX() - location.getX());
				if (distance <= snapDistance && distance < bestXDistance) {
					bestXDistance = distance;
					bestX = pointLocation.getX();
				}
				distance = Math.abs(pointLocation.getY() - location.getY());
				if (distance <= snapDistance && distance < bestYDistance) {
					bestYDistance = distance;
					bestY = pointLocation.getY();
				}
			}
			if (bestX == null)
				bestX = location.getX();
			if (bestY == null)
				bestY = location.getY();
			returnLocation.setLocation(bestX, bestY);
		}
		setChanged();
		notifyObservers();
		clearChanged();
		return returnLocation;
	}

	@Override
	public Point2D onControlPointDragged(ControlPoint controlPoint, Point2D location) {
		if (getSourcePoint() != null && controlPoint == resizePoints.getFirst())
			return getSourcePoint().getLocation();
		if (getDestinationPoint() != null && controlPoint == resizePoints.getLast())
			return getDestinationPoint().getLocation();
		Point2D returnLocation = (Point2D) location.clone();
		if (snapDistance > 0) {
			double bestXDistance = Double.MAX_VALUE, bestYDistance = Double.MAX_VALUE;
			Double bestX = null, bestY = null;
			for (ResizeControlPoint rcp : resizePoints) {
				if (rcp == controlPoint)
					continue;
				Point2D pointLocation = rcp.getLocation();
				double distance = Math.abs(pointLocation.getX() - location.getX());
				if (distance <= snapDistance && distance < bestXDistance) {
					bestXDistance = distance;
					bestX = pointLocation.getX();
				}
				distance = Math.abs(pointLocation.getY() - location.getY());
				if (distance <= snapDistance && distance < bestYDistance) {
					bestYDistance = distance;
					bestY = pointLocation.getY();
				}
			}
			if (bestX == null)
				bestX = location.getX();
			if (bestY == null)
				bestY = location.getY();
			returnLocation.setLocation(bestX, bestY);
		}
		setChanged();
		notifyObservers();
		clearChanged();
		return returnLocation;
	}

	public AnchorPoint getSourcePoint() {
		return sourcePoint;
	}

	public void setSourcePoint(AnchorPoint sourcePoint) {
		this.sourcePoint = sourcePoint;
		if (sourcePoint != null)
			resizePoints.getFirst().setLocation(sourcePoint.getLocation());
		setChanged();
		notifyObservers();
		clearChanged();
	}

	public AnchorPoint getDestinationPoint() {
		return destinationPoint;
	}

	public void setDestinationPoint(AnchorPoint destinationPoint) {
		this.destinationPoint = destinationPoint;
		if (destinationPoint != null)
			resizePoints.getLast().setLocation(destinationPoint.getLocation());
		setChanged();
		notifyObservers();
		clearChanged();
	}

	public LinkedList<ResizeControlPoint> getResizePoins() {
		return resizePoints;
	}

	@Override
	public void onClick(MouseEvent e, Point2D userSpaceLocation) {
		if (e.getClickCount() != 2 || e.getButton() != MouseEvent.BUTTON1)
			return;
		Point2D newResizePointLocation = null;
		int whereToInsert = -1;
		double bestDistance = Double.MAX_VALUE;

		ListIterator<ResizeControlPoint> iterator = resizePoints.listIterator();
		Point2D p2 = iterator.next().getLocation(), p1;
		int count = 0;
		do {
			p1 = p2;
			p2 = iterator.next().getLocation();
			count++;
			double r = GeomHelper.getProjectionr(p1, p2, userSpaceLocation);
			if (r < 0 || r > 1)
				continue;
			Point2D projectionPoint = GeomHelper.getProjectionPoint(p1, p2, r);
			double distance = GeomHelper.pointDistance(projectionPoint, userSpaceLocation);
			if (distance < bestDistance) {
				bestDistance = distance;
				whereToInsert = count;
				newResizePointLocation = projectionPoint;
			}
		} while (iterator.hasNext());
		resizePoints.add(whereToInsert, new ResizeControlPoint(this, newResizePointLocation));
		setChanged();
		notifyObservers();
		clearChanged();
	}

	@Override
	public void onControlPointClicked(ControlPoint controlPoint, MouseEvent e,
			Point2D userSpaceLocation) {
		if (e.getClickCount() != 2 || e.getButton() != MouseEvent.BUTTON1)
			return;
		if (controlPoint == resizePoints.getFirst() || controlPoint == resizePoints.getLast())
			return;
		resizePoints.remove(controlPoint);
		setChanged();
		notifyObservers();
		clearChanged();
	}
}
