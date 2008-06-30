package edu.raf.gef.gui.swing;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.actions.ActionBestFitZoom;
import edu.raf.gef.editor.actions.ActionCopyDiagramObject;
import edu.raf.gef.editor.actions.ActionCutDiagramObject;
import edu.raf.gef.editor.actions.ActionDeleteSelectedObject;
import edu.raf.gef.editor.actions.ActionPasteDiagramObject;
import edu.raf.gef.editor.actions.ActionPrintDiagram;
import edu.raf.gef.editor.actions.ActionRedoDiagramOperation;
import edu.raf.gef.editor.actions.ActionShowProperties;
import edu.raf.gef.editor.actions.ActionUndoDiagramOperation;
import edu.raf.gef.gui.ActionContextController;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.actions.ActionSaveDiagram;
import edu.raf.gef.gui.swing.menus.StandardMenuActions;

public class DiagramPluginFrame extends JPanel implements ActionContextController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8896598624804157856L;

	protected final GefDiagram diagram;

	public DiagramPluginFrame(GefDiagram diagram, MainFrame mainFrame) {
		this.diagram = diagram;
		setLayout(new BorderLayout());
		add(diagram.getView().getCanvas(), BorderLayout.CENTER);
		setFocusable(true);
		JComponent c = diagram.getView().getCanvas();
		setActionMap(c.getActionMap());
		c.setFocusable(true);
		// TODO: IoC here for Services!
		// doing sensitive actions here
		getActionMap().put(StandardMenuActions.CUT, new ActionCutDiagramObject(mainFrame, diagram));
		getActionMap().put(StandardMenuActions.COPY,
			new ActionCopyDiagramObject(mainFrame, diagram));
		getActionMap().put(StandardMenuActions.PASTE,
			new ActionPasteDiagramObject(mainFrame, diagram));
		getActionMap().put(StandardMenuActions.SAVE, new ActionSaveDiagram(mainFrame, diagram));
		getActionMap().put(StandardMenuActions.DELETE,
			new ActionDeleteSelectedObject(mainFrame, diagram));
		getActionMap().put(StandardMenuActions.BEST_FIT_ZOOM, new ActionBestFitZoom(mainFrame));

		getActionMap().put(StandardMenuActions.UNDO,
			new ActionUndoDiagramOperation(mainFrame, diagram));

		getActionMap().put(StandardMenuActions.REDO,
			new ActionRedoDiagramOperation(mainFrame, diagram));

		ActionShowProperties properties = new ActionShowProperties(mainFrame.getFrame(), diagram);

		getActionMap().put(StandardMenuActions.PROPERTIES, properties);

		getActionMap().put(StandardMenuActions.PRINT,
			new ActionPrintDiagram(mainFrame.getFrame(), diagram));

	}

	public GefDiagram getDiagram() {
		return diagram;
	}

	@Override
	public void onActivated(MainFrame main, ActionContextController previousContext) {
		getDiagram().onActivated(main, previousContext);
	}

	@Override
	public void onDeactivated(MainFrame main, ActionContextController nextContext) {
		getDiagram().onDeactivated(main, nextContext);
	}
}
