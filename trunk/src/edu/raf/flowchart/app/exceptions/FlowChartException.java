package edu.raf.flowchart.app.exceptions;

public abstract class FlowChartException extends Exception {
	public FlowChartException(String message, Throwable cause) {
		super(message, cause);
	}
}
