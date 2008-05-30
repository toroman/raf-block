package edu.raf.gef.app.errors;

import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * For logging errors which should kind of popup to a user in a window as well.
 * Non-gui classes should throw exceptions. Invoker's from Gui method should
 * catch them and then present them in this manner (if needed). Normal classes
 * should use logging for tracing instead of this.
 */
public class GraphicalErrorHandler {
	private final String classParent;
	private final Component guiParent;

	private static final Logger log = Logger.getLogger(GraphicalErrorHandler.class.getName());

	public void handleError(final String method, final String message, final Throwable ex) {
		log.logp(Level.SEVERE, classParent, method, message, ex);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(guiParent, message, "Information about the error",
					JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	public void handleErrorBlocking(final String method, final String message, final Throwable ex) {
		log.logp(Level.SEVERE, classParent, method, message, ex);
		JOptionPane.showMessageDialog(guiParent, message, "Information about the error",
			JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Creates ErrorHandler for the particular class that will use it.
	 * <p>
	 * Create a new instance in every class that uses it.
	 * 
	 * @param classParent
	 *            Where are errors thrown from
	 * @param guiParent
	 *            Which component (or null) is connected with the parent class.
	 *            Used for displaying error messages
	 */
	public <T> GraphicalErrorHandler(Class<T> classParent, Component guiParent) {
		this.classParent = classParent.getName();
		this.guiParent = guiParent;
	}

}
