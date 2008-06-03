package edu.raf.gef.editor;

import java.util.Observable;
import java.util.Observer;

import javax.swing.tree.MutableTreeNode;

import edu.raf.gef.gui.swing.DiagramPluginFrame;

public interface IDiagramTreeModel extends MutableTreeNode, Observer {

	public abstract void update(Observable o, Object arg);

	public abstract GefDiagram getDiagram();

	public abstract DiagramPluginFrame getDiagramEditorComponent();

	public abstract void setDiagramEditorComponent(DiagramPluginFrame cmp);

}