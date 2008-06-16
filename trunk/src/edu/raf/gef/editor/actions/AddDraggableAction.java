package edu.raf.gef.editor.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import edu.raf.gef.app.IResources;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.control.state.DiagramAddDraggableState;
import edu.raf.gef.editor.control.state.DiagramSelectionState;
import edu.raf.gef.editor.model.object.Draggable;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.actions.ResourceConfiguredAction;
import edu.raf.gef.gui.swing.DiagramPluginFrame;

public abstract class AddDraggableAction extends ResourceConfiguredAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -721380454889573427L;
	private final Class<? extends Draggable> type;
	private PropertyChangeListener selectedEditorListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			setEnabled(worksOn(mainFrame.getTabbedDiagrams().getSelectedComponent()));
		}
	};

	private MainFrame mainFrame;

	public AddDraggableAction(MainFrame mainFrame, Class<? extends Draggable> type, String ID) {
		super(mainFrame.getFrame(), ID);
		this.mainFrame = mainFrame;
		this.type = type;
		setEnabled(worksOn(mainFrame.getTabbedDiagrams().getSelectedComponent()));
		mainFrame.addPropertyChangeListener(MainFrame.SELECTED_EDITOR_PROPERTY,
			selectedEditorListener);
	}

	/**
	 * Other diagrams may override this method and make their objects work only
	 * with their diagram.
	 * 
	 * @param selectedDiagram
	 */
	protected boolean worksOn(Component focused) {
		return focused != null;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final GefDiagram dg = mainFrame.getSelectedDiagram();
		if (dg == null) {
			getGeh().handleUserError("Can't add object", "Diagram not selected!");
			return;
		}
		DiagramSelectionState defState = new DiagramSelectionState(dg);
		dg.getController().setState(new DiagramAddDraggableState(dg, type, defState, null));
	}

	/**
	 * Since add drawable action is used by plugins (mostly), the getResources
	 * function is abstractized here :)
	 */
	protected abstract IResources getResources();
}
