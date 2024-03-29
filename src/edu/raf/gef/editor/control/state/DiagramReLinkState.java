package edu.raf.gef.editor.control.state;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.control.edit.EditReLink;
import edu.raf.gef.editor.control.state.util.DiagramDefaultState;
import edu.raf.gef.editor.control.state.util.IDiagramAbstractState;
import edu.raf.gef.editor.model.object.impl.AnchorPoint;
import edu.raf.gef.editor.model.object.impl.Link;

public class DiagramReLinkState extends DiagramDefaultState {

	private Link link;
	private AnchorPoint oldAnchorPoint;
	private boolean asSource;
	private boolean firstTime = true;

	private IDiagramAbstractState fallbackState;

	public DiagramReLinkState(GefDiagram diagram, Link link, boolean asSource,
			IDiagramAbstractState fallbackState) {
		super(diagram);
		this.fallbackState = fallbackState;
		this.link = link;
		this.asSource = asSource;
		if (asSource) {
			this.oldAnchorPoint = link.getSourceAnchor();
			this.link.setSourcePoint(null);
			this.link.getResizePoins().getFirst().setDraggingOffset(0, 0);
		} else {
			this.oldAnchorPoint = link.getDestinationAnchor();
			this.link.setDestinationPoint(null);
			this.link.getResizePoins().getLast().setDraggingOffset(0, 0);
		}
		diagram.getController().clearFocusedObjects();
		diagram.getController().addToFocusedObjects(link);
	}
	
	@Override
	public boolean mousePressed(MouseEvent e, Point2D userSpaceLocation) {
		if (super.mousePressed(e, userSpaceLocation))
			return true;
		firstTime = true;
		return true;
	}

	@Override
	public boolean mouseDragged(MouseEvent e, Point2D userSpaceLocation) {
		if (super.mouseDragged(e, userSpaceLocation))
			return true;
		if (firstTime)
			if (asSource)
				link.getResizePoins().getFirst().dragStartedAt(userSpaceLocation);
			else
				link.getResizePoins().getLast().dragStartedAt(userSpaceLocation);
		else
			if (asSource)
				link.getResizePoins().getFirst().dragTo(userSpaceLocation);
			else
				link.getResizePoins().getLast().dragTo(userSpaceLocation);
		firstTime = false;
		return true;
	}

	@Override
	public boolean mouseReleased(MouseEvent e, Point2D userSpaceLocation) {
		if (super.mouseReleased(e, userSpaceLocation))
			return true;
		firstTime = true;
		AnchorPoint newAnchor = diagram.getModel().getAcceptingAnchorAt(userSpaceLocation, link,
			asSource);
		if (newAnchor != null) {
			
			if (oldAnchorPoint != null)
				oldAnchorPoint.setLink(null);
			
			if (asSource)
				link.setSourcePoint(newAnchor);
			else
				link.setDestinationPoint(newAnchor);
			newAnchor.setLink(link);

			if (diagram.getUndoManager() != null) {
				diagram.getUndoManager().addEdit(new EditReLink(diagram, link, asSource, oldAnchorPoint, newAnchor));
			}
			
			if (link != null)
				diagram.getController().setState(new DiagramSelectionState(diagram, link));
			else
				diagram.getController().setState(new DiagramSelectionState(diagram));
			return true;
		} else {
			onReLinkRefused();
			return true;
		}
	}

	public void onReLinkRefused() {
		if (oldAnchorPoint != null) {
			if (asSource) {
				oldAnchorPoint.setLink(link);
				link.setSourcePoint(oldAnchorPoint);
			} else {
				oldAnchorPoint.setLink(link);
				link.setDestinationPoint(oldAnchorPoint);
			}
		} else {
			diagram.getModel().removeElement(link);
			link.getSourceAnchor().setLink(null);
			link.setSourcePoint(null);
			link = null;
		}
		diagram.getController().setState(fallbackState);
	}

	@Override
	public boolean keyPressed(KeyEvent e) {
		if (super.keyPressed(e))
			return true;
		/*
		 * Operation canceled
		 */
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			onReLinkRefused();
			return true;
		}
		return false;
	}
}
