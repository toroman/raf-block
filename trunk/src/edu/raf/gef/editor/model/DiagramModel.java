package edu.raf.gef.editor.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import edu.raf.gef.editor.model.events.DrawableAddedEvent;
import edu.raf.gef.editor.model.events.DrawableZOrderEvent;
import edu.raf.gef.editor.model.object.Drawable;

public class DiagramModel extends Observable implements Observer {
	private final ArrayList<Drawable> drawables;
	/**
	 * Return unmodifiable version, because the list writing is encapsulated.
	 */
	private final Collection<Drawable> readOnlyDrawables;

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
		drawables = new ArrayList<Drawable>();
		readOnlyDrawables = Collections.unmodifiableCollection(drawables);
		nonDrawables = new ArrayList<Object>();
	}

	public boolean addElement(Drawable element) {
		if (drawables.add(element)) {
			setChanged();
			notifyObservers(new DrawableAddedEvent(element));
			clearChanged();
			return true;
		} else {
			return false;
		}
	}

	public boolean removeElement(Drawable element) {
		if (drawables.remove(element)) {
			setChanged();
			notifyObservers(new DrawableAddedEvent(element));
			clearChanged();
			return true;
		} else {
			return false;
		}
	}

	public boolean moveForward(Drawable element) {
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
	
	public Drawable getDrawableAt (Point2D point) {
		for (Drawable drawable: drawables) {
			if (drawable.isUnderLocation (point))
				return drawable;
		}
		return null;
	}

	/**
	 * 
	 * @return The unmodifiable version of drawable elements!
	 */
	public Collection<Drawable> getDrawables() {
		return readOnlyDrawables;
	}

	@Override
	public void update(Observable observable, Object param) {
		setChanged();
		notifyObservers();
		clearChanged();
	}
}
