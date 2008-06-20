package edu.raf.gefcomponents.beaneditor.gui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

public class UndoablePropertyEdit extends AbstractUndoableEdit {

	private Object oldValue;

	private Object newValue;

	private Method setter;

	private Object object;

	@Override
	public String getPresentationName() {
		return this.setter.getName();
	}
	
	public UndoablePropertyEdit(Object object, Method getter, Method setter, Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		this.oldValue = getter.invoke(object);
		this.newValue = value;
		this.setter = setter;
		this.object = object;
	}

	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		try {
			this.setter.invoke(this.object, oldValue);
		} catch (Exception e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, "Error setting value!", e);
			throw new CannotUndoException();
		}
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		try {
			this.setter.invoke(this.object, newValue);
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
