package edu.raf.gef.editor.control.state;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.control.edit.EditDrag;
import edu.raf.gef.editor.control.state.util.DiagramDefaultState;
import edu.raf.gef.editor.model.object.AnchorPointContainer;
import edu.raf.gef.editor.model.object.Draggable;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.editor.model.object.Focusable;
import edu.raf.gef.editor.model.object.impl.AnchorPoint;
import edu.raf.gef.editor.model.object.impl.Lasso;
import edu.raf.gef.editor.model.object.impl.Link;
import edu.raf.gef.editor.model.object.impl.ResizeControlPoint;

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

	public DiagramSelectionState(GefDiagram diagram, Drawable newObject) {
		this(diagram);
		mousePressedDrawable = newObject;
		hasDragOccured = false;

		mousePressedIsDraggable = mousePressedDrawable instanceof Draggable;
		mousePressedIsFocusable = mousePressedDrawable instanceof Focusable;
		if (mousePressedDrawable instanceof Draggable) {
			startLocation = ((Draggable) mousePressedDrawable).getLocation();
		}
		if (mousePressedIsFocusable) {
			diagram.getController().clearFocusedObjects();
			diagram.getController().addToFocusedObjects((Focusable) mousePressedDrawable);
		}
	}

	@Override
	public boolean mousePressed(MouseEvent e, Point2D userSpaceLocation) {
		if (super.mousePressed(e, userSpaceLocation))
			return true;
		mousePressedDrawable = diagram.getModel().getDrawableAt(userSpaceLocation);
		boolean isCtrlDown = (e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) != 0;
		boolean isLinkFinalPoint = mousePressedDrawable instanceof ResizeControlPoint
				&& ((ResizeControlPoint) mousePressedDrawable).getParent() instanceof Link
				&& (((Link) ((ResizeControlPoint) mousePressedDrawable).getParent())
						.getResizePoins().getFirst() == mousePressedDrawable || ((Link) ((ResizeControlPoint) mousePressedDrawable)
						.getParent()).getResizePoins().getLast() == mousePressedDrawable);
		boolean isAnchorPoint = mousePressedDrawable instanceof AnchorPoint;
		if ((isAnchorPoint && ((AnchorPoint) mousePressedDrawable).getLink() != null)
				|| (isLinkFinalPoint)) {
			if (isCtrlDown) {
				Link link;
				ResizeControlPoint rcp;
				if (mousePressedDrawable instanceof ResizeControlPoint) {
					link = (Link) ((ResizeControlPoint) mousePressedDrawable).getParent();
					rcp = (ResizeControlPoint) mousePressedDrawable;
				} else {
					link = ((AnchorPoint) mousePressedDrawable).getLink();
					rcp = link.getSourceAnchor() == mousePressedDrawable ? link.getResizePoins()
							.getFirst() : link.getResizePoins().getLast();
				}
				if (rcp == link.getResizePoins().getFirst()) {
					diagram.getController().setState(
						new DiagramReLinkState(diagram, link, true, new DiagramSelectionState(
								diagram)));
					return true;
				}
				if (rcp == link.getResizePoins().getLast()) {
					diagram.getController().setState(
						new DiagramReLinkState(diagram, link, false, new DiagramSelectionState(
								diagram)));
					return true;
				}
			} else {
				if (isLinkFinalPoint) {
					if (((Link) ((ResizeControlPoint) mousePressedDrawable).getParent())
							.getResizePoins().getFirst() == mousePressedDrawable)
						mousePressedDrawable = ((Link) ((ResizeControlPoint) mousePressedDrawable)
								.getParent()).getSourceAnchor();
					else
						mousePressedDrawable = ((Link) ((ResizeControlPoint) mousePressedDrawable)
								.getParent()).getDestinationAnchor();
				}
			}
		}

		hasDragOccured = false;
		mousePressedIsDraggable = mousePressedDrawable != null
				&& mousePressedDrawable instanceof Draggable;
		mousePressedIsFocusable = mousePressedDrawable != null
				&& mousePressedDrawable instanceof Focusable;
		startLocation = userSpaceLocation;
		return false;
	}

	@Override
	public boolean mouseDragged(MouseEvent e, Point2D userSpaceLocation) {
		if (super.mouseDragged(e, userSpaceLocation))
			return true;
		boolean firstTime = !hasDragOccured;
		Set<Focusable> focusedObjects = diagram.getController().getFocusedObjects();
		hasDragOccured = true;
		if (!mousePressedIsDraggable && !mousePressedIsFocusable) {
			if (lasso == null) {
				lasso = new Lasso(diagram.getModel());
				diagram.getModel().addTemporaryDrawable(lasso);
			}
			lasso.setCoords(startLocation, userSpaceLocation);
			return true;
		}
		if (mousePressedIsDraggable && !mousePressedIsFocusable) {
			// Dragging of a non-focusable object is possible, and it won't
			// affect the currently focused object
			if (firstTime)
				((Draggable) mousePressedDrawable).dragStartedAt(startLocation);
			else
				((Draggable) mousePressedDrawable).dragTo(userSpaceLocation);
			return true;
		}
		if (mousePressedIsDraggable && mousePressedIsFocusable) {
			Focusable object = (Focusable) mousePressedDrawable;
			if (!object.isFocused())
				diagram.getController().toggleFocus(object,
					(e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) != 0);
			Set<Link> linksToDrag = new HashSet<Link>();
			for (Focusable focusable : focusedObjects) {
				if (focusable instanceof Draggable) {
					Draggable draggable = (Draggable) focusable;
					if (firstTime)
						draggable.dragStartedAt(startLocation);
					else
						draggable.dragTo(userSpaceLocation);
				}
				if (focusable instanceof AnchorPointContainer) {
					for (Link link : ((AnchorPointContainer) focusable).getLinks()) {
						AnchorPointContainer source = link.getSourceAnchor().getParent();
						AnchorPointContainer dest = link.getDestinationAnchor().getParent();
						if (source instanceof Focusable && dest instanceof Focusable
								&& ((Focusable) source).isFocused()
								&& ((Focusable) dest).isFocused())
							linksToDrag.add(link);
					}
				}
			}
			for (Link link : linksToDrag) {
				if (firstTime)
					for (ResizeControlPoint point : link.getResizePoins())
						point.dragStartedAt(startLocation);
				else
					for (ResizeControlPoint point : link.getResizePoins())
						point.dragTo(userSpaceLocation);
			}
		}
		return true;
	}

	@Override
	public boolean mouseReleased(MouseEvent e, Point2D userSpaceLocation) {
		if (super.mouseReleased(e, userSpaceLocation))
			return true;
		if (hasDragOccured) {
			if (!mousePressedIsDraggable && !mousePressedIsFocusable && lasso != null) {

				// Lasso

				Rectangle2D lassoBounds = lasso.getBounds();
				diagram.getModel().removeTemporaryDrawable(lasso);
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
				return true;
			} else {
				if (diagram.getUndoManager() != null) {
					if (mousePressedIsFocusable) {
						Set<Draggable> draggedAndFocused = new HashSet<Draggable>();
						Set<Focusable> focusedObjects = diagram.getController().getFocusedObjects();
						for (Focusable focusable : focusedObjects)
							if (focusable instanceof Draggable) {
								draggedAndFocused.add((Draggable) focusable);
							}
						diagram.getUndoManager().addEdit(
							new EditDrag(diagram, startLocation, userSpaceLocation,
									draggedAndFocused.toArray(new Draggable[0])));
					} else {
						if (mousePressedIsDraggable)
							diagram.getUndoManager().addEdit(
								new EditDrag(diagram, startLocation, userSpaceLocation,
										(Draggable) mousePressedDrawable));
					}
				}
			}
		} else {
			if (mousePressedIsFocusable) {
				Focusable focusable = (Focusable) mousePressedDrawable;
				diagram.getController().toggleFocus(focusable,
					(e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) != 0);
			}
			if (mousePressedDrawable != null)
				mousePressedDrawable.onClick(e, userSpaceLocation);
			else
				diagram.getController().clearFocusedObjects();
		}
		return true;
	}
}
