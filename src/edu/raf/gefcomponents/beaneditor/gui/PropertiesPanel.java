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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import edu.raf.gef.services.beaneditor.annotations.Property;

/**
 * Prikazuje osobine nekog objekta.
 * 
 * @author Srecko Toroman
 * 
 */
public class PropertiesPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3544816945480445707L;
	private ArrayList<PropertyPair> properties;
	private Object object;
	private JPanel tableNames;
	private JPanel tableValues;
	public JLabel tooltipLabel;
	public JLabel propertiesLabel;
	private JSplitPane splitPane;
	protected Dimension nameDimension = new Dimension(60, 22);
	protected Dimension fieldDimension = new Dimension(60, 22);

	public PropertiesPanel() {
		super();
		properties = new ArrayList<PropertyPair>();
		tableNames = new JPanel();
		tableNames.setFocusCycleRoot(true);
		tableNames.setFocusable(true);
		tableNames.setBackground(Color.WHITE);

		tableValues = new JPanel();
		tableValues.setFocusCycleRoot(true);
		tableValues.setFocusable(true);
		tableValues.setBackground(Color.WHITE);
		BoxLayout layout1 = new BoxLayout(tableNames, BoxLayout.Y_AXIS);
		BoxLayout layout2 = new BoxLayout(tableValues, BoxLayout.Y_AXIS);
		tableNames.setLayout(layout1);
		tableValues.setLayout(layout2);

		tooltipLabel = new JLabel("Tooltip");
		propertiesLabel = new JLabel("Properties:");
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableNames,
				tableValues);
		splitPane.setVisible(false);
		splitPane.setDividerSize(1);
		// layout
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(propertiesLabel, gbc.clone());
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridx = 0;
		gbc.weighty = 0.0;
		gbc.gridy = 1;
		this.add(splitPane, gbc.clone());
		gbc.anchor = GridBagConstraints.SOUTHWEST;
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weighty = 1.0;
		this.add(tooltipLabel, gbc);
	}

	/**
	 * Ovom metodom se saopstava panelu cije propertiese da prikaze.
	 * 
	 * @param activeObject -
	 *            bilo koji Object sa <tt>@Property</tt> anotacijama
	 */
	public void setObject(Object activeObject) {
		this.object = activeObject;
		properties.clear();
		if (activeObject != null) {
			createProperties();
		}
		refreshTable();
	}

	public Object getObject() {
		return object;
	}

	private void refreshTable() {
		tableNames.removeAll();
		tableValues.removeAll();
		for (PropertyPair pair : properties) {
			tableNames.add(pair.namePanel);
			tableValues.add(pair.fieldPanel);
		}
		this.splitPane.setVisible(properties.size() > 0);
		validate();
		repaint();
	}

	private void createProperties() {
		properties = new ArrayList<PropertyPair>();
		Class<?> klass = object.getClass();
		for (Method mth : klass.getMethods()) {
			Annotation ann = mth.getAnnotation(Property.class);
			if (ann == null)
				continue;
			PropertyPair prop = new PropertyPair((Property) ann, mth, object,
					this);
			properties.add(prop);
		}
		Collections.sort(properties);
	}

}
