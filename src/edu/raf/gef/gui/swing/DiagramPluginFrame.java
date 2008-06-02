package edu.raf.gef.gui.swing;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.gui.ActionContextController;
import edu.raf.gef.gui.MainFrame;

public class DiagramPluginFrame extends JPanel implements ActionContextController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8896598624804157856L;

	protected final GefDiagram diagram;

	public DiagramPluginFrame(GefDiagram diagram) {
		this.diagram = diagram;
		setLayout(new BorderLayout());
		add(diagram.getView().getCanvas(), BorderLayout.CENTER);
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
