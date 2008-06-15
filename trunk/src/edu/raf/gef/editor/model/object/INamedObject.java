package edu.raf.gef.editor.model.object;

import edu.raf.gef.services.beaneditor.annotations.Property;

public interface INamedObject extends VetoableJavaBean {
	@Property(editable = true)
	public String getName();

	public void setName(String name);
}
