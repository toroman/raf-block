package edu.raf.gef.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.raf.gef.app.errors.GraphicalErrorHandler;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.plugin.DiagramPlugin;
import edu.raf.gef.plugin.PluginCreationController;

public class CreateDiagramAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2834774936418220606L;

	public static final String ID = "new";

	private MainFrame mainFrame;

	private GraphicalErrorHandler geh;

	/**
	 * Factory.
	 */
	private PluginCreationController creationController;

	private final DiagramPlugin plugin;

	public CreateDiagramAction(MainFrame mf, DiagramPlugin plugin) {
		super(plugin.getResources().getString("plugin.name"));
		this.plugin = plugin;
		this.mainFrame = mf;
		/*
		 * Default factory
		 */
		this.creationController = new PluginCreationController(plugin);
	}

	/**
	 * Reconfigure the factory.
	 * 
	 * @param controller
	 */
	public void setCreationController(PluginCreationController controller) {
		this.creationController = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		mainFrame.showDiagram(creationController.createDiagram(), plugin);
	}

	protected GraphicalErrorHandler getGeh() {
		if (geh == null)
			geh = new GraphicalErrorHandler(getClass(), mainFrame.getFrame());
		return geh;
	}
}
