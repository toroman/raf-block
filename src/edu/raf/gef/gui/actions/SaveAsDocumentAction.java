package edu.raf.gef.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import edu.raf.gef.plugin.AbstractPlugin;
import edu.raf.gef.plugin.DiagramPlugin;

public class SaveAsDocumentAction extends ResourceConfiguredAction implements
		ContextSensitiveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9119376104759267475L;

	public static final String ID = "saveas";

	public SaveAsDocumentAction(Component onErrorComponent) {
		super(onErrorComponent, ID);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	/**
	 * This action can be applied to plugins which can be saved.
	 * <p>
	 * Those are diagram plugins at the moment, but since this can be changed in
	 * the future, this mechanism may change.
	 * <p>
	 * TODO: For example, perhaps the best way is to "ask" the plugin - hey, can
	 * you be saved now? Is there need to be saved now? etc.
	 */
	@Override
	public boolean worksOn(AbstractPlugin plugin) {
		return plugin instanceof DiagramPlugin;
	}

}
