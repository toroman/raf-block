package edu.raf.gef.editor.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.editor.model.object.Focusable;
import edu.raf.gef.editor.view.grid.DiagramGrid;
import edu.raf.gef.editor.view.util.AffineTransformManager;
import edu.raf.gef.editor.view.util.RepaintAndInertionThread;
import edu.raf.gef.util.TransientObservable;
import edu.raf.gef.util.TransientObserver;

public class DiagramView implements TransientObserver {
	private final DiagramModel model;
	private final JComponent canvas;
	private AffineTransformManager affineTransformManager;
	private DiagramGrid grid;
	private final RepaintAndInertionThread repaintAndInertionThread;
	private boolean antialiasing;

	public DiagramView(DiagramModel model) {
		super();
		this.model = model;
		model.addObserver(this);
		canvas = createCanvas();
		affineTransformManager = new AffineTransformManager(this);
		grid = null;
		repaintAndInertionThread = new RepaintAndInertionThread(this);
		setAntialiasing(true);
		// repaintAndInertionThread.start();
	}

	/**
	 * Override to reconfigure
	 * 
	 */
	protected JComponent createCanvas() {
		JComponent c = new JPanel() {
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
		if (antialiasing)
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
		Collection<Drawable> temporaryDrawables = model.getTemporaryDrawables();
		for (Drawable temp : temporaryDrawables)
			temp.paint(g);
	}

	/**
	 * The invokeLater is used to bundle up all the requests for repainting into
	 * one. Only when all the changes are done will the view be repainted.
	 */

	@Override
	public void update(TransientObservable o, Object arg) {
		getCanvas().repaint();
	}

	public JComponent getCanvas() {
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

	public void setAntialiasing(boolean antialiasing) {
		this.antialiasing = antialiasing;
	}

	public boolean getAntialiasing() {
		return this.antialiasing;
	}
}
