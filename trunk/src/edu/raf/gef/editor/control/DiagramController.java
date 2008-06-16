package edu.raf.gef.editor.control;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.raf.gef.app.errors.GraphicalErrorHandler;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.control.state.DiagramSelectionState;
import edu.raf.gef.editor.control.state.util.IDiagramAbstractState;
import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.AnchorPointContainer;
import edu.raf.gef.editor.model.object.Focusable;
import edu.raf.gef.editor.model.object.impl.Link;
import edu.raf.gef.util.GeomHelper;
import edu.raf.gef.util.TransientObservable;

public class DiagramController implements MouseListener, MouseWheelListener, MouseMotionListener,
		KeyListener {
	private IDiagramAbstractState state;
	private GraphicalErrorHandler geh;
	private final GefDiagram diagram;
	private List<GefFocusListener> focusListeners = new ArrayList<GefFocusListener>();

	protected synchronized void fireFocusEvent(Focusable f, boolean given) {
		GefFocusEvent event = new GefFocusEvent(this, f, given == true ? GefFocusEvent.FOCUS_GIVEN
				: GefFocusEvent.FOCUS_LOST);
		for (GefFocusListener gf : focusListeners) {
			gf.focusChanged(event);
		}
	}

	protected synchronized void fireFocusEvent(GefFocusEvent event) {
		for (GefFocusListener gf : focusListeners) {
			gf.focusChanged(event);
		}
	}

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
	private Set<Focusable> unmodifiableFocusedObjects;

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
			fireFocusEvent(focusable, true);
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
			fireFocusEvent(focusable, false);
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
		fireFocusEvent(new GefFocusEvent(this, null, GefFocusEvent.FOCUS_CLEARED));
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
		unmodifiableFocusedObjects = Collections.unmodifiableSet(focusedObjects);
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
		// plz don't use System.out for logging!
		// System.out.println(state.getClass().getName());
	}

	public DiagramModel getModel() {
		return diagram.getModel();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		state.mouseClicked(e, diagram.getView().getAffineTransformManager().getInverseTransform()
				.transform(GeomHelper.castTo2D(e.getPoint()), null));
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		state.mousePressed(e, diagram.getView().getAffineTransformManager().getInverseTransform()
				.transform(GeomHelper.castTo2D(e.getPoint()), null));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		state.mouseReleased(e, diagram.getView().getAffineTransformManager().getInverseTransform()
				.transform(GeomHelper.castTo2D(e.getPoint()), null));
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		state.mouseWheelMoved(e, diagram.getView().getAffineTransformManager()
				.getInverseTransform().transform(GeomHelper.castTo2D(e.getPoint()), null));
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
		state.mouseDragged(e, diagram.getView().getAffineTransformManager().getInverseTransform()
				.transform(GeomHelper.castTo2D(e.getPoint()), null));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		state.mouseMoved(e, diagram.getView().getAffineTransformManager().getInverseTransform()
				.transform(GeomHelper.castTo2D(e.getPoint()), null));
	}

	public final Set<Focusable> getFocusedObjects() {
		return unmodifiableFocusedObjects;
	}

	// Cut/Copy/Paste section

	/**
	 * Cuts the currently focused objects.
	 * 
	 * @return Set<Focusable>
	 */
	public Object cut() {
		Object obj = copy();
		for (Focusable focusable : getFocusedObjects()) {
			if (focusable instanceof AnchorPointContainer) {
				for (Link link : ((AnchorPointContainer) focusable).getLinks()) {
					getModel().removeElement(link);
					link.deleteObserver(getModel());
				}
			}
			getModel().removeElement(focusable);
			if (focusable instanceof TransientObservable)
				((TransientObservable)focusable).deleteObserver(getModel());
		}
		clearFocusedObjects();
		return obj;
	}

	/**
	 * Copies the currently focused objects.
	 * 
	 * @return Set<Focusable>
	 */
	public Object copy() {
		Set<Focusable> objectsToCopy = new HashSet<Focusable>();
		for (Focusable focusable : getFocusedObjects())
			if (focusable instanceof Link) {
				// A focused link
				Link link = (Link) focusable;
				boolean sourceok = false;
				if (link.getSourceAnchor() != null) {
					AnchorPointContainer source = link.getSourceAnchor().getParent();
					if (source instanceof Focusable && ((Focusable) source).isFocused())
						sourceok = true;
				}
				boolean destinationok = false;
				if (link.getDestinationAnchor() != null) {
					AnchorPointContainer dest = link.getDestinationAnchor().getParent();
					if (dest instanceof Focusable && ((Focusable) dest).isFocused())
						destinationok = true;
				}
				if (destinationok && sourceok) {
					// This link should be copied
					objectsToCopy.add(link);
				}
			} else {
				objectsToCopy.add(focusable);
			}
		return objectsToCopy;
	}

	/**
	 * Inserts the given object into the diagram. Object needs to be a
	 * Focusable, or a collection of Focusables.
	 */
	@SuppressWarnings("unchecked")
	public void paste(Object object) {
		Set<Focusable> objectsToCopy;

		if (object instanceof Focusable) {
			objectsToCopy = new HashSet<Focusable>();
			objectsToCopy.add((Focusable)object);
		} else if (object instanceof Collection) {
			objectsToCopy = new HashSet<Focusable>();
			boolean isFocusableSet = true;
			for (Object o : objectsToCopy)
				if (!(o instanceof Focusable))
					isFocusableSet = false;
				else
					objectsToCopy.add((Focusable)o);
			if (!isFocusableSet)
				return;
		} else
			return;

		clearFocusedObjects();
		for (Focusable focusable : objectsToCopy) {
			getModel().addElement(focusable);
			if (focusable instanceof TransientObservable)
				((TransientObservable)focusable).deleteObserver(getModel());
			addToFocusedObjects(focusable);
		}
	}
}
