package edu.raf.gef.editor.control;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import edu.raf.gef.app.errors.GraphicalErrorHandler;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.control.state.DiagramSelectionState;
import edu.raf.gef.editor.control.state.util.IDiagramAbstractState;
import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.Focusable;
import edu.raf.gef.util.GeomHelper;

public class DiagramController implements MouseListener, MouseWheelListener, MouseMotionListener,
		KeyListener {
	private IDiagramAbstractState state;
	private GraphicalErrorHandler geh;
	private final GefDiagram diagram;

	/**
	 * A set of focused objects. Should, at all times, be synchronized with the
	 * exact isFocused() values of those objects. No Focusable outside this set
	 * should ever be focused. Use convenience methods like
	 * addToFocusableObjects(), removeFromFocusableObjects(),
	 * clearFocusableObjects() etc.
	 * 
	 * In short, do not invoke setFocus() yourself!
	 */
	private Set<Focusable> focusedObjects;

	/**
	 * Inserts the focusable object into the list of focused Focusables. Invokes
	 * setFocused (true).
	 * 
	 * @param focusable
	 *            the Focusable to insert
	 * @return true if the object was inserted, false if it was already there
	 */
	public boolean addToFocusedObjects(Focusable focusable) {
		if (focusedObjects.add(focusable)) {
			focusable.setFocused(true);
			return true;
		}
		return false;
	}

	/**
	 * Removes the focusable object from the list of focused Focusables. Invokes
	 * setFocused (false).
	 * 
	 * @param focusable
	 *            the Focusable to insert
	 * @return true if the object was removed, false if it wasn't already there
	 */
	public boolean removeFromFocusedObjects(Focusable focusable) {
		if (focusedObjects.remove(focusable)) {
			focusable.setFocused(false);
			return true;
		}
		return false;
	}

	/**
	 * Empties the list, and de-focuses all objects inside it.
	 */
	public void clearFocusedObjects() {
		for (Focusable object : focusedObjects)
			object.setFocused(false);
		focusedObjects.clear();
	}

	/**
	 * If CTRL is pressed (passed as the 2nd parameter) then the focus is
	 * toggled: focusable (the 1st parameter) becomes focused if it wasn't, and
	 * vice-versa. If CTRL wasn't pressed, it defocuses all the objects and then
	 * focuses the parameter one. This is a convenience method, and although it
	 * sounds stupid, it's used in a few occasions, so to avoid C/P.
	 * 
	 * @param focusable
	 * @param isCtrlDown
	 */

	public void toggleFocus(Focusable focusable, boolean isCtrlDown) {
		if (isCtrlDown) {
			if (focusable.isFocused()) {
				removeFromFocusedObjects(focusable);
			} else {
				addToFocusedObjects(focusable);
			}
		} else {
			clearFocusedObjects();
			addToFocusedObjects((Focusable) focusable);
		}
	}

	public DiagramController(GefDiagram diagram) {
		setState(new DiagramSelectionState(diagram));
		this.diagram = diagram;
		focusedObjects = new HashSet<Focusable>();
		Component c = diagram.getView().getCanvas();
		c.addMouseListener(this);
		c.addMouseMotionListener(this);
		c.addMouseWheelListener(this);
		c.addKeyListener(this);
	}

	public void setState(IDiagramAbstractState state) {
		if (this.state != null)
			this.state.onStateLeft();
		this.state = state;
		System.out.println(state.getClass().getName());
	}

	public DiagramModel getModel() {
		return diagram.getModel();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		state.mouseClicked(e, diagram.getView().getAffineTransformManager().getInverseTransform().transform(
			GeomHelper.castTo2D(e.getPoint()), null));
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		state.mousePressed(e, diagram.getView().getAffineTransformManager().getInverseTransform().transform(
			GeomHelper.castTo2D(e.getPoint()), null));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		state.mouseReleased(e, diagram.getView().getAffineTransformManager().getInverseTransform().transform(
			GeomHelper.castTo2D(e.getPoint()), null));
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		state.mouseWheelMoved(e, diagram.getView().getAffineTransformManager().getInverseTransform().transform(
			GeomHelper.castTo2D(e.getPoint()), null));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		state.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		state.keyReleased(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		state.keyTyped(e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		state.mouseDragged(e, diagram.getView().getAffineTransformManager().getInverseTransform().transform(
			GeomHelper.castTo2D(e.getPoint()), null));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		state.mouseMoved(e, diagram.getView().getAffineTransformManager().getInverseTransform().transform(
			GeomHelper.castTo2D(e.getPoint()), null));
	}

	public final Set<Focusable> getFocusedObjects() {
		return Collections.unmodifiableSet(focusedObjects);
	}
}
