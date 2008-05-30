package edu.raf.flowchart.blocks;

import java.awt.Color;
import java.awt.Graphics2D;

import edu.raf.gef.editor.model.object.DiagramObjectImpl;

public class ExecutionBlock extends DiagramObjectImpl {

	@Override
	public void paint(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

}
