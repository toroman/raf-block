package edu.raf.gef.diagram.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import edu.raf.gef.app.ResourceHelper;
import edu.raf.gef.app.Resources;
import edu.raf.gef.diagram.BlockDiagram;

public class DiagramView {
	private BlockDiagram diagram;
	private Color backgroundColor;

	public DiagramView(BlockDiagram diagram) {
		this.diagram = diagram;
		backgroundColor = ResourceHelper.stringToColor(Resources.getGlobal().getProperty(
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
