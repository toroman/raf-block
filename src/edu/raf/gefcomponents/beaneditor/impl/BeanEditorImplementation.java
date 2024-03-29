package edu.raf.gefcomponents.beaneditor.impl;

import javax.swing.undo.UndoManager;

import edu.raf.gef.services.beaneditor.IBeanEditor;
import edu.raf.gefcomponents.beaneditor.gui.PropertiesPanel;

public class BeanEditorImplementation implements IBeanEditor {

	private PropertiesPanel _view;

	public BeanEditorImplementation() {
		this._view = new PropertiesPanel();
	}

	public synchronized PropertiesPanel getView() {
		if (_view == null)
			_view = new PropertiesPanel();
		return _view;
	}

	@Override
	public void addBean(Object bean, UndoManager mgr) {
		getView().setUndoManager(mgr);
		getView().setObject(bean);
		getView().grabFocus();
	}

	@Override
	public void removeBean(Object bean) {
		getView().setObject(null);
	}

}
