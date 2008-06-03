package edu.raf.flowchart;

import java.util.ArrayList;
import java.util.List;

import edu.raf.flowchart.actions.AddFlowchartDraggableAction;
import edu.raf.flowchart.actions.AddFlowchartLinkAction;
import edu.raf.flowchart.blocks.ExecutionBlock;
import edu.raf.flowchart.diagram.FlowChartDiagram;
import edu.raf.flowchart.link.FlowchartLink;
import edu.raf.gef.app.IResources;
import edu.raf.gef.app.Resources;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.model.object.Draggable;
import edu.raf.gef.editor.model.object.impl.Link;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.SelectedDiagramProvider;
import edu.raf.gef.gui.actions.ActionAddDiagram;
import edu.raf.gef.gui.swing.menus.StandardMenuParts;
import edu.raf.gef.plugin.DiagramPlugin;

public class FlowChartPlugin implements DiagramPlugin {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4600587151373699739L;

	static public final IResources resources = new Resources(FlowChartPlugin.class.getPackage());

	private final List<Class<? extends Draggable>> draggables;
	private final List<Class<? extends Link>> links;

//	private final List<AddFlowchartDraggableAction> addObjects = new ArrayList<AddFlowchartDraggableAction>();

	/**
	 * Instead of passing the whole frame, more "nice" would be to pass only
	 * necessary parts, like MenuManager, but let it be like this ...
	 */
	protected MainFrame mainFrame;

	public FlowChartPlugin() {
		draggables = new ArrayList<Class<? extends Draggable>>();
		draggables.add(ExecutionBlock.class);
		
		links = new ArrayList<Class<? extends Link>>();
		links.add(FlowchartLink.class);
	}

	@Override
	public Class<? extends GefDiagram> getDiagramClass() {
		return FlowChartDiagram.class;
	}

	@Override
	public String[] getSupportedFileExtensions() {
		return new String[] { ".rfc" };
	}

	@Override
	public IResources getResources() {
		return resources;
	}

	@Override
	public void setMainFrame(MainFrame mf) {
		this.mainFrame = mf;
		mf.getMenuManager().addAction(StandardMenuParts.NEW_DIAGRAM_PART,
			new ActionAddDiagram(mf, this));
		SelectedDiagramProvider sdp = new SelectedDiagramProvider() {
			public GefDiagram getSelectedDiagram() {
				return FlowChartPlugin.this.mainFrame.getSelectedDiagram();
			}
		};
		for (Class<? extends Draggable> d : draggables) {
			if (Draggable.class.isAssignableFrom(d)) {
				mf.getToolbarManager().addAction(getClass().getName(),
					new AddFlowchartDraggableAction(d.asSubclass(Draggable.class), d.getName(), sdp));
			}
		}
		for (Class<? extends Link> d : links) {
			if (Link.class.isAssignableFrom(d)) {
				mf.getToolbarManager().addAction(getClass().getName(),
					new AddFlowchartLinkAction(d.asSubclass(Link.class), d.getName(), sdp));
			}
		}
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

}
