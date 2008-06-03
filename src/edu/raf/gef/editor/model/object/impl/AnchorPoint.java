package edu.raf.gef.editor.model.object.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.raf.gef.editor.model.object.AnchorPointContainer;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.util.MathHelper;

public abstract class AnchorPoint extends ControlPoint {

	private static final int DIMENSION = 11; // has to be an even number
	private Link link;

	private Color color;

	public boolean willAcceptLinkAsSource(Link link) {
		return this.link == null;
	}

	public boolean willAcceptLinkAsDestination(Link link) {
		return this.link == null;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public Link getLink() {
		return link;
	}

	public AnchorPoint(AnchorPointContainer parent, Point2D initLocation) {
		super(parent, initLocation);
		setColor(Color.WHITE);
		link = null;
	}

	@Override
	public AnchorPointContainer getParent() {
		return (AnchorPointContainer) super.getParent();
	}

	@Override
	public Rectangle2D getBoundingRectangle() {
		return new Rectangle2D.Double(getLocation().getX() - DIMENSION / 2, getLocation().getY()
				- DIMENSION / 2, DIMENSION, DIMENSION);
	}

	@Override
	public Drawable getDrawableUnderLocation(Point2D location) {
		double x = getLocation().getX();
		double y = getLocation().getY();
		if (MathHelper.isBetween(location.getX(), x - DIMENSION / 2, x + DIMENSION / 2)
				&& MathHelper.isBetween(location.getY(), y - DIMENSION / 2, y + DIMENSION / 2))
			return this;
		return null;
	}

	@Override
	public void onClick(MouseEvent e, Point2D userSpaceLocation) {
		super.onClick(e, userSpaceLocation);
	}

	@Override
	public void paint(Graphics2D g) {
		int x = (int) getLocation().getX();
		int y = (int) getLocation().getY();
		g.setColor(getColor());
		g.fillRect(x - DIMENSION / 2, y - DIMENSION / 2, DIMENSION - 1, DIMENSION - 1);
		g.setColor(Color.BLACK);
		g.drawRect(x - DIMENSION / 2, y - DIMENSION / 2, DIMENSION - 1, DIMENSION - 1);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
