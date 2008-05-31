package edu.raf.gef.editor.model.object.impl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.raf.gef.editor.model.object.ControlPoint;
import edu.raf.gef.editor.model.object.ControlPointContainer;
import edu.raf.gef.editor.model.object.Focusable;
import edu.raf.gef.util.MathHelper;

public class ResizeControlPoint extends ControlPoint {

	public ResizeControlPoint(ControlPointContainer parent, Point2D initLocation) {
		super(parent, initLocation);
	}
	
	@Override
	public void onClick(MouseEvent e) {
		
	}

	@Override
	public Rectangle2D getBoundingRectangle() {
		return new Rectangle2D.Double(getLocation().getX() - 2, getLocation().getY() - 2, 5, 5);
	}

	@Override
	public boolean isUnderLocation(Point2D location) {
		double x = getLocation().getX();
		double y = getLocation().getY();
		return (MathHelper.isBetween(location.getX(), x - 2, x + 2) &&
				MathHelper.isBetween(location.getY(), y - 2, y + 2));
	}

	@Override
	public void paint(Graphics2D g) {
		if (getParent() instanceof Focusable) {
			Focusable focusableParent = (Focusable) getParent();
			if (!focusableParent.isFocused())
				return;
		}
		int x = (int) getLocation().getX();
		int y = (int) getLocation().getY();
		g.setColor(Color.BLACK);
		g.fillRect(x - 2, y - 2, 5, 5);
	}

}
