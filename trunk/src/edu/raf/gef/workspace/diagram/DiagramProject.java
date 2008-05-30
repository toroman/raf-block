package edu.raf.gef.workspace.diagram;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.workspace.AbstractProject;
import edu.raf.gef.workspace.Workspace;

public class DiagramProject extends AbstractProject {

	protected final GefDiagram diagram;

	public DiagramProject(Workspace workspace, GefDiagram diagram) {
		super(workspace, diagram.getModel().getTitle());
		this.diagram = diagram;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1537366729155566784L;

}
