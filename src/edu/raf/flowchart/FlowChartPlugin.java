package edu.raf.flowchart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JInternalFrame;

import edu.raf.flowchart.actions.AddFlowchartObjectAction;
import edu.raf.flowchart.blocks.ExecutionBlock;
import edu.raf.flowchart.diagram.FlowChartDiagram;
import edu.raf.gef.app.Resources;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.model.object.Draggable;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.SelectedDiagramProvider;
import edu.raf.gef.gui.actions.CreateDocumentAction;
import edu.raf.gef.gui.swing.DiagramPluginFrame;
import edu.raf.gef.gui.swing.menus.StandardMenuParts;
import edu.raf.gef.plugin.DiagramPlugin;

public class FlowChartPlugin implements DiagramPlugin {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4600587151373699739L;

	static public final Resources resources = new Resources(FlowChartPlugin.class.getPackage()
			.getName().replace('.', File.separatorChar)
			+ "/res/");

	private final List<Class<? extends Drawable>> drawables;

	private final List<AddFlowchartObjectAction> addObjects = new ArrayList<AddFlowchartObjectAction>();

	/**
	 * Instead of passing the whole frame, more "nice" would be to pass only
	 * necessary parts, like MenuManager, but let it be like this ...
	 */
	protected MainFrame mainFrame;

	public FlowChartPlugin() {
		drawables = new ArrayList<Class<? extends Drawable>>();
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
	public void setMainFrame(MainFrame mf) {
		this.mainFrame = mf;
		mf.getMenuManager().addAction(StandardMenuParts.NEW_DIAGRAM_PART,
			new CreateDocumentAction(mf, this));
		SelectedDiagramProvider sdp = new SelectedDiagramProvider() {
			public GefDiagram getSelectedDiagram() {
				return FlowChartPlugin.this.mainFrame.getSelectedDiagram();
			}
		};
		for (Class<? extends Drawable> d : drawables) {
			if (Draggable.class.isAssignableFrom(d)) {
				mf.getToolbarManager().addAction(getClass().getName(),
					new AddFlowchartObjectAction(d.asSubclass(Draggable.class), d.getName(), sdp));
			}
		}

		// this.mainFrame.addPropertyChangeListener(MainFrame.SELECTED_FRAME_PROPERTY,
		// new PropertyChangeListener() {
		// public void propertyChange(PropertyChangeEvent evt) {
		// JInternalFrame selectedFrame = evt.getNewValue();
		// if (! (selectedFrame instanceof DiagramPluginFrame)
		// }
		// });
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}
}
