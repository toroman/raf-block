package edu.raf.gef.editor.actions;

import java.awt.event.ActionEvent;

import edu.raf.gef.app.Resources;
import edu.raf.gef.editor.model.object.DrawableElement;
import edu.raf.gef.gui.actions.ResourceConfiguredAction;

public class AddDrawableAction extends ResourceConfiguredAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -721380454889573427L;
	private final Class<? extends DrawableElement> type;
	private final Resources resources;

	public AddDrawableAction(Class<? extends DrawableElement> type, Resources resources, String ID) {
		super(null, ID);
		this.type = type;
		this.resources = resources;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	@Override
	protected Resources getResources() {
		return resources;
	}
}
