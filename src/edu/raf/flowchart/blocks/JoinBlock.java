package edu.raf.flowchart.blocks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import edu.raf.flowchart.syntax.ExecutionManager;
import edu.raf.gef.editor.model.object.AnchorPointContainer;
import edu.raf.gef.editor.model.object.constraint.ControlPointConstraint;
import edu.raf.gef.editor.model.object.impl.AnchorPoint;
import edu.raf.gef.editor.model.object.impl.DestinationAnchorPoint;
import edu.raf.gef.editor.model.object.impl.Link;
import edu.raf.gef.editor.model.object.impl.RectangularObject;
import edu.raf.gef.util.GeomHelper;

public class JoinBlock extends RectangularObject implements FlowchartBlock {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4121118197825742163L;
	private ControlPointConstraint anchorConstraint = new ControlPointConstraint() {
		@Override
		public Point2D updateLocation(Point2D oldLocation) {
			Point2D center = new Point2D.Double(getX() + getWidth() / 2, getY() + getHeight() / 2);
			double distance = GeomHelper.pointDistance(center, oldLocation);
			return GeomHelper.getProjectionPoint(center, oldLocation, (getWidth() / 2) / distance);
		}
	};

	private static int INSTANCE_COUNTER = 0;

	private String name = "Join" + ++INSTANCE_COUNTER;

	public JoinBlock() {
		super();
		setMinDimension(new Dimension(40, 40));
		addResizeConstraints();

		addAnchor(true, new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				return new Point2D.Double(getX() + getWidth() / 2, getY() + getHeight());
			}
		}, null);
	}

	@Override
	public AnchorPoint getDestinationPointAt(Point2D location, Link link) {
		if (!isPointOverObject(location))
			return null;
		RemovingDestinationAnchorPoint newAnchor = new RemovingDestinationAnchorPoint(this,
				location);
		destinationAnchors.add(newAnchor);
		newAnchor.addConstraint(anchorConstraint);
		newAnchor.setLocation(anchorConstraint.updateLocation(newAnchor.getLocation()));
		setChanged();
		notifyObservers();
		clearChanged();
		return newAnchor;
	}

	@Override
	public void setWidth(double width) {
		setSize(width);
	}

	@Override
	public void setHeight(double height) {
		setSize(height);
	}

	public void setSize(double size) {
		super.setWidth(size);
		super.setHeight(size);
	}

	@Override
	public Dimension2D getPrefferedDimension() {
		return new Dimension(50, 50);
	}

	private void addResizeConstraints() {
		ControlPointConstraint mainDiagonal = new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				double offset = oldLocation.getX() - getX();
				return new Point2D.Double(getX() + offset, getY() + offset);
			}
		};
		ControlPointConstraint secondDiagonal = new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				double offset = oldLocation.getX() - (getX() + getWidth());
				return new Point2D.Double(getX() + getWidth() + offset, getY() - offset);
			}
		};
		ControlPointConstraint west = new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				return new Point2D.Double(getX(), getY() + getWidth() / 2);
			}
		};
		ControlPointConstraint north = new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				return new Point2D.Double(getX() + getHeight() / 2, getY());
			}
		};
		ControlPointConstraint east = new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				return new Point2D.Double(getX() + getWidth(), getY() + getWidth() / 2);
			}
		};
		ControlPointConstraint south = new ControlPointConstraint() {
			@Override
			public Point2D updateLocation(Point2D oldLocation) {
				return new Point2D.Double(getX() + getHeight() / 2, getY() + getHeight());
			}
		};
		for (int i = 0; i < 8; i += 2) {
			if (i % 4 == 0)
				resizeControlPoints.get(i).addConstraint(mainDiagonal);
			if (i % 4 == 2)
				resizeControlPoints.get(i).addConstraint(secondDiagonal);
		}
		resizeControlPoints.get(1).addConstraint(north);
		resizeControlPoints.get(3).addConstraint(east);
		resizeControlPoints.get(5).addConstraint(south);
		resizeControlPoints.get(7).addConstraint(west);
	}

	@Override
	protected boolean isPointOverObject(Point2D point) {
		return GeomHelper.pointDistance(point.getX(), point.getY(), getX() + getWidth() / 2, getY()
				+ getHeight() / 2) < getWidth() / 2 + 2;
	}

	public void removeAnchor(RemovingDestinationAnchorPoint removingDestinationAnchorPoint) {
		destinationAnchors.remove(removingDestinationAnchorPoint);
	}
	
	public void restoreAnchor(RemovingDestinationAnchorPoint anchor) {
		if (!destinationAnchors.contains(anchor))
			destinationAnchors.add(anchor);
	}

	@Override
	protected void paintRectangular(Graphics2D g) {
		g.setColor(getBackgroundColor());
		g.fillOval((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
		g.setColor(Color.DARK_GRAY);
		g.drawOval((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
	}
	
	private class RemovingDestinationAnchorPoint extends DestinationAnchorPoint {

		private static final long serialVersionUID = -1917150570420067292L;
		
		public RemovingDestinationAnchorPoint(AnchorPointContainer parent, Point2D initLocation) {
			super(parent, initLocation);
		}

		@Override
		public void setLink(Link link) {
			if (link == null) {
				removeAnchor(this);
			} else {
				restoreAnchor(this);
				super.setLink(link);
			}
		}
	}

	@Override
	public FlowchartBlock executeAndReturnNext(ExecutionManager context) {
		if (sourceAnchors.get(0).getLink() == null) {
			context.raiseError(this, "Not connected.");
			return null;
		}
		if (!(sourceAnchors.get(0).getLink().getDestinationAnchor().getParent() instanceof FlowchartBlock)) {
			context.raiseError(this, "Not connected with flowchart object!");
			return null;
		}
		// don't execute anything
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
