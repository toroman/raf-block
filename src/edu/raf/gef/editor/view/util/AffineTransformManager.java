package edu.raf.gef.editor.view.util;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Observable;

import edu.raf.gef.editor.view.DiagramView;
import edu.raf.gef.util.MathHelper;

/**
 * This class is used to manage the AffineTransform of the DiagramView. It might
 * seem obsolete, but it does have it's usages. All the transformation calculae
 * will be put here.
 * 
 */
public class AffineTransformManager extends Observable {
	/**
	 * The transform and it's inverse precalculated
	 */
	private AffineTransform transform, inverse;
	/**
	 * The transform should be made so that the userSpaceLocation matches the deviceSpaceLocation
	 */
	private Point2D userSpaceLocation, deviceSpaceLocation;
	/**
	 * Scale
	 */
	double scaleValue;
	
	private static final double SCALE_ROUNDING_FACTOR = 0.05;
	
	public AffineTransformManager(DiagramView view) {
		transform = new AffineTransform();
		inverse = new AffineTransform();
		scaleValue = 1;
		userSpaceLocation = new Point2D.Double(0, 0);
		// TODO Trebalo bi da prochita dimenziju view-a
		deviceSpaceLocation = new Point2D.Double(145, 130);
		this.addObserver(view);
		matchTransform();
	}
	
	private boolean autoMatchTransform = true;
	
	public synchronized void matchTransform () {
		autoMatchTransform = true;
		transform.setToIdentity();
		double userX = userSpaceLocation.getX();
		double userY = userSpaceLocation.getY();
//		System.out.println(userSpaceLocation + " " + deviceSpaceLocation + " " + scaleValue);
		transform.translate(userX * -1 * scaleValue, userY * -1 * scaleValue);
		transform.scale(scaleValue, scaleValue);
		transform.translate(deviceSpaceLocation.getX() / scaleValue, deviceSpaceLocation.getY() / scaleValue);
		try {
			inverse = transform.createInverse();
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		setChanged();
		notifyObservers();
		clearChanged();
	}
	
	public synchronized void setAutoMatchTransform (boolean bool) {
		autoMatchTransform = bool;
	}
	
	public synchronized AffineTransform getAffineTransform() {
		return transform;
	}	
	
	public synchronized AffineTransform getInverseTransform() {
		return inverse;
	}
	
	public synchronized void setUserSpaceLocation (double x, double y) {
		userSpaceLocation.setLocation(x, y);
		if (autoMatchTransform)
			matchTransform();
	}
	
	public synchronized void setUserSpaceLocation (Point2D point) {
		userSpaceLocation = point;
		if (autoMatchTransform)
			matchTransform();
	}
	
	public synchronized void setDeviceSpaceLocation (Point2D point) {
		deviceSpaceLocation = point;
		if (autoMatchTransform)
			matchTransform();
	}
	
	public synchronized void setDeviceSpaceLocation (double x, double y) {
		deviceSpaceLocation.setLocation(x, y);
		if (autoMatchTransform)
			matchTransform();
	}
	
	public synchronized void setScale (double scale) {
		this.scaleValue = Math.exp(Math.floor(Math.log(scaleValue) / SCALE_ROUNDING_FACTOR) * SCALE_ROUNDING_FACTOR);
		this.scaleValue = MathHelper.setBetween(scale, 0.1, 4);
		if (autoMatchTransform)
			matchTransform();
	}

	public void modifyScale(double scrollAmount) {
		setScale(scaleValue * (1 + scrollAmount * 0.07));
	}

	public String toString() {
		return "User [" + userSpaceLocation.getX() + ", " + userSpaceLocation.getY() + "] " +
				"Device [" + deviceSpaceLocation.getX() + ", " + deviceSpaceLocation.getY() + "] " +
				"Scale " + scaleValue;
	}

	public Point2D getUserSpaceLocation() {
		return userSpaceLocation;
	}

	public Point2D getDeviceSpaceLocation() {
		return deviceSpaceLocation;
	}

	public double getScaleValue() {
		return scaleValue;
	}
}
