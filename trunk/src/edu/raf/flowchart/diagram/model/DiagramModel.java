package edu.raf.flowchart.diagram.model;

import java.util.ArrayList;

import edu.raf.flowchart.diagram.Diagram;
import edu.raf.flowchart.diagram.model.object.DiagramElement;

public class DiagramModel {
	@SuppressWarnings("unused")
	private Diagram diagram;
	
	private ArrayList <DiagramElement> elements;
	
	public DiagramModel(Diagram diagram) {
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
