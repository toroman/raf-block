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

import edu.raf.gef.app.exceptions.GefException;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.IDiagramTreeNode;
import edu.raf.gef.services.ServiceManager;
import edu.raf.gef.services.mime.ProjectsFileHandler;
import edu.raf.gef.workspace.Workspace;

public class DiagramProject extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1406273266034959927L;

	protected transient Workspace workspace;

	private String projectName;

	public DiagramProject(Workspace workspace, File projectFolder) {
		super();
		this.projectName = projectFolder.getName();
		this.workspace = workspace;
		ensureFileSystem();
	}

	public String getProjectName() {
		return projectName;
	}

	@Override
	public String toString() {
		return projectName;
	}

	public void addDiagram(GefDiagram diagram) {
		workspace.insertNodeInto(diagram.getTreeModel(workspace), this, 0);
	}

	public void save() throws GefException {
		ensureFileSystem();
		// save meta data
		File metaFile = getMetaFile();

		OutputStream os;
		try {
			os = new BufferedOutputStream(new FileOutputStream(metaFile));
		} catch (FileNotFoundException e) {
			throw new GefException("Error saving project meta data to "
					+ metaFile.getAbsolutePath(), e);
		}

		XStream xs = new XStream(new DomDriver());
		xs.registerConverter(createConvertor(null, null));
		xs.toXML(this, os);

		try {
			os.close();
		} catch (IOException e) {
		}

		// save diagrams
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

	private void ensureFileSystem() {
		File projectFolder = getProjectFolder();
		if (!projectFolder.exists()) {
			projectFolder.mkdirs();
		}
		File metaFolder = new File(projectFolder, ".project");
		if (!metaFolder.exists()) {
			metaFolder.mkdir();
		}
	}

	/**
	 * Iterate through all files and invoke registered file handlers.
	 * 
	 * @param projectFolder
	 * @return
	 * @throws GefException
	 */
	public static DiagramProject createFrom(Workspace workspace, File projectFolder)
			throws GefException {
		// read meta
		File metaFile = new File(new File(projectFolder, ".project"), "project.xml");
		InputStream is;
		try {
			is = new BufferedInputStream(new FileInputStream(metaFile));
		} catch (FileNotFoundException e) {
			throw new GefException("Couldn't read project configuration!", e);
		}
		XStream xs = new XStream(new DomDriver());
		xs.registerConverter(createConvertor(workspace, projectFolder));
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
		List<ProjectsFileHandler> handlers = ServiceManager.getServices()
				.getServiceImplementations(ProjectsFileHandler.class);

		for (File file : files) {
			for (ProjectsFileHandler handler : handlers) {
				if (handler.canHandleFile(file)) {
					handler.openFile(file, dproject);
					break;
				}
			}
		}
		return dproject;
	}

	private static Converter createConvertor(final Workspace workspace2, final File projectFolder) {
		return new Converter() {
			public void marshal(Object diagramObj, HierarchicalStreamWriter writer,
					MarshallingContext context) {
				writer.addAttribute("serialVersionUID", Long
						.toString(DiagramProject.serialVersionUID));
			}

			public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
				DiagramProject project = new DiagramProject(workspace2, projectFolder);
				return project;
			}

			public boolean canConvert(Class arg0) {
				return DiagramProject.class.equals(arg0);
			}
		};
	}

	public void renameTo(String neuNamen) throws GefException {
		if (!neuNamen.matches("[a-zA-Z0-9 -_]+")) {
			throw new GefException(
					"Only letters, numbers, dash, space and underscore characters are allowed.");
		}
		if (neuNamen.equals(getProjectName())) {
			return;
		}
		File old = getProjectFolder();
		File neu = new File(old.getParent(), neuNamen);
		old.renameTo(neu);
		this.projectName = neuNamen;
	}
}
