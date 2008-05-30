package edu.raf.gef.plugin;

import edu.raf.gef.app.Resources;
import edu.raf.gef.gui.MainFrame;

public interface AbstractPlugin {
	/**
	 * @return Plugin's Resources
	 */
	public Resources getResources();

	/**
	 * This method will be called after Main Frame has been completely
	 * initialized (menus, toolbars etc) so that this plugin can do stuff with
	 * it.
	 * 
	 * @param mf
	 */
	public void setMainFrame(MainFrame mf);
}
