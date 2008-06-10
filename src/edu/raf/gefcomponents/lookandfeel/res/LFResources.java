package edu.raf.gefcomponents.lookandfeel.res;

import java.io.File;

import edu.raf.gef.app.Resources;

public class LFResources extends Resources {

	public LFResources() {
		super(LFResources.class.getPackage());
	}

	@Override
	protected String getBundlePath() {
		return super.location + "locales" + File.separator + "LFMessageBundle";
	}
}
