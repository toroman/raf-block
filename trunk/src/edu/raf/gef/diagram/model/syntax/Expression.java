package edu.raf.gef.diagram.model.syntax;

import java.io.Serializable;

/**
 * Expression is something that can be executed to get some value.
 * 
 */
public interface Expression extends Serializable {
	public Object evaluate(FlowContext context);
}
