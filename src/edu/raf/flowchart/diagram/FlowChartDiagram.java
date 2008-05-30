package edu.raf.flowchart.diagram;

import java.awt.Color;
import java.awt.Graphics2D;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.view.DiagramView;

public class FlowChartDiagram extends GefDiagram {
	@Override
	protected DiagramView createView() {
		return new DiagramView(getModel()) {
			@Override
			public void drawDiagram(Graphics2D g) {
				super.drawDiagram(g);
				g.setColor(Color.BLUE);
				g.drawString(getClass().getName() + " : Radi", 20, 187);
			}
		};
	}
}
