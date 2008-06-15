package edu.raf.gef.editor.structure;

import java.util.EventObject;

public class CompositeUpdateEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2597632684146530592L;
	private Object param;

	public CompositeUpdateEvent(Object childSource, Object childParam) {
		super(childSource);
		this.param = childParam;
	}

	public Object getParam() {
		return param;
	}
}
