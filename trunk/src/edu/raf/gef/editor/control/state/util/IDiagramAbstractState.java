package edu.raf.gef.editor.control.state.util;

import java.awt.event.KeyListener;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputListener;

public interface IDiagramAbstractState extends MouseInputListener, MouseWheelListener, KeyListener {
	/**
	 * Needs to be invoked to dispose of a state
	 */
	public void onStateLeft();
}
