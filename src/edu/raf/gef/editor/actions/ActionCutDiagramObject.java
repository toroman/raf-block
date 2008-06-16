package edu.raf.gef.editor.actions;

import java.awt.event.ActionEvent;

import edu.raf.gef.Main;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.actions.ResourceConfiguredAction;
import edu.raf.gef.gui.swing.menus.StandardMenuActions;

public class ActionCutDiagramObject extends ResourceConfiguredAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2157632324539292887L;
	private MainFrame mainFrame;

	public ActionCutDiagramObject(MainFrame mainFrame) {
		super(mainFrame.getFrame(), StandardMenuActions.CUT);
		this.mainFrame = mainFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!myIsEnabled()) {
			getGeh().handleUserError(getResources().getString("exception.wrongcontext.title"),
				getResources().getString("exception.wrongcontext.message"));
			return;
		}
		Main.simpleClipboard = mainFrame.getSelectedDiagram().getController().cut();
	}

	private boolean myIsEnabled() {
		return mainFrame.getSelectedDiagram() != null
				&& mainFrame.getSelectedDiagram().getController().getFocusedObjects().size() > 0;
	}
}
