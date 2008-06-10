/*
RAF UML - Student project for Object oriented programming and design
Copyright (C) <2007>  Ivan Bocic, Sasa Sijak, Srecko Toroman

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package edu.raf.gefcomponents.beaneditor.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JColorChooser;
import javax.swing.JTextField;

public class ColorField extends PropertyField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 492673908582874579L;
	private JTextField text = null;

	public ColorField(PropertyPair propertyPair) {
		super(propertyPair);
		setLayout(new BorderLayout());
		addMouseListener(this);
	}

	@Override
	public void paintComponent(Graphics g1) {
	    super.paintComponent(g1);
	    Graphics2D g = (Graphics2D) g1;
		// da se primeti transparency - moze malo bolje
		Rectangle c = g.getClipBounds();
		int step = 3;
		Color[] colors = { Color.BLACK, Color.WHITE };
		for (int x = c.width - c.height + 1; x < c.width; x += step) {
			for (int y = 1; y < c.height; y += step) {
				g.setColor(colors[Math.abs((x/step + y/step) % 2)]);
				g.fillRect(x, y, step, step);
			}
		}
		// iscrtaj boju
		Color color = (Color) parent.getValue();
		g.setColor(color);
		g.fill3DRect(c.width - c.height + 1, 1, c.height - 1, c.height - 1,
				true);
		// tekst boje
		g.setColor(Color.BLACK);
		g.drawString(getHexRGBA(color), 2, 14);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (text != null)
			return;

		// open chooser ?
		if (e.getX() >= this.getWidth() - this.getHeight()) {
			Color c = JColorChooser.showDialog(this, "Color", (Color) parent
					.getValue());
			if (c != null) {
				try {
					parent.setValue(c);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				repaint();
			}
			return;
		}
		// clicked on text.
		text = new JTextField();
		text.setText(getHexRGBA((Color) parent.getValue()));
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				textLostFocus();
			}
		});
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						parent.setValue(hexRgbaToColor(text.getText()));
						textLostFocus();
					} catch (Exception ex) {
						// prikazi poruku o gresci
						ex.printStackTrace();
					}
				}
			}
		});
		add(text);
		text.requestFocus();
		validate();
	}

	protected Object hexRgbaToColor(String hex) {
		int r = Integer.valueOf(hex.substring(0, 2), 16);
		int g = Integer.valueOf(hex.substring(2, 4), 16);
		int b = Integer.valueOf(hex.substring(4, 6), 16);
		int a = Integer.valueOf(hex.substring(6, 8), 16);
		return new Color(r, g, b, a);
	}

	private String getHexRGBA(Color color) {
		return hex(color.getRed()) + hex(color.getGreen())
				+ hex(color.getBlue()) + hex(color.getAlpha());
	}

	private String hex(int v) {
		String h = Integer.toHexString(v).toUpperCase();
		if (h.length() < 2)
			return "0" + h;
		return h;
	}

	public void textLostFocus() {
		if (text != null) {
			try {
				parent.setValue(hexRgbaToColor(text.getText()));
			} catch (Exception ex) {
				// TODO Prikazhi greshku
				ex.printStackTrace();
			}
			this.remove(text);
			text = null;
		}
		validate();
		repaint();
	}
}
