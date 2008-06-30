package edu.raf.gef.editor.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.List;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.control.GefFocusEvent;
import edu.raf.gef.editor.control.GefFocusListener;
import edu.raf.gef.editor.model.object.Focusable;
import edu.raf.gef.gui.actions.ResourceConfiguredAction;
import edu.raf.gef.gui.swing.menus.StandardMenuActions;
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
	private Object bean;
	private GefDiagram diagram;

	private GefFocusListener selectionListener = new GefFocusListener() {
		public void focusChanged(GefFocusEvent event) {
			actionPerformed(null);
		}
	};

	public ActionShowProperties(Component component, GefDiagram diagram) {
		super(component, StandardMenuActions.PROPERTIES);
		this.serviceManager = ServiceManager.getServices();
		this.diagram = diagram;
		this.diagram.getController().addFocusListener(selectionListener);
		this.serviceManager.addServiceManagerListener(this);
		List<? extends IBeanEditor> t = serviceManager.getServiceImplementations(IBeanEditor.class);
		if (t.size() == 0) {
			// no bean editing services
			setEnabled(false);
		} else {
			// TODO: give option to user to select which bean editor to use!
			srvBeanEditor = t.get(0);
			setEnabled(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (srvBeanEditor == null) {
			return;
		}
		if (bean != null) {
			srvBeanEditor.removeBean(bean);
		}
		Collection<Focusable> focused = diagram.getController().getFocusedObjects();
		if (focused != null && focused.size() == 1) {
			srvBeanEditor.addBean(bean = focused.iterator().next(), diagram.getUndoManager());
		}
	}

	@Override
	public <I, T extends I> void serviceAdded(T implementation, Class<I> service) {
		if (IBeanEditor.class.isAssignableFrom(service) && srvBeanEditor == null) {
			srvBeanEditor = (IBeanEditor) implementation;
			setEnabled(true);
		}
	}

}
