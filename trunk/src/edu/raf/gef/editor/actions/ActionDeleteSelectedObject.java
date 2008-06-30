package edu.raf.gef.editor.actions;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.HashMap;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.control.GefFocusEvent;
import edu.raf.gef.editor.control.GefFocusListener;
import edu.raf.gef.editor.control.edit.EditDeleteDrawables;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.editor.model.object.Focusable;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.actions.ResourceConfiguredAction;
import edu.raf.gef.gui.swing.menus.StandardMenuActions;

public class ActionDeleteSelectedObject extends ResourceConfiguredAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8194210499828792577L;

	private GefDiagram diagram;

	private GefFocusListener listener = new GefFocusListener() {
		public void focusChanged(GefFocusEvent event) {
			boolean enabled = isEnabled();
			firePropertyChange("enabled", !enabled, enabled);
		}
	};

	public ActionDeleteSelectedObject(MainFrame mainFrame, GefDiagram diagram) {
		super(mainFrame.getFrame(), StandardMenuActions.DELETE);
		this.diagram = diagram;
		diagram.getController().addFocusListener(listener);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		Focusable[] focused = (Focusable[]) diagram.getController().getFocusedObjects().toArray(
			new Focusable[0]);
		final HashMap<Drawable, Boolean> removedDrawables = new HashMap<Drawable, Boolean>();
		for (Focusable f : focused) {
			Collection<Drawable> removed = diagram.getModel().removeElement(f);
			for (Drawable d : removed)
				if (d instanceof Focusable && ((Focusable) d).isFocused())
					removedDrawables.put(d, true);
				else
					removedDrawables.put(d, false);
			diagram.getController().removeFromFocusedObjects(f);
		}
		if (diagram.getUndoManager() != null)
			diagram.getUndoManager().addEdit(new EditDeleteDrawables(diagram, removedDrawables));
	}

	@Override
	public boolean isEnabled() {
		return diagram.getController().getFocusedObjects().size() > 0;
	}
}
