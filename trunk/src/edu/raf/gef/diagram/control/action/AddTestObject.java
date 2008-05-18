package edu.raf.gef.diagram.control.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.raf.gef.diagram.BlockDiagram;
import edu.raf.gef.diagram.control.DiagramController;
import edu.raf.gef.diagram.model.object.TestObject;

@SuppressWarnings("serial")
public class AddTestObject extends AbstractAction {

	private BlockDiagram diagram;
	
	public AddTestObject(BlockDiagram diagram, String text) {
		super (text);
		this.diagram = diagram;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		diagram.getController().setState(DiagramController.ADD_DIAGRAM_OBJECT, TestObject.class);
	}

}
