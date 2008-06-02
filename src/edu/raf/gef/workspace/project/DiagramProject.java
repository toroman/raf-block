package edu.raf.gef.workspace.project;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.workspace.Workspace;

public class DiagramProject extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1406273266034959927L;

	protected Workspace workspace;

	public DiagramProject(String projectName) {
		super(projectName);
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	public void addDiagram(GefDiagram diagram) {
		this.add(diagram.getTreeModel());
	}

}
