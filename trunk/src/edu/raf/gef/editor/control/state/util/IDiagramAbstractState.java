package edu.raf.gef.editor.control.state.util;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

public interface IDiagramAbstractState {
	/**
	 * Needs to be invoked to dispose of a state
	 */
	public void onStateLeft();

	/**
	 * Why make this methods return a boolean value? If some state takes care of
	 * an action, it should return true. If the subclass (if any) should take
	 * care of it, return false. For example, I don't want a subclass of
	 * DiagramDefaultState (which takes care of scrolling) to do anything when
	 * scrolling takes place.
	 * 
	 * @param e event
	 * @return has the event been taken care of
	 */
	public boolean keyPressed(KeyEvent e);

	public boolean keyReleased(KeyEvent e);

	public boolean keyTyped(KeyEvent e);

	public boolean mouseClicked(MouseEvent e, Point2D userSpaceLocation);

	public boolean mouseDragged(MouseEvent e, Point2D userSpaceLocation);

	public boolean mouseEntered(MouseEvent e, Point2D userSpaceLocation);

	public boolean mouseExited(MouseEvent e, Point2D userSpaceLocation);

	public boolean mouseMoved(MouseEvent e, Point2D userSpaceLocation);

	public boolean mousePressed(MouseEvent e, Point2D userSpaceLocation);

	public boolean mouseReleased(MouseEvent e, Point2D userSpaceLocation);

	public boolean mouseWheelMoved(MouseWheelEvent e, Point2D userSpaceLocation);
}
