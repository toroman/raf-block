package edu.raf.flowchart.diagram.model.object;

import java.awt.Dimension;
import java.awt.geom.Point2D;

/**
 * Ono što je u RAF-UMLu bio UMLBox. Imaće celu funkcionalnost sa resize-om, rotacijom i time, tekst -
 * labele, i ovo je kontejner za LinkAchore (ako se tako ta klasa bude zvala). Podklase ovoga će
 * biti realni objekti, definisani draw() metodom i koordinatama i tipovima anchora. O tom potom.
 * 
 * @author Boca
 * 
 */

public abstract class DiagramObject extends DiagramElement {
	private Point2D position;
	private Dimension dimension;

	protected abstract double getMinWidth();
	protected abstract double getMinHeight();
	
	public Point2D getPosition() {
		return position;
	}

	public void setPosition(Point2D position) {
		this.position = position;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}
	
	public DiagramObject (Point2D upperLeft) {
		this.position = upperLeft;
		this.dimension = new Dimension ((int)getMinWidth(), (int)getMinHeight());
	}
}
