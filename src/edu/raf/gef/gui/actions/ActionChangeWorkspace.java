package edu.raf.gef.gui.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JOptionPane;

import edu.raf.gef.app.Resources;
import edu.raf.gef.app.exceptions.GefException;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.workspace.chooser.WorkspaceChooser;

public class ActionChangeWorkspace extends ResourceConfiguredAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5284033315667618268L;
	private MainFrame mainFrame;

	public ActionChangeWorkspace(MainFrame mainFrame) {
		super(mainFrame.getFrame(), "ActionChangeWorkspace");
		this.mainFrame = mainFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		WorkspaceChooser wcho = new WorkspaceChooser();
		wcho.setLocation(getResources().getProperty("workspace"));
		String workspace = wcho.open(mainFrame.getFrame());
		try {
			File f = new File(workspace);
			f.mkdirs();
			if (!f.isDirectory()) {
				throw new GefException("You must select a folder!");
			}
			if (!(f.canRead() && f.canWrite())) {
				throw new GefException("You must have privilege to read and write to that folder!");
			}
		} catch (Exception ex) {
			getGeh().handleError("Invalid location",
				"Can't use that location because of: " + ex.getMessage(), ex);
			return;
		}
		Resources.getGlobal().setProperty("workspace", workspace);
		JOptionPane.showMessageDialog(mainFrame.getFrame(),
			"Changes will apply next time the program is started.");
	}

}
