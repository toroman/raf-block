package edu.raf.gef.plugin;

import java.util.HashSet;
import java.util.Set;

import edu.raf.gef.app.errors.GefError;

public class ComponentDiscoveryUtils {
	final Set<AbstractPlugin> plugins = new HashSet<AbstractPlugin>();;

	@SuppressWarnings("unchecked")
	public void discover(String[] klasses) {
		for (String className : klasses) {
			Class<?> klass;
			try {
				klass = Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			if (!DiagramPlugin.class.isAssignableFrom(klass)) {
				throw new GefError("Invalid plugin!");
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

	@SuppressWarnings("unchecked")
	public AbstractPlugin[] getPlugins() {
		return plugins.toArray(new AbstractPlugin[0]);
	}
}
