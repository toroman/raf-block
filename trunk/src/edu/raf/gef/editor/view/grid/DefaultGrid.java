package edu.raf.gef.editor.view.grid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;

import javax.swing.UIManager;

import edu.raf.gef.editor.view.DiagramView;
import edu.raf.gef.editor.view.util.AffineTransformManager;
import edu.raf.gef.util.MathHelper;

public class DefaultGrid implements DiagramGrid {

	private final DiagramView view;
	private double gridDensity;

	public DefaultGrid(DiagramView view) {
		this(view, 10);
	}

	public DefaultGrid(DiagramView view, double density) {
		this.view = view;
		this.gridDensity = density;
	}

	public double getGridDensity() {
		return gridDensity;
	}

	public void setGridDensity(double gridDensity) {
		this.gridDensity = gridDensity;
	}

	@Override
	public void paintGrid(Graphics2D g, Dimension2D gridDimension) {
		double drawToX = gridDimension.getWidth();
		double drawToY = gridDimension.getHeight();

		AffineTransformManager atm = view.getAffineTransformManager();

		Point2D.Double startFrom = new Point2D.Double(0, 0);
		atm.getAffineTransform().transform(startFrom, startFrom);
		// g.drawOval((int)startFrom.getX() - 20, (int)startFrom.y - 20, 40,
		// 40);

		Color backgroundColor = UIManager.getColor("Panel.background");

		double scale = atm.getAffineTransform().getScaleX();

		double smallestUnit = scale * getGridDensity();
		while (smallestUnit < 20)
			smallestUnit *= 10;
		while (smallestUnit > 110)
			smallestUnit /= 10;

		Color[] colors = new Color[3];

		double delta = 1 - MathHelper.setBetween(smallestUnit / 400, 0d, 1d);
		colors[0] = new Color((int) (backgroundColor.getRed() * delta), (int) (backgroundColor
				.getGreen() * delta), (int) (backgroundColor.getBlue() * delta));

		delta = 1 - MathHelper.setBetween(smallestUnit / 40, 0d, 1d);
		colors[1] = new Color((int) (backgroundColor.getRed() * delta), (int) (backgroundColor
				.getGreen() * delta), (int) (backgroundColor.getBlue() * delta));

		colors[2] = Color.BLACK;

		for (int passes = 0; passes < 3; passes++) {
			int kmekmekme = (int) Math.pow(10, passes);
			g.setColor(colors[passes]);

			double verticalLineIndex = (int) (-1 * startFrom.getX() / smallestUnit);
			double horizontalLineIndex = (int) (-1 * startFrom.getY() / smallestUnit);
			for (double x = startFrom.getX() - ((int) (startFrom.getX() / (smallestUnit)))
					* smallestUnit; x <= drawToX; x += smallestUnit, verticalLineIndex++) {
				if (verticalLineIndex % kmekmekme == 0)
					g.drawLine((int) x, (int) 0, (int) x, (int) drawToY);
			}
			for (double y = startFrom.getY() - ((int) (startFrom.getY() / (smallestUnit)))
					* smallestUnit; y <= drawToY; y += smallestUnit, horizontalLineIndex++) {
				if (horizontalLineIndex % kmekmekme == 0)
					g.drawLine((int) 0, (int) y, (int) drawToX, (int) y);
			}
		}
	}

	@Override
	public Point2D snappedToGrid(Point2D location) {
		return location;
	}
}
