package edu.raf.gef.editor;

import edu.raf.gef.services.beaneditor.annotations.Property;

public interface INamedObject {
	@Property
	public String getName();

	public void setName(String s);
}
