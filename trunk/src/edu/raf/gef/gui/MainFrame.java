package edu.raf.gef.gui;

import java.awt.Component;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.InputStream;
import java.util.logging.Level;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import edu.raf.gef.Main;
import edu.raf.gef.app.Resources;
import edu.raf.gef.app.exceptions.GefException;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.IDiagramTreeNode;
import edu.raf.gef.editor.IDrawableNode;
import edu.raf.gef.editor.actions.ActionExportDiagram;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.editor.model.object.Focusable;
import edu.raf.gef.gui.actions.ActionChangeLanguage;
import edu.raf.gef.gui.actions.ActionExitApplication;
import edu.raf.gef.gui.actions.ActionNewProject;
import edu.raf.gef.gui.actions.ActionShowHelp;
import edu.raf.gef.gui.actions.ActionShowPluginManager;
import edu.raf.gef.gui.actions.OpenDocumentAction;
import edu.raf.gef.gui.standard.ApplicationWindow;
import edu.raf.gef.gui.swing.DiagramPluginFrame;
import edu.raf.gef.gui.swing.StandardToolbars;
import edu.raf.gef.gui.swing.ToolbarManager;
import edu.raf.gef.gui.swing.menus.MenuManager;
import edu.raf.gef.gui.swing.menus.MenuManagerSAXImporter;
import edu.raf.gef.gui.swing.menus.MenuPart;
import edu.raf.gef.gui.swing.menus.StandardMenuParts;
import edu.raf.gef.plugin.AbstractPlugin;
import edu.raf.gef.workspace.Workspace;
import edu.raf.gef.workspace.panel.WorkspaceComponent;

/**
 * Main window, defines creational functions.
 * 
 */
public class MainFrame extends ApplicationWindow {
	protected static final long serialVersionUID = 4040204356233038729L;

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	private WorkspaceComponent workspace;

	private JSplitPane mainSplitPane;

	private JTabbedPane tabbedDiagrams;

	private JSplitPane tabbedTools;

	private ActionContextController currentActionContext;

	private MenuManager diagramContextMenu;

	private JSplitPane lastTool;

	public static final String SELECTED_EDITOR_PROPERTY = "selDiagram";

