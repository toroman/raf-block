package edu.raf.gef.editor.actions;

import java.awt.event.ActionEvent;

import edu.raf.gef.Main;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.control.GefFocusEvent;
import edu.raf.gef.editor.control.GefFocusListener;
import edu.raf.gef.editor.structure.SerializedDiagramObject;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.actions.ResourceConfiguredAction;
import edu.raf.gef.gui.swing.menus.StandardMenuActions;

public class ActionPasteDiagramObject extends ResourceConfiguredAction {

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

	public ActionPasteDiagramObject(MainFrame mainFrame, GefDiagram diagram) {
		super(mainFrame.getFrame(), StandardMenuActions.CUT);
		this.diagram = diagram;
		diagram.getController().addFocusListener(listener);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!isEnabled()) {
			getGeh().handleUserError(getResources().getString("exception.wrongcontext.title"),
				getResources().getString("exception.wrongcontext.message"));
			return;
		}
		Object deserialized = Main.simpleClipboard;
		if (Main.simpleClipboard instanceof SerializedDiagramObject) {
			deserialized = ((SerializedDiagramObject) Main.simpleClipboard).deserialize();

		}
		diagram.getController().paste(deserialized);

	}

	@Override
	public boolean isEnabled() {
		return Main.simpleClipboard != null;
	}

}
