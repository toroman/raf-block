package edu.raf.gefcomponents.lookandfeel.res;

import java.io.File;

import edu.raf.gef.app.Resources;

public class LFResources extends Resources {

	public LFResources() {
		super(LFResources.class.getPackage());
		System.out.println("Look and feel resources initialized");
	}

	@Override
	protected String getBundlePath() {
		return super.location + "locales" + File.separator + "LFMessageBundle";
	}
}
