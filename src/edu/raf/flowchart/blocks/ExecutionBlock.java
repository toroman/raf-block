package edu.raf.flowchart.blocks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.impl.RectangularObject;

public class ExecutionBlock extends RectangularObject {
	
	public ExecutionBlock(DiagramModel model) {
		super(model);
	}

	@Override
	public Rectangle2D getBoundingRectangle() {
		return new Rectangle2D.Double (getX(), getY(), getWidth(), getHeight());
	}
	
	@Override
	public Dimension2D getMinDimension() {
		return new Dimension (100, 100);
	}
	
	@Override
	protected void paintRectangular(Graphics2D g) {
		g.setColor(Color.WHITE);
//		System.out.println("Crtanje");
		g.fillRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
		g.setColor(Color.DARK_GRAY);
		g.drawRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());		
	}
}
