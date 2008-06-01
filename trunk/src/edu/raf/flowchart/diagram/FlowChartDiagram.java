package edu.raf.flowchart.diagram;

import java.awt.Graphics2D;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.view.DiagramView;
import edu.raf.gef.editor.view.grid.DefaultGrid;

public class FlowChartDiagram extends GefDiagram {
	public FlowChartDiagram() {
		super();
		getView().setGrid(new DefaultGrid(getView()));
	}
	
	@Override
	protected DiagramView createView() {
		return new DiagramView(getModel()) {
			@Override
			public void drawDiagram(Graphics2D g) {
				super.drawDiagram(g);
			}
		};
	}
}
