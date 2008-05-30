package edu.raf.gef.editor.view;

import java.awt.Graphics2D;

import edu.raf.gef.editor.model.object.DrawableElement;

public interface Painter<T extends DrawableElement> {
	public void paint(Graphics2D g, T object);
}
