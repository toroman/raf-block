package edu.raf.gef.editor.actions;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.actions.ResourceConfiguredAction;
import edu.raf.gef.gui.swing.menus.StandardMenuActions;

public class ActionRedoDiagramOperation extends ResourceConfiguredAction implements
		PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2157632324539292887L;
	private MainFrame mainFrame;

	public ActionRedoDiagramOperation(MainFrame mainFrame) {
		super(mainFrame.getFrame(), StandardMenuActions.CUT);
		this.mainFrame = mainFrame;
		mainFrame.addPropertyChangeListener(MainFrame.SELECTED_EDITOR_PROPERTY, this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!isEnabled()) {
			getGeh().handleUserError(getResources().getString("exception.wrongcontext.title"),
				getResources().getString("exception.wrongcontext.message"));
			return;
		}
		mainFrame.getSelectedDiagram().getUndoManager().redo();
	}

	@Override
	public Object getValue(String key) {
		if (isEnabled() && NAME.equals(key)) {
			return mainFrame.getSelectedDiagram().getUndoManager().getPresentationName();
		} else {
			return super.getValue(key);
		}
	}

	@Override
	public boolean isEnabled() {
		return mainFrame.getSelectedDiagram() != null
				&& mainFrame.getSelectedDiagram().getUndoManager().canRedo();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		boolean enabled = isEnabled();
		firePropertyChange("enabled", !enabled, enabled);
	}
}
