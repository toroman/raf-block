package edu.raf.gef.util;

import java.awt.Point;
import java.awt.geom.Point2D;

public class GeomHelper {
	public static Point2D castTo2D (Point point) {
		return new Point2D.Double (point.getX(), point.getY());
	}
}
