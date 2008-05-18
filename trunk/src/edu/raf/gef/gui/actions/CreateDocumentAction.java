package edu.raf.gef.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JInternalFrame;

import edu.raf.gef.app.errors.GraphicalErrorHandler;
import edu.raf.gef.app.framework.EditorPlugin;
import edu.raf.gef.app.framework.PluginContainer;
import edu.raf.gef.gui.MainFrame;

public class CreateDocumentAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2834774936418220606L;

	public static final String ID = "new";

	private PluginContainer plugin;

	private MainFrame mainFrame;

	private GraphicalErrorHandler geh;

	public CreateDocumentAction(MainFrame mf, PluginContainer plugin) {
		super(plugin.getResources().getString("plugin.name"));
		this.plugin = plugin;
		this.mainFrame = mf;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JInternalFrame frame = new JInternalFrame("A plugin :P", true, true, true);
		EditorPlugin instance;
		try {
			instance = plugin.getPluginClass().newInstance();
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
