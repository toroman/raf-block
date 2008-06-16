package edu.raf.flowchart.actions;

import java.awt.Component;

import edu.raf.flowchart.FlowChartPlugin;
import edu.raf.flowchart.diagram.FlowChartDiagram;
import edu.raf.gef.app.IResources;
import edu.raf.gef.editor.actions.AddLinkAction;
import edu.raf.gef.editor.model.object.impl.Link;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.swing.DiagramPluginFrame;

public class AddFlowchartLinkAction extends AddLinkAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4851339176480390608L;

	public AddFlowchartLinkAction(MainFrame mf, Class<? extends Link> type, String ID) {
		super(mf, type, ID);
	}

	@Override
	protected IResources getResources() {
		return FlowChartPlugin.resources;
	}

	@Override
	protected boolean worksOn(Component focused) {
		return focused != null && (focused instanceof DiagramPluginFrame)
				&& (((DiagramPluginFrame) focused).getDiagram() instanceof FlowChartDiagram);
	}
}
