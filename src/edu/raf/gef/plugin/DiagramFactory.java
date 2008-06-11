package edu.raf.gef.plugin;

import edu.raf.gef.app.exceptions.GefCreationalException;
import edu.raf.gef.editor.GefDiagram;

public class DiagramFactory {
	private DiagramPlugin plugin;

	public DiagramFactory(DiagramPlugin plugin) {
		this.plugin = plugin;
	}

	public GefDiagram createDiagram() {
		try {
			GefDiagram dg = plugin.getDiagramClass().newInstance();
			return dg;
		} catch (Exception ex) {
			throw new GefCreationalException("Couldn't create diagram.", ex);
		}
	}
}
