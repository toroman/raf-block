package edu.raf.gef.gui.actions;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.AbstractAction;

import edu.raf.gef.app.errors.GraphicalErrorHandler;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.plugin.DiagramPlugin;
import edu.raf.gef.workspace.project.DiagramProject;

public class ActionAddDiagram extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2834774936418220606L;

	private MainFrame mainFrame;

	private GraphicalErrorHandler geh;

	private final DiagramPlugin plugin;

	public ActionAddDiagram(MainFrame mf, DiagramPlugin plugin) {
		super(plugin.getResources().getString("plugin.name"));
		this.plugin = plugin;
		this.mainFrame = mf;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DiagramProject project = mainFrame.getWorkspaceComponent()
				.getSelectedProject();
		if (project == null) {
			getGeh().handleUserError("Wrong usage",
					"You must select one project only!");
			return;
		}
		GefDiagram diagram = null;
		try {
			diagram = plugin.getDiagramClass().getConstructor(
					DiagramProject.class).newInstance(project);
		} catch (Throwable t) {
			getGeh().handleErrorBlocking("actionPerformed",
					"Couldn't create new diagram!", t);
			return;
		}
		mainFrame.getWorkspaceComponent().setSelected(
				diagram.getTreeModel(mainFrame.getWorkspace()));
	}

	protected GraphicalErrorHandler getGeh() {
		if (geh == null)
			geh = new GraphicalErrorHandler(getClass(), mainFrame.getFrame());
		return geh;
	}
}
