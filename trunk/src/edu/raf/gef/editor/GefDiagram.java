package edu.raf.gef.editor;

import edu.raf.gef.app.util.GefUndoManager;
import edu.raf.gef.editor.control.DiagramController;
import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.view.DiagramView;
import edu.raf.gef.gui.ActionContextController;
import edu.raf.gef.gui.MainFrame;

/**
 * GefDiagram is an composition of GEF MVC components.
 */
public class GefDiagram implements ActionContextController {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4600587151373699739L;

	protected final DiagramModel model;
	protected final DiagramView view;
	protected final DiagramController controller;
	protected final GefUndoManager undoManager;
	protected final IDiagramTreeModel treeModel;

	public GefDiagram() {
		// models knows nothing
		model = createModel();

		// view knows about (observes) the model
		view = createView();

		// controller knows both
		controller = createController();

		// every diagram has its' own undo manager
		undoManager = createUndoManager();

		// tree model is representing this diagram in a workspace explorer
		treeModel = createTreeModel(this);
	}

	private IDiagramTreeModel createTreeModel(GefDiagram gefDiagram) {
		return new DefaultDiagramTreeModel(this);
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

	@Override
	public void onActivated(MainFrame main, ActionContextController previousContext) {
		// restore clicked state
	}

	@Override
	public void onDeactivated(MainFrame main, ActionContextController nextContext) {
		// TODO Auto-generated method stub
	}

	public IDiagramTreeModel getTreeModel() {
		return treeModel;
	}

}
