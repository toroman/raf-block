package edu.raf.gef.app.exceptions;

/**
 * Base of all GEF's runtime exceptions.
 * 
 */
public class GefRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4568908157838256478L;

	public GefRuntimeException(Exception cause) {
		super(cause);
	}

	public GefRuntimeException(String message) {
		super(message);
	}

	public GefRuntimeException(String message, Exception cause) {
		super(message, cause);
	}
}
