package edu.raf.gef.workspace.project;

import java.io.OutputStream;

import javax.swing.tree.DefaultMutableTreeNode;

import com.thoughtworks.xstream.XStream;

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
		workspace.insertNodeInto(diagram.getTreeModel(workspace), this, 0);
	}

	public void saveDiagram(GefDiagram diagram) {
		XStream xs = diagram.getSerializator();
		OutputStream os = null;
		xs.toXML(diagram, os);
	}

}
