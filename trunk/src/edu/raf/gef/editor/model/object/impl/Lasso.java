package edu.raf.gef.editor.model.object.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.util.TransientObservable;

public class Lasso extends TransientObservable implements Drawable {

	private double x, y, width, height;
	
	private final static Stroke dashedStroke = new BasicStroke (1.0f,
		BasicStroke.CAP_BUTT,
		BasicStroke.JOIN_MITER,
		1.0f,
		new float[] {5.0f, 5.0f},
		0);
	
	@Override
	public Rectangle2D getBoundingRectangle() {
		return new Rectangle2D.Double(0, 0, 0, 0);
	}

	@Override
	public Drawable getDrawableUnderLocation(Point2D location) {
		return null;
	}

	@Override
	public void paint(Graphics2D g) {
		Stroke tempStroke = g.getStroke();
		g.setStroke(dashedStroke);
		g.setColor(Color.BLACK);
		g.drawRect((int)x, (int)y, (int)width, (int)height);
		g.setStroke(tempStroke);
	}
	
	public void setCoords (Point2D pointFrom, Point2D pointTo) {
		double minx, maxx, miny, maxy;
		if (pointFrom.getX() < pointTo.getX()) {
			minx = pointFrom.getX();
			maxx = pointTo.getX();
		} else {
			minx = pointTo.getX();
			maxx = pointFrom.getX();
		}
		if (pointFrom.getY() < pointTo.getY()) {
			miny = pointFrom.getY();
			maxy = pointTo.getY();
		} else {
			miny = pointTo.getY();
			maxy = pointFrom.getY();
		}
		this.x = minx;
		this.y = miny;
		this.width = maxx - minx;
		this.height = maxy - miny;
		setChanged();
		notifyObservers();
		clearChanged();
	}
	
	public Rectangle2D getBounds () {
		return new Rectangle2D.Double (x, y, width, height);
	}

	public Lasso(DiagramModel model) {
		super();
		this.addObserver(model);
	}

	@Override
	public void onClick(MouseEvent e, Point2D userSpaceLocation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParent(DiagramModel diagramModel) {
		
	}

}
