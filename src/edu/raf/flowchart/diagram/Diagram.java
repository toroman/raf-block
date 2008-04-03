package edu.raf.flowchart.diagram;

import edu.raf.flowchart.diagram.control.DiagramController;
import edu.raf.flowchart.diagram.model.DiagramModel;
import edu.raf.flowchart.diagram.view.DiagramView;
import edu.raf.flowchart.gui.DiagramPanel;

public class Diagram {
	private DiagramModel model;
	private DiagramView view;
	private DiagramController controller;
	
	private DiagramPanel parentPanel;
	
	public Diagram(DiagramPanel panel) {
		this.parentPanel = panel;
		model = new DiagramModel(this);
		view = new DiagramView(this);
		controller = new DiagramController (this);
		panel.addMouseListener(controller);
		panel.addMouseMotionListener(controller);
		panel.addMouseWheelListener(controller);
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

	public DiagramPanel getPanel() {
		return parentPanel;
	}
}
