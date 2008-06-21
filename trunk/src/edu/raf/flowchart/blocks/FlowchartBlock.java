package edu.raf.flowchart.blocks;

import edu.raf.flowchart.syntax.ExecutionManager;
import edu.raf.gef.editor.INamedObject;

public interface FlowchartBlock extends INamedObject {
	public FlowchartBlock executeAndReturnNext(ExecutionManager context);
}
