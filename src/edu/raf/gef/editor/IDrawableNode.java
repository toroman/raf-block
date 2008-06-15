package edu.raf.gef.editor;

import javax.swing.tree.MutableTreeNode;

import edu.raf.gef.editor.model.object.Drawable;

public interface IDrawableNode extends MutableTreeNode {

	public Drawable getDrawable();

}
