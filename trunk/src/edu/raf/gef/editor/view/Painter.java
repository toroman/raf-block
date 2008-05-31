package edu.raf.gef.editor.view;

import java.awt.Graphics2D;

import edu.raf.gef.editor.model.object.Drawable;

public interface Painter<T extends Drawable> {
	public void paint(Graphics2D g, T object);
}
