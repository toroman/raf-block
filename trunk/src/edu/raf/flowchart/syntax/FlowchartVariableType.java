package edu.raf.flowchart.syntax;

import edu.raf.flowchart.FlowChartPlugin;

public enum FlowchartVariableType {
	INTEGER, REAL, TEXT;

	@Override
	public String toString() {
		try {
			return FlowChartPlugin.resources.getString(getClass().getName() + "." + name());
		} catch (RuntimeException e) {
			return super.toString();
		}
	}

	public Object parseString(String s) {
		if (this == INTEGER) {
			return Long.valueOf(s);
		} else if (this == REAL) {
			return Double.valueOf(s);
		} else {
			return s;
		}
	}
}
