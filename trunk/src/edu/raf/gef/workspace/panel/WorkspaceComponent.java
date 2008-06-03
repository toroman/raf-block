package edu.raf.gef.workspace.panel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import edu.raf.gef.workspace.Workspace;
import edu.raf.gef.workspace.project.DiagramProject;

public class WorkspaceComponent extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3164075486971114416L;
	private Workspace workspace;

	private JTree trWorkspace;

	public WorkspaceComponent(Workspace workspace) {
		initComponents();
		setWorkspace(workspace);
		setPreferredSize(new Dimension(250, 300));
	}

	private void initComponents() {
		trWorkspace = new JTree();
		trWorkspace.setRootVisible(true);
		Container con = this;
		con.setLayout(new BorderLayout());
		con.add(trWorkspace, BorderLayout.CENTER);
	}

	public void setWorkspace(Workspace workspace) {
		if (workspace == null)
			throw new NullPointerException();
		this.workspace = workspace;
		trWorkspace.setModel(workspace);
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	public JTree getTree() {
		return trWorkspace;
	}

	public DiagramProject getSelectedProject() {
		if (trWorkspace.getSelectionCount() != 1)
			return null;
		TreePath path = trWorkspace.getSelectionPath();
		for (Object node : path.getPath()) {
			if (node instanceof DiagramProject)
				return (DiagramProject) node;
		}
		return null;
	}

	public void setSelectedProject(DiagramProject project) {
		trWorkspace.setSelectionPath(new TreePath(new Object[] { getTree().getModel().getRoot(),
				project }));
	}
}
