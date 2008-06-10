package edu.raf.gefcomponents.beaneditor.impl;

import edu.raf.gef.services.beaneditor.IBeanEditor;
import edu.raf.gefcomponents.beaneditor.gui.PropertiesPanel;

public class BeanEditorImplementation implements IBeanEditor {

	private PropertiesPanel _view;

	public BeanEditorImplementation() {

	}

	public synchronized PropertiesPanel getView() {
		if (_view == null)
			_view = new PropertiesPanel();
		return _view;
	}

	@Override
	public void addBean(Object bean) {
		getView().setObject(bean);
	}

	@Override
	public void removeBean(Object bean) {
		getView().setObject(null);
	}

}
