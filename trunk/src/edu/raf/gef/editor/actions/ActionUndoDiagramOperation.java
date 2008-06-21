package edu.raf.gef.editor.actions;

import java.awt.event.ActionEvent;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.actions.ResourceConfiguredAction;
import edu.raf.gef.gui.swing.menus.StandardMenuActions;

public class ActionUndoDiagramOperation extends ResourceConfiguredAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2157632324539292887L;
	boolean isDiagramSelected = false;
	private final GefDiagram diagram;

	public ActionUndoDiagramOperation(MainFrame mainFrame, GefDiagram diagram) {
		super(mainFrame.getFrame(), StandardMenuActions.UNDO);
		this.diagram = diagram;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!isEnabled()) {
			getGeh().handleUserError(getResources().getString("exception.wrongcontext.title"),
				getResources().getString("exception.wrongcontext.message"));
			return;
		}
		diagram.getUndoManager().redo();
	}

	@Override
	public Object getValue(String key) {
		if (isEnabled() && NAME.equals(key)) {
			return diagram.getUndoManager().getRedoPresentationName();
		} else {
			return super.getValue(key);
		}
	}

	@Override
	public boolean isEnabled() {
		return diagram.getUndoManager().canRedo();
	}

}
