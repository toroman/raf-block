package edu.raf.gef.editor.actions;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.HashMap;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.control.edit.EditDeleteDrawables;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.editor.model.object.Focusable;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.actions.ResourceConfiguredAction;
import edu.raf.gef.gui.swing.menus.StandardMenuActions;

public class ActionDeleteSelectedObject extends ResourceConfiguredAction {

	private MainFrame mainFrame;

	public ActionDeleteSelectedObject(MainFrame mainFrame) {
		super(mainFrame.getFrame(), StandardMenuActions.DELETE);
		this.mainFrame = mainFrame;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8194210499828792577L;

	@Override
	public void actionPerformed(ActionEvent e) {
		final GefDiagram dg = mainFrame.getSelectedDiagram();
		Focusable[] focused = (Focusable[]) dg.getController().getFocusedObjects().toArray(
			new Focusable[0]);
		final HashMap <Drawable, Boolean> removedDrawables = new HashMap <Drawable, Boolean>();
		for (Focusable f : focused) {
			Collection <Drawable> removed = dg.getModel().removeElement(f);
			for (Drawable d: removed)
				if (d instanceof Focusable && ((Focusable)d).isFocused())
					removedDrawables.put(d, true);
				else
					removedDrawables.put(d, false);
			dg.getController().removeFromFocusedObjects(f);
		}
		if (dg.getUndoManager() != null)
			dg.getUndoManager().addEdit(new EditDeleteDrawables(dg, removedDrawables));
	}
}
