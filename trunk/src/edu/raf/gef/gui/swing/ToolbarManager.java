package edu.raf.gef.gui.swing;

import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import sun.awt.OrientableFlowLayout;

public class ToolbarManager {
	private JPanel toolbar = null;

	private Map<String, JToolBar> toolbars = new HashMap<String, JToolBar>();

	public void addAction(String group, Action action) {
		JToolBar tb = toolbars.get(group);
		if (tb == null) {
			tb = new JToolBar(group);
			toolbars.put(group, tb);
			if (toolbar != null)
				toolbar.add(tb);
		}
		tb.add(action);
	}

	public synchronized JPanel getToolbar() {
		if (toolbar == null) {
			toolbar = buildToolbar();
		}
		return toolbar;
	}

	private JPanel buildToolbar() {
		if (toolbar != null)
			throw new IllegalStateException(
					"Toolbar should be build once and only once by one manager");
		toolbar = new JPanel();
		OrientableFlowLayout lay = new OrientableFlowLayout();
		lay.setAlignment(FlowLayout.LEFT);
		toolbar.setLayout(lay);
		for (Entry<String, JToolBar> entry : toolbars.entrySet()) {
			toolbar.add(entry.getValue());
		}
		return toolbar;
	}
}
