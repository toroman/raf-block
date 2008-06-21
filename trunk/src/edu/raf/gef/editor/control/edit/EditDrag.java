package edu.raf.gef.editor.control.edit;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.model.object.AnchorPointContainer;
import edu.raf.gef.editor.model.object.Draggable;
import edu.raf.gef.editor.model.object.Focusable;
import edu.raf.gef.editor.model.object.impl.Link;
import edu.raf.gef.editor.model.object.impl.ResizeControlPoint;

public class EditDrag extends AbstractUndoableEdit {

	private static final long serialVersionUID = 5546606758853345427L;

	private final GefDiagram diagram;
	private final Point2D startLocation;
	private final Point2D endLocation;
	private final Draggable[] draggables;

	public EditDrag(GefDiagram diagram, Point2D startLocation, Point2D endLocation,
			Draggable... draggables) {
		this.diagram = diagram;
		this.startLocation = startLocation;
		this.endLocation = endLocation;
		this.draggables = draggables;
	}

	private static void obradi(Draggable object, Point2D from, Point2D to) {
		object.dragStartedAt(from);
		object.dragTo(to);
		object.dragEndedAt(to);
	}

	private static void dragObjectsFromTo(GefDiagram diagram, Point2D startLocation,
			Point2D endLocation, Draggable... draggables) {
		if (!(draggables[0] instanceof Focusable)) {
			obradi(draggables[0], startLocation, endLocation);
		} else {
			Set<Link> linksToDrag = new HashSet<Link>();
			for (Draggable draggable : draggables) {
				obradi(draggable, startLocation, endLocation);
				if (draggable instanceof AnchorPointContainer) {
					for (Link link : ((AnchorPointContainer) draggable).getLinks()) {
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
				for (ResizeControlPoint point : link.getResizePoins())
					obradi(point, startLocation, endLocation);
			}
		}
	}

	@Override
	public void undo() throws CannotUndoException {
		dragObjectsFromTo(diagram, endLocation, startLocation, draggables);
		super.undo();
	}

	@Override
	public void redo() throws CannotRedoException {
		dragObjectsFromTo(diagram, startLocation, endLocation, draggables);
		super.redo();
	}

}
