package edu.raf.gef.editor.model.object;

import java.beans.VetoableChangeListener;

public interface VetoableJavaBean {
	public void addListener(VetoableChangeListener listener);

	public void removeListener(VetoableChangeListener listener);
}
