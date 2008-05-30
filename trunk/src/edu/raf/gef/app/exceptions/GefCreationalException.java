package edu.raf.gef.app.exceptions;

/**
 * Exception that indicates something failed with object creation.
 */
public class GefCreationalException extends GefRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1349052931615081579L;

	public GefCreationalException(Exception ex) {
		super(ex);
	}

	public GefCreationalException(String msg, Exception ex) {
		super(msg, ex);
	}

	public GefCreationalException(String msg) {
		super(msg);
	}
}
