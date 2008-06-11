package edu.raf.gef.editor.actions;

import java.awt.event.ActionEvent;
import java.util.List;

import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.actions.ResourceConfiguredAction;
import edu.raf.gef.services.ServiceManager;
import edu.raf.gef.services.ServiceManager.ServiceManagerListener;
import edu.raf.gef.services.beaneditor.IBeanEditor;

public class ActionShowProperties extends ResourceConfiguredAction implements
		ServiceManagerListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9147144256085234172L;
	private ServiceManager serviceManager;
	private IBeanEditor srvBeanEditor;
	private MainFrame mainFrame;
	private Object bean;

	public ActionShowProperties(MainFrame mf, ServiceManager serviceManager) {
		super(mf.getFrame(), "ShowProperties");
		this.mainFrame = mf;
		this.serviceManager = serviceManager;
		this.serviceManager.addServiceManagerListener(this);
		List<? extends IBeanEditor> t = serviceManager.getServiceImplementations(IBeanEditor.class);
		if (t.size() == 0) {
			setEnabled(false);
		} else {
			// TODO: give option to user to select which bean editor to use!
			srvBeanEditor = t.get(0);
			setEnabled(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (bean != null)
			srvBeanEditor.removeBean(bean);
		bean = mainFrame.getSelectedDiagram().getController().getFocusedObjects();
		srvBeanEditor.addBean(bean);
	}

	@Override
	public <I, T extends I> void serviceAdded(T implementation, Class<I> service) {
		if (service != IBeanEditor.class)
			return;
		if (srvBeanEditor == null) {
			srvBeanEditor = (IBeanEditor) implementation;
			setEnabled(true);
		}
	}

}
