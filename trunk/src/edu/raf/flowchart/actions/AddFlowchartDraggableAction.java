package edu.raf.flowchart.actions;

import java.awt.Component;

import edu.raf.flowchart.FlowChartPlugin;
import edu.raf.flowchart.diagram.FlowChartDiagram;
import edu.raf.gef.app.IResources;
import edu.raf.gef.editor.actions.AddDraggableAction;
import edu.raf.gef.editor.model.object.Draggable;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.swing.DiagramPluginFrame;

public class AddFlowchartDraggableAction extends AddDraggableAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4851339176480390608L;

	public AddFlowchartDraggableAction(MainFrame mf, Class<? extends Draggable> type, String ID) {
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
