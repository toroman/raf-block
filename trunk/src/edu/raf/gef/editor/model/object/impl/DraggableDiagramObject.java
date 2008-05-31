package edu.raf.gef.editor.model.object.impl;

import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.Draggable;

public abstract class DraggableDiagramObject extends DiagramObject implements Draggable {
	public DraggableDiagramObject(DiagramModel model) {
		super (model);
	}
}
