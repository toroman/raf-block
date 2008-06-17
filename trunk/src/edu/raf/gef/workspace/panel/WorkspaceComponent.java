package edu.raf.gef.workspace.panel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import edu.raf.gef.app.Resources;
import edu.raf.gef.app.exceptions.GefException;
import edu.raf.gef.gui.swing.menus.MenuManager;
import edu.raf.gef.gui.swing.menus.MenuManagerSAXImporter;
import edu.raf.gef.gui.swing.menus.PopupListener;
import edu.raf.gef.workspace.Workspace;
import edu.raf.gef.workspace.project.DiagramProject;

public class WorkspaceComponent extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3164075486971114416L;
	private Workspace workspace;

	private JTree trWorkspace;
	private MenuManager menuManager;

	private static final String ID = "WorkspaceComponent";

	public WorkspaceComponent(Workspace workspace) {
		initComponents();
		setWorkspace(workspace);
		setPreferredSize(new Dimension(250, 300));
	}

	private void initComponents() {
		trWorkspace = new JTree();
		trWorkspace.setRootVisible(true);
		menuManager = new MenuManager(trWorkspace, true);
		// try restore configuration from XML
		InputStream is = null;
		try {
			is = Resources.getGlobal().getResource(ID + ".menu");
			MenuManagerSAXImporter.fillMenu(menuManager, is, Resources.getGlobal());
		} catch (GefException e) {
			// no configuration
			Logger.getAnonymousLogger().log(Level.FINEST, "Menu not configured");
		} catch (Throwable t) {
			Logger.getAnonymousLogger().log(Level.SEVERE, "Couldn't read menu.", t);
			System.exit(-1);
		}
		trWorkspace.add(menuManager.getPopupMenu());
		trWorkspace.addMouseListener(new PopupListener(menuManager.getPopupMenu()));
		Container con = this;
		con.setLayout(new BorderLayout());
		con.add(trWorkspace, BorderLayout.CENTER);
	}

	public void setWorkspace(Workspace workspace) {
		if (workspace == null)
			throw new NullPointerException();
		this.workspace = workspace;
		trWorkspace.setModel(workspace);
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	public JTree getTree() {
		return trWorkspace;
	}

	public DiagramProject getSelectedProject() {
		if (trWorkspace.getSelectionCount() != 1)
			return null;
		TreePath path = trWorkspace.getSelectionPath();
		for (Object node : path.getPath()) {
			if (node instanceof DiagramProject)
				return (DiagramProject) node;
		}
		return null;
	}

	public void setSelectedProject(DiagramProject project) {
		trWorkspace.setSelectionPath(new TreePath(new Object[] { getTree().getModel().getRoot(),
				project }));
	}

	public void setSelected(TreeNode lastNode) {
		LinkedList<TreeNode> list = new LinkedList<TreeNode>();
		list.addLast(lastNode);
		while (lastNode.getParent() != null) {
			list.addFirst(lastNode = lastNode.getParent());
		}
		trWorkspace.setSelectionPath(new TreePath(list.toArray()));
	}
}
