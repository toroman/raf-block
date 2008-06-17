package edu.raf.flowchart.blocks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import edu.raf.gef.editor.model.object.AnchorPointContainer;
import edu.raf.gef.editor.model.object.constraint.ControlPointConstraint;
import edu.raf.gef.editor.model.object.impl.RectangularObject;

public class ExecutionBlock extends RectangularObject implements AnchorPointContainer {

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
		g.setColor(Color.WHITE);
		g.fillRect((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
		g.setColor(Color.DARK_GRAY);
		g.drawRect((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
	}
}
