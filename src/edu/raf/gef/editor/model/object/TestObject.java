package edu.raf.gef.editor.model.object;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

/**
 * Minimalistički test grafički objekat. Najobičniji pravougaonik.
 * @author Boca
 *
 */

public class TestObject extends DiagramObject {

	public TestObject(Point2D upperLeft) {
		super(upperLeft);
	}
	
	@Override
	protected double getMinHeight() {
		return 100;
	}
	
	@Override
	protected double getMinWidth() {
		return 100;
	}

	public void drawElement(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.drawRect((int) getPosition().getX(), (int) getPosition().getY(), (int) getDimension().getWidth(),
				(int) getDimension().getHeight());
	}
}
