/**
 * Adapted class from "http://www.javalobby.org/java/forums/t19448.html". Thanks to the author!
 */
package edu.raf.gef.gui.actions;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.Action;
import javax.swing.JComponent;

public class GlobalAction extends ResourceConfiguredAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3751709858365594035L;
	Action delegate;
	JComponent source;

	private PropertyChangeListener focusOwnerListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			if ("focusOwner".equals(evt.getPropertyName())) {
				boolean listens = false;
				if (evt.getNewValue() instanceof JComponent) {
					JComponent comp = (JComponent) evt.getNewValue();
					Action action = comp.getActionMap().get(getId());
					if (action != null) {
						listens = true;
						changeDelegate(comp, action);
					}
				}
				setEnabled(listens);
			}
		}
	};

	public GlobalAction(Component parent, String id) {
		super(parent, id);
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(
			focusOwnerListener);
	}

	/*-------------------[ Delegate Listener ]--------------------------*/

	protected void removeDelegate() {
		// TODO Auto-generated method stub

	}

	PropertyChangeSupport support = new PropertyChangeSupport(this);

	private PropertyChangeListener delegateListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			support.firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
		}
	};

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

	private void changeDelegate(JComponent comp, Action action) {
		if (delegate != null)
			delegate.removePropertyChangeListener(delegateListener);
		source = comp;
		delegate = action;
		if (delegate != null) {
			delegate.addPropertyChangeListener(delegateListener);
			boolean enabled = delegate.isEnabled();
			support.firePropertyChange("enabled", !enabled, enabled);
		} else {
			setEnabled(false);
		}
	}

	public boolean isEnabled() {
		return delegate != null && delegate.isEnabled();
	}

	public void setEnabled(boolean enabled) {
		// if (delegate != null)
		// delegate.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	public Object getValue(String key) {
		Object defaultValue = super.getValue(key);
		if (delegate != null) {
			Object delegatesValue = delegate.getValue(key);
			return delegatesValue != null ? delegatesValue : defaultValue;
		} else {
			return defaultValue;
		}
	}

	public void putValue(String key, Object value) {
		super.putValue(key, value);
		// if (key.equals(Action.NAME))
		// actionName = (String) value;
		// else if (key.equals(Action.SHORT_DESCRIPTION))
		// shortDescription = (String) value;
		// else if (key.equals(Action.LONG_DESCRIPTION))
		// longDescription = (String) value;
		// else if (delegate != null)
		// delegate.putValue(key, value);
	}

	public void actionPerformed(ActionEvent ae) {
		if (delegate != null) {
			delegate.actionPerformed(new ActionEvent(source, ae.getID(), ae.getActionCommand(), ae
					.getWhen(), ae.getModifiers()));
		} else {
			getGeh().handleUserError("Operation not applicable",
				"You can't do this with in the current context.");
		}
	}

}
