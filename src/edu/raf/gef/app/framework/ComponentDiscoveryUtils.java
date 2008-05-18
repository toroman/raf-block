package edu.raf.gef.app.framework;

import java.util.HashSet;
import java.util.Set;

import edu.raf.gef.app.errors.ProgrammingError;

public class ComponentDiscoveryUtils {
	static final Set<PluginContainer> plugins = new HashSet<PluginContainer>();;

	@SuppressWarnings("unchecked")
	public static void discover(String[] klasses) {
		for (String className : klasses) {
			Class<?> klass;
			try {
				klass = Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			if (!EditorPlugin.class.isAssignableFrom(klass)) {
				throw new ProgrammingError("Invalid plugin!");
			}
			PluginContainer container = new PluginContainer((Class<? extends EditorPlugin>) klass);
			plugins.add(container);
		}
	}

	@SuppressWarnings("unchecked")
	public static PluginContainer[] getPlugins() {
		return plugins.toArray(new PluginContainer[0]);
	}
}
