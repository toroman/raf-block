package edu.raf.gef.editor.control.state;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;

import edu.raf.gef.app.exceptions.GefCreationalException;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.control.state.util.DiagramDefaultState;
import edu.raf.gef.editor.control.state.util.IDiagramAbstractState;
import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.Draggable;

/**
 * State for adding objects.
 * 
 * @param <T>
 *            Type of the object to be added.
 */
public class DiagramAddDraggableState extends DiagramDefaultState {
	private final IDiagramAbstractState fallbackState;
	private final IDiagramAbstractState nextState;
	private final Class<? extends Draggable> objectType;

	/**
	 * 
	 * @param diagram
	 * @param fallbackState
	 *            Which state to take if operation fails or is canceled
	 * @param nextState
	 *            Which state to activate if operation succeeds. If null, the
	 *            next state will be the default DiagramSelectionState, with the
	 *            new Draggable in focus.
	 */
	public <T extends Draggable> DiagramAddDraggableState(GefDiagram diagram, Class<T> objectType,
			IDiagramAbstractState fallbackState, IDiagramAbstractState nextState) {
		super(diagram);
		this.fallbackState = fallbackState;
		this.nextState = nextState;
		this.objectType = objectType;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void mousePressed(MouseEvent arg0) {
		try {
			Constructor<Draggable> constructor = (Constructor<Draggable>) objectType
					.getConstructor(DiagramModel.class);
			Draggable draggable = constructor.newInstance(diagram.getModel());
			draggable.setLocation(arg0.getPoint());
			diagram.getModel().addElement(draggable);
			if (nextState == null) {
				diagram.getController().setState(new DiagramSelectionState (diagram, draggable));
			} else
				diagram.getController().setState(nextState);

			// .getUndoManager().undoableEditHappened(
			// new UndoableEditEvent(dg.getModel(), new
			// AddDrawableEdit(dg.getModel(), de)));
		} catch (Exception ex) {
			diagram.getController().setState(fallbackState);
			throw new GefCreationalException(ex);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		/*
		 * Operation canceled
		 */
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			diagram.getController().setState(fallbackState);
		}
	}
}
