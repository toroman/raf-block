package edu.raf.gef.editor;

import javax.swing.tree.MutableTreeNode;

import edu.raf.gef.gui.swing.DiagramPluginFrame;
import edu.raf.gef.util.TransientObservable;
import edu.raf.gef.util.TransientObserver;

public interface IDiagramTreeNode extends MutableTreeNode, TransientObserver {

	public abstract void update(TransientObservable o, Object arg);

	public abstract GefDiagram getDiagram();

	public abstract DiagramPluginFrame getDiagramEditorComponent();

	public abstract void setDiagramEditorComponent(DiagramPluginFrame cmp);

}