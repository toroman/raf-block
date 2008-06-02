package edu.raf.gef.editor;

import java.util.Observable;
import java.util.Observer;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.gui.swing.DiagramPluginFrame;

public class DefaultDiagramTreeModel extends DefaultMutableTreeNode implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3857356427558399368L;

	private GefDiagram diagram;

	private DiagramPluginFrame component;

	public DefaultDiagramTreeModel(GefDiagram gefDiagram) {
		this.diagram = gefDiagram;
		diagram.getModel().addObserver(this);
		recreateModel();
	}

	@Override
	public void update(Observable o, Object arg) {
		recreateModel();
	}

	public GefDiagram getDiagram() {
		return diagram;
	}

	public DiagramPluginFrame getDiagramEditorComponent() {
		return component;
	}

	public void setDiagramEditorComponent(DiagramPluginFrame cmp) {
		component = cmp;
	}

	private void recreateModel() {
		this.removeAllChildren();
		setUserObject(diagram.getModel().getTitle());
		for (Drawable d : this.diagram.getModel().getDrawables()) {
			add(new DefaultDrawableTreeNode(this, d));
		}
	}

}
