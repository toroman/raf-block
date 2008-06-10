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

import java.awt.Dimension;

/**
 * Editor vrednosti tipa Double
 * 
 * @author Srecko Toroman
 * 
 */
class DoubleField extends StringField {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4338270321107842455L;
	private static final Dimension DIMENSION = new Dimension(100, 24);

	public DoubleField(PropertyPair propertyPair) {
		super(propertyPair);
	}

	@Override
	public Dimension getPreferredSize() {
		return DIMENSION;
	}

	@Override
	protected void setValue(String value) {
		try {
			Double dbl = new Double(value);
			parent.setValue(dbl);
		} catch (Exception e) {
		}
	}
}