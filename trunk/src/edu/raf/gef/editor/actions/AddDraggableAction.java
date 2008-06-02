package edu.raf.gef.editor.actions;

import java.awt.event.ActionEvent;

import edu.raf.gef.app.IResources;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.control.state.DiagramAddDraggableState;
import edu.raf.gef.editor.control.state.DiagramSelectionState;
import edu.raf.gef.editor.model.object.Draggable;
import edu.raf.gef.gui.SelectedDiagramProvider;
import edu.raf.gef.gui.actions.ResourceConfiguredAction;

public abstract class AddDraggableAction extends ResourceConfiguredAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -721380454889573427L;
	private final Class<? extends Draggable> type;
	private SelectedDiagramProvider selDiagram;

	public AddDraggableAction(Class<? extends Draggable> type, String ID,
			SelectedDiagramProvider selDiagram) {
		super(null, ID);
		this.type = type;
		this.selDiagram = selDiagram;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final GefDiagram dg = selDiagram.getSelectedDiagram();
		if (dg == null) {
			getGeh().handleUserError("Can't add object", "Diagram not selected!");
			return;
		}
		DiagramSelectionState defState = new DiagramSelectionState(dg);
		dg.getController().setState(
			new DiagramAddDraggableState (dg, type, defState, null));
//		Draggable de;
//		try {
//			de = this.type.newInstance();
//		} catch (Exception ex) {
//			getGeh().handleError("actionPerformed", "Error creating object!", ex);
//			return;
//		}
//		dg.getUndoManager().undoableEditHappened(
//			new UndoableEditEvent(dg.getModel(), new AddDrawableEdit(dg.getModel(), de)));
	}
	
	public GefDiagram getSelectedDiagram () {
		return selDiagram.getSelectedDiagram();
	}

	/**
	 * Since add drawable action is used by plugins (mostly), the getResources
	 * function is abstractized here :)
	 */
	protected abstract IResources getResources();
}
