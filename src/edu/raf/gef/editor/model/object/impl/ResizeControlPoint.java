package edu.raf.gef.editor.model.object.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.raf.gef.editor.model.object.ControlPointContainer;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.editor.model.object.Focusable;
import edu.raf.gef.util.MathHelper;

public class ResizeControlPoint extends ControlPoint {

	public final static int SIZE_FACTOR = 7; // not an even number

	public ResizeControlPoint(ControlPointContainer parent, Point2D initLocation) {
		super(parent, initLocation);
	}

	@Override
	public Rectangle2D getBoundingRectangle() {
		return new Rectangle2D.Double(getLocation().getX() - SIZE_FACTOR / 2, getLocation().getY()
				- SIZE_FACTOR / 2, SIZE_FACTOR, SIZE_FACTOR);
	}
	
	public Drawable getDrawableUnderLocationIgnoringFocus (Point2D location) {
		double x = getLocation().getX();
		double y = getLocation().getY();
		if (MathHelper.isBetween(location.getX(), x - SIZE_FACTOR / 2, x + SIZE_FACTOR / 2)
				&& MathHelper.isBetween(location.getY(), y - SIZE_FACTOR / 2, y + SIZE_FACTOR / 2))
			return this;
		return null;
	}

	@Override
	public Drawable getDrawableUnderLocation(Point2D location) {
		if (!isParentFocused())
			return null;
		return getDrawableUnderLocationIgnoringFocus(location);
	}
	
	private boolean isParentFocused() {
		if (getParent() instanceof Focusable) {
			return ((Focusable) getParent()).isFocused();
		}
		return false;
	}

	@Override
	public void paint(Graphics2D g) {
		if (!isParentFocused())
			return;
		int x = (int) getLocation().getX();
		int y = (int) getLocation().getY();
		g.setColor(Color.BLACK);
		g.fillRect(x - SIZE_FACTOR / 2, y - SIZE_FACTOR / 2, SIZE_FACTOR - 1, SIZE_FACTOR - 1);
	}
}
