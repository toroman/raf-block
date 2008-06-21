package edu.raf.gefcomponents.beaneditor.gui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 * Makes property editing an undoable edit.
 * <p>
 * We use reflection to read the current value. Then we store both new and old
 * value and set them on undo / redo accordingly with the supplied setter
 * method.
 * 
 * @author toroman
 * 
 */
public class UndoablePropertyEdit extends AbstractUndoableEdit {

	private Object oldValue;

	private Object newValue;

	private Method setter;

	private Object source;

	@Override
	public String getPresentationName() {
		return this.setter.getName();
	}

	public UndoablePropertyEdit(Object source, Object oldValue, Object newValue, Method setter)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.setter = setter;
		this.source = source;
	}

	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		try {
			this.setter.invoke(this.source, oldValue);
		} catch (Exception e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, "Error setting value!", e);
			throw new CannotUndoException();
		}
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		try {
			this.setter.invoke(this.source, newValue);
		} catch (Exception e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, "Error setting value!", e);
			throw new CannotRedoException();
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1433910064820869902L;

}
