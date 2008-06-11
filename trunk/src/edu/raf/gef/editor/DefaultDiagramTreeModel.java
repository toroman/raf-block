package edu.raf.gef.editor;

import java.util.Observable;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.gui.swing.DiagramPluginFrame;

public class DefaultDiagramTreeModel extends DefaultMutableTreeNode implements IDiagramTreeNode {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.raf.gef.editor.IDiagramTreeModel#update(java.util.Observable,
	 *      java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		recreateModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.raf.gef.editor.IDiagramTreeModel#getDiagram()
	 */
	public GefDiagram getDiagram() {
		return diagram;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.raf.gef.editor.IDiagramTreeModel#getDiagramEditorComponent()
	 */
	public DiagramPluginFrame getDiagramEditorComponent() {
		return component;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.raf.gef.editor.IDiagramTreeModel#setDiagramEditorComponent(edu.raf.gef.gui.swing.DiagramPluginFrame)
	 */
	public void setDiagramEditorComponent(DiagramPluginFrame cmp) {
		component = cmp;
	}

	private void recreateModel() {
		this.removeAllChildren();
		setUserObject(diagram.getModel().getTitle());
		for (Drawable d : this.diagram.getModel().getDrawables()) {
			insert(new DefaultDrawableTreeNode(d), 0);
		}
	}
}
