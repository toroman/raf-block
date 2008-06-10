package edu.raf.gefcomponents.perspective.res;

import java.io.File;

import edu.raf.gef.app.Resources;

public class PerspectiveResources extends Resources {

	public PerspectiveResources() {
		super(PerspectiveResources.class.getPackage());
	}

	@Override
	protected String getBundlePath() {
		return super.location + "locales" + File.separator + "PerspectiveMessageBundle";
	}
}
