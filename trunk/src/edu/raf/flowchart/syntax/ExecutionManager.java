package edu.raf.flowchart.syntax;

import java.util.HashMap;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import edu.raf.flowchart.blocks.FlowchartBlock;
import edu.raf.flowchart.blocks.StartBlock;
import edu.raf.gef.editor.GefDiagram;

public class ExecutionManager {
	@SuppressWarnings("unused")
	private GefDiagram diagram;

	private HashMap<String, Object> variables = new HashMap<String, Object>();

	public ExecutionManager(GefDiagram diagram) {
		this.diagram = diagram;
	}

	public void execute(StartBlock start) {
		variables.clear();
		FlowchartBlock current = start;
		while (current != null) {
			logger.info("AT:" + current);
			current = current.executeAndReturnNext(this);
		}
	}

	/**
	 * Call this when END is reached.
	 */
	public void end() {
		JOptionPane.showMessageDialog(null, "Successfully terminated!");
	}

	public void raiseError(FlowchartBlock block, String error) {
		logger.severe(block.getName() + ": " + error);
	}

	/**
	 * Use JS to evaluate
	 * 
	 * @param cond
	 * @return
	 */
	public Object evaluate(String cond) {
		logger.info("EVAL: " + cond);
		return Boolean.TRUE;
	}

	public void readInput(String varname) {
		String value = JOptionPane.showInputDialog(varname);
		variables.put(varname, value);
	}

	public void writeOutput(String varname) {
		Object value = variables.get(varname);
		JOptionPane.showConfirmDialog(null, value);
	}

	private static final Logger logger = Logger.getLogger(ExecutionManager.class.getName());
}
