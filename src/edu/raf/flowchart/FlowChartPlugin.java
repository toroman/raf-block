package edu.raf.flowchart;

import java.util.ArrayList;
import java.util.List;

import edu.raf.flowchart.actions.ActionExecuteAlgorithm;
import edu.raf.flowchart.actions.AddFlowchartDraggableAction;
import edu.raf.flowchart.actions.AddFlowchartLinkAction;
import edu.raf.flowchart.blocks.ConditionBlock;
import edu.raf.flowchart.blocks.EndBlock;
import edu.raf.flowchart.blocks.ExecutionBlock;
import edu.raf.flowchart.blocks.InputBlock;
import edu.raf.flowchart.blocks.JoinBlock;
import edu.raf.flowchart.blocks.OutputBlock;
import edu.raf.flowchart.blocks.StartBlock;
import edu.raf.flowchart.diagram.FlowChartDiagram;
import edu.raf.flowchart.link.FlowchartLink;
import edu.raf.gef.Main;
import edu.raf.gef.app.IResources;
import edu.raf.gef.app.Resources;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.model.object.Draggable;
import edu.raf.gef.editor.model.object.impl.Link;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.actions.ActionAddDiagram;
import edu.raf.gef.gui.swing.menus.StandardMenuParts;
import edu.raf.gef.plugin.DiagramPlugin;
import edu.raf.gef.services.ServiceManager;
import edu.raf.gef.services.mime.ProjectsFileHandler;

public class FlowChartPlugin implements DiagramPlugin {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4600587151373699739L;

	static public final IResources resources = new Resources(FlowChartPlugin.class.getPackage());

	private final List<Class<? extends Draggable>> draggables;
	private final List<Class<? extends Link>> links;
	/**
	 * Instead of passing the whole frame, more "nice" would be to pass only
	 * necessary parts, like MenuManager, but let it be like this ...
	 */
	protected MainFrame mainFrame;

	private final FlowChartFileHandler handler = new FlowChartFileHandler();

	public FlowChartPlugin() {
		draggables = new ArrayList<Class<? extends Draggable>>();
		draggables.add(ExecutionBlock.class);
		draggables.add(ConditionBlock.class);
		draggables.add(JoinBlock.class);
		draggables.add(InputBlock.class);
		draggables.add(OutputBlock.class);
		draggables.add(StartBlock.class);
		draggables.add(EndBlock.class);

		links = new ArrayList<Class<? extends Link>>();
		links.add(FlowchartLink.class);

		ServiceManager.getServices().addServiceImplementation(handler, ProjectsFileHandler.class);
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
		for (Class<? extends Draggable> d : draggables) {
			if (Draggable.class.isAssignableFrom(d)) {
				mf.getToolbarManager()
						.addAction(
							getClass().getName(),
							new AddFlowchartDraggableAction(mf, d.asSubclass(Draggable.class), d
									.getName()));
			}
		}

		String linkGroup = getClass().getName() + ".links";
		for (Class<? extends Link> d : links) {
			if (Link.class.isAssignableFrom(d)) {
				mf.getToolbarManager().addAction(linkGroup,
					new AddFlowchartLinkAction(mf, d.asSubclass(Link.class), d.getName()));
			}
		}

		String executionGroup = getClass().getName() + ".exec";
		mf.getToolbarManager().addAction(executionGroup, new ActionExecuteAlgorithm(mainFrame));
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

}
