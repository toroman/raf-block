package edu.raf.gef.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.raf.gef.gui.help.HelpFrame;

public class ActionShowHelp extends AbstractAction {

	private static final long serialVersionUID = -7939728644735558604L;

	public ActionShowHelp() {
		super ("Manual");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		new HelpFrame().setVisible(true);
	}
}
