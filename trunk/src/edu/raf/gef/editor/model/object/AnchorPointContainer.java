package edu.raf.gef.editor.model.object;

import java.awt.geom.Point2D;
import java.util.Collection;

import edu.raf.gef.editor.model.object.impl.AnchorPoint;
import edu.raf.gef.editor.model.object.impl.Link;

public interface AnchorPointContainer extends ControlPointContainer {
	public AnchorPoint getSourcePointAt (Point2D location, Link link);
	public AnchorPoint getDestinationPointAt (Point2D location, Link link);
	public Collection <Link> getLinks();
	public Collection <AnchorPoint> getAnchorPoints();
}
