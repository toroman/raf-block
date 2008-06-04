package edu.raf.flowchart.blocks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.constraint.ControlPointConstraint;
import edu.raf.gef.editor.model.object.impl.RectangularObject;
import edu.raf.gef.util.GeomHelper;
import edu.raf.gef.util.MathHelper;

public class StartBlock extends RectangularObject {

	private static final Color COLOR = Color.white;
	
	public StartBlock(DiagramModel model) {
		super(model);
		addAnchor(true, new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				return new Point2D.Double (getX() + getWidth()/2, getY() + getHeight());
			}
		}, null);
		setMinDimension(new Dimension (60, 40));
		
		ControlPointConstraint constraint = new ControlPointConstraint () {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				double maxy = getY() + getWidth();
				if (oldLocation.getY() > maxy)
					return new Point2D.Double (oldLocation.getX(), maxy);
				return oldLocation;
			}
		};
		for (int index = 0; index < 8; index++) {
			if ((getRoleOfIndex(index) & SOUTH_MASK) != 0)
				resizeControlPoints.get(index).addConstraint(constraint);
		}
	}

	@Override
	protected void paintRectangular(Graphics2D g) {
		double arcDimension = (int)(Math.min(getWidth(), getHeight()));
		g.setColor(COLOR);
		g.fillOval((int)getX(), (int)getY(), (int)arcDimension, (int)arcDimension);
		g.fillOval((int)(getX() + getWidth() - (int)arcDimension), (int)getY(), (int)arcDimension, (int)arcDimension);
		g.setColor(Color.DARK_GRAY);
		g.drawArc((int)getX(), (int)getY(), (int)arcDimension, (int)arcDimension, 90, 180);
		g.drawArc((int)(getX() + getWidth() - (int)arcDimension), (int)getY(), (int)arcDimension, (int)arcDimension, -90, 180);
		g.setColor(COLOR);
		g.fillRect((int)(getX() + arcDimension/2), (int)getY(), (int)(getWidth() - arcDimension), (int)getHeight());
		g.setColor(Color.DARK_GRAY);
		g.drawLine((int)(getX() + arcDimension/2), (int)getY(), (int)(getX() + getWidth() - arcDimension/2), (int)getY());
		g.drawLine((int)(getX() + arcDimension/2), (int)(getY() + getHeight()), (int)(getX() + getWidth() - arcDimension/2), (int)(getY() + getHeight()));
	}
	
	@Override
	public Dimension2D getPrefferedDimension() {
		return new Dimension (120, 50);
	}
	
	@Override
	public boolean isPointOverObject(Point2D point) {
		double arcDimension = (int)(Math.min(getWidth(), getHeight()));
		if (GeomHelper.pointDistance(point.getX(), point.getY(), getX() + arcDimension/2, getY() + arcDimension/2) <= arcDimension/2)
			return true;
		if (GeomHelper.pointDistance(point.getX(), point.getY(), getX() + getWidth() - arcDimension/2, getY() + arcDimension/2) <= arcDimension/2)
			return true;
		return (MathHelper.isBetween(point.getX(), getX() + arcDimension/2, getX() + getWidth() - arcDimension/2))
				&& MathHelper.isBetween(point.getY(), getY(), getY() + getHeight());
	}
}
