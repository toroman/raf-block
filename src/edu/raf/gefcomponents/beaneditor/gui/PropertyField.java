/*
 * RAF UML - Student project for Object oriented programming and design Copyright (C) <2007> Ivan
 * Bocic, Sasa Sijak, Srecko Toroman
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

/**
 * Editor vrednosti apstraktnog tipa
 * 
 * @author Srecko Toroman
 * 
 */
abstract class PropertyField extends JPanel implements MouseListener, FocusListener {
    /**
     * 
     */
    private static final long serialVersionUID = -4338270321107842455L;
    protected final PropertyPair parent;
    private Font font;
    
    public PropertyField(PropertyPair propertyPair) {
        this.parent = propertyPair;
        this.setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g1) {
        Graphics2D g = (Graphics2D)g1;
        g.setFont(font);
        Color prethodnaBoja = g.getColor();
        Rectangle c = g.getClipBounds();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, c.width, c.height);
        g.setColor(Color.DARK_GRAY);
        g.draw3DRect(0, 0, c.width, c.height, true);
        g.setColor(prethodnaBoja);
    }

    @Override
    public Dimension getPreferredSize() {
        return parent.parentPanel.fieldDimension;
    }

    @Override
    public Dimension getMinimumSize() {
        return parent.parentPanel.fieldDimension;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
    // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
    // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    // TODO Auto-generated method stub

    }

    @Override
    public void focusGained(FocusEvent e) {
    // TODO Auto-generated method stub

    }

    @Override
    public void focusLost(FocusEvent e) {
    // TODO Auto-generated method stub

    }

}