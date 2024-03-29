package edu.raf.gef.editor.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import com.thoughtworks.xstream.converters.Converter;

import edu.raf.gef.editor.model.events.DrawableAddedEvent;
import edu.raf.gef.editor.model.events.DrawableZOrderEvent;
import edu.raf.gef.editor.model.object.AnchorPointContainer;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.editor.model.object.impl.AnchorPoint;
import edu.raf.gef.editor.model.object.impl.Link;
import edu.raf.gef.editor.structure.CompositeUpdateEvent;
import edu.raf.gef.editor.structure.DiagramModelConverter;
import edu.raf.gef.util.TransientObservable;
import edu.raf.gef.util.TransientObserver;

/**
 * The default model.
 * 
 * @author ibocic06
 * 
 */

public class DiagramModel extends TransientObservable implements TransientObserver {
	transient static private int INSTANCE_COUNTER = 0;

	private ArrayList<Drawable> drawables;
	private transient ArrayList<Drawable> temporaryDrawables;
	/**
	 * Return unmodifiable version, because the list writing is encapsulated.
	 */
	private transient Collection<Drawable> readOnlyDrawables;
	private transient Collection<Drawable> readOnlyTemporaryDrawables;

	private String title = "Diagram " + ++INSTANCE_COUNTER;

	public Converter getConverter() {
		return new DiagramModelConverter(getClass());
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public DiagramModel() {
		super();
		drawables = new ArrayList<Drawable>();
		temporaryDrawables = new ArrayList<Drawable>();
		readOnlyDrawables = Collections.unmodifiableCollection(drawables);
		readOnlyTemporaryDrawables = Collections.unmodifiableCollection(temporaryDrawables);
	}

	public synchronized boolean addTemporaryDrawable(Drawable element) {
		if (temporaryDrawables.add(element)) {
			setChanged();
			notifyObservers();
			return true;
		} else
			return false;
	}

	public synchronized boolean removeTemporaryDrawable(Drawable element) {
		if (temporaryDrawables.remove(element)) {
			setChanged();
			notifyObservers();
			return true;
		} else
			return false;
	}

	public Collection<Drawable> getTemporaryDrawables() {
		return readOnlyTemporaryDrawables;
	}

	public synchronized boolean addElement(Drawable element) {
		element.setParent(this);
		if (element instanceof TransientObservable)
			((TransientObservable) element).addObserver(this);
		if (drawables.add(element)) {
			if (element instanceof Link) {
				Link link = (Link) element;
				if (link.getSourceAnchor() != null)
					link.getSourceAnchor().setLink(link);
				if (link.getDestinationAnchor() != null)
					link.getDestinationAnchor().setLink(link);
			}
			setChanged();
			notifyObservers(new DrawableAddedEvent(element, true));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Removes the drawable from the model.
	 * 
	 * @param element
	 *            the model to remove
	 * @return the elements removed. Note that a link is also removed when it's
	 *         container is removed, etc.
	 */

	public synchronized Collection<Drawable> removeElement(Drawable element) {
		Collection<Drawable> set = new HashSet<Drawable>();
		if (drawables.remove(element)) {
			if (element instanceof Link) {
				Link link = (Link) element;
				if (link.getSourceAnchor() != null)
					link.getSourceAnchor().setLink(null);
				if (link.getDestinationAnchor() != null)
					link.getDestinationAnchor().setLink(null);
			}
			if (element instanceof AnchorPointContainer) {
				for (Link link : ((AnchorPointContainer) element).getLinks()) {
					set.add(link);
					removeElement(link);
				}
			}
			if (element instanceof TransientObservable)
				((TransientObservable) element).deleteObservers();
			setChanged();
			notifyObservers(new DrawableAddedEvent(element, false));
			set.add(element);
		}
		return set;
	}

	public synchronized boolean moveForward(Drawable element) {
		// XXX: Bocke, this is added by Srecko, we should investigate it
		if (element instanceof Link)
			return false;

		int index = drawables.indexOf(element);
		if (index == drawables.size() - 1)
			return false;
		drawables.remove(index);
		drawables.add(element);
		setChanged();
		notifyObservers(new DrawableZOrderEvent(element));
		return true;
	}

	public synchronized Drawable getDrawableAt(Point2D point) {
		for (int i = drawables.size() - 1; i >= 0; i--) {
			Drawable drawable = drawables.get(i).getDrawableUnderLocation(point);
			if (drawable != null)
				return drawable;
		}
		return null;
	}

	/**
	 * Gets the anchor point at <i>location</i>, that will accept <i>link</i>
	 * as source/destination.
	 * 
	 * @param location
	 * @param link
	 * @param asSource
	 * @return AnchorPoint if found, else null
	 */
	public synchronized AnchorPoint getAcceptingAnchorAt(Point2D location, Link link,
			boolean asSource) {

		if (asSource)
			for (Drawable d : getDrawables()) {
				if (d instanceof AnchorPointContainer) {
					AnchorPoint point = ((AnchorPointContainer) d).getSourcePointAt(location, link);
					if ((point != null) && link.willAcceptAnchorAsSource(point)
							&& point.willAcceptLinkAsSource(link)) {
						return point;
					}
				}
			}
		else
			for (Drawable d : getDrawables()) {
				if (d instanceof AnchorPointContainer) {
					AnchorPoint point = ((AnchorPointContainer) d).getDestinationPointAt(location,
						link);
					if ((point != null) && link.willAcceptAnchorAsDestination(point)
							&& point.willAcceptLinkAsDestination(link)) {
						return point;
					}
				}
			}

		return null;
	}

	/**
	 * 
	 * @return The unmodifiable version of drawable elements!
	 */
	public synchronized Collection<Drawable> getDrawables() {
		return readOnlyDrawables;
	}

	@Override
	public void update(TransientObservable o, Object arg) {
		setChanged();
		notifyObservers(new CompositeUpdateEvent(o, arg));
		// Logger.getAnonymousLogger().info("Broj observera za " + this + " je "
		// + super.countObservers());
	}

}
