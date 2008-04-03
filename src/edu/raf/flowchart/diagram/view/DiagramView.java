package edu.raf.flowchart.diagram.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import edu.raf.flowchart.app.ResourceHelper;
import edu.raf.flowchart.app.Resources;
import edu.raf.flowchart.diagram.Diagram;

public class DiagramView {
	private Diagram diagram;
	private Color backgroundColor;

	public DiagramView(Diagram diagram) {
		this.diagram = diagram;
		backgroundColor = ResourceHelper.stringToColor(Resources.getInstance().getProperty(
				"diagramBackgroundColor"));
	}

	public void drawDiagram(Graphics2D g, Dimension panelSize) {
		g.setColor(backgroundColor);
		g.fillRect(0, 0, (int) panelSize.getWidth() + 1, (int) panelSize.getHeight() + 1);

		for (int i = diagram.getModel().getElements().size() - 1; i >= 0; i--)
			diagram.getModel().getElements().get(i).drawElement(g);
	}

	public void redraw() {
		diagram.getPanel().repaint();
	}
}
