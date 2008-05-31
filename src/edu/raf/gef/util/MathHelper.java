package edu.raf.gef.util;

public class MathHelper {
	public static synchronized boolean isBetween (double num, double min, double max) {
		return ((num >= min) && (num <= max));
	}
}
