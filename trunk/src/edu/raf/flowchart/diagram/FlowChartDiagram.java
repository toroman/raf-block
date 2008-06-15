package edu.raf.flowchart.diagram;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.view.grid.DefaultGrid;
import edu.raf.gef.workspace.project.DiagramProject;

public class FlowChartDiagram extends GefDiagram {
	public FlowChartDiagram(DiagramProject project) {
		super(project);
		getView().setGrid(new DefaultGrid(getView()));
	}
}
