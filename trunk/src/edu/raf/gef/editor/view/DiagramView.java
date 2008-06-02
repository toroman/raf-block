package edu.raf.gef.editor.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.editor.model.object.Focusable;
import edu.raf.gef.editor.view.grid.DiagramGrid;
import edu.raf.gef.editor.view.util.AffineTransformManager;
import edu.raf.gef.editor.view.util.RepaintAndInertionThread;

public class DiagramView implements Observer {
	private final DiagramModel model;
	private final Component canvas;
	private AffineTransformManager affineTransformManager;
	private DiagramGrid grid;
	private final RepaintAndInertionThread repaintAndInertionThread;

	public DiagramView(DiagramModel model) {
		this.model = model;
		model.addObserver(this);
		canvas = createCanvas();
		affineTransformManager = new AffineTransformManager(this);
		grid = null;
		repaintAndInertionThread = new RepaintAndInertionThread (this);
		repaintAndInertionThread.start();
	}

	/**
	 * Override to reconfigure
	 * 
	 */
	protected Component createCanvas() {
		Component c = new JPanel() {
			private static final long serialVersionUID = -4189098950039589957L;

			@Override
			public void paintComponent(Graphics g1) {
				Graphics2D g = (Graphics2D) g1;
				DiagramView.this.drawDiagram(g);
			}
		};

		c.setBackground(Color.GRAY);
		return c;
	}

	protected void drawDiagram(Graphics2D g) {
		Rectangle r = g.getClipBounds();
		g.clearRect(0, 0, (int) r.getWidth() + 1, (int) r.getHeight() + 1);
		if (getGrid() != null)
			getGrid().paintGrid(g, canvas.getSize());
		g.transform(affineTransformManager.getAffineTransform());
		List<Drawable> focused = new LinkedList<Drawable>();
		for (Drawable de : model.getDrawables()) {
			if (de instanceof Focusable && ((Focusable) de).isFocused())
				focused.add(de);
			else
				de.paint(g);
		}
		for (Drawable de : focused)
			de.paint(g);
	}

	/**
	 * The invokeLater is used to bundle up all the requests for repainting into
	 * one. Only when all the changes are done will the view be repainted.
	 */

	@Override
	public void update(Observable o, Object arg) {
		getCanvas().repaint();
		return;
	}

	public Component getCanvas() {
		return canvas;
	}

	public AffineTransformManager getAffineTransformManager() {
		return affineTransformManager;
	}

	public DiagramGrid getGrid() {
		return grid;
	}

	public void setGrid(DiagramGrid grid) {
		this.grid = grid;
	}

	public DiagramModel getModel() {
		return model;
	}

	public RepaintAndInertionThread getRepaintAndInertionThread() {
		return repaintAndInertionThread;
	}
}
