package edu.raf.flowchart.actions;

import edu.raf.flowchart.FlowChartPlugin;
import edu.raf.gef.app.Resources;
import edu.raf.gef.editor.actions.AddDraggableAction;
import edu.raf.gef.editor.model.object.Draggable;
import edu.raf.gef.gui.SelectedDiagramProvider;
import edu.raf.gef.gui.actions.ContextSensitiveAction;
import edu.raf.gef.plugin.AbstractPlugin;

public class AddFlowchartObjectAction extends AddDraggableAction implements ContextSensitiveAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4851339176480390608L;

	public AddFlowchartObjectAction(Class<? extends Draggable> type, String ID,
			SelectedDiagramProvider sdp) {
		super(type, ID, sdp);
	}

	@Override
	protected Resources getResources() {
		return FlowChartPlugin.resources;
	}

	public boolean worksOn(AbstractPlugin plugin) {
		return plugin != null && plugin instanceof FlowChartPlugin;
	}
}
