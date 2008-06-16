package edu.raf.gef.editor.structure;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import edu.raf.gef.app.exceptions.GefRuntimeException;
import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.model.object.Drawable;

/**
 * Default serializer for Diagram models
 * 
 * @author Srecko Toroman
 * 
 */
public class ModelConverter implements Converter {
	private Class<? extends DiagramModel> modelType;

	public ModelConverter(Class<? extends DiagramModel> modelType) {
		this.modelType = modelType;
	}

	public void marshal(Object objModel, HierarchicalStreamWriter writer, MarshallingContext context) {
		DiagramModel model = (DiagramModel) objModel;
		writer.addAttribute("title", model.getTitle());
		writer.startNode("drawables");
		context.convertAnother(model.getDrawables());
		writer.endNode();
	}

	@SuppressWarnings("unchecked")
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		DiagramModel model;
		try {
			model = modelType.newInstance();
		} catch (Exception e) {
			throw new GefRuntimeException("Couldn't initialize model!", e);
		}
		model.setTitle(reader.getAttribute("title"));
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if ("drawables".equals(reader.getNodeName())) {
				Object other = context.convertAnother(model, ArrayList.class);
				// funny behaviour here :0
				List<Drawable> drawables = (List<Drawable>) ((List) other).get(0);
				for (Drawable d : drawables) {
					model.addElement(d);
				}
			}
			reader.moveUp();
		}
		return model;
	}

	public boolean canConvert(Class cl) {
		throw new IllegalStateException(
				"This converter shouldn't be registered in Xstream, but invoked manually");
	}

}
