package edu.raf.gef.plugin;

import java.util.HashSet;
import java.util.Set;

import edu.raf.gef.app.errors.GefError;

public class ComponentDiscoveryUtils {
	final Set<AbstractPlugin> plugins = new HashSet<AbstractPlugin>();
	private String[] klasses;;

	@SuppressWarnings("unchecked")
	public void discover(String[] klasses) {
		this.klasses = klasses;

		for (String className : klasses) {
			Class<?> klass;
			try {
				klass = Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			if (!AbstractPlugin.class.isAssignableFrom(klass)) {
				throw new GefError("Invalid plugin " + className);
			}

			try {
				plugins.add((AbstractPlugin) klass.newInstance());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public String[] getKlasses() {
		return klasses;
	}

	@SuppressWarnings("unchecked")
	public AbstractPlugin[] getPlugins() {
		return plugins.toArray(new AbstractPlugin[0]);
	}
}
