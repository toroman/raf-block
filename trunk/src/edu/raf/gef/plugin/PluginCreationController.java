package edu.raf.gef.plugin;

import edu.raf.gef.app.exceptions.GefCreationalException;
import edu.raf.gef.editor.GefDiagram;

public class PluginCreationController {
	private DiagramPlugin plugin;

	public PluginCreationController(DiagramPlugin plugin) {
		this.plugin = plugin;
	}

	public GefDiagram createDiagram() {
		try {
			return (GefDiagram) plugin.getDiagramClass().newInstance();
		} catch (Exception ex) {
			throw new GefCreationalException("Couldn't create diagram.", ex);
		}
	}
}
