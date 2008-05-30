package edu.raf.gef.editor.model.object;

/**
 * 
 * Elements that can be selected (focused, clicked on, not necessarily movable)
 * should implement this interface! Implementations should implement Java Beans
 * model.
 */
public interface SelectableElement {
	public boolean isSelected();

	/**
	 * 
	 * @param selected
	 * @return true if operation has succeeded, false if action failed (veto)
	 */
	public boolean setSelected(boolean selected);

	public final String SELECTED_PROPERTY = "selected";
}
