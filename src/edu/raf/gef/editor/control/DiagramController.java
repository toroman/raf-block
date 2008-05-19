package edu.raf.gef.editor.control;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import edu.raf.gef.editor.BlockDiagram;
import edu.raf.gef.editor.control.state.AddDiagramObjectBehaviour;
import edu.raf.gef.editor.control.state.SelectionBehaviour;
import edu.raf.gef.editor.control.state.util.TotalMouseAdapter;

public class DiagramController extends TotalMouseAdapter {
	private int currentStateIndex;
	private ArrayList <TotalMouseAdapter> possibleStates;

	@SuppressWarnings("unused")
	private BlockDiagram diagram;
	
	public DiagramController(BlockDiagram diagram) {
		this.diagram = diagram;
		
		possibleStates = new ArrayList<TotalMouseAdapter>();
		possibleStates.add(new SelectionBehaviour (diagram));
		possibleStates.add(new AddDiagramObjectBehaviour (diagram));

		currentStateIndex = DEFAULT;
	}
	
	@SuppressWarnings("unchecked")
	public void setState (int index, Class optionalArgumentClass) {
		currentStateIndex = index;
		if (index == ADD_DIAGRAM_OBJECT) {
			((AddDiagramObjectBehaviour)(possibleStates.get(ADD_DIAGRAM_OBJECT))).setType (optionalArgumentClass);
		}
	}
	
	public static final int SELECTION = 0;
	public static final int ADD_DIAGRAM_OBJECT = 1;
	
	private static final int DEFAULT = SELECTION;
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		possibleStates.get(currentStateIndex).mouseClicked(arg0);
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		possibleStates.get(currentStateIndex).mouseDragged(arg0);
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		possibleStates.get(currentStateIndex).mouseEntered(arg0);
	}
	
	@Override
	public void mouseExited(MouseEvent arg0) {
		possibleStates.get(currentStateIndex).mouseExited(arg0);
	}
	
	@Override
	public void mouseMoved(MouseEvent arg0) {
		possibleStates.get(currentStateIndex).mouseMoved(arg0);
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		possibleStates.get(currentStateIndex).mousePressed(arg0);
	}
	
	@Override
	public void mouseReleased(MouseEvent arg0) {
		possibleStates.get(currentStateIndex).mouseReleased(arg0);
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		possibleStates.get(currentStateIndex).mouseWheelMoved(arg0);
	}
}
