package edu.raf.gef.editor.control.edit;

import java.util.Map;
import java.util.Map.Entry;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.editor.model.object.Focusable;
import edu.raf.gef.editor.model.object.impl.AnchorPoint;
import edu.raf.gef.editor.model.object.impl.Link;

public class EditDeleteDrawables extends AbstractUndoableEdit {
	private static final long serialVersionUID = -2588141434832553766L;
	
	private final GefDiagram diagram;
	private final Map<Drawable, Boolean> removedDrawables;
	
	public EditDeleteDrawables(GefDiagram diagram, Map<Drawable, Boolean> removedDrawables) {
		super();
		this.diagram = diagram;
		this.removedDrawables = removedDrawables;
	}

	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		diagram.getController().clearFocusedObjects();
		for (Entry <Drawable, Boolean> entry: removedDrawables.entrySet()) {
			if (!(entry.getKey() instanceof Link)) {
				diagram.getModel().addElement(entry.getKey());
				if (entry.getValue() == true)
					diagram.getController().addToFocusedObjects((Focusable)entry.getKey());
			}
		}
		for (Entry <Drawable, Boolean> entry: removedDrawables.entrySet()) {
			if (entry.getKey() instanceof Link) {
				Link link = (Link)entry.getKey();
				diagram.getModel().addElement(link);
				AnchorPoint source = link.getSourceAnchor();
				AnchorPoint dest = link.getDestinationAnchor();
				source.setLink(link);
				dest.setLink(link);
				if (entry.getValue() == true)
					diagram.getController().addToFocusedObjects(link);
			}
		}
	}
	
	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		for (Drawable d: removedDrawables.keySet())
			diagram.getModel().removeElement(d);
	}
}
