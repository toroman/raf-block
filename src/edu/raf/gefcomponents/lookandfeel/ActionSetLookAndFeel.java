package edu.raf.gefcomponents.lookandfeel;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import edu.raf.gef.app.errors.GraphicalErrorHandler;

public class ActionSetLookAndFeel extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8617413697277506264L;

	private String name;

	final private GraphicalErrorHandler geh;

	final private Component component;

	public ActionSetLookAndFeel(String name, Component component) {
		super(name);
		this.component = component;
		this.name = name;
		this.geh = new GraphicalErrorHandler(getClass(), component);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			UIManager.setLookAndFeel(name);
		} catch (Exception ex) {
			geh.handleErrorBlocking("actionPerformed", "Couldn't change look and feel!", ex);
		}
		SwingUtilities.updateComponentTreeUI(component);
	}

}
