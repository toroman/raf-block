package edu.raf.flowchart.blocks;

import edu.raf.flowchart.exceptions.FCExecutionException;
import edu.raf.flowchart.syntax.IExecutionManager;
import edu.raf.gef.editor.INamedObject;

public interface FlowchartBlock extends INamedObject {
	public FlowchartBlock executeAndReturnNext(IExecutionManager context)
			throws FCExecutionException;
}
