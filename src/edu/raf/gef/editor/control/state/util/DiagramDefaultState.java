package edu.raf.gef.editor.control.state.util;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.view.util.AffineTransformManager;
import edu.raf.gef.editor.view.util.RepaintAndInertionThread;
import edu.raf.gef.util.GeomHelper;

public class DiagramDefaultState implements IDiagramAbstractState {

	protected final GefDiagram diagram;

	public DiagramDefaultState(GefDiagram diagram) {
		this.diagram = diagram;
	}

	@Override
	public void onStateLeft() {

	}

	@Override
	public boolean mouseClicked(MouseEvent arg0, Point2D userSpaceLocation) {
		return false;
	}

	@Override
	public boolean mouseEntered(MouseEvent arg0, Point2D userSpaceLocation) {
		return false;
	}

	@Override
	public boolean mouseExited(MouseEvent arg0, Point2D userSpaceLocation) {
		return false;
	}

	@Override
	public boolean mousePressed(MouseEvent e, Point2D userSpaceLocation) {
		RepaintAndInertionThread repainter = diagram.getView().getRepaintAndInertionThread();
		if (repainter != null)
			repainter.setRadi(false);
		if (e.getButton() == MouseEvent.BUTTON3) {
			AffineTransformManager atm = diagram.getView().getAffineTransformManager();
			atm.setAutoMatchTransform(false);
			atm.setDeviceSpaceLocation(GeomHelper.castTo2D(e.getPoint()));
			atm.setUserSpaceLocation(userSpaceLocation);
			atm.matchTransform();
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseReleased(MouseEvent e, Point2D userSpaceLocation) {
		RepaintAndInertionThread repainter = diagram.getView().getRepaintAndInertionThread();
		if (repainter != null)
			repainter.setRadi(true);
		if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0)
			return true;
		return false;
	}

	@Override
	public boolean mouseDragged(MouseEvent e, Point2D userSpaceLocation) {
		if ((e.getModifiersEx() & MouseEvent.BUTTON3_DOWN_MASK) != 0) {
			AffineTransformManager atm = diagram.getView().getAffineTransformManager();
			atm.setDeviceSpaceLocation(GeomHelper.castTo2D(e.getPoint()));
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseMoved(MouseEvent e, Point2D userSpaceLocation) {
		return false;
	}

	@Override
	public boolean mouseWheelMoved(MouseWheelEvent e, Point2D userSpaceLocation) {
		AffineTransformManager atm = diagram.getView().getAffineTransformManager();
		atm.setAutoMatchTransform(false);
		atm.setDeviceSpaceLocation(GeomHelper.castTo2D(e.getPoint()));
		atm.setUserSpaceLocation(userSpaceLocation);
		diagram.getView().getAffineTransformManager().modifyScale (e.getWheelRotation() * -1);
		atm.matchTransform();
		return true;
	}

	@Override
	public boolean keyPressed(KeyEvent e) {
		return false;
	}

	@Override
	public boolean keyReleased(KeyEvent e) {
		return false;
	}

	@Override
	public boolean keyTyped(KeyEvent e) {
		return false;
	}

}
