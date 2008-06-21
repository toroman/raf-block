package edu.raf.gef.editor.control.edit;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.model.object.impl.AnchorPoint;
import edu.raf.gef.editor.model.object.impl.Link;

public class EditReLink extends AbstractUndoableEdit {

	private static final long serialVersionUID = 765851400298737216L;

	private GefDiagram diagram;
	private Link link;
	private boolean asSource;
	private AnchorPoint currentAnchorPoint;
	private AnchorPoint otherAnchorPoint;

	public EditReLink(GefDiagram diagram, Link link, boolean asSource, AnchorPoint oldAnchor,
			AnchorPoint newAnchor) {
		this.diagram = diagram;
		this.link = link;
		this.asSource = asSource;
		this.currentAnchorPoint = newAnchor;
		this.otherAnchorPoint = oldAnchor;
	}

	private void clear() {
		if (asSource)
			link.getSourceAnchor().setLink(null);
		else
			link.getDestinationAnchor().setLink(null);
	}

	private void swap() {
		if (otherAnchorPoint == null) {
			otherAnchorPoint = currentAnchorPoint;
			currentAnchorPoint = null;
			clear();
			diagram.getModel().removeElement(link);
			link.getSourceAnchor().setLink(null);
			return;
		}
		if (currentAnchorPoint == null) {
			currentAnchorPoint = otherAnchorPoint;
			otherAnchorPoint = null;
			diagram.getModel().addElement(link);

			currentAnchorPoint.setLink(link);
			link.setDestinationPoint(currentAnchorPoint);
			link.getSourceAnchor().setLink(link);

			return;
		}
		AnchorPoint tempAnchor = currentAnchorPoint;
		currentAnchorPoint.setLink(null);
		clear();
		currentAnchorPoint = otherAnchorPoint;
		currentAnchorPoint.setLink(link);
		if (asSource) {
			link.setSourcePoint(currentAnchorPoint);
		} else {
			link.setDestinationPoint(currentAnchorPoint);
		}
		otherAnchorPoint = tempAnchor;
	}

	@Override
	public void undo() throws CannotUndoException {
		swap();
		super.undo();
	}

	@Override
	public void redo() throws CannotRedoException {
		swap();
		super.redo();
	}
}
