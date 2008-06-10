package edu.raf.gefcomponents.beaneditor.res;

import java.io.File;

import edu.raf.gef.app.Resources;

public class BeansResources extends Resources {

	public BeansResources() {
		super(BeansResources.class.getPackage());
	}

	@Override
	protected String getBundlePath() {
		return super.location + "locales" + File.separator + "BeansMessageBundle";
	}
}
