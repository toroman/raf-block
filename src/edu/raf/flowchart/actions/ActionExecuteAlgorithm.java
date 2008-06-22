package edu.raf.flowchart.actions;

import java.awt.event.ActionEvent;
import java.util.Collection;

import edu.raf.flowchart.FlowChartPlugin;
import edu.raf.flowchart.blocks.StartBlock;
import edu.raf.flowchart.exceptions.FCExecutionException;
import edu.raf.flowchart.scripting.ScriptExecutionManager;
import edu.raf.flowchart.syntax.IExecutionManager;
import edu.raf.gef.app.IResources;
import edu.raf.gef.editor.model.object.Focusable;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.actions.ResourceConfiguredAction;

public class ActionExecuteAlgorithm extends ResourceConfiguredAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8538571672506242385L;

	private MainFrame mainFrame;

	public ActionExecuteAlgorithm(MainFrame mainFrame) {
		super(mainFrame.getFrame(), "ExecuteAlgorithm");
		this.mainFrame = mainFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Collection<Focusable> c = mainFrame.getSelectedDiagram().getController()
				.getFocusedObjects();
		if (c.size() == 1) {
			Focusable f = c.iterator().next();
			if (f instanceof StartBlock) {
				IExecutionManager mgr = ScriptExecutionManager.createJavaScript(mainFrame
						.getSelectedDiagram());
				try {
					mgr.run((StartBlock) f);
				} catch (FCExecutionException e1) {
					getGeh().handleUserError(
						"Execution error occurred!",
						"Reason is: " + e1.getMessage()
								+ "\n\nCheck your log for detailed information.");
				}
				return;
			}
		}
		getGeh().handleUserError(getValue(NAME).toString(), "You should select a start node!");
	}

	@Override
	protected IResources getResources() {
		return FlowChartPlugin.resources;
	}
}
