package edu.raf.flowchart.blocks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import edu.raf.flowchart.syntax.FlowchartVariableType;
import edu.raf.flowchart.syntax.IExecutionManager;
import edu.raf.gef.editor.model.object.constraint.ControlPointConstraint;
import edu.raf.gef.editor.model.object.impl.RectangularObject;
import edu.raf.gef.services.beaneditor.annotations.Property;

public class InputBlock extends RectangularObject implements FlowchartBlock {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8369232407226259942L;

	private static int INSTANCE_COUNTER = 0;

	private String name = "Input" + ++INSTANCE_COUNTER;

	private FlowchartVariableType varType;

	public InputBlock() {
		super();
		varType = FlowchartVariableType.REAL;
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
		setMinDimension(new Dimension(60, 40));
		setTitle("Input");
	}

	@Override
	protected void paintRectangular(Graphics2D g) {
		Polygon p = new Polygon(new int[] { (int) (getX()), (int) (getX() + getWidth()),
				(int) (getX() + getWidth() - getHeight() / 2), (int) (getX() + getHeight() / 2) },
				new int[] { (int) (getY()), (int) (getY()), (int) (getY() + getHeight()),
						(int) (getY() + getHeight()) }, 4);

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
		Polygon p = new Polygon(new int[] { (int) (getX()), (int) (getX() + getWidth()),
				(int) (getX() + getWidth() - getHeight() / 2), (int) (getX() + getHeight() / 2) },
				new int[] { (int) (getY()), (int) (getY()), (int) (getY() + getHeight()),
						(int) (getY() + getHeight()) }, 4);
		return p.contains(point);
	}

	public FlowchartBlock executeAndReturnNext(IExecutionManager context) {
		if (sourceAnchors.get(0).getLink() == null) {
			context.raiseError(this, "Not connected.");
			return null;
		}
		if (!(sourceAnchors.get(0).getLink().getDestinationAnchor().getParent() instanceof FlowchartBlock)) {
			context.raiseError(this, "Not connected with flowchart object!");
			return null;
		}
		String[] variables = getTitle().trim().split("[ ,]+");
		for (String var : variables) {
			context.readInput(var, varType);
		}
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

	@Property
	public FlowchartVariableType getVarType() {
		return varType;
	}

	public void setVarType(FlowchartVariableType varType) {
		this.varType = varType;
	}
}
