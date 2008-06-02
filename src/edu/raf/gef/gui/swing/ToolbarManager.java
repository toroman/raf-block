package edu.raf.gef.gui.swing;

import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import sun.awt.OrientableFlowLayout;

/**
 * Class that provides access to toolbar and manages its' creation.
 * 
 */
public class ToolbarManager {
	private JPanel toolbarPanel = null;

	private Map<String, JToolBar> toolbars = new HashMap<String, JToolBar>();

	private final List<Action> actions;

	public ToolbarManager() {
		actions = new ArrayList<Action>();
		toolbarPanel = new JPanel();
		OrientableFlowLayout lay = new OrientableFlowLayout();
		lay.setAlignment(FlowLayout.LEFT);
		toolbarPanel.setLayout(lay);
	}

	public void addAction(String group, Action action) {
		JToolBar toolbar = toolbars.get(group);
		if (toolbar == null) {
			toolbar = createToolbar(group);
			toolbarPanel.add(toolbar);
			toolbars.put(group, toolbar);
		}
		toolbar.add(action);
		actions.add(action);
	}

	private JToolBar createToolbar(String group) {
		return new BiggerBetterToolBar(group);
	}

	public JPanel getToolbar() {
		return toolbarPanel;
	}

	public Iterator<Action> getActions() {
		return actions.iterator();
	}

	@SuppressWarnings("serial")
	private class BiggerBetterToolBar extends JToolBar {
		private Integer enabledActionCount = 0;

		@Override
		public JButton add(Action a) {
			enabledActionCount ++;
			final JButton button = new JButton(a);
			button.addPropertyChangeListener(new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getPropertyName().equals("enabled")) {
						if (evt.getNewValue().equals(evt.getOldValue()))
							return;
						button.setVisible((Boolean) evt.getNewValue());
						enabledActionCount += button.isVisible() ? 1 : -1;
						BiggerBetterToolBar.this.setVisible(enabledActionCount > 0);
					}
				}
			});
			return (JButton)super.add(button);
		}

		public BiggerBetterToolBar(String name) {
			super(name);
		}
	}
}
