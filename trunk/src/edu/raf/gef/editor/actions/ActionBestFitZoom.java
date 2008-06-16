package edu.raf.gef.editor.actions;

import java.awt.event.ActionEvent;

import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.actions.ResourceConfiguredAction;
import edu.raf.gef.gui.swing.menus.StandardMenuActions;

public class ActionBestFitZoom extends ResourceConfiguredAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2157632324539292887L;
	private MainFrame mainFrame;

	public ActionBestFitZoom(MainFrame mainFrame) {
		super(mainFrame.getFrame(), StandardMenuActions.BEST_FIT_ZOOM);
		this.mainFrame = mainFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!myIsEnabled()) {
			getGeh().handleUserError(getResources().getString("exception.wrongcontext.title"),
				getResources().getString("exception.wrongcontext.message"));
			return;
		}
		mainFrame.getSelectedDiagram().getView().getAffineTransformManager().bestFit();
	}

	private boolean myIsEnabled() {
		return mainFrame.getSelectedDiagram() != null;
	}

}
