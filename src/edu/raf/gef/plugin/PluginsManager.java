package edu.raf.gef.plugin;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import edu.raf.gef.app.Resources;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.standard.ApplicationDialog;

public class PluginsManager extends ApplicationDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6496246833764368980L;
	private Resources resources;
	private MainFrame mainFrame;
	private PluginsTableModel model;

	public PluginsManager(MainFrame mf) {
		super("pluginManager");
		this.mainFrame = mf;
	}

	@Override
	public Component createContents() {
		JPanel c = new JPanel();
		c.setLayout(new BorderLayout());
		JTable table = new JTable(model = new PluginsTableModel());
		c.add(new JScrollPane(table), BorderLayout.CENTER);
		model.refresh(mainFrame.getPlugins());
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		return c;
	}

	@Override
	public Resources getResources() {
		return Resources.getGlobal();
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}
}
