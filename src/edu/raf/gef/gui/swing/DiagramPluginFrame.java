package edu.raf.gef.gui.swing;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import edu.raf.gef.Main;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.actions.ActionShowProperties;
import edu.raf.gef.gui.ActionContextController;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.actions.ActionSaveDiagram;
import edu.raf.gef.gui.actions.SaveDocumentAction;
import edu.raf.gef.gui.swing.menus.StandardMenuActions;

public class DiagramPluginFrame extends JPanel implements
		ActionContextController {

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
		getActionMap().put(StandardMenuActions.PROPERTIES,
				new ActionShowProperties(mainFrame, Main.getServices()));
		getActionMap().put(StandardMenuActions.SAVE,
				new ActionSaveDiagram(mainFrame, diagram));
	}

	public GefDiagram getDiagram() {
		return diagram;
	}

	@Override
	public void onActivated(MainFrame main,
			ActionContextController previousContext) {
		getDiagram().onActivated(main, previousContext);
	}

	@Override
	public void onDeactivated(MainFrame main,
			ActionContextController nextContext) {
		getDiagram().onDeactivated(main, nextContext);
	}
}
