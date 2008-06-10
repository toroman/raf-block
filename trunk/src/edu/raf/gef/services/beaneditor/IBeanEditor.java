package edu.raf.gef.services.beaneditor;

/**
 * Bean editor is a service that offers classical JavaBean editing. It has
 * nothing more but adding a bean for editing and removing.
 * 
 */
public interface IBeanEditor {
	/**
	 * Add bean to the editor
	 * 
	 * @param bean
	 */
	public void addBean(Object bean);

	/**
	 * Remove bean from the editor (stop editing)
	 * 
	 * @param bean
	 */
	public void removeBean(Object bean);
}
