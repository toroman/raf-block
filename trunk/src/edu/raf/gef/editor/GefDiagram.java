package edu.raf.gef.editor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.WeakHashMap;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.raf.gef.app.exceptions.GefException;
import edu.raf.gef.app.util.GefUndoManager;
import edu.raf.gef.editor.control.DiagramController;
import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.structure.GefDiagramConverter;
import edu.raf.gef.editor.view.DiagramView;
import edu.raf.gef.gui.ActionContextController;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.workspace.Workspace;
import edu.raf.gef.workspace.project.DiagramProject;

/**
 * GefDiagram is an composition of GEF MVC components.
 */
public class GefDiagram implements ActionContextController {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4600587151373699739L;

	protected final DiagramModel model;
	protected transient final DiagramView view;
	protected transient final DiagramController controller;

	protected transient final GefUndoManager undoManager;
	protected transient final DiagramProject project;
	protected transient WeakHashMap<Workspace, IDiagramTreeNode> tree;

	/**
	 * Internal constructor, from restored model.
	 * 
	 * @param project
	 * @param model
	 */
	public GefDiagram(DiagramProject project, DiagramModel model) {
		// model is not created
		this.model = model;

		// view knows about (observes) the model
		view = createView();

		// controller knows both
		controller = createController();

		// every diagram has its' own undo manager
		undoManager = createUndoManager();

		// add to parent
		this.project = project;
		project.addDiagram(this);
	}

	public GefDiagram(DiagramProject project) {
		// models knows nothing
		model = createModel();

		// view knows about (observes) the model
		view = createView();

		// controller knows both
		controller = createController();

		// every diagram has its' own undo manager
		undoManager = createUndoManager();

		// add to parent
		this.project = project;
		project.addDiagram(this);
	}

	protected IDiagramTreeNode createTreeModel(Workspace ws) {
		return new DefaultDiagramTreeModel(this, ws);
	}

	protected DiagramModel createModel() {
		return new DiagramModel();
	}

	protected GefUndoManager createUndoManager() {
		return new GefUndoManager();
	}

	protected DiagramController createController() {
		return new DiagramController(this);
	}

	protected DiagramView createView() {
		return new DiagramView(getModel());
	}

	public DiagramModel getModel() {
		return model;
	}

	public DiagramView getView() {
		return view;
	}

	public DiagramController getController() {
		return controller;
	}

	public GefUndoManager getUndoManager() {
		return undoManager;
	}

	public DiagramProject getProject() {
		return project;
	}

	@Override
	public void onActivated(MainFrame main, ActionContextController previousContext) {
		// restore clicked state
	}

	@Override
	public void onDeactivated(MainFrame main, ActionContextController nextContext) {
		// TODO Auto-generated method stub
	}

	/**
	 * Tree model is representing this diagram in a workspace explorer
	 */
	public synchronized IDiagramTreeNode getTreeModel(Workspace workspace) {
		if (tree == null)
			tree = new WeakHashMap<Workspace, IDiagramTreeNode>();
		IDiagramTreeNode node = tree.get(workspace);
		if (node == null) {
			tree.put(workspace, node = createTreeModel(workspace));
		}
		return node;
	}

	@Override
	public String toString() {
		return super.toString() + getModel().getTitle();
	}

	/**
	 * Be sure to overload this if you plan to change the extension of the
	 * plugin!
	 * 
	 * @return
	 * @throws GefException
	 */
	public File getDiagramFile() throws GefException {
		return new File(this.project.getProjectFolder(), getModel().getTitle() + ".dgr");
	}

	public void save() throws GefException {
		File file = getDiagramFile();
		XStream xs = new XStream(new DomDriver());
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(file));
		} catch (FileNotFoundException ex) {
			throw new GefException("Couldn't open file for writing!", ex);
		}
		xs.registerConverter(new GefDiagramConverter(this.project, getClass(), getModel()
				.getConverter()));
		xs.toXML(this, os);
		try {
			os.close();
		} catch (IOException ex) {
		}
	}
}
