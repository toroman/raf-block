package edu.raf.gef.editor.control.state;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Set;
import java.util.Vector;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.control.state.util.DiagramDefaultState;
import edu.raf.gef.editor.model.object.Draggable;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.editor.model.object.Focusable;
import edu.raf.gef.editor.model.object.impl.Lasso;

public class DiagramSelectionState extends DiagramDefaultState {

	/**
	 * The Drawable object found underneath the cursor when the action begun
	 */
	private Drawable mousePressedDrawable;
	/**
	 * Is that object Focusable ili Draggable. False if object null
	 */
	private boolean mousePressedIsFocusable, mousePressedIsDraggable;
	/**
	 * True if drag occured, used to see if the onClick() method should be
	 * invoked
	 */
	private boolean hasDragOccured;
	/**
	 * The location of the cursor when the action begun, used for lasso
	 */
	private Point2D startLocation;
	private Lasso lasso = null;

	public DiagramSelectionState(GefDiagram diagram) {
		super(diagram);
	}

	public DiagramSelectionState(GefDiagram diagram, Draggable newObjectToDrag) {
		this(diagram);
		mousePressedDrawable = newObjectToDrag;
		hasDragOccured = false;
		mousePressedIsDraggable = mousePressedDrawable instanceof Draggable;
		mousePressedIsFocusable = mousePressedDrawable instanceof Focusable;
		startLocation = newObjectToDrag.getLocation();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mousePressedDrawable = diagram.getModel().getDrawableAt(e.getPoint());
		hasDragOccured = false;
		mousePressedIsDraggable = mousePressedDrawable != null
				&& mousePressedDrawable instanceof Draggable;
		mousePressedIsFocusable = mousePressedDrawable != null
				&& mousePressedDrawable instanceof Focusable;
		startLocation = e.getPoint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		boolean firstTime = !hasDragOccured;
		Set<Focusable> focusedObjects = diagram.getController().getFocusedObjects();
		hasDragOccured = true;
		if (!mousePressedIsDraggable && !mousePressedIsFocusable) {
			if (lasso == null) {
				lasso = new Lasso(diagram.getModel());
				diagram.getModel().addElement(lasso);
			}
			lasso.setCoords(startLocation, e.getPoint());
			return;
		}
		if (mousePressedIsDraggable && !mousePressedIsFocusable) {
			// Dragging of a non-focusable object is possible, and it won't
			// affect the currently focused object
			if (firstTime)
				((Draggable) mousePressedDrawable).dragStartedAt(startLocation);
			else
				((Draggable) mousePressedDrawable).dragTo(e.getPoint());
			return;
		}
		if (mousePressedIsDraggable && mousePressedIsFocusable) {
			Focusable object = (Focusable) mousePressedDrawable;
			if (!object.isFocused())
				diagram.getController().toggleFocus(object,
					(e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) != 0);
			for (Focusable focusable : focusedObjects)
				if (focusable instanceof Draggable) {
					Draggable draggable = (Draggable) focusable;
					if (firstTime)
						draggable.dragStartedAt(startLocation);
					else
						draggable.dragTo(e.getPoint());
				}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (hasDragOccured) {
			if (!mousePressedIsDraggable && !mousePressedIsFocusable) {
				Rectangle2D lassoBounds = lasso.getBounds();
				diagram.getModel().removeElement(lasso);
				lasso = null;
				if ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == 0)
					diagram.getController().clearFocusedObjects();
				// The fact that I'm first copying the list of all the
				// focusables may seem weird, but it's to avoid
				// concurrentModificationExceptions. When I put some object in
				// the list of the focused object, it moves it forward in the
				// list (Z-order, right?) so the list of all the drawables is
				// modified while iterating through it.
				Collection<Focusable> allFocusables = new Vector<Focusable>();
				for (Drawable drawable : diagram.getModel().getDrawables())
					if (drawable instanceof Focusable)
						allFocusables.add((Focusable) drawable);
				for (Focusable focusable : allFocusables)
					if (lassoBounds.contains(focusable.getBoundingRectangle()))
						diagram.getController().addToFocusedObjects(focusable);
				return;
			}
		} else {
			if (mousePressedIsFocusable) {
				Focusable focusable = (Focusable) mousePressedDrawable;
				diagram.getController().toggleFocus(focusable,
					(e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) != 0);
			}
			if (mousePressedDrawable != null)
				mousePressedDrawable.onClick(e);
			else
				diagram.getController().clearFocusedObjects();
		}
	}

	@Override
	public void onStateLeft() {
		// diagram.getController().clearFocusedObjects();
	}
}
