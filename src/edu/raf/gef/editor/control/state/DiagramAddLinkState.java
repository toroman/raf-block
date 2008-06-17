package edu.raf.gef.editor.control.state;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;

import edu.raf.gef.app.exceptions.GefCreationalException;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.control.state.util.DiagramDefaultState;
import edu.raf.gef.editor.control.state.util.IDiagramAbstractState;
import edu.raf.gef.editor.model.object.impl.AnchorPoint;
import edu.raf.gef.editor.model.object.impl.Link;

public class DiagramAddLinkState extends DiagramDefaultState {
	private final IDiagramAbstractState fallbackState;
	private final Class<? extends Link> objectType;

	public <T extends Link> DiagramAddLinkState(GefDiagram diagram, Class<T> objectType,
			IDiagramAbstractState fallbackState) {
		super(diagram);
		this.fallbackState = fallbackState;
		this.objectType = objectType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean mousePressed(MouseEvent e, Point2D userSpaceLocation) {
		if (super.mousePressed(e, userSpaceLocation))
			return true;
		try {
			Constructor<Link> constructor = (Constructor<Link>) objectType
					.getConstructor();
			Link newLink = constructor.newInstance();

			AnchorPoint sourceAnchor = diagram.getModel().getAcceptingAnchorAt(
				userSpaceLocation, newLink, true);

			if (sourceAnchor != null) {
				newLink.setSourcePoint(sourceAnchor);
				newLink.getResizePoins().getLast().setLocation(userSpaceLocation);
				sourceAnchor.setLink(newLink);
				diagram.getModel().addElement(newLink);	
				diagram.getController().setState(new DiagramReLinkState (diagram, newLink, false, new DiagramSelectionState (diagram)));
				return true;
			} else {
				newLink = null;
				return false;
			}
		} catch (Exception ex) {
			diagram.getController().setState(fallbackState);
			throw new GefCreationalException(ex);
		}
	}
	
	@Override
	public boolean keyPressed(KeyEvent e) {
		if (super.keyPressed(e))
			return true;
		/*
		 * Operation canceled
		 */
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			diagram.getController().setState(fallbackState);
			return true;
		}
		return false;
	}
}
