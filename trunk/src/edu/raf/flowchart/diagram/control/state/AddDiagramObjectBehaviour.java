package edu.raf.flowchart.diagram.control.state;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import edu.raf.flowchart.diagram.Diagram;
import edu.raf.flowchart.diagram.control.DiagramController;
import edu.raf.flowchart.diagram.model.object.DiagramObject;

public class AddDiagramObjectBehaviour extends DefaultMouseBehaviour {

	Class <DiagramObject> diagramObject;
	
	public AddDiagramObjectBehaviour(Diagram diagram) {
		super(diagram);
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		super.mousePressed(arg0);
		try {
			Constructor<DiagramObject> constructor = diagramObject.getConstructor(Point2D.class);
			DiagramObject diagramObject = constructor.newInstance(arg0.getPoint());
			diagram.getModel().addElement(diagramObject);
			diagram.getView().redraw();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} finally {
			diagram.getController().setState(DiagramController.SELECTION, null);
		}
	}

	public void setType(Class <DiagramObject> class1) {
		this.diagramObject = class1;
	}
}
