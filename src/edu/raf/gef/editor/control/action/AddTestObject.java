package edu.raf.gef.editor.control.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import edu.raf.gef.editor.BlockDiagram;
import edu.raf.gef.editor.control.DiagramController;
import edu.raf.gef.editor.model.object.TestObject;

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
