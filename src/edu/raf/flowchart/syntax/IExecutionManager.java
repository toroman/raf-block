package edu.raf.flowchart.syntax;

import edu.raf.flowchart.blocks.FlowchartBlock;
import edu.raf.flowchart.blocks.StartBlock;
import edu.raf.flowchart.exceptions.FCExecutionException;

public interface IExecutionManager {

	public abstract void run(StartBlock start) throws FCExecutionException;

	/**
	 * Call this when END is reached.
	 */
	public abstract void end();

	public abstract void raiseError(FlowchartBlock block, String error);

	/**
	 * Use JS to evaluate
	 * 
	 * @param cond
	 * @return
	 */
	public abstract Object evaluate(String cond) throws FCExecutionException;

	public abstract void readInput(String varname, FlowchartVariableType type);

	public abstract void writeOutput(Object object);

}