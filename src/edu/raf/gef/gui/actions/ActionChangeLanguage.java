package edu.raf.gef.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import edu.raf.gef.app.Resources;
import edu.raf.gef.gui.MainFrame;

public class ActionChangeLanguage extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8101847482560863543L;

	private MainFrame mainFrame;

	private String locale;

	public ActionChangeLanguage(MainFrame mainFrame, String locale) {
		super(locale);
		this.mainFrame = mainFrame;
		this.locale = locale;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Resources.getGlobal().setProperty("currentLocale", locale);
		JOptionPane.showMessageDialog(mainFrame.getFrame(), Resources.getGlobal().getString(
			"ActionChangeLanguage.afterRestart"));
	}

}
