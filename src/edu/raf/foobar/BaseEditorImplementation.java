package edu.raf.foobar;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;

import javax.swing.JPanel;

import edu.raf.gef.app.Resources;
import edu.raf.gef.app.framework.EditorPlugin;

public class BaseEditorImplementation implements EditorPlugin {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4600587151373699739L;

	static final private Resources resources = new Resources(BaseEditorImplementation.class
			.getPackage().getName().replace('.', File.separatorChar)
			+ "/res/");

	private JPanel panel;

	public BaseEditorImplementation() {
		panel = new JPanel() {
			private final Font myfont = Font.decode("Comic-32");

			@Override
			protected void paintComponent(Graphics g1) {
				Graphics2D g = (Graphics2D) g1;
				g.setFont(myfont);
				g.setPaint(new GradientPaint(0.0f, 50.0f, Color.RED, 150.0f, 50.0f, Color.BLACK));
				g.drawString("Hellooo", 50, 50);
			}
		};
	}

	public JPanel getPanel() {
		return panel;
	}

	public static Resources getResources() {
		return resources;
	}
}
