package edu.raf.gef.gui;

import java.awt.Component;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import edu.raf.gef.Main;
import edu.raf.gef.app.Resources;
import edu.raf.gef.editor.DefaultDiagramTreeModel;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.gui.actions.ActionExitApplication;
import edu.raf.gef.gui.actions.ActionNewProject;
import edu.raf.gef.gui.actions.ActionShowPluginManager;
import edu.raf.gef.gui.actions.ContextSensitiveAction;
import edu.raf.gef.gui.actions.OpenDocumentAction;
import edu.raf.gef.gui.actions.StandardToolbars;
import edu.raf.gef.gui.standard.ApplicationWindow;
import edu.raf.gef.gui.swing.DiagramPluginFrame;
import edu.raf.gef.gui.swing.ToolbarManager;
import edu.raf.gef.gui.swing.menus.MenuManager;
import edu.raf.gef.gui.swing.menus.StandardMenuParts;
import edu.raf.gef.plugin.AbstractPlugin;
import edu.raf.gef.util.MergeIterator;
import edu.raf.gef.workspace.Workspace;
import edu.raf.gef.workspace.panel.WorkspaceComponent;

/**
 * Main window, defines creational functions.
 * 
 */
public class MainFrame extends ApplicationWindow {
	protected static final long serialVersionUID = 4040204356233038729L;

	private WorkspaceComponent workspace;

	private JSplitPane mainSplitPane;

	private JTabbedPane tabbedDiagrams;

	private ActionContextController currentActionContext;

	public MainFrame() {
		super("mainFrame");
		setResources(Resources.getGlobal());
		addMenubar();
		addStatusbar();
		addToolbar();
	}

	@Override
	protected void init() {
		for (AbstractPlugin plugin : Main.getComponentDiscoveryUtils().getPlugins()) {
			plugin.setMainFrame(this);
		}
		validateActions(new MergeIterator<Action>(getMenuManager().getActions(),
				getToolbarManager().getActions()), tabbedDiagrams.getSelectedComponent());
	}

	private void initWorkspaceComponents() {
		Workspace restore = new Workspace(Workspace.getWorkspaceFileFromResources(getResources()));
		restore.setWorkspaceLocationToProperties(getResources());
		workspace = new WorkspaceComponent(restore);
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace.setWorkspace(workspace);
	}

	public Workspace getWorkspace() {
		return workspace.getWorkspace();
	}

	public WorkspaceComponent getWorkspaceComponent() {
		return workspace;
	}

	@Override
	protected ToolbarManager createToolbarManager() {
		ToolbarManager tbm = super.createToolbarManager();
		tbm.addAction(StandardToolbars.STANDARD.name(), new OpenDocumentAction(this));
		return tbm;
	}

	@Override
	protected MenuManager createMenuManager() {
		MenuManager menu = super.createMenuManager();
		menu.getPart(StandardMenuParts.FILE_CREATION_PART).add(new ActionNewProject(this));
		menu.getPart(StandardMenuParts.PLUGIN_MANAGER).add(new ActionShowPluginManager(this));
		menu.getPart(StandardMenuParts.FILE_EXIT_PART).add(new ActionExitApplication(this));
		return menu;
	}

	protected void showDiagram(DefaultDiagramTreeModel selectedDiagram) {
		DiagramPluginFrame frame = selectedDiagram.getDiagramEditorComponent();
		if (frame == null) {
			frame = new DiagramPluginFrame(selectedDiagram.getDiagram());
			selectedDiagram.setDiagramEditorComponent(frame);
			tabbedDiagrams.addTab(selectedDiagram.getDiagram().getModel().getTitle(), frame);
		}
		tabbedDiagrams.setSelectedComponent(frame);
	}

	public GefDiagram getSelectedDiagram() {
		Component cmp = tabbedDiagrams.getSelectedComponent();
		if (cmp == null || !(cmp instanceof DiagramPluginFrame))
			return null;
		return ((DiagramPluginFrame) cmp).getDiagram();
	}

	@Override
	public Component createContents() {
		initWorkspaceComponents();
		initTabbedDiagramComponents();

		JTree tree = workspace.getTree();
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				Object sel = e.getNewLeadSelectionPath().getLastPathComponent();
				if (sel instanceof DefaultDiagramTreeModel) {
					showDiagram((DefaultDiagramTreeModel) sel);
				}
			}
		});

		mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, workspace, tabbedDiagrams);
		mainSplitPane.setOneTouchExpandable(true);
		return mainSplitPane;
	}

	private void initTabbedDiagramComponents() {
		tabbedDiagrams = new JTabbedPane();
		tabbedDiagrams.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Component cmp = tabbedDiagrams.getSelectedComponent();
				validateActions(new MergeIterator<Action>(getToolbarManager().getActions(),
						getMenuManager().getActions()), cmp);

				ActionContextController context = null;
				if (cmp != null && cmp instanceof ActionContextController) {
					context = (ActionContextController) cmp;
				}
				if (currentActionContext != null) {
					currentActionContext.onDeactivated(MainFrame.this, context);
				}
				if (context != null) {
					context.onActivated(MainFrame.this, currentActionContext);
				}
				currentActionContext = context;
				getFrame().repaint();
			}

		});
		tabbedDiagrams.addTab("Welcome", new JLabel("Welcome to RAF Graphical Editing Framework!"));
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
	private void validateActions(Iterator<Action> actions, Object focused) {
		while (actions.hasNext()) {
			Action action = actions.next();
			boolean actionAgrees = true;
			if (action instanceof ContextSensitiveAction) {
				actionAgrees = ((ContextSensitiveAction) action).worksOn(focused);
			}
			if (actionAgrees && action instanceof ContextSensitiveAction) {
				actionAgrees = ((ContextSensitiveAction) action).worksOn(focused);
			}
			action.setEnabled(actionAgrees);
		}
	}

	public AbstractPlugin[] getPlugins() {
		return Main.getComponentDiscoveryUtils().getPlugins();
	}
}
