package edu.raf.gefcomponents.lookandfeel;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ActionSetLookAndFeel extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8617413697277506264L;

	private String className;

	private LookAndFeelPlugin plugin;

	public ActionSetLookAndFeel(String name, LookAndFeelPlugin plugin) {
		this.plugin = plugin;
		this.className = name;
		putValue(NAME, className.substring(className.lastIndexOf('.') + 1));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		plugin.activateLookAndFeel(className);
		plugin.getResources().setProperty("lookAndFeel", className);
		plugin.getResources().saveProperties();
	}
}
