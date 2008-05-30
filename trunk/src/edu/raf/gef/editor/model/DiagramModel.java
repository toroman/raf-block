package edu.raf.gef.editor.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import edu.raf.gef.editor.model.events.DrawableAddedEvent;
import edu.raf.gef.editor.model.events.DrawableZOrderEvent;
import edu.raf.gef.editor.model.object.DrawableElement;

public class DiagramModel extends Observable {
	private final ArrayList<DrawableElement> drawables;
	/**
	 * Return unmodifiable version, because the list writing is encapsulated.
	 */
	private final Collection<DrawableElement> readOnlyDrawables;

	/**
	 * Other data to be persisted with the model!
	 */
	private final List<Object> nonDrawables;

	private String title = "Untitled Diagram";

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public DiagramModel() {
		drawables = new ArrayList<DrawableElement>();
		readOnlyDrawables = Collections.unmodifiableCollection(drawables);
		nonDrawables = new ArrayList<Object>();
	}

	public boolean addElement(DrawableElement element) {
		if (drawables.add(element)) {
			setChanged();
			notifyObservers(new DrawableAddedEvent(element));
			clearChanged();
			return true;
		} else {
			return false;
		}
	}

	public boolean removeElement(DrawableElement element) {
		if (drawables.remove(element)) {
			setChanged();
			notifyObservers(new DrawableAddedEvent(element));
			clearChanged();
			return true;
		} else {
			return false;
		}
	}

	public boolean moveForward(DrawableElement element) {
		int index = drawables.indexOf(element);
		if (index == drawables.size() - 1)
			return false;
		drawables.remove(index);
		drawables.add(element);
		setChanged();
		notifyObservers(new DrawableZOrderEvent(element));
		clearChanged();
		return true;
	}

	/**
	 * 
	 * @return The unmodifiable version of drawable elements!
	 */
	public Collection<DrawableElement> getDrawables() {
		return readOnlyDrawables;
	}

}
