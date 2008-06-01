package edu.raf.gef.editor.view.grid;

import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

public interface DiagramGrid {
	/**
	 * Paints the grid onto the specified graphics. That is before any
	 * trasformations have been made, in order to preserve line width.
	 * 
	 * @param g
	 *            graphics on which to paint on
	 * @param gridDimension
	 *            the viewport dimensions
	 */
	public void paintGrid(Graphics2D g, Dimension2D gridDimension);

	/**
	 * Calculates the location where the argument-passed point should be if grid
	 * snappig were on.
	 * 
	 * @param location
	 *            source point
	 * @return snapped, destination point
	 */
	public Point2D snappedToGrid(Point2D location);
}
