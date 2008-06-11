package edu.raf.gef.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.raf.gef.app.errors.GraphicalErrorHandler;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.plugin.DiagramFactory;
import edu.raf.gef.plugin.DiagramPlugin;
import edu.raf.gef.workspace.project.DiagramProject;

public class ActionAddDiagram extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2834774936418220606L;

	private MainFrame mainFrame;

	private GraphicalErrorHandler geh;

	/**
	 * Factory.
	 */
	private DiagramFactory creationController;

	private final DiagramPlugin plugin;

	public ActionAddDiagram(MainFrame mf, DiagramPlugin plugin) {
		super(plugin.getResources().getString("plugin.name"));
		this.plugin = plugin;
		this.mainFrame = mf;
		/*
		 * Default factory
		 */
		this.creationController = new DiagramFactory(plugin);
	}

	/**
	 * Reconfigure the factory.
	 * 
	 * @param controller
	 */
	public void setCreationController(DiagramFactory controller) {
		this.creationController = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DiagramProject project = mainFrame.getWorkspaceComponent().getSelectedProject();
		if (project == null) {
			getGeh().handleUserError("Wrong usage", "You must select one project only!");
			return;
		}
		project.addDiagram(creationController.createDiagram());
	}

	protected GraphicalErrorHandler getGeh() {
		if (geh == null)
			geh = new GraphicalErrorHandler(getClass(), mainFrame.getFrame());
		return geh;
	}
}
