package edu.raf.gef.plugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.raf.gef.app.errors.GefError;

public class ComponentDiscoveryUtils {
	final Set<AbstractPlugin> plugins = new HashSet<AbstractPlugin>();
	private HashSet<String> klasses = new HashSet<String>();

	@SuppressWarnings("unchecked")
	public void discover(String on, String off) {
		klasses.clear();
		klasses.addAll(Arrays.asList(on.split("\\s")));
		klasses.removeAll(Arrays.asList(off.split("\\s")));
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
		return klasses.toArray(new String[0]);
	}

	@SuppressWarnings("unchecked")
	public AbstractPlugin[] getPlugins() {
		return plugins.toArray(new AbstractPlugin[0]);
	}
}
