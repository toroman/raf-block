package edu.raf.gef.editor.control.edit;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.model.object.AnchorPointContainer;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.editor.model.object.Focusable;
import edu.raf.gef.editor.model.object.impl.AnchorPoint;
import edu.raf.gef.editor.model.object.impl.Link;

public class EditAddDrawables extends AbstractUndoableEdit {
	
	private static final long serialVersionUID = -5442652399288693734L;
	
	private final Drawable[] drawables;
	private final GefDiagram diagram;

	public static synchronized void addDrawables (GefDiagram diagram, Drawable... drawables) {
		diagram.getController().clearFocusedObjects();
		
		for (Drawable d: drawables)
			if (!(d instanceof Link)) {
				diagram.getModel().addElement(d);
				if (d instanceof Focusable)
					diagram.getController().addToFocusedObjects((Focusable)d);
			}
		for (Drawable d: drawables)
			if (d instanceof Link) {
				diagram.getModel().addElement(d);
				diagram.getModel().moveForward(d);
				diagram.getController().addToFocusedObjects((Focusable)d);
			}
		for (Drawable d: drawables)
			if (d instanceof AnchorPointContainer) {
				AnchorPointContainer container = (AnchorPointContainer)d;
				for (Link link: container.getLinks()) {
					if (!diagram.getModel().getDrawables().contains(link)) {
						for (AnchorPoint ac: container.getAnchorPoints())
							if (ac.getLink().equals(link)) {
								ac.setLink(null);
							}
					}
				}
			}
	}
	
	public EditAddDrawables(GefDiagram diagram, Drawable... drawables) {
		super();
		this.drawables = drawables;
		this.diagram = diagram;
	}

	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		for (Drawable drawable: drawables)
			diagram.getModel().removeElement(drawable);
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		addDrawables(diagram, drawables);
	}
}
