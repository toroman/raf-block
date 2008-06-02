package edu.raf.gef.editor;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import edu.raf.gef.editor.model.object.Drawable;

public class DefaultDrawableTreeNode extends DefaultMutableTreeNode {

	private TreeNode parent;
	private Drawable drawable;

	public DefaultDrawableTreeNode(TreeNode parent, Drawable drawable) {
		super(drawable.getClass().getName());
		this.parent = parent;
		this.drawable = drawable;
	}

}
