package edu.raf.flowchart.scripting;

import java.util.logging.Logger;

import javax.swing.JOptionPane;

import edu.raf.flowchart.blocks.FlowchartBlock;
import edu.raf.flowchart.blocks.StartBlock;
import edu.raf.flowchart.exceptions.FCExecutionException;
import edu.raf.flowchart.syntax.FlowchartVariableType;
import edu.raf.flowchart.syntax.IExecutionManager;
import edu.raf.gef.editor.GefDiagram;

/**
 * Interprets commands through some scripting language
 * 
 * @author toroman
 * 
 */
public class ScriptExecutionManager implements IExecutionManager {
	@SuppressWarnings("unused")
	private GefDiagram diagram;

	private IScriptEngine scriptEngine;

	public ScriptExecutionManager(GefDiagram diagram, IScriptEngine scriptEngine) {
		this.scriptEngine = scriptEngine;
		this.diagram = diagram;
	}

	public static ScriptExecutionManager createJavaScript(GefDiagram diagram) {
		return new ScriptExecutionManager(diagram, new JavaScriptEngine());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.raf.flowchart.syntax.IExecutionManager#execute(edu.raf.flowchart.blocks.StartBlock)
	 */
	public void run(StartBlock start) throws FCExecutionException {
		scriptEngine.clear();
		FlowchartBlock current = start;
		while (current != null) {
			logger.info("AT:" + current);
			current = current.executeAndReturnNext(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.raf.flowchart.syntax.IExecutionManager#end()
	 */
	public void end() {
		JOptionPane.showMessageDialog(null, "Successfully terminated!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.raf.flowchart.syntax.IExecutionManager#raiseError(edu.raf.flowchart.blocks.FlowchartBlock,
	 *      java.lang.String)
	 */
	public void raiseError(FlowchartBlock block, String error) {
		logger.severe(block.getName() + ": " + error);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.raf.flowchart.syntax.IExecutionManager#evaluate(java.lang.String)
	 */
	public Object evaluate(String cond) throws FCExecutionException {
		logger.info("EVAL: " + cond);
		try {
			return scriptEngine.eval(cond);
		} catch (Exception e) {
			throw new FCExecutionException("Couldn't execute: " + cond, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.raf.flowchart.syntax.IExecutionManager#readInput(java.lang.String)
	 */
	public void readInput(String varname, FlowchartVariableType type) {
		String value = JOptionPane.showInputDialog(varname);
		scriptEngine.set(varname, type.parseString(value));
	}

	@Override
	public void writeOutput(Object object) {
		JOptionPane.showMessageDialog(null, object);
	}

	private static final Logger logger = Logger.getLogger(ScriptExecutionManager.class.getName());
}
