package edu.raf.gef.gui;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import edu.raf.gef.Main;
import edu.raf.gef.app.Resources;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.gui.actions.ContextSensitiveAction;
import edu.raf.gef.gui.actions.OpenDocumentAction;
import edu.raf.gef.gui.actions.StandardToolbars;
import edu.raf.gef.gui.swing.ApplicationMdiFrame;
import edu.raf.gef.gui.swing.DiagramPluginFrame;
import edu.raf.gef.gui.swing.ToolbarManager;
import edu.raf.gef.gui.swing.menus.MenuManager;
import edu.raf.gef.plugin.AbstractPlugin;
import edu.raf.gef.plugin.ActionSensitivePlugin;
import edu.raf.gef.workspace.Workspace;
import edu.raf.gef.workspace.panel.WorkspaceComponent;

/**
 * Main window, defines creational functions.
 * 
 */
public class MainFrame extends ApplicationMdiFrame implements InternalFrameListener {
	protected static final long serialVersionUID = 4040204356233038729L;

	private WorkspaceComponent workspace;

	public MainFrame() {
		super("mainFrame");
		setResources(Resources.getGlobal());
		addMenubar();
		addStatusbar();
		addToolbar();
	}

	@Override
	protected void init() {
		initWorkspaceComponents();
		wireEvents();
		for (AbstractPlugin plugin : Main.getComponentDiscoveryUtils().getPlugins()) {
			plugin.setMainFrame(this);
		}
	}

	private void wireEvents() {
		addPropertyChangeListener(SELECTED_FRAME_PROPERTY, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent e) {
				JInternalFrame frame = (JInternalFrame) e.getNewValue();
				AbstractPlugin plugin = null;
				if (frame instanceof DiagramPluginFrame) {
					plugin = ((DiagramPluginFrame) frame).getPlugin();
				}
				validateActions(getToolbarManager().getActions().iterator(), plugin);
				validateActions(getMenuManager().getActions(), plugin);
				getStatusManager().setStatusMessage("Frame " + frame + " activated.");
			}
		});
	}

	private void initWorkspaceComponents() {
		Workspace restore = new Workspace(Workspace.getWorkspaceFileFromResources(getResources()));
		restore.setWorkspaceLocationToProperties(getResources());
		workspace = new WorkspaceComponent(restore);

		JInternalFrame wsFrame = new JInternalFrame("Workspace", true, false, false, false);
		wsFrame.setLayout(new BorderLayout());
		wsFrame.add(workspace, BorderLayout.CENTER);
		wsFrame.setBounds(100, 100, 200, 500);
		wsFrame.setVisible(true);
		wsFrame.addInternalFrameListener(this);
		getDesktop().add(wsFrame);
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace.setWorkspace(workspace);
	}

	public Workspace getWorkspace() {
		return workspace.getWorkspace();
	}

	@Override
	protected MenuManager createMenuManager() {
		MenuManager menu = super.createMenuManager();
		return menu;
	}

	@Override
	protected ToolbarManager createToolbarManager() {
		ToolbarManager tbm = super.createToolbarManager();
		tbm.addAction(StandardToolbars.STANDARD.name(), new OpenDocumentAction(this));
		return tbm;
	}

	/**
	 * Run through all actions on menu and toolbar to disable those which aren't
	 * usable currently (depending on the active plugin and action)
	 * 
	 * @param actions
	 *            Actions to validate
	 * @param plugin
	 *            Selected plugin or null if none selected
	 */
	private void validateActions(Iterator<Action> actions, AbstractPlugin plugin) {
		while (actions.hasNext()) {
			Action action = actions.next();
			boolean actionAgrees = true;
			boolean pluginAgrees = true;
			if (action instanceof ContextSensitiveAction) {
				actionAgrees = ((ContextSensitiveAction) action).worksOn(plugin);
			}
			if (plugin instanceof ActionSensitivePlugin) {
				pluginAgrees = ((ActionSensitivePlugin) plugin).worksWith(action);
			}
			action.setEnabled(actionAgrees && pluginAgrees);
		}
	}

	public DiagramPluginFrame showDiagram(GefDiagram diagram, AbstractPlugin plugin) {
		DiagramPluginFrame frame = new DiagramPluginFrame(diagram, plugin);
		frame.setSize(300, 300);
		frame.setVisible(true);
		frame.addInternalFrameListener(this);
		getDesktop().add(frame);
		return frame;
	}

	public GefDiagram getSelectedDiagram() {
		JInternalFrame jif = getDesktop().getSelectedFrame();
		if (jif == null || !(jif instanceof DiagramPluginFrame))
			return null;
		DiagramPluginFrame dpf = (DiagramPluginFrame) jif;
		return dpf.getDiagram();
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent e) {

	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent e) {
		pcs.firePropertyChange(SELECTED_FRAME_PROPERTY, Integer.MIN_VALUE, null);
	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent e) {

	}

	@Override
	public void internalFrameIconified(InternalFrameEvent e) {

	}

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {

	}
}
