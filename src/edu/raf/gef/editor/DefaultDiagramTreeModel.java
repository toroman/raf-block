package edu.raf.gef.editor;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import edu.raf.gef.editor.model.events.DrawableAddedEvent;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.gui.swing.DiagramPluginFrame;
import edu.raf.gef.util.TransientObservable;
import edu.raf.gef.workspace.Workspace;

public class DefaultDiagramTreeModel implements IDiagramTreeNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3857356427558399368L;

	private GefDiagram diagram;

	private DiagramPluginFrame component;

	private ArrayList<IDrawableNode> nodes = new ArrayList<IDrawableNode>();

	private MutableTreeNode parent;

	private Workspace workspace;

	public DefaultDiagramTreeModel(GefDiagram gefDiagram, Workspace ws) {
		this.diagram = gefDiagram;
		this.workspace = ws;
		diagram.getModel().addObserver(this);
		for (Drawable d : this.diagram.getModel().getDrawables()) {
			nodes.add(new DefaultDrawableTreeNode(d, this));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.raf.gef.editor.IDiagramTreeModel#update(java.util.Observable,
	 *      java.lang.Object)
	 */
	@Override
	public void update(TransientObservable o, Object arg) {
		if (arg instanceof DrawableAddedEvent) {
			DrawableAddedEvent dae = ((DrawableAddedEvent) arg);
			Drawable d = dae.getSource();
			if (dae.isAdded()) {
				IDrawableNode node = new DefaultDrawableTreeNode(d, this);
				nodes.add(node);
				workspace.nodesWereInserted(this,
						new int[] { nodes.size() - 1 });
			} else {
				// O(n) remove
				Iterator<IDrawableNode> it = nodes.iterator();
				int index = 0;
				while (it.hasNext()) {
					IDrawableNode id = it.next();
					if (id.getDrawable() == d) {
						it.remove();
						workspace.nodesWereRemoved(this, new int[] { index },
								new IDrawableNode[] { id });
						break;
					}
					index++;
				}
			}

		}
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

	@Override
	public void insert(MutableTreeNode child, int index) {
		throw new UnsupportedOperationException("This shouldn't be called.");
	}

	@Override
	public void remove(int index) {
		throw new UnsupportedOperationException("This shouldn't be called.");
	}

	@Override
	public void remove(MutableTreeNode node) {
		throw new UnsupportedOperationException("This shouldn't be called.");
	}

	@Override
	public void removeFromParent() {
		throw new UnsupportedOperationException("This shouldn't be called.");
	}

	@Override
	public void setParent(MutableTreeNode newParent) {
		this.parent = newParent;
	}

	@Override
	public void setUserObject(Object object) {
		throw new UnsupportedOperationException("This shouldn't be called.");
	}

	@Override
	public Enumeration children() {
		final Iterator it = diagram.getModel().getDrawables().iterator();
		return new Enumeration() {
			public boolean hasMoreElements() {
				return it.hasNext();
			}

			public Object nextElement() {
				return it.next();
			}
		};
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return nodes.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return nodes.size();
	}

	@Override
	public int getIndex(TreeNode node) {
		return nodes.indexOf(node);
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}
}
