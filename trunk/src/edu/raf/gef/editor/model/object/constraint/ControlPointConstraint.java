package edu.raf.gef.editor.model.object.constraint;

import java.awt.geom.Point2D;

public interface ControlPointConstraint {
	public Point2D updateLocation (Point2D oldLocation);
}
