package edu.raf.flowchart.blocks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import edu.raf.flowchart.exceptions.FCExecutionException;
import edu.raf.flowchart.syntax.IExecutionManager;
import edu.raf.gef.editor.model.object.AnchorPointContainer;
import edu.raf.gef.editor.model.object.constraint.ControlPointConstraint;
import edu.raf.gef.editor.model.object.impl.RectangularObject;

public class ExecutionBlock extends RectangularObject implements AnchorPointContainer,
		FlowchartBlock {

	/**
	 */
	private static final long serialVersionUID = -5497560976688614627L;

	private static int INSTANCE_COUNTER = 0;

	private String name = "Expression" + ++INSTANCE_COUNTER;

	public ExecutionBlock() {
		super();
		setMinDimension(new Dimension(60, 40));
		super.addAnchor(false, new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				return new Point2D.Double(getX() + getWidth() / 2, getY());
			}
		}, null);
		super.addAnchor(true, new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				return new Point2D.Double(getX() + getWidth() / 2, getY() + getHeight());
			}
		}, null);
		setTitle("Execution");
	}

	@Override
	public Dimension2D getPrefferedDimension() {
		return new Dimension(100, 50);
	}

	@Override
	protected void paintRectangular(Graphics2D g) {
		g.setColor(getBackgroundColor());
		g.fillRect((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
		g.setColor(Color.DARK_GRAY);
		g.drawRect((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
	}
	@Override
	public FlowchartBlock executeAndReturnNext(IExecutionManager context) throws FCExecutionException {
		if (sourceAnchors.get(0).getLink() == null) {
			context.raiseError(this, "Not connected.");
			return null;
		}
		if (!(sourceAnchors.get(0).getLink().getDestinationAnchor().getParent() instanceof FlowchartBlock)) {
			context.raiseError(this, "Not connected with flowchart object!");
			return null;
		}
		context.evaluate(getTitle());
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
}
