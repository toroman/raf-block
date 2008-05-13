package edu.raf.flowchart.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JInternalFrame;

import edu.raf.flowchart.app.errors.GraphicalErrorHandler;
import edu.raf.flowchart.gui.MainFrame;
import edu.raf.flowchart.gui.swing.EditorPlugin;

public class CreateDocumentAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2834774936418220606L;

	public static final String ID = "new";

	private Class<? extends EditorPlugin> editorPlugin;

	private MainFrame mainFrame;

	private GraphicalErrorHandler geh;

	public CreateDocumentAction(MainFrame mf, Class<? extends EditorPlugin> editorPlugin) {
		super(editorPlugin.getName());
		this.editorPlugin = editorPlugin;
		this.mainFrame = mf;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JInternalFrame frame = new JInternalFrame("A plugin :P", true, true, true);
		EditorPlugin instance;
		try {
			instance = editorPlugin.newInstance();
		} catch (Exception ex) {
			getGeh().handleError("createDocumentAction", "Plugin failed to initialize", ex);
			return;
		}

		frame.setSize(300, 300);
		frame.setVisible(true);
		frame.add(instance.getPanel());
		mainFrame.getDesktop().add(frame);
	}

	protected GraphicalErrorHandler getGeh() {
		if (geh == null)
			geh = new GraphicalErrorHandler(getClass(), mainFrame.getFrame());
		return geh;
	}
}
