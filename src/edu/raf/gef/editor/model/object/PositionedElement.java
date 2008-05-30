package edu.raf.gef.editor.model.object;

import java.awt.geom.Point2D;

/**
 * Elements with a "coordinate", which may be moved.
 */
public interface PositionedElement {
	/**
	 * Dynamic property telling is object currently movable or "locked" in
	 * place.
	 * 
	 * @return
	 */
	public boolean isMovable();

	/**
	 * Enables "locking" of the object.
	 * 
	 * @param value
	 */
	public boolean setMovable(boolean value);

	/**
	 * 
	 * @return Use this to find where is the object, usually this should be a
	 *         center.
	 */
	public Point2D getLocation();

	/**
	 * Use this to change the position. May throw Vetoable if the bean is
	 * vetoable exception.
	 * 
	 * @param x
	 * @param y
	 */
	public boolean setLocation(double x, double y);

	public final String LOCATION_PROPERTY = "location";
	
	public final String MOVABLE_PROPERTY = "movable";
}
