package edu.raf.gef.gui.swing;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JInternalFrame;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.plugin.AbstractPlugin;

public class DiagramPluginFrame extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8896598624804157856L;

	protected final AbstractPlugin plugin;

	protected final GefDiagram diagram;

	public DiagramPluginFrame(GefDiagram diagram, AbstractPlugin plugin) {
		super(diagram.getModel().getTitle(), true, true, true);
		this.plugin = plugin;
		this.diagram = diagram;
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.add(diagram.getView().getCanvas(), BorderLayout.CENTER);
	}

	public AbstractPlugin getPlugin() {
		return plugin;
	}

	public GefDiagram getDiagram() {
		return diagram;
	}
}
