package edu.raf.gef.editor;

import java.util.WeakHashMap;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import edu.raf.gef.app.util.GefUndoManager;
import edu.raf.gef.editor.control.DiagramController;
import edu.raf.gef.editor.model.DiagramModel;
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
	protected GefDiagram(DiagramProject project, DiagramModel model) {
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

	public static Converter getConverter(final DiagramProject project) {
		return new Converter() {
			public void marshal(Object diagramObj, HierarchicalStreamWriter writer,
					MarshallingContext context) {
				GefDiagram diagram = (GefDiagram) diagramObj;
				writer.startNode("model");
				DiagramModel.getConverter().marshal(diagram.getModel(), writer, context);
				writer.endNode();
			}

			public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
				GefDiagram diagram = null;
				while (reader.hasMoreChildren()) {
					reader.moveDown();
					if ("model".equals(reader.getNodeName())) {
						DiagramModel model = (DiagramModel) DiagramModel.getConverter().unmarshal(
							reader, context);
						diagram = new GefDiagram(project, model);
					}
					reader.moveUp();
				}
				return diagram;
			}

			@SuppressWarnings("unchecked")
			public boolean canConvert(Class cl) {
				return GefDiagram.class.equals(cl);
			}
		};
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

	public void save() {
		// TODO Auto-generated method stub
		
	}

}
