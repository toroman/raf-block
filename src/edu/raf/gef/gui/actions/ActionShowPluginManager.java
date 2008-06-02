package edu.raf.gef.gui.actions;

import java.awt.event.ActionEvent;

import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.plugin.PluginsManager;

public class ActionShowPluginManager extends ResourceConfiguredAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8101847482560863543L;

	private PluginsManager manager = null;

	private MainFrame mainFrame;

	@Override
	public void actionPerformed(ActionEvent e) {
		getManager().open(mainFrame.getFrame());
	}

	public PluginsManager getManager() {
		if (manager == null)
			manager = new PluginsManager(mainFrame);
		return manager;
	}

	public ActionShowPluginManager(MainFrame mainFrame) {
		super(mainFrame.getFrame(), "ActionShowPluginManager");
		this.mainFrame = mainFrame;
	}
}
