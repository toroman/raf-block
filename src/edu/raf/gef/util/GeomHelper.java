package edu.raf.gef.util;

import java.awt.Point;
import java.awt.geom.Point2D;

public class GeomHelper {
	public static Point2D castTo2D (Point point) {
		return new Point2D.Double (point.getX(), point.getY());
	}
	
	/**
	 * Ova metoda vratja vektor projekcije tachke C na duzh AB. Ako je r izmedju
	 * 0 i 1, onda je projekcija unutar AB, u suprotnom je na pravoj AB ali ne
	 * na duzhi.
	 */
	public static double getProjectionr(double ax, double ay, double bx,
			double by, double cx, double cy) {
		double r_numerator = (cx - ax) * (bx - ax) + (cy - ay) * (by - ay);
		double r_denomenator = (bx - ax) * (bx - ax) + (by - ay) * (by - ay);
		return r_numerator / r_denomenator;
	}

	/**
	 * Ova metoda vratja vaktor projekcije tachke C na duzh AB. Ako je r izmedju
	 * 0 i 1, onda je projekcija unutar AB, u suprotnom je na pravoj AB ali ne
	 * na duzhi.
	 */
	public static double getProjectionr(Point2D a, Point2D b,
			Point2D c) {
		double r_numerator = (c.getX() - a.getX()) * (b.getX() - a.getX()) + (c.getY() - a.getY())
				* (b.getY() - a.getY());
		double r_denomenator = (b.getX() - a.getX()) * (b.getX() - a.getX()) + (b.getY() - a.getY())
				* (b.getY() - a.getY());
		return r_numerator / r_denomenator;
	}

	/**
	 * Slichno metodi getProjectionr, osim shto vratja tu tachku umesto njenog
	 * linearnog faktora.
	 */
	public static Point2D getProjectionPoint(double ax, double ay,
			double bx, double by, double cx, double cy) {
		double r = getProjectionr(ax, ay, bx, by, cx, cy);
		return getProjectionPoint(ax, ay, bx, by, r);
	}

	/**
	 * Generishe tachku projekcije na osnovu duzhi AB i faktora r.
	 */
	public static Point2D getProjectionPoint(double ax, double ay,
			double bx, double by, double r) {
		return new Point2D.Double(ax + r * (bx - ax), ay + r * (by - ay));
	}
	
	/**
	 * Generishe tachku projekcije na osnovu duzhi AB i faktora r.
	 */
	public static Point2D getProjectionPoint(Point2D p1, Point2D p2, double r) {
		return getProjectionPoint(p1.getX(), p1.getY(), p2.getX(), p2.getY(), r);
	}

	/**
	 * Vratja daljinu izmedju dve tachke.
	 */
	public static double pointDistance(double ax, double ay, double bx,
			double by) {
		return Math.sqrt((ax - bx) * (ax - bx) + (ay - by) * (ay - by));
	}

	/**
	 * Vratja daljinu izmedju dve tachke.
	 */
	public static double pointDistance(Point2D a, Point2D b) {
		return pointDistance(a.getX(), a.getY(), b.getX(), b.getY());
	}
	
	/**
	 * Sabira koordinate. Vratja novu tachku, ove dve ostaju nepromenjene
	 * @param a prva
	 * @param b druga
	 * @return (a.x+b.x, a.y+b.y)
	 */
	
	public static Point2D addPoints (Point2D a, Point2D b) {
		return new Point2D.Double (a.getX() + b.getX(), a.getY() + b.getY());
	}
	
	/**
	 * Oduzima koordinate. Vratja novu tachku, ove dve ostaju nepromenjene
	 * @param a prva
	 * @param b druga
	 * @return (a.x-b.x, a.y-b.y)
	 */
	
	public static Point2D substractPoints (Point2D a, Point2D b) {
		return new Point2D.Double (a.getX() - b.getX(), a.getY() - b.getY());
	}
}
