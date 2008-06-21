package edu.raf.flowchart.blocks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import edu.raf.flowchart.syntax.ExecutionManager;
import edu.raf.gef.editor.model.object.constraint.ControlPointConstraint;
import edu.raf.gef.editor.model.object.impl.AnchorPoint;
import edu.raf.gef.editor.model.object.impl.Link;
import edu.raf.gef.editor.model.object.impl.RectangularObject;

public class ConditionBlock extends RectangularObject implements FlowchartBlock {

	// 0--------1
	// / \
	// 5 2
	// \ /
	// 4--------3

	/**
	 * 
	 */
	private static final long serialVersionUID = -3766628340203826777L;

	private static int INSTANCE_COUNTER = 0;

	private String name = "Condition" + ++INSTANCE_COUNTER;

	private AnchorPoint anchorOnTrue;

	private AnchorPoint anchorOnFalse;

	public ConditionBlock() {
		super();
		addAnchor(false, new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				return new Point2D.Double(getX() + getWidth() / 2, getY());
			}
		}, null);
		anchorOnFalse = addAnchor(true, new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				return new Point2D.Double(getX(), getY() + getHeight() / 2);
			}
		}, null);
		anchorOnTrue = addAnchor(true, new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				return new Point2D.Double(getX() + getWidth(), getY() + getHeight() / 2);
			}
		}, null);

		ControlPointConstraint constraint = new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				double maxy = getY() + getWidth();
				if (oldLocation.getY() > maxy)
					return new Point2D.Double(oldLocation.getX(), maxy);
				return oldLocation;
			}
		};
		for (int index = 0; index < 8; index++) {
			if ((getRoleOfIndex(index) & SOUTH_MASK) != 0)
				resizeControlPoints.get(index).addConstraint(constraint);
		}
		setMinDimension(new Dimension(60, 40));
		setTitle("If <condition>");
	}

	@Override
	protected void paintRectangular(Graphics2D g) {
		double arcDimension = (int) (Math.min(getWidth(), getHeight()));
		Polygon p = new Polygon(new int[] { (int) (getX() + arcDimension / 2),
				(int) (getX() + getWidth() - arcDimension / 2), (int) (getX() + getWidth()),
				(int) (getX() + getWidth() - arcDimension / 2), (int) (getX() + arcDimension / 2),
				(int) (getX()) }, new int[] { (int) (getY()), (int) (getY()),
				(int) (getY() + getHeight() / 2), (int) (getY() + getHeight()),
				(int) (getY() + getHeight()), (int) (getY() + getHeight() / 2) }, 6);

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
		double arcDimension = (int) (Math.min(getWidth(), getHeight()));
		Polygon p = new Polygon(new int[] { (int) (getX() + arcDimension / 2),
				(int) (getX() + getWidth() - arcDimension / 2), (int) (getX() + getWidth()),
				(int) (getX() + getWidth() - arcDimension / 2), (int) (getX() + arcDimension / 2),
				(int) (getX()) }, new int[] { (int) (getY()), (int) (getY()),
				(int) (getY() + getHeight() / 2), (int) (getY() + getHeight()),
				(int) (getY() + getHeight()), (int) (getY() + getHeight() / 2) }, 6);
		return p.contains(point);
	}

	@Override
	public AnchorPoint getSourcePointAt(Point2D location, Link link) {
		if (getDrawableUnderLocation(location) == null)
			return null;
		int index;
		if (location.getX() < getX() + getWidth() / 2)
			index = 0;
		else
			index = 1;
		return sourceAnchors.get(index);
	}

	@Override
	public FlowchartBlock executeAndReturnNext(ExecutionManager context) {
		if (anchorOnFalse.getLink() == null || anchorOnTrue.getLink() == null) {
			context.raiseError(this, "Object not connected!");
			return null;
		}
		Object onFalse = anchorOnFalse.getLink().getDestinationAnchor().getParent();
		Object onTrue = anchorOnTrue.getLink().getDestinationAnchor().getParent();
		if (!(onFalse instanceof FlowchartBlock && onTrue instanceof FlowchartBlock)) {
			context.raiseError(this, "Connected with non flowchart objects!");
			return null;
		}

		String cond = this.getTitle();
		Object result = context.evaluate(cond);
		if (Boolean.TRUE.equals(result)) {
			return (FlowchartBlock) onTrue;
		} else {
			return (FlowchartBlock) onFalse;
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String s) {
		this.name = name;
	}

}