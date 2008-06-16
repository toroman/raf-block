package edu.raf.gef.editor.model.object.impl;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;

import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.ControlPointContainer;
import edu.raf.gef.editor.model.object.Focusable;
import edu.raf.gef.editor.model.object.VetoableJavaBean;
import edu.raf.gef.util.TransientObservable;

/**
 * 
 */
public abstract class DiagramObject extends TransientObservable implements Focusable,
		ControlPointContainer, VetoableJavaBean {
	private boolean focused = false;
	private final DiagramModel model;
	private transient VetoableChangeSupport vcs = new VetoableChangeSupport(this);

	private Object readResolve() {
		vcs = new VetoableChangeSupport(this);
		return this;
	}

	public final DiagramModel getModel() {
		return model;
	}

	public DiagramObject(final DiagramModel model) {
		this.model = model;
		this.addObserver(model);
	}

	@Override
	public boolean isFocused() {
		return focused;
	}

	@Override
	public boolean setFocused(boolean focused) {
		boolean old = this.focused;
		this.focused = focused;
		if (focused)
			getModel().moveForward(this);
		setChanged();
		notifyObservers();
		try {
			vcs.fireVetoableChange(Focusable.FOCUSED_PROPERTY, old, focused);
		} catch (PropertyVetoException e) {
			this.focused = old;
			return false;
		}
		return true;
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
	public void onClick(MouseEvent e, Point2D userSpaceLocation) {

	}
}
