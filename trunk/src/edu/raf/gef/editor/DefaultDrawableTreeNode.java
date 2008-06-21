package edu.raf.gef.editor;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.editor.model.object.impl.RectangularObject;

public class DefaultDrawableTreeNode extends DefaultMutableTreeNode implements IDrawableNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2588579623932593298L;
	private Drawable drawable;
	private IDiagramTreeNode diagramTree;

	public DefaultDrawableTreeNode(Drawable drawable, IDiagramTreeNode diagramTree) {
		super(getNameFromDrawable(drawable));
		this.drawable = drawable;
		this.diagramTree = diagramTree;
	}

	private static Object getNameFromDrawable(Drawable drawable) {
		String name = drawable.getClass().getName();
		int indexOfDot = name.lastIndexOf('.');
		if (indexOfDot >= 0)
			name = name.substring(indexOfDot + 1);
		return name;
	}

	@Override
	public Drawable getDrawable() {
		return drawable;
	}

	@Override
	public TreeNode getParent() {
		return diagramTree;
	}

	@Override
	public void setParent(MutableTreeNode newParent) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString() {
		if (userObject instanceof RectangularObject) {
			return ((RectangularObject) userObject).getTitle();
		} else if (userObject instanceof INamedObject) {
			return ((INamedObject) userObject).getName();
		} else {

			return userObject.toString();
		}
	}

}
