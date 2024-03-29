package edu.raf.flowchart.blocks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import edu.raf.flowchart.exceptions.FCExecutionException;
import edu.raf.flowchart.syntax.IExecutionManager;
import edu.raf.gef.editor.model.object.constraint.ControlPointConstraint;
import edu.raf.gef.editor.model.object.impl.RectangularObject;

public class OutputBlock extends RectangularObject implements FlowchartBlock {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4029973414439459416L;
	private static int INSTANCE_COUNTER = 0;
	private String name = "Output" + ++INSTANCE_COUNTER;

	public OutputBlock() {
		super();
		addAnchor(false, new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				return new Point2D.Double(getX() + getWidth() / 2, getY());
			}
		}, null);
		addAnchor(true, new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				return new Point2D.Double(getX() + getWidth() / 2, getY() + getHeight());
			}
		}, null);

		ControlPointConstraint southConstraint = new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				double maxy = getY() + getWidth();
				if (oldLocation.getY() > maxy)
					return new Point2D.Double(oldLocation.getX(), maxy);
				return oldLocation;
			}
		};
		ControlPointConstraint northConstraint = new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				double miny = getY() + getHeight() - getWidth();
				if (oldLocation.getY() < miny)
					return new Point2D.Double(oldLocation.getX(), miny);
				return oldLocation;
			}
		};
		for (int index = 0; index < 8; index++) {
			if ((getRoleOfIndex(index) & SOUTH_MASK) != 0)
				resizeControlPoints.get(index).addConstraint(southConstraint);
			if ((getRoleOfIndex(index) & NORTH_MASK) != 0)
				resizeControlPoints.get(index).addConstraint(northConstraint);
		}
		for (int index = 0; index < 8; index++) {
			if ((getRoleOfIndex(index) & SOUTH_MASK) != 0)
				resizeControlPoints.get(index).addConstraint(southConstraint);
			if ((getRoleOfIndex(index) & NORTH_MASK) != 0)
				resizeControlPoints.get(index).addConstraint(northConstraint);
		}
		setMinDimension(new Dimension(60, 40));

		setText("Output");
	}

	@Override
	protected void paintRectangular(Graphics2D g) {
		Polygon p = new Polygon(new int[] { (int) (getX() + getHeight() / 2),
				(int) (getX() + getWidth() - getHeight() / 2), (int) (getX() + getWidth()),
				(int) (getX()) }, new int[] { (int) (getY()), (int) (getY()),
				(int) (getY() + getHeight()), (int) (getY() + getHeight()) }, 4);

		g.setColor(getBackgroundColor());
		g.fillPolygon(p);
		g.setColor(Color.DARK_GRAY);
		g.drawPolygon(p);
	}

	@Override
	public Dimension2D getPrefferedDimension() {
		return new Dimension(120, 50);
	}

	@Override
	public boolean isPointOverObject(Point2D point) {
		Polygon p = new Polygon(new int[] { (int) (getX() + getHeight() / 2),
				(int) (getX() + getWidth() - getHeight() / 2), (int) (getX() + getWidth()),
				(int) (getX()) }, new int[] { (int) (getY()), (int) (getY()),
				(int) (getY() + getHeight()), (int) (getY() + getHeight()) }, 4);
		return p.contains(point);
	}

	public FlowchartBlock executeAndReturnNext(IExecutionManager context)
			throws FCExecutionException {
		if (sourceAnchors.get(0).getLink() == null) {
			context.raiseError(this, "Not connected.");
			return null;
		}
		if (!(sourceAnchors.get(0).getLink().getDestinationAnchor().getParent() instanceof FlowchartBlock)) {
			context.raiseError(this, "Not connected with flowchart object!");
			return null;
		}
		context.writeOutput(context.evaluate(getText()));
		return (FlowchartBlock) sourceAnchors.get(0).getLink().getDestinationAnchor().getParent();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String s) {
		this.name = s;
	}

	@Override
	public void setWidth(double newWidth) {
		super.setWidth(Math.max(newWidth, getHeight()));
	}

	@Override
	public void setHeight(double newHeight) {
		super.setHeight(Math.min(newHeight, getWidth()));
	}
}
