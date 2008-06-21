package edu.raf.gef.app.util;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * Default Swing UndoManager.
 * <p>
 * Extended here in case of changing (easier replace).
 */
public class GefUndoManager extends UndoManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9160087188556129972L;

	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();

	@Override
	public synchronized boolean addEdit(UndoableEdit anEdit) {
		boolean result = super.addEdit(anEdit);
		notifyListeners();
		return result;
	}

	@Override
	public synchronized void undo() throws CannotUndoException {
		super.undo();
		notifyListeners();

	}

	@Override
	public synchronized void redo() throws CannotRedoException {
		super.redo();
		notifyListeners();
	}

	public void notifyListeners() {
		for (ActionListener aci : listeners) {
			aci.actionPerformed(null);
		}
	}

	public void addListener(ActionListener obs) {
		synchronized (this.listeners) {
			this.listeners.add(obs);
		}
	}

	public void removeListener(ActionListener obs) {
		synchronized (this.listeners) {
			this.listeners.remove(obs);
		}
	}

}
