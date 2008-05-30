package edu.raf.gef.editor.model.object;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.util.Collection;
import java.util.Collections;

/**
 * 
 */
public class DiagramObjectImpl implements SelectableElement, DrawableElement, PositionedElement,
		SizableElement, VetoableJavaBean {
	private boolean selected = false;

	private boolean movable = true;

	private final VetoableChangeSupport vcs = new VetoableChangeSupport(this);

	private Point2D location = new Point2D.Double(0, 0);

	private int width;

	private int height;

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public boolean setSelected(boolean selected) {
		boolean old = this.selected;
		this.selected = selected;
		try {
			vcs.fireVetoableChange(SelectableElement.SELECTED_PROPERTY, old, selected);
		} catch (PropertyVetoException e) {
			this.selected = old;
			return false;
		}
		return true;
	}

	@Override
	public void paint(Graphics2D g) {
	}

	@Override
	public void addListener(VetoableChangeListener listener) {
		vcs.addVetoableChangeListener(listener);
	}

	@Override
	public void removeListener(VetoableChangeListener listener) {
		vcs.removeVetoableChangeListener(listener);
	}

	@Override
	public Point2D getLocation() {
		return location;
	}

	@Override
	public boolean isMovable() {
		return movable;
	}

	@Override
	public boolean setLocation(double x, double y) {
		Point2D old = this.location;
		Point2D nov = (Point2D) old.clone();
		this.location = nov;
		try {
			vcs.fireVetoableChange(PositionedElement.LOCATION_PROPERTY, old, nov);
		} catch (PropertyVetoException ex) {
			this.location = old;
			return false;
		}
		return true;
	}

	@Override
	public boolean setMovable(boolean value) {
		boolean old = this.movable;
		this.movable = value;
		try {
			vcs.fireVetoableChange(MOVABLE_PROPERTY, old, value);
		} catch (PropertyVetoException ex) {
			this.movable = old;
			return false;
		}
		return true;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public Collection<ResizePoint> getResizePoints() {
		return Collections.emptyList();
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public boolean isSizable() {
		return true;
	}

	@Override
	public boolean setSizable(boolean sizable) {
		return false;
	}

	@Override
	public boolean setSize(int w, int h) {
		width = w;
		height = h;
		return true;
	}

}
