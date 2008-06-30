package edu.raf.flowchart;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.raf.flowchart.diagram.FlowChartDiagram;
import edu.raf.gef.app.exceptions.GefRuntimeException;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.model.DiagramModel;
import edu.raf.gef.editor.structure.DiagramModelConverter;
import edu.raf.gef.editor.structure.GefDiagramConverter;
import edu.raf.gef.services.mime.ProjectsFileHandler;
import edu.raf.gef.workspace.project.DiagramProject;

/**
 * Here we define what files FlowChart editor can handle and *how*.
 * 
 * @author toroman
 * 
 */
public class FlowChartFileHandler implements ProjectsFileHandler {

	@Override
	public boolean canHandleFile(File file) {
		return file.getName().endsWith(".fc");
	}

	@Override
	public void openFile(File file, DiagramProject owner) {
		XStream xs = new XStream(new DomDriver());
		Converter c = new GefDiagramConverter(owner, FlowChartDiagram.class,
				new DiagramModelConverter(DiagramModel.class));
		xs.registerConverter(c);

		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException ex) {
			throw new GefRuntimeException("Couldn't open file!", ex);
		}
		// it is automatically added to the project
		GefDiagram dg = (GefDiagram) xs.fromXML(is);

		try {
			is.close();
		} catch (IOException ex) {
		}
	}
}