	public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(property, listener);
	}

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
	}

	private void initWorkspace() {
		Workspace restore;
		try {
			restore = Workspace.createWorkspace(Workspace
					.getWorkspaceFileFromResources(getResources()));
		} catch (GefException e) {
			getGeh().handleErrorBlocking("initWorkspace",
				"Couldn't open workspace, please change it!", e);
			return;
		}
		restore.setWorkspaceLocationToProperties(getResources());
		workspace = new WorkspaceComponent(this, restore);
		addTool("Navigator", workspace);
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
		menu.getPart(StandardMenuParts.NEW_PROJECT_PART).add(new ActionNewProject(this));
		menu.getPart(StandardMenuParts.PLUGIN_MANAGER).add(new ActionShowPluginManager(this));
		menu.getPart(StandardMenuParts.FILE_EXIT_PART).add(new ActionExitApplication(this));
		menu.getPart(StandardMenuParts.HELP).add(new ActionShowHelp());

		MenuPart pLangs = menu.getPart(StandardMenuParts.LANGUAGE);
		String[] langs = { "sr_SR_CYRILLIC", "sr_SR_LATIN", "en" };
		for (String lang : langs) {
			pLangs.add(new ActionChangeLanguage(this, lang));
		}

		menu.getPart(StandardMenuParts.FILE_PERSISTING_PART).add(new ActionExportDiagram(this));
		return menu;
	}

	protected void showDiagram(IDiagramTreeNode selectedDiagram) {
		DiagramPluginFrame frame = selectedDiagram.getDiagramEditorComponent();
		if (frame == null) {
			frame = new DiagramPluginFrame(selectedDiagram.getDiagram(), this);
			selectedDiagram.setDiagramEditorComponent(frame);
			tabbedDiagrams.addTab(selectedDiagram.getDiagram().getModel().getTitle(), frame);
		}
		tabbedDiagrams.setSelectedComponent(frame);
	}

	private void initDiagramContextMenu() {
		diagramContextMenu = new MenuManager(getFrame(), true);
		// try restore configuration from XML
		InputStream is = null;
		try {
			is = getResources().getResource("DiagramContextMenuConfiguration");
			MenuManagerSAXImporter.fillMenu(diagramContextMenu, is, getResources());
		} catch (GefException e) {
			// no configuration
			getLog().log(Level.FINEST, "Menu not configured");
		} catch (Throwable t) {
			getGeh().handleErrorBlocking("createMenuManager", "Couldn't read menu configuration!",
				t);
			System.exit(-1);
		}
	}

	public GefDiagram getSelectedDiagram() {
		Component cmp = tabbedDiagrams.getSelectedComponent();
		if (cmp == null || !(cmp instanceof DiagramPluginFrame))
			return null;
		return ((DiagramPluginFrame) cmp).getDiagram();
	}

	@Override
	public Component createContents() {
		initTabbedTools();
		initWorkspace();
		initTabbedDiagramComponents();
		initDiagramContextMenu();

		JTree tree = workspace.getTree();
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				onWorkspaceSelectionChanged(e);
			}
		});

		mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tabbedTools, tabbedDiagrams);
		mainSplitPane.setOneTouchExpandable(true);
		mainSplitPane.setContinuousLayout(true);
		return mainSplitPane;
	}

	private void initTabbedTools() {
		tabbedTools = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		tabbedTools.setContinuousLayout(true);
	}

	protected void onWorkspaceSelectionChanged(TreeSelectionEvent e) {
		Object sel = null;
		if (e.getNewLeadSelectionPath() != null)
			sel = e.getNewLeadSelectionPath().getLastPathComponent();
		if (sel instanceof IDiagramTreeNode) {
			showDiagram((IDiagramTreeNode) sel);
		} else if (sel instanceof IDrawableNode) {
			setObjectSelected(e.getPath(), true, true);
		}
	}

	private void setObjectSelected(TreePath path, boolean select, boolean deselectOthers) {
		GefDiagram diagram = null;
		for (Object o : path.getPath()) {
			if (o instanceof IDiagramTreeNode) {
				diagram = ((IDiagramTreeNode) o).getDiagram();
				break;
			}
		}
		if (diagram == null) {
			return;
		}
		if (deselectOthers) {
			diagram.getController().clearFocusedObjects();
		}

		Drawable d = ((IDrawableNode) path.getLastPathComponent()).getDrawable();
		if (d instanceof Focusable) {
			if (select) {
				diagram.getController().addToFocusedObjects((Focusable) d);
			} else {
				diagram.getController().removeFromFocusedObjects((Focusable) d);
			}
			diagram.getView().getCanvas().repaint();
		}
	}

	private void initTabbedDiagramComponents() {
		tabbedDiagrams = new JTabbedPane();
		tabbedDiagrams.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				handleDiagramSelectionChanged(e);
			}

		});
		tabbedDiagrams.addTab("Welcome", new JLabel("Welcome to RAF Graphical Editing Framework!"));
	}

	protected void handleDiagramSelectionChanged(ChangeEvent e) {
		pcs.firePropertyChange(SELECTED_EDITOR_PROPERTY, null, tabbedDiagrams
				.getSelectedComponent());
		tabbedDiagrams.grabFocus();
		Component cmp = tabbedDiagrams.getSelectedComponent();
		if (cmp instanceof JComponent) {
			((JComponent) cmp).grabFocus();
		}

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
	}

	public JTabbedPane getTabbedDiagrams() {
		return tabbedDiagrams;
	}

	public AbstractPlugin[] getPlugins() {
		return Main.getComponentDiscoveryUtils().getPlugins();
	}

	@Override
	protected void onFrameClosing(WindowEvent e) {
		super.onFrameClosing(e);
		if (this.workspace != null) {
			try {
				workspace.getWorkspace().save();
			} catch (GefException ex) {
				getGeh().handleErrorBlocking("Save", "Couldn't save workspace!", ex);
			}
		}
	}

	public void addTool(String title, JComponent view) {
		if (lastTool == null) {
			tabbedTools.add(view);
			lastTool = tabbedTools;
		} else {
			JSplitPane oldLastTool = lastTool;
			lastTool = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, view, null);
			oldLastTool.add(lastTool);
		}
	}
}
