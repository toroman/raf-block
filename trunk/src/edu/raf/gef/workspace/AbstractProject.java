package edu.raf.gef.workspace;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class AbstractProject extends DefaultMutableTreeNode {

	protected final Workspace workspace;

	public AbstractProject(Workspace workspace, String projectName) {
		super(projectName);
		this.workspace = workspace;
		workspace.getRoot().add(this);
	}

}
