package edu.raf.flowchart.blocks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.LinkedList;

import edu.raf.flowchart.link.DestinationAnchorPoint;
import edu.raf.flowchart.link.SourceAnchorPoint;
import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.AnchorPointContainer;
import edu.raf.gef.editor.model.object.impl.AnchorPoint;
import edu.raf.gef.editor.model.object.impl.Link;
import edu.raf.gef.editor.model.object.impl.RectangularObject;

public class ExecutionBlock extends RectangularObject implements AnchorPointContainer {

	private SourceAnchorPoint sourceAnchor;
	private DestinationAnchorPoint destinationAnchor;

	public ExecutionBlock(DiagramModel model) {
		super(model);
	}

	@Override
	protected void initControlPoints() {
		super.initControlPoints();
		sourceAnchor = new SourceAnchorPoint(this, new Point2D.Double(0, 0));
		destinationAnchor = new DestinationAnchorPoint(this, new Point2D.Double(0, 0));
	}

	@Override
	protected void updateControlPointLocations() {
		super.updateControlPointLocations();
		sourceAnchor.setLocation(new Point2D.Double(getX() + getWidth() / 2, getY() + getHeight()));
		destinationAnchor.setLocation(new Point2D.Double(getX() + getWidth() / 2, getY()));
		Link link = sourceAnchor.getLink();
		if (link != null)
			link.onAnchorMoved();
		link = destinationAnchor.getLink();
		if (link != null)
			link.onAnchorMoved();
	}

	@Override
	public Rectangle2D getBoundingRectangle() {
		return new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
	}

	@Override
	public Dimension2D getMinDimension() {
		return new Dimension(100, 100);
	}

	@Override
	protected void paintRectangular(Graphics2D g) {
		g.setColor(Color.WHITE);
		// System.out.println("Crtanje");
		g.fillRect((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
		g.setColor(Color.DARK_GRAY);
		g.drawRect((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
	}

	@Override
	protected void paintChildren(Graphics2D g) {
		sourceAnchor.paint(g);
		destinationAnchor.paint(g);
	}

	@Override
	public AnchorPoint getDestinationPointAt(Point2D location, Link link) {
		if (getBoundingRectangle().contains(location)
				|| destinationAnchor.getBoundingRectangle().contains(location))
			return destinationAnchor;
		return null;
	}

	@Override
	public AnchorPoint getSourcePointAt(Point2D location, Link link) {
		if (getBoundingRectangle().contains(location)
				|| sourceAnchor.getBoundingRectangle().contains(location))
			return sourceAnchor;
		return null;
	}
	
	@Override
	public Collection<Link> getLinks() {
		LinkedList <Link> links = new LinkedList <Link>();
		if (sourceAnchor.getLink() != null)
			links.add(sourceAnchor.getLink());
		if (destinationAnchor.getLink() != null)
			links.add(destinationAnchor.getLink());
		return links;
	}
}
