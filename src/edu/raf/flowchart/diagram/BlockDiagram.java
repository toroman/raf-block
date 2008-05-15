package edu.raf.flowchart.diagram;

import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.raf.flowchart.app.Resources;
import edu.raf.flowchart.app.framework.EditorPlugin;
import edu.raf.flowchart.diagram.control.DiagramController;
import edu.raf.flowchart.diagram.model.DiagramModel;
import edu.raf.flowchart.diagram.view.DiagramView;

public class BlockDiagram implements EditorPlugin {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4600587151373699739L;

	static final private Resources resources = new Resources(BlockDiagram.class.getPackage()
			.getName().replace('.', File.separatorChar)
			+ "/res/");

	private DiagramModel model;
	private DiagramView view;
	private DiagramController controller;
	private JPanel panel;

	public BlockDiagram() {
		model = new DiagramModel(this);
		view = new DiagramView(this);
		controller = new DiagramController(this);
		panel = new JPanel();
		panel.addMouseListener(controller);
		panel.addMouseMotionListener(controller);
		panel.addMouseWheelListener(controller);
		panel.add(new JLabel("Ovo je blok dijagram :)"));
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

	public JPanel getPanel() {
		return panel;
	}

	public static Resources getResources() {
		return resources;
	}
}
