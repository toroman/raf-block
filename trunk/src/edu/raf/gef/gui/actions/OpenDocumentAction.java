package edu.raf.gef.gui.actions;

import java.awt.event.ActionEvent;

import edu.raf.gef.app.errors.GraphicalErrorHandler;
import edu.raf.gef.gui.MainFrame;

public class OpenDocumentAction extends ResourceConfiguredAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8226163387224084206L;

	@SuppressWarnings("unused")
	public final static String ID = "open";

	public OpenDocumentAction(MainFrame mainFrame) {
		super(mainFrame);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new GraphicalErrorHandler(getClass(), mainFrame.getFrame()).handleError("actionPerformed",
			"Not implemented yet!", null);
	}

}
