package edu.raf.gef.gui.actions;

import java.awt.event.ActionEvent;

import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.plugin.PluginsManager;

public class ActionExitApplication extends ResourceConfiguredAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8101847482560863543L;

	private PluginsManager manager = null;

	private MainFrame mainFrame;

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO: do it nicer :)
		this.mainFrame.close();
		System.exit(0);
	}

	public PluginsManager getManager() {
		if (manager == null)
			manager = new PluginsManager(mainFrame);
		return manager;
	}

	public ActionExitApplication(MainFrame mainFrame) {
		super(mainFrame.getFrame(), "ActionExitApplication");
		this.mainFrame = mainFrame;
	}
}
