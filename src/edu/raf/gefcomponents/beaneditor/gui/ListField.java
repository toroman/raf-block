package edu.raf.gefcomponents.beaneditor.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ListField extends PropertyField {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8784981716228472815L;
	private Class<?> type;
	private JPanel editori = null;
	private ArrayList<Object> args;
	private Component parentComponent;

	public ListField(PropertyPair propertyPair, Component parent) {
		super(propertyPair);
		this.parentComponent = parent;
		JButton btn = new JButton("...");
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.EAST;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		type = super.parent.property.type(); // tip Liste
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onShowEditor();
			}
		});
		add(btn, gbc);
	}

	@SuppressWarnings("unchecked")
	protected void onShowEditor() {
		args = new ArrayList<Object>((List) parent.getValue());
		final JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(parentComponent),
				"List editor", true);
		dlg.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int w = dlg.getWidth();
				int h = dlg.getPreferredSize().height;
				dlg.setSize(w, h);
			}
		});
		Container c = dlg.getContentPane();
		c.setLayout(new BorderLayout());
		JPanel komande = new JPanel();
		komande.setLayout(new FlowLayout(FlowLayout.RIGHT));
		c.add(komande, BorderLayout.SOUTH);

		JButton cancel = new JButton("Odustani");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dlg.setVisible(false);
				dlg.dispose();
			}
		});
		komande.add(cancel);

		JButton add = new JButton("Dodaj");
		add.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent e) {
				try {
					args.add(type.newInstance());
					refreshList(dlg);
				} catch (Exception ex) {
					log.log(Level.SEVERE, "Greska kod pravljenja instance", ex);
				}
			}
		});
		komande.add(add);

		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					parent.setValue(args);
				} catch (Exception ex) {
					log.log(Level.SEVERE, "Nisam uspeo da setujem vrednost!", ex);
				} finally {
					dlg.setVisible(false);
					dlg.dispose();
				}
			}
		});
		komande.add(ok);

		komande.updateUI();
		editori = new JPanel();
		c.add(editori, BorderLayout.CENTER);
		refreshList(dlg);
		dlg.pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		dlg.setLocation(screen.width / 2 - dlg.getWidth() / 2, screen.height / 2 - dlg.getHeight()
				/ 2);
		dlg.setVisible(true);

	}

	@SuppressWarnings("unchecked")
	protected void refreshList(final JDialog dlg) {
		editori.removeAll();
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[] { 16, 200 };
		editori.setLayout(layout);
		GridBagConstraints gbcDelete = new GridBagConstraints();
		GridBagConstraints gbcPanel = new GridBagConstraints();
		gbcDelete.gridx = 0;
		gbcDelete.fill = GridBagConstraints.VERTICAL;
		gbcDelete.gridy = -1;

		gbcPanel.gridx = 1;
		gbcPanel.fill = GridBagConstraints.HORIZONTAL;
		gbcPanel.weightx = 1.0;
		gbcPanel.gridy = -1;

		for (Object each : args) {
			gbcPanel.gridy++;
			gbcDelete.gridy++;
			final PropertiesPanel prop = new PropertiesPanel();
			prop.remove(prop.propertiesLabel);
			prop.remove(prop.tooltipLabel);
			prop.setObject(each);

			final JButton delete = new JButton("X");
			ActionListener deleteListener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					args.remove(prop.getObject());
					editori.remove(prop);
					editori.remove(delete);
					editori.updateUI();
					Rectangle r = dlg.getBounds();
					r.height = dlg.getPreferredSize().height;
					dlg.setBounds(r);
				}
			};
			delete.addActionListener(deleteListener);

			editori.add(delete, gbcDelete.clone());
			editori.add(prop, gbcPanel.clone());
		}
		editori.updateUI();
		dlg.pack();
	}

	@Override
	public void paintComponent(Graphics g1) {
		super.paintComponent(g1);
		Graphics2D g = (Graphics2D) g1;
		List<?> list = (List<?>) parent.getValue();
		g.drawString("Count: " + list.size(), 2, 14);
	}

	private static final Logger log = Logger.getLogger(ListField.class.getName());
}
