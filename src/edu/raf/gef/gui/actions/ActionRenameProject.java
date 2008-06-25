package edu.raf.gef.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.swing.menus.StandardMenuActions;
import edu.raf.gef.workspace.panel.WorkspaceComponent;
import edu.raf.gef.workspace.project.DiagramProject;

public class ActionRenameProject extends ResourceConfiguredAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8668795587430712033L;
	private WorkspaceComponent workspace;

	public ActionRenameProject(WorkspaceComponent ws) {
		super(ws, StandardMenuActions.RENAME);
		this.workspace = ws;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DiagramProject prj = workspace.getSelectedProject();
		String neuNamen = JOptionPane.showInputDialog("Rename", prj.getProjectName());
		if (neuNamen == null || neuNamen.equals(prj.getProjectName())) {
			return;
		}
		try {
			prj.renameTo(neuNamen);
		} catch (Exception ex) {
			getGeh().handleError("Error", "Couldn't rename project.", ex);
		}
		workspace.repaint();
	}
}
