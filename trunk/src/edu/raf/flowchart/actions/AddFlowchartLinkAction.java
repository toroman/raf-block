package edu.raf.flowchart.actions;

import edu.raf.flowchart.FlowChartPlugin;
import edu.raf.flowchart.diagram.FlowChartDiagram;
import edu.raf.gef.app.IResources;
import edu.raf.gef.editor.actions.AddLinkAction;
import edu.raf.gef.editor.model.object.impl.Link;
import edu.raf.gef.gui.SelectedDiagramProvider;
import edu.raf.gef.gui.actions.ContextSensitiveAction;
import edu.raf.gef.gui.swing.DiagramPluginFrame;

public class AddFlowchartLinkAction extends AddLinkAction implements ContextSensitiveAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4851339176480390608L;

	public AddFlowchartLinkAction(Class<? extends Link> type, String ID,
			SelectedDiagramProvider sdp) {
		super(type, ID, sdp);
	}

	@Override
	protected IResources getResources() {
		return FlowChartPlugin.resources;
	}

	public boolean worksOn(Object focused) {
		return focused != null && (focused instanceof DiagramPluginFrame)
				&& (((DiagramPluginFrame) focused).getDiagram() instanceof FlowChartDiagram);
	}
}
