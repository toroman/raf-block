package edu.raf.gef.gui.swing;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import sun.awt.OrientableFlowLayout;

/**
 * Class that provides access to toolbar and manages its' creation.
 * 
 */
public class ToolbarManager {
	private JPanel toolbar = null;

	private Map<String, JToolBar> toolbars = new HashMap<String, JToolBar>();

	private final List<Action> actions;

	public ToolbarManager() {
		actions = new ArrayList<Action>();
	}

	public void addAction(String group, Action action) {
		JToolBar tb = toolbars.get(group);
		if (tb == null) {
			tb = new JToolBar(group);
			toolbars.put(group, tb);
		}
		tb.add(action);
		if (toolbar != null)
			toolbar.add(tb);
		actions.add(action);
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

	public List<Action> getActions() {
		return actions;
	}
}
