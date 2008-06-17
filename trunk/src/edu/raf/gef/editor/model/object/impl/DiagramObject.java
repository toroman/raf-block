package edu.raf.gef.editor.model.object.impl;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.ControlPointContainer;
import edu.raf.gef.editor.model.object.Focusable;
import edu.raf.gef.util.TransientObservable;

/**
 * Basic Diagram object capable of being selected, dragged, painted etc.
 */
public abstract class DiagramObject extends TransientObservable implements Focusable,
		ControlPointContainer {
	private boolean focused = false;
	private DiagramModel model;

	@Override
	public void setParent(DiagramModel diagramModel) {
		this.model = diagramModel;
		this.addObserver(model);
	}

	public final DiagramModel getModel() {
		return model;
	}

	public DiagramObject() {
		super();
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
		return true;
	}

	@Override
	public void onClick(MouseEvent e, Point2D userSpaceLocation) {

	}
}
