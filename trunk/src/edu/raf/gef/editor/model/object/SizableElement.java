package edu.raf.gef.editor.model.object;

import java.util.Collection;

/**
 * Regardless of the stupid name, this interface can be useful.
 */
public interface SizableElement {
	/**
	 * @return Tells us if the object can be resized (at the moment).
	 */
	public boolean isSizable();

	/**
	 * @param sizable
	 */
	public boolean setSizable(boolean sizable);

	/**
	 * @return relative width
	 */
	public int getWidth();

	/**
	 * @return relative height
	 */
	public int getHeight();

	public boolean setSize(int w, int h);

	public Collection<ResizePoint> getResizePoints();
}
