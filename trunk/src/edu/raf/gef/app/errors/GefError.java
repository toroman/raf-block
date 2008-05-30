package edu.raf.gef.app.errors;

/**
 * Base of all GEF's errors
 */
public class GefError extends Error {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2204313959990325276L;

	public GefError(String string) {
		super(string);
	}

}
