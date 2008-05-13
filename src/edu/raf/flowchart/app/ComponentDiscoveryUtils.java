package edu.raf.flowchart.app;

import java.util.HashSet;
import java.util.Set;

import edu.raf.flowchart.app.errors.ProgrammingError;
import edu.raf.flowchart.gui.swing.EditorPlugin;

public class ComponentDiscoveryUtils {
	static final Set<Class<? extends EditorPlugin>> plugins = new HashSet<Class<? extends EditorPlugin>>();;

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
			plugins.add((Class<? extends EditorPlugin>) klass);
		}
	}

	@SuppressWarnings("unchecked")
	public static Class<EditorPlugin>[] getPlugins() {
		return (Class<EditorPlugin>[]) plugins.toArray(new Class[0]);
	}
}
