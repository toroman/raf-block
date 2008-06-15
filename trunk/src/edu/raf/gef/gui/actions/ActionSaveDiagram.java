package edu.raf.gef.gui.actions;

import java.awt.event.ActionEvent;
import java.io.OutputStream;

import com.thoughtworks.xstream.XStream;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.swing.menus.StandardMenuActions;
import edu.raf.gef.workspace.Workspace;
import edu.raf.gef.workspace.project.DiagramProject;

public class ActionSaveDiagram extends ResourceConfiguredAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6010745315029229873L;

	private GefDiagram diagram;

	private MainFrame mainFrame;

	public ActionSaveDiagram(MainFrame mainFrame, GefDiagram diagram) {
		super(mainFrame.getFrame(), StandardMenuActions.SAVE);
		this.diagram = diagram;
		this.mainFrame = mainFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.diagram.getProject().saveDiagram(diagram);
	}

}
