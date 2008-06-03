package edu.raf.gef.editor;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.raf.gef.editor.model.object.Drawable;

public class DefaultDrawableTreeNode extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2588579623932593298L;

	public DefaultDrawableTreeNode(Drawable drawable) {
		super(drawable);
	}

	@Override
	public String toString() {
		return userObject.toString();
	}

}
