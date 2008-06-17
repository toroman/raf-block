package edu.raf.gef.editor.actions;

import java.awt.event.ActionEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import edu.raf.gef.Main;
import edu.raf.gef.editor.structure.SerializedDiagramObject;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.actions.ResourceConfiguredAction;
import edu.raf.gef.gui.swing.menus.StandardMenuActions;

public class ActionPasteDiagramObject extends ResourceConfiguredAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2157632324539292887L;
	private MainFrame mainFrame;

	public ActionPasteDiagramObject(MainFrame mainFrame) {
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
		Object deserialized = Main.simpleClipboard;
		if (Main.simpleClipboard instanceof SerializedDiagramObject) {
			deserialized = ((SerializedDiagramObject)Main.simpleClipboard).deserialize();

		}
		mainFrame.getSelectedDiagram().getController().paste(deserialized);

	}

	private boolean myIsEnabled() {
		return mainFrame.getSelectedDiagram() != null && Main.simpleClipboard != null;
	}

}
