package edu.raf.gef.editor.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.DrawableElement;
import edu.raf.gef.editor.model.object.PositionedElement;

public class DiagramView implements Observer {
	private final DiagramModel model;
	private final Component canvas;
	private AffineTransform matrix;

	public DiagramView(DiagramModel model) {
		this.model = model;
		model.addObserver(this);
		canvas = createCanvas();
		matrix = new AffineTransform();
		matrix.setToIdentity();
	}

	/**
	 * Override to reconfigure
	 * 
	 */
	protected Component createCanvas() {
		Component c = new JPanel() {
			private static final long serialVersionUID = -4189098950039589957L;

			@Override
			public void paint(Graphics g1) {
				Graphics2D g = (Graphics2D) g1;
				DiagramView.this.drawDiagram(g);
			}
		};

		c.setBackground(Color.GRAY);
		return c;
	}

	public void drawDiagram(Graphics2D g) {
		Rectangle r = g.getClipBounds();
		g.clearRect(0, 0, (int) r.getWidth() + 1, (int) r.getHeight() + 1);
		g.setColor(Color.RED);
		g.drawString("Hallo from DiagramView.drawDiagram", 100, 50);
		g.setTransform(matrix);

		for (DrawableElement de : model.getDrawables()) {
			if (de instanceof PositionedElement) {
				PositionedElement pe = (PositionedElement) de;
				Point2D location = pe.getLocation();
				g.translate(location.getX(), location.getY());
				de.paint(g);
				g.translate(-location.getX(), -location.getY());
			} else {
				de.paint(g);
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o != model)
			return;
		canvas.repaint();
	}

	public Component getCanvas() {
		return canvas;
	}
}
