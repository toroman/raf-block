/*
RAF GEF - Graphical Editor Framework
Copyright (C) <2007>  Srecko Toroman, Ivan Bocic

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
package edu.raf.gef.gui.swing.menus;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.raf.gef.app.Resources;

/**
 * Parses Menu XML file and fills the menu manager with the standard menus
 * configuration.
 * <p>
 * This class doesn't <i>sucks</i>! It is a SAX2.0 handler for our menu xml
 * format.
 * 
 * @since May 28. 2008.
 * @version 1.0
 * @author Srecko Toroman (Срећко Тороман) <a
 *         href="mailto:sreckotoroman@gmail.com">sreckotoroman@gmail.com</a>
 */
public class SAXMenuImporter extends DefaultHandler {
	private MenuManager manager;
	private Stack<String> xpath;
	private Resources resources;

	public SAXMenuImporter(MenuManager manager, Resources resources) {
		this.manager = manager;
		this.resources = resources;
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attr)
			throws SAXException {
		// root element
		if ("menubar".equalsIgnoreCase(name)) {
			xpath = new Stack<String>();
			xpath.add(MenuManager.ROOT);
		} else if ("container".equals(name)) {
			String parentPart = xpath.peek();
			String id = attr.getValue("id");
			if (id == null) {
				// TODO: add XML Scheme instead and don't check manually?
				throw new SAXException("ID musn't be null! Element: " + name);
			}

			MenuPartContainer container = manager.createContainer(parentPart, id);

			String text = attr.getValue("text");
			container.getMenu().setText(text != null ? text : id);

			String mnemonic = attr.getValue("mnemonic");
			if (mnemonic != null)
				container.getMenu().setMnemonic(mnemonic.charAt(0));

			String icon = attr.getValue("icon");
			container.getMenu().setIcon(icon != null ? resources.getIcon(icon) : null);
			xpath.push(id);
		} else if ("part".equals(name)) {
			String parentContainer = xpath.peek();
			String id = attr.getValue("id");
			if (id == null) {
				// TODO: add XML Scheme instead and don't check manually?
				throw new SAXException("ID musn't be null! Element: " + name);
			}
			manager.createPart(parentContainer, id);
			xpath.push(id);
		}
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		if ("menubar".equals(name)) {
			xpath.clear();
			xpath = null;
		} else if ("container".equals(name)) {
			xpath.pop();
		} else if ("part".equals(name)) {
			xpath.pop();
		}
	}

}