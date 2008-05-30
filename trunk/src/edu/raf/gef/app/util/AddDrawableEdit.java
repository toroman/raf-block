package edu.raf.gef.app.util;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.DrawableElement;

public class AddDrawableEdit extends AbstractUndoableEdit {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5181813457080488250L;
	private DiagramModel model;
	private DrawableElement element;

	public AddDrawableEdit(DiagramModel model, DrawableElement element) {
		this.model = model;
		this.element = element;
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		model.addElement(element);
	}

	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		model.removeElement(element);
	}
}
