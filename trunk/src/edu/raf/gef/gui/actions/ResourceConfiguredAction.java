package edu.raf.gef.gui.actions;

import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import edu.raf.gef.app.IResources;
import edu.raf.gef.app.Resources;
import edu.raf.gef.app.errors.GraphicalErrorHandler;

public abstract class ResourceConfiguredAction extends AbstractAction {
	private GraphicalErrorHandler geh;
	private final Component onErrorComponent;
	private final String id;

	public ResourceConfiguredAction(Component onErrorComponent, String id) {
		this.onErrorComponent = onErrorComponent;
		this.id = id;
		IResources res = getResources();
		String name = res.getString(id + ".name");
		if (name == null)
			name = id;
		putValue(NAME, name);
		try {
			Icon icon = res.getIcon(id + ".icon");
			putValue(SMALL_ICON, icon);
		} catch (Throwable ex) {
			getLog().log(Level.FINE, "No icon for: " + id); // ,ex
		}
		try {
			String accelerator = res.getString(id + ".accelerator");
			KeyStroke ks = KeyStroke.getKeyStroke(accelerator);
			putValue(ACCELERATOR_KEY, ks);
		} catch (Exception ex) {
			getLog().log(Level.FINE, "No accelerator for: " + id); // ,ex
		}
	}


	/**
	 * 
	 * @return Unique (non translatable) identification of this action type,
	 *         usable in ActionMaps.
	 */
	public String getId() {
		return id;
	}

	protected IResources getResources() {
		return Resources.getGlobal();
	}

	protected GraphicalErrorHandler getGeh() {
		if (geh == null)
			geh = new GraphicalErrorHandler(getClass(), onErrorComponent);
		return geh;
	}

	protected Logger getLog() {
		return Logger.getLogger(getClass().getName());
	}

}
