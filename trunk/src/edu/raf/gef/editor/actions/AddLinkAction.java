package edu.raf.gef.editor.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import edu.raf.gef.app.IResources;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.control.state.DiagramAddLinkState;
import edu.raf.gef.editor.control.state.DiagramSelectionState;
import edu.raf.gef.editor.model.object.impl.Link;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.actions.ResourceConfiguredAction;

public abstract class AddLinkAction extends ResourceConfiguredAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -721380454889573427L;
	private final Class<? extends Link> type;
	private MainFrame mainFrame;

	private PropertyChangeListener selectedEditorListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			setEnabled(worksOn(mainFrame.getTabbedDiagrams().getSelectedComponent()));
		}
	};

	public AddLinkAction(MainFrame mf, Class<? extends Link> type, String ID) {
		super(mf.getFrame(), ID);
		this.type = type;
		this.mainFrame = mf;
		setEnabled(worksOn(mainFrame.getTabbedDiagrams().getSelectedComponent()));
		mf.addPropertyChangeListener(MainFrame.SELECTED_EDITOR_PROPERTY, selectedEditorListener);
	}

	/**
	 * Override this to redefine where can this link be added
	 * 
	 * @param selectedDiagram
	 * @return
	 */
	protected boolean worksOn(Component selectedDiagram) {
		return selectedDiagram != null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final GefDiagram dg = this.mainFrame.getSelectedDiagram();
		if (dg == null) {
			getGeh().handleUserError("Can't add object", "Diagram not selected!");
			return;
		}
		DiagramSelectionState defState = new DiagramSelectionState(dg);
		dg.getController().setState(new DiagramAddLinkState(dg, type, defState));
	}

	/**
	 * Since add drawable action is used by plugins (mostly), the getResources
	 * function is abstractized here :)
	 */
	protected abstract IResources getResources();
}
