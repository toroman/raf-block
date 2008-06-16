package edu.raf.gef.gui.actions;

import java.awt.event.ActionEvent;

import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.plugin.PluginsManager;
import edu.raf.gef.workspace.project.DiagramProject;

public class ActionNewProject extends ResourceConfiguredAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8101847482560863543L;

	private PluginsManager manager = null;

	private MainFrame mainFrame;

	public ActionNewProject(MainFrame mainFrame) {
		super(mainFrame.getFrame(), "ActionNewProject");
		this.mainFrame = mainFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DiagramProject project = new DiagramProject("Untitled");
		mainFrame.getWorkspace().addProject(project);
		mainFrame.getWorkspaceComponent().setSelectedProject(project);
	}

	public PluginsManager getManager() {
		if (manager == null)
			manager = new PluginsManager(mainFrame);
		return manager;
	}
}