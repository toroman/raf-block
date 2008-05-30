package edu.raf.gef.editor.control.state;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;

import edu.raf.gef.app.exceptions.GefCreationalException;
import edu.raf.gef.editor.control.DiagramController;
import edu.raf.gef.editor.control.state.util.DiagramDefaultState;
import edu.raf.gef.editor.control.state.util.IDiagramAbstractState;
import edu.raf.gef.editor.model.object.DrawableElement;

/**
 * State for adding objects.
 * 
 * @param <T>
 *            Type of the object to be added.
 */
public class DiagramAddObjectState<T extends DrawableElement> extends DiagramDefaultState {
	private final IDiagramAbstractState fallbackState;
	private final IDiagramAbstractState nextState;
	private final Class<T> objectType;

	/**
	 * 
	 * @param diagram
	 * @param fallbackState
	 *            Which state to take if operation fails or is canceled
	 * @param nextState
	 *            Which state to activate if operation succeeds
	 */
	public DiagramAddObjectState(DiagramController controller, Class<T> objectType,
			IDiagramAbstractState fallbackState, IDiagramAbstractState nextState) {
		super(controller);
		this.fallbackState = fallbackState;
		this.nextState = nextState;
		this.objectType = objectType;
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		try {
			Constructor<T> constructor = objectType.getConstructor(Point2D.class);
			DrawableElement de = constructor.newInstance(arg0.getPoint());
			controller.getModel().addElement(de);
			controller.setState(nextState);
		} catch (Exception ex) {
			controller.setState(fallbackState);
			throw new GefCreationalException(ex);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		/*
		 * Operation canceled
		 */
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			controller.setState(fallbackState);
		}
	}
}
