package edu.raf.gef.workspace.project;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.raf.gef.Main;
import edu.raf.gef.app.exceptions.GefException;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.IDiagramTreeNode;
import edu.raf.gef.services.mime.FileHandler;
import edu.raf.gef.workspace.Workspace;

public class DiagramProject extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1406273266034959927L;

	protected transient Workspace workspace;

	private static Converter converter = new Converter() {
		public void marshal(Object diagramObj, HierarchicalStreamWriter writer,
				MarshallingContext context) {
			writer.addAttribute("name", ((DiagramProject) diagramObj).getProjectName());
		}

		public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
			String name = reader.getAttribute("name");
			return new DiagramProject(name);
		}

		public boolean canConvert(Class arg0) {
			return DiagramProject.class.equals(arg0);
		}
	};

	public DiagramProject(String projectName) {
		super(projectName);
	}

	public String getProjectName() {
		return (String) getUserObject();
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	public void addDiagram(GefDiagram diagram) {
		workspace.insertNodeInto(diagram.getTreeModel(workspace), this, 0);
	}

	public void save() throws GefException {
		File projectFolder = ensureFileSystem();
		File metaFolder = getMetaFolder();
		File metaFile = getMetaFile();

		OutputStream os;
		try {
			os = new BufferedOutputStream(new FileOutputStream(metaFile));
		} catch (FileNotFoundException e) {
			throw new GefException("Error saving project meta data to "
					+ metaFile.getAbsolutePath(), e);
		}

		XStream xs = new XStream(new DomDriver());
		xs.registerConverter(converter);
		xs.toXML(this, os);

		try {
			os.close();
		} catch (IOException e) {
		}

		int count = getChildCount();
		for (int i = 0; i < count; ++i) {
			IDiagramTreeNode diagramNode = (IDiagramTreeNode) getChildAt(i);
			diagramNode.getDiagram().save();
		}
	}

	private File getMetaFile() {
		return new File(getMetaFolder(), "project.xml");
	}

	private File getMetaFolder() {
		return new File(getProjectFolder(), ".project");
	}

	public File getProjectFolder() {
		return new File(this.workspace.getLocation(), getProjectName());
	}

	private File ensureFileSystem() throws GefException {
		File projectFolder = getProjectFolder();
		if (!projectFolder.exists()) {
			projectFolder.mkdirs();
		}
		File metaFolder = new File(projectFolder, ".project");
		if (!metaFolder.exists()) {
			metaFolder.mkdir();
		}
		return projectFolder;
	}

	/**
	 * Iterate through all files and invoke registered file handlers.
	 * 
	 * @param projectFolder
	 * @return
	 * @throws GefException
	 */
	public static DiagramProject createFrom(File projectFolder) throws GefException {
		// read meta
		File metaFile = new File(new File(projectFolder, ".project"), "project.xml");
		InputStream is;
		try {
			is = new BufferedInputStream(new FileInputStream(metaFile));
		} catch (FileNotFoundException e) {
			throw new GefException("Couldn't read project configuration!", e);
		}
		XStream xs = new XStream(new DomDriver());
		xs.registerConverter(converter);
		DiagramProject dproject = (DiagramProject) xs.fromXML(is);
		try {
			is.close();
		} catch (IOException ioe) {
		}

		// read files

		File[] files = projectFolder.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isFile();
			}
		});
		List<FileHandler> handlers = Main.getServices()
				.getServiceImplementations(FileHandler.class);

		for (File file : files) {
			for (FileHandler handler : handlers) {
				if (handler.canHandleFile(file)) {
					handler.handleFile(file, dproject);
					break;
				}
			}
		}
		return dproject;
	}
}
