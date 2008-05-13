package edu.raf.flowchart.gui.actions;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import edu.raf.flowchart.app.Resources;
import edu.raf.flowchart.app.errors.GraphicalErrorHandler;
import edu.raf.flowchart.gui.MainFrame;

public abstract class ResourceConfiguredAction extends AbstractAction {
	final protected MainFrame mainFrame;
	private GraphicalErrorHandler geh;

	public ResourceConfiguredAction(MainFrame mf) {
		this.mainFrame = mf;
		String id;
		try {
			id = (String) getClass().getField("ID").get(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		Resources res = getResources();
		{
			String name = res.getString(id + ".name");
			if (name == null)
				name = id;
			putValue(NAME, name);
		}
		{
			Icon icon = res.getIcon(id + ".icon");
			if (icon != null)
				putValue(SMALL_ICON, icon);
		}
		{
			String accelerator = res.getString(id + ".accelerator");
			if (accelerator != null) {
				KeyStroke ks = KeyStroke.getKeyStroke(accelerator);
				putValue(ACCELERATOR_KEY, ks);
			}
		}

	}

	protected Resources getResources() {
		return Resources.getGlobal();
	}

	protected GraphicalErrorHandler getGeh() {
		if (geh == null)
			geh = new GraphicalErrorHandler(getClass(), mainFrame.getFrame());
		return geh;
	}
}
