package edu.raf.gef.util;

public class MathHelper {
	public static boolean isBetween (double num, double min, double max) {
		return ((num >= min) && (num <= max));
	}
	
	public static double setBetween (double num, double min, double max) {
		if (num < min)
			num = min;
		if (num > max)
			num = max;
		return num;
	}
}
