package edu.raf.flowchart;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.raf.flowchart.blocks.ExecutionBlock;
import edu.raf.flowchart.diagram.FlowChartDiagram;
import edu.raf.gef.app.Resources;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.model.object.DrawableElement;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.actions.CreateDocumentAction;
import edu.raf.gef.gui.swing.menus.StandardMenuParts;
import edu.raf.gef.plugin.DiagramPlugin;

public class FlowChartPlugin implements DiagramPlugin {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4600587151373699739L;

	static final private Resources resources = new Resources(FlowChartPlugin.class.getPackage()
			.getName().replace('.', File.separatorChar)
			+ "/res/");

	private final List<Class<? extends DrawableElement>> drawables;

	/**
	 * Instead of passing the whole frame, more "nice" would be to pass only
	 * necessary parts, like MenuManager, but let it be like this ...
	 */
	private MainFrame mainFrame;

	public FlowChartPlugin() {
		drawables = new ArrayList<Class<? extends DrawableElement>>();
		drawables.add(ExecutionBlock.class);
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
	public Resources getResources() {
		return resources;
	}

	@Override
	public List<Class<? extends DrawableElement>> getDrawableClasses() {
		return drawables;
	}

	@Override
	public void setMainFrame(MainFrame mf) {
		this.mainFrame = mf;
		mf.getMenuManager().addAction(StandardMenuParts.NEW_DIAGRAM_PART,
			new CreateDocumentAction(mf, this));
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}
}
