package edu.raf.gef.editor.structure;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import edu.raf.gef.app.exceptions.GefRuntimeException;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.workspace.project.DiagramProject;

/**
 * An example converter. Converts only model.
 * 
 * @author Srecko Toroman
 * 
 */
public class GefDiagramConverter implements Converter {
	private final DiagramProject project;
	private final Class<? extends GefDiagram> diagramClazz;
	private final Converter modelConverter;

	public GefDiagramConverter(DiagramProject owner, Class<? extends GefDiagram> clazz,
			Converter modelConverter) {
		this.project = owner;
		this.diagramClazz = clazz;
		this.modelConverter = modelConverter;
	}

	public void marshal(Object diagramObj, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		GefDiagram diagram = (GefDiagram) diagramObj;
		writer.startNode("model");
		modelConverter.marshal(diagram.getModel(), writer, context);
		writer.endNode();
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		GefDiagram diagram = null;
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if ("model".equals(reader.getNodeName())) {
				DiagramModel model = (DiagramModel) modelConverter.unmarshal(reader, context);
				try {
					diagram = diagramClazz.getConstructor(DiagramProject.class, DiagramModel.class)
							.newInstance(project, model);
				} catch (Exception e) {
					throw new GefRuntimeException("Couldn't create diagram!", e);
				}
			}
			reader.moveUp();
		}
		return diagram;
	}

	@SuppressWarnings("unchecked")
	public boolean canConvert(Class cl) {
		return diagramClazz.equals(cl);
	}
}
