package edu.raf.flowchart.app.framework;

import java.util.Locale;

import edu.raf.flowchart.app.Resources;
import edu.raf.flowchart.app.errors.ProgrammingError;

/**
 * Framework will wrap plugins in this class.
 * 
 * @param <T>
 */
public class PluginContainer {
	private final Class<? extends EditorPlugin> pluginClass;

	public PluginContainer(Class<? extends EditorPlugin> klass) {
		this.pluginClass = klass;

	}

	public Class<? extends EditorPlugin> getPluginClass() {
		return pluginClass;
	}

	public synchronized Resources getResources() {
		try {
			return (Resources) pluginClass.getMethod("getResources", Locale.class).invoke(null,
				Resources.getLocale());
		} catch (Exception ex) {
			throw new ProgrammingError(
					"Invalid plugin! Plugin must have a static getResources() method.");
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pluginClass == null) ? 0 : pluginClass.hashCode());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PluginContainer other = (PluginContainer) obj;
		if (pluginClass == null) {
			if (other.pluginClass != null)
				return false;
		} else if (!pluginClass.equals(other.pluginClass))
			return false;
		return true;
	}

}
