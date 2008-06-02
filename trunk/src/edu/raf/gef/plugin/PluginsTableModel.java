package edu.raf.gef.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.table.AbstractTableModel;

import edu.raf.gef.app.Resources;

/**
 * Represents a table of plugins, each plugin represented with a description,
 * status (on/off) and class name.
 * 
 */
public class PluginsTableModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6702768556767345599L;

	private static final String DISABLED_COMPONENTS = "disabled_components";

	private ArrayList<PluginRow> rows = new ArrayList<PluginRow>();

	private HashMap<String, AbstractPlugin> plugins = new HashMap<String, AbstractPlugin>();

	HashSet<String> all = new HashSet<String>();

	HashSet<String> off = new HashSet<String>();

	private String[] columnNames = { "Active", "Running", "Component" };

	public PluginsTableModel() {
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public void refresh(AbstractPlugin[] loadedPlugins) {
		plugins.clear();
		for (AbstractPlugin pl : loadedPlugins) {
			plugins.put(pl.getClass().getName(), pl);
		}

		int last = rows.size() - 1;
		rows.clear();
		if (last >= 0)
			fireTableRowsDeleted(0, last);

		all.clear();
		off.clear();
		String strall = Resources.getGlobal().getProperty("components");
		String stroff = Resources.getGlobal().getProperty(DISABLED_COMPONENTS);
		all.addAll(Arrays.asList(strall.split("\\s")));
		off.addAll(Arrays.asList(stroff.split("\\s")));

		for (String klass : all) {
			PluginRow row = new PluginRow();
			row.status = !off.contains(klass);
			row.running = plugins.containsKey(klass);
			if (row.running)
				row.plugin = plugins.get(klass);
			rows.add(row);
		}

		fireTableRowsInserted(0, rows.size() - 1);
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		return rows.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return rows.get(rowIndex).status;
		case 1:
			return rows.get(rowIndex).running;
		case 2:
			return rows.get(rowIndex).plugin.getClass().getName();
		default:
			return null;
		}
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		Boolean active = (Boolean) value;
		PluginRow row = rows.get(rowIndex);
		if (active.booleanValue() == row.status)
			return;
		row.status = !row.status;
		if (row.status) {
			off.remove(row.plugin.getClass().getName());
		} else {
			off.add(row.plugin.getClass().getName());
		}
		StringBuilder disabled = new StringBuilder();

		for (String klass : off) {
			disabled.append(' ');
			disabled.append(klass);
		}
		Resources.getGlobal().setProperty(DISABLED_COMPONENTS, disabled.toString());
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == 0;
	}

	private static class PluginRow {
		public boolean status;
		public boolean running;
		public AbstractPlugin plugin;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex < 2)
			return Boolean.class;
		else
			return String.class;
	}
}
