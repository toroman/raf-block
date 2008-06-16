package edu.raf.gef.editor.actions;

import java.awt.event.ActionEvent;

import edu.raf.gef.editor.GefDiagram;
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
		GefDiagram dg = mainFrame.getSelectedDiagram();
		Focusable[] focused = (Focusable[]) dg.getController().getFocusedObjects().toArray(
			new Focusable[0]);
		for (Focusable f : focused) {
			dg.getController().removeFromFocusedObjects(f);
			dg.getModel().removeElement(f);
		}
	}
}
