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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComboBox;

public class EnumField extends PropertyField {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8001064617234376832L;

	private JComboBox combo = null;

	public EnumField(PropertyPair propertyPair) {
		super(propertyPair);
		setLayout(new BorderLayout());
		Class<?> type = parent.getValue().getClass();
		Object[] values;
		try {
			values = (Object[]) type.getMethod("values").invoke(null);
			Arrays.sort(values);
		} catch (Exception ex) {
			log.log(Level.SEVERE, "Cudna vrsta Enum tipa!", ex);
			values = new String[] { "" };
		}

		combo = new JComboBox(values);
		combo.setEditable(false);
		combo.setSelectedItem(parent.getValue());
		combo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object item = combo.getSelectedItem();
				setValue(item);
			}
		});
		combo.addFocusListener(new FocusAdapter () {
			@Override
			public void focusLost(FocusEvent event) {
				Object item = combo.getSelectedItem();
				setValue(item);
			}
		});
		add(combo, BorderLayout.CENTER);
	}
	
	protected void setValue(Object choice) {
		try {
			parent.setValue(choice);
		} catch (Exception ex) {
			// TODO: prikazi gresku u nekom prozoru
			System.out.println(ex.getMessage());
		}
	}

	private static final Logger log = Logger.getLogger(EnumField.class
			.getName());
}
