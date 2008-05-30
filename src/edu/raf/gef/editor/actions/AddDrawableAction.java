package edu.raf.gef.editor.actions;

import java.awt.event.ActionEvent;

import javax.swing.event.UndoableEditEvent;

import edu.raf.gef.app.Resources;
import edu.raf.gef.app.exceptions.GefCreationalException;
import edu.raf.gef.app.util.AddDrawableEdit;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.model.object.DrawableElement;
import edu.raf.gef.gui.SelectedDiagramProvider;
import edu.raf.gef.gui.actions.ResourceConfiguredAction;

public abstract class AddDrawableAction extends ResourceConfiguredAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -721380454889573427L;
	private final Class<? extends DrawableElement> type;
	private SelectedDiagramProvider selDiagram;

	public AddDrawableAction(Class<? extends DrawableElement> type, String ID,
			SelectedDiagramProvider selDiagram) {
		super(null, ID);
		this.type = type;
		this.selDiagram = selDiagram;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final GefDiagram dg = selDiagram.getSelectedDiagram();
		if (dg == null) {
			getGeh().handleUserError("Can't add object", "Diagram not selected!");
			return;
		}
		DrawableElement de;
		try {
			de = this.type.newInstance();
		} catch (Exception ex) {
			getGeh().handleError("actionPerformed", "Error creating object!", ex);
			return;
		}
		dg.getUndoManager().undoableEditHappened(
			new UndoableEditEvent(dg.getModel(), new AddDrawableEdit(dg.getModel(), de)));
	}

	/**
	 * Since add drawable action is used by plugins (mostly), the getResources
	 * function is abstractized here :)
	 */
	protected abstract Resources getResources();
}
