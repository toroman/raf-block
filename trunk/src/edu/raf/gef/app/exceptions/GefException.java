package edu.raf.gef.app.exceptions;

/**
 * Base for all GEF's non-runtime exceptions
 */
public class GefException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1320951872263719198L;

	public GefException(Throwable cause) {
		super(cause);
	}

	public GefException(String message) {
		super(message);
	}

	public GefException(String message, Throwable cause) {
		super(message, cause);
	}
}
