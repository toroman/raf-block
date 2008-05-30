package edu.raf.gef.gui.actions;

import java.awt.Component;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import edu.raf.gef.app.Resources;
import edu.raf.gef.app.errors.GraphicalErrorHandler;

public abstract class ResourceConfiguredAction extends AbstractAction {
	private GraphicalErrorHandler geh;
	private final Component onErrorComponent;
	
	public ResourceConfiguredAction(Component onErrorComponent, String id) {
		this.onErrorComponent = onErrorComponent;
		Resources res = getResources();
		String name = res.getString(id + ".name");
		if (name == null)
			name = id;
		putValue(NAME, name);
		Icon icon = res.getIcon(id + ".icon");
		if (icon != null)
			putValue(SMALL_ICON, icon);
		String accelerator = res.getString(id + ".accelerator");
		if (accelerator != null) {
			KeyStroke ks = KeyStroke.getKeyStroke(accelerator);
			putValue(ACCELERATOR_KEY, ks);
		}
	}

	protected Resources getResources() {
		return Resources.getGlobal();
	}

	protected GraphicalErrorHandler getGeh() {
		if (geh == null)
			geh = new GraphicalErrorHandler(getClass(), onErrorComponent);
		return geh;
	}
}
