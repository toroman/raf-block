package edu.raf.flowchart.diagram.control.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.raf.flowchart.diagram.Diagram;
import edu.raf.flowchart.diagram.control.DiagramController;
import edu.raf.flowchart.diagram.model.object.TestObject;

@SuppressWarnings("serial")
public class AddTestObject extends AbstractAction {

	private Diagram diagram;
	
	public AddTestObject(Diagram diagram, String text) {
		super (text);
		this.diagram = diagram;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		diagram.getController().setState(DiagramController.ADD_DIAGRAM_OBJECT, TestObject.class);
	}

}
