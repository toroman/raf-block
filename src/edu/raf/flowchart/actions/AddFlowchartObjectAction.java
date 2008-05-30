package edu.raf.flowchart.actions;

import edu.raf.flowchart.FlowChartPlugin;
import edu.raf.gef.app.Resources;
import edu.raf.gef.editor.actions.AddDrawableAction;
import edu.raf.gef.editor.model.object.DrawableElement;
import edu.raf.gef.gui.SelectedDiagramProvider;

public class AddFlowchartObjectAction extends AddDrawableAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4851339176480390608L;

	public AddFlowchartObjectAction(Class<? extends DrawableElement> type, String ID,
			SelectedDiagramProvider selDiagram) {
		super(type, ID, selDiagram);
	}

	@Override
	protected Resources getResources() {
		return FlowChartPlugin.resources;
	}
}
