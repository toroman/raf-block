package edu.raf.gef.app.exceptions;

import edu.raf.gef.editor.model.syntax.Expression;

/**
 * Thrown when expression couldn't be evaluated.
 */
public class EvaluationException extends FlowChartException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7750131708042762482L;

	private Expression expression;

	public Expression getExpression() {
		return expression;
	}

	public EvaluationException(Expression exp, Throwable cause) {
		super("Expression couldn't be evaluated!", cause);
		this.expression = exp;
	}

}
