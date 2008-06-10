package edu.raf.gef.gui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.Action;

public final class StandardOverridableAction extends ResourceConfiguredAction implements
		OverridableAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4673031454432255966L;

	protected Action override;

	public StandardOverridableAction(Component onErrorComponent, String id) {
		super(onErrorComponent, id);
		setEnabled(false);
	}

	@Override
	public final void setOverridenBy(Action action) {
		this.override = action;
		if (action != null) {
			setEnabled(action.isEnabled());
		} else {
			setEnabled(false);
		}
	}

	@Override
	public boolean isEnabled() {
		return override != null && override.isEnabled();
	}

	@Override
	public final void actionPerformed(ActionEvent e) {
		if (this.override != null)
			this.override.actionPerformed(e);
		else
			setEnabled(false);
	}

}
