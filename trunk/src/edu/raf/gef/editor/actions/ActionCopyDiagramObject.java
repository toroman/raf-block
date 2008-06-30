package edu.raf.gef.editor.actions;

import java.awt.event.ActionEvent;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.raf.gef.Main;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.control.GefFocusEvent;
import edu.raf.gef.editor.control.GefFocusListener;
import edu.raf.gef.editor.structure.SerializedDiagramObject;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.actions.ResourceConfiguredAction;
import edu.raf.gef.gui.swing.menus.StandardMenuActions;

public class ActionCopyDiagramObject extends ResourceConfiguredAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2157632324539292887L;
	private GefDiagram diagram;

	private GefFocusListener listener = new GefFocusListener() {
		public void focusChanged(GefFocusEvent event) {
			boolean enabled = isEnabled();
			firePropertyChange("enabled", !enabled, enabled);
		}
	};

	public ActionCopyDiagramObject(MainFrame mainFrame, GefDiagram diagram) {
		super(mainFrame.getFrame(), StandardMenuActions.COPY);
		this.diagram = diagram;
		this.diagram.getController().addFocusListener(listener);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!isEnabled()) {
			getGeh().handleUserError(getResources().getString("exception.wrongcontext.title"),
				getResources().getString("exception.wrongcontext.message"));
			return;
		}
		XStream xs = new XStream(new DomDriver());
		Main.simpleClipboard = new SerializedDiagramObject(xs.toXML(diagram.getController().copy()));
	}

	@Override
	public boolean isEnabled() {
		return diagram.getController().getFocusedObjects().size() > 0;
	}

}
