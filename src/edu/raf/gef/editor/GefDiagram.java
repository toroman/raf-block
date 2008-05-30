package edu.raf.gef.editor;

import edu.raf.gef.app.util.GefUndoManager;
import edu.raf.gef.editor.control.DiagramController;
import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.view.DiagramView;

/**
 * GefDiagram is an composition of GEF MVC components.
 */
public class GefDiagram {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4600587151373699739L;

	protected final DiagramModel model;
	protected final DiagramView view;
	protected final DiagramController controller;
	protected final GefUndoManager undoManager;

	public GefDiagram() {
		// models knows nothing
		model = createModel();

		// view knows about (observes) the model
		view = createView();

		// controller knows both
		controller = createController();

		// every diagram has its' own undo manager
		undoManager = createUndoManager();
	}

	protected DiagramModel createModel() {
		return new DiagramModel();
	}

	protected GefUndoManager createUndoManager() {
		return new GefUndoManager();
	}

	protected DiagramController createController() {
		return new DiagramController(getModel(), getView());
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

	public GefUndoManager getCommandManager() {
		return undoManager;
	}

}
