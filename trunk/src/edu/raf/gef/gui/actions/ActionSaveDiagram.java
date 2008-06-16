package edu.raf.gef.gui.actions;

import java.awt.event.ActionEvent;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.swing.menus.StandardMenuActions;

public class ActionSaveDiagram extends ResourceConfiguredAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6010745315029229873L;

	private GefDiagram diagram;

	public ActionSaveDiagram(MainFrame mainFrame, GefDiagram diagram) {
		super(mainFrame.getFrame(), StandardMenuActions.SAVE);
		this.diagram = diagram;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			diagram.save(); 
		} catch (Exception ex) {
			getGeh().handleError("Save Diagram", "Error saving diagram!", ex);
		}
	}

}
