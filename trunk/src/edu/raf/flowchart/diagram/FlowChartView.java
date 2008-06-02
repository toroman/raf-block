package edu.raf.flowchart.diagram;

import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.view.DiagramView;
import edu.raf.gef.editor.view.grid.DiagramGrid;

public class FlowChartView extends DiagramView {

	public FlowChartView(DiagramModel model) {
		super(model);
	}

	@Override
	public DiagramGrid getGrid() {
		return null;
	}
}
