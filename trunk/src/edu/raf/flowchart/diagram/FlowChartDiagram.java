package edu.raf.flowchart.diagram;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.view.grid.DefaultGrid;

public class FlowChartDiagram extends GefDiagram {
	public FlowChartDiagram() {
		super();
		getView().setGrid(new DefaultGrid(getView()));
	}
}
