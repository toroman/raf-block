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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

/**
 * Prikazuje ime propertija.
 * 
 * @author Srecko Toroman
 * 
 */
class PropertyName extends JPanel implements FocusListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3412741687219338139L;
	private final PropertyPair parentPair;
	private Font font = Font.decode("Sans");
	private boolean focused = false;

	public PropertyName(PropertyPair parent) {
		super();
		this.parentPair = parent;
		this.setFocusable(true);
		this.addFocusListener(this);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				requestFocus();
			}
		});
	}

	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		if (focused)
			g.setColor(Color.WHITE);
		else
			g.setColor(Color.LIGHT_GRAY);
		Rectangle c = g.getClipBounds();
		g.fill3DRect(0, 0, c.width, c.height, true);
		g.setColor(Color.BLACK);
		Font oldFont = g.getFont();
		g.setFont(font);
		g.drawString(parentPair.getTitle(), 2, 14);
		g.setFont(oldFont);
	}

	public void focusGained(FocusEvent e) {
		focused = true;
		repaint();
	}
	
	@Override
	public Dimension getPreferredSize() {
	    return this.parentPair.parentPanel.nameDimension;
	}
	
	@Override
	public Dimension getMinimumSize() {
	    return this.parentPair.parentPanel.nameDimension;
	}

	public void focusLost(FocusEvent e) {
		focused = false;
		repaint();
	}

	public boolean isFocused() {
		return focused;
	}

}