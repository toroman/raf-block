package edu.raf.gef.editor.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import edu.raf.gef.editor.model.events.DrawableAddedEvent;
import edu.raf.gef.editor.model.events.DrawableZOrderEvent;
import edu.raf.gef.editor.model.object.AnchorPointContainer;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.editor.model.object.impl.AnchorPoint;
import edu.raf.gef.editor.model.object.impl.Link;
import edu.raf.gef.editor.structure.CompositeUpdateEvent;

public class DiagramModel extends Observable implements Observer {
	transient static private int STUPID_COUNTER = 0;

	private ArrayList<Drawable> drawables;
	/**
	 * Return unmodifiable version, because the list writing is encapsulated.
	 */
	private final Collection<Drawable> readOnlyDrawables;

	private String title = "untitled" + ++STUPID_COUNTER;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public DiagramModel() {
		drawables = new ArrayList<Drawable>();
		readOnlyDrawables = Collections.unmodifiableCollection(drawables);
	}

	public Converter getConverter() {
		return new Converter() {
			public void marshal(Object objModel,
					HierarchicalStreamWriter writer, MarshallingContext context) {
				DiagramModel model = (DiagramModel) objModel;
				writer.addAttribute("title", model.getTitle());
				writer.startNode("drawables");
				context.convertAnother(model.drawables);
				writer.endNode();
			}

			@SuppressWarnings("unchecked")
			public Object unmarshal(HierarchicalStreamReader reader,
					UnmarshallingContext context) {
				DiagramModel model = new DiagramModel();
				model.setTitle(reader.getAttribute("title"));
				while (reader.hasMoreChildren()) {
					reader.moveDown();
					if ("drawables".equals(reader.getNodeName())) {
						model.drawables = (ArrayList<Drawable>) context
								.convertAnother(model, ArrayList.class);
					}
					reader.moveUp();
				}
				return model;
			}

			public boolean canConvert(Class cl) {
				return DiagramModel.class.equals(cl);
			}

		};
	}

	public synchronized boolean addElement(Drawable element) {
		if (drawables.add(element)) {
			setChanged();
			notifyObservers(new DrawableAddedEvent(element, true));
			return true;
		} else {
			return false;
		}
	}

	public synchronized boolean removeElement(Drawable element) {
		if (drawables.remove(element)) {
			setChanged();
			notifyObservers(new DrawableAddedEvent(element, false));
			return true;
		} else {
			return false;
		}
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
			Drawable drawable = drawables.get(i)
					.getDrawableUnderLocation(point);
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
	public synchronized AnchorPoint getAcceptingAnchorAt(Point2D location,
			Link link, boolean asSource) {

		AnchorPoint acceptingAnchor = null;

		if (asSource)
			for (Drawable d : getDrawables()) {
				if (d instanceof AnchorPointContainer) {
					AnchorPoint point = ((AnchorPointContainer) d)
							.getSourcePointAt(location, link);
					if ((point != null) && link.willAcceptAnchorAsSource(point)
							&& point.willAcceptLinkAsSource(link)) {
						acceptingAnchor = point;
						break;
					}
				}
			}
		else
			for (Drawable d : getDrawables()) {
				if (d instanceof AnchorPointContainer) {
					AnchorPoint point = ((AnchorPointContainer) d)
							.getDestinationPointAt(location, link);
					if ((point != null)
							&& link.willAcceptAnchorAsDestination(point)
							&& point.willAcceptLinkAsDestination(link)) {
						acceptingAnchor = point;
						break;
					}
				}
			}

		return acceptingAnchor;
	}

	/**
	 * 
	 * @return The unmodifiable version of drawable elements!
	 */
	public synchronized Collection<Drawable> getDrawables() {
		return readOnlyDrawables;
	}

	@Override
	public void update(Observable observable, Object param) {
		setChanged();
		notifyObservers(new CompositeUpdateEvent(observable, param));
	}
}
