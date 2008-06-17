package edu.raf.flowchart.diagram;

import java.io.File;

import edu.raf.gef.app.exceptions.GefException;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.view.grid.DefaultGrid;
import edu.raf.gef.gui.ActionContextController;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.workspace.project.DiagramProject;

public class FlowChartDiagram extends GefDiagram {
	public FlowChartDiagram(DiagramProject project) {
		super(project);
		getView().setGrid(new DefaultGrid(getView()));
	}

	public FlowChartDiagram(DiagramProject project, DiagramModel model) {
		super(project, model);
		getView().setGrid(new DefaultGrid(getView()));
	}

	@Override
	public void onDeactivated(MainFrame main, ActionContextController nextContext) {

	}

	@Override
	public File getDiagramFile() throws GefException {
		return new File(this.project.getProjectFolder(), getModel().getTitle() + ".fc");
	}
}
