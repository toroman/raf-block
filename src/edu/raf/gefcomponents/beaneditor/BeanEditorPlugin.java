package edu.raf.gefcomponents.beaneditor;

import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.plugin.AbstractPlugin;
import edu.raf.gef.services.ServiceManager;
import edu.raf.gef.services.beaneditor.IBeanEditor;
import edu.raf.gefcomponents.beaneditor.impl.BeanEditorImplementation;
import edu.raf.gefcomponents.beaneditor.res.BeansResources;

public class BeanEditorPlugin implements AbstractPlugin {

	private MainFrame mainFrame;

	private BeanEditorImplementation editor;

	private static class ResourceHolder {
		static private final BeansResources instance = new BeansResources();
	}

	public BeanEditorPlugin() {
		editor = new BeanEditorImplementation();
		ServiceManager.getServices().addServiceImplementation(editor, IBeanEditor.class);
	}

	@Override
	public BeansResources getResources() {
		return ResourceHolder.instance;
	}

	@Override
	public void setMainFrame(MainFrame mf) {
		this.mainFrame = mf;
		mf.addTool(getResources().getString("tab.name"), editor.getView());
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}
}
