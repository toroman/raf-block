package edu.raf.gefcomponents.perspective;

import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.plugin.AbstractPlugin;
import edu.raf.gefcomponents.perspective.res.PerspectiveResources;

public class PerspectivePlugin implements AbstractPlugin {

	private static class ResourcesInstanceHolder {
		static final PerspectiveResources resources = new PerspectiveResources();
	}

	@Override
	public PerspectiveResources getResources() {
		return ResourcesInstanceHolder.resources;
	}

	@Override
	public void setMainFrame(MainFrame mf) {
		// mf.getMenuManager().addAction(StandardMenuParts.WINDOW_ARRANGEMENT_PART,
		// null);
	}
}
