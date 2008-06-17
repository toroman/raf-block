package edu.raf.gefcomponents.beaneditor.impl;

import edu.raf.gef.services.beaneditor.IBeanEditor;
import edu.raf.gefcomponents.beaneditor.BeanEditorPlugin;
import edu.raf.gefcomponents.beaneditor.gui.PropertiesPanel;

public class BeanEditorImplementation implements IBeanEditor {

	private PropertiesPanel _view;
	private BeanEditorPlugin plugin;

	public BeanEditorImplementation(BeanEditorPlugin beanEditorPlugin) {
		this.plugin = beanEditorPlugin;
	}

	public synchronized PropertiesPanel getView() {
		if (_view == null)
			_view = new PropertiesPanel();
		return _view;
	}

	@Override
	public void addBean(Object bean) {
		getView().setObject(bean);
		plugin.getMainFrame().getTabbedTools().setSelectedComponent(getView());
	}

	@Override
	public void removeBean(Object bean) {
		getView().setObject(null);
	}

}