package edu.raf.gef.gui.actions;

import java.awt.event.ActionEvent;

import edu.raf.gef.gui.MainFrame;

public class OpenDocumentAction extends ResourceConfiguredAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8226163387224084206L;

	@SuppressWarnings("unused")
	public final static String ID = "open";

	public OpenDocumentAction(MainFrame mainFrame) {
		super(mainFrame.getFrame(), ID);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		getGeh().handleError("actionPerformed", "Not implemented yet!", null);
	}

}
