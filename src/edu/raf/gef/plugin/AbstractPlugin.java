package edu.raf.gef.plugin;

import edu.raf.gef.app.IResources;
import edu.raf.gef.gui.MainFrame;

public interface AbstractPlugin {
	/**
	 * @return Plugin's Resources
	 */
	public IResources getResources();

	/**
	 * This method will be called after Main Frame has been completely
	 * initialized (menus, toolbars etc) so that this plugin can do stuff with
	 * it.
	 * <p>
	 * It would be interesting to make the MainFrame be a component as well, and
	 * then use setComponentDiscovery or setServiceManager method instead. In
	 * fact it would be just a plain bean resolver (so we are IoC-ing a IoC
	 * container!)
	 * 
	 * @param mf
	 */
	public void setMainFrame(MainFrame mf);
}
