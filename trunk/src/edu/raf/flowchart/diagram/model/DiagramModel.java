package edu.raf.flowchart.diagram.model;

import java.util.ArrayList;

import edu.raf.flowchart.diagram.BlockDiagram;
import edu.raf.flowchart.diagram.model.object.DiagramElement;

public class DiagramModel {
	@SuppressWarnings("unused")
	private BlockDiagram diagram;
	
	private ArrayList <DiagramElement> elements;
	
	public DiagramModel(BlockDiagram diagram) {
		elements = new ArrayList<DiagramElement>();
	}
	
	public void addElement (DiagramElement element) {
		elements.add(element);
	}
	
	public void moveForward (DiagramElement element) {
		elements.remove(element);
		elements.add(element);
	}

	public ArrayList <DiagramElement> getElements() {
		return elements;
	}
}
