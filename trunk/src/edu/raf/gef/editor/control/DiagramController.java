package edu.raf.gef.editor.control;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import edu.raf.gef.app.errors.GraphicalErrorHandler;
import edu.raf.gef.editor.control.state.util.DiagramDefaultState;
import edu.raf.gef.editor.control.state.util.IDiagramAbstractState;
import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.view.DiagramView;

public class DiagramController implements MouseListener, MouseWheelListener, MouseMotionListener,
		KeyListener {
	private IDiagramAbstractState state;
	private GraphicalErrorHandler geh;
	private final DiagramModel model;
	private final DiagramView view;

	public DiagramController(DiagramModel model, DiagramView view) {
		state = new DiagramDefaultState(this);
		this.model = model;
		this.view = view;
		Component c = view.getCanvas();
		c.addMouseListener(this);
		c.addMouseMotionListener(this);
		c.addMouseWheelListener(this);
		c.addKeyListener(this);
	}

	public void setState(IDiagramAbstractState state) {
		this.state = state;
	}

	public DiagramModel getModel() {
		return model;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		state.mouseClicked(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}
}
