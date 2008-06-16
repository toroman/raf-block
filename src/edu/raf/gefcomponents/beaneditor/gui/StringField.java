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
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;

/**
 * Editor vrednosti tipa String
 * 
 * @author Srecko Toroman
 * 
 */
class StringField extends PropertyField {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4338270321107842455L;
	private JTextField text = null;

	public StringField(PropertyPair propertyPair) {
		super(propertyPair);
		setLayout(new BorderLayout());
		addMouseListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.black);
		g.drawString(parent.getValue().toString(), 4, 17);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (text != null)
			return;
		text = new JTextField(parent.getValue().toString());
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					setValue(text.getText());
					textLostFocus();
				}
			}
		});
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				textLostFocus();
			}
		});
		add(text);
		text.requestFocus();
		validate();
	}

	protected void setValue(String value) {
		try {
			parent.setValue(value);
		} catch (Exception ex) {
			// TODO: prikazi gresku u nekom prozoru
			System.out.println(ex.getMessage());
		}
	}

	public void textLostFocus() {
		if (text != null) {
			setValue(text.getText());
			this.remove(text);
			text = null;
		}
		validate();
		repaint();
	}
}