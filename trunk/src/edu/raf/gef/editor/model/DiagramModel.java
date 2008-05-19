package edu.raf.gef.editor.model;

import java.util.ArrayList;

import edu.raf.gef.editor.BlockDiagram;
import edu.raf.gef.editor.model.object.DiagramElement;

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
