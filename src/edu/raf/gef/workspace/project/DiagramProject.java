package edu.raf.gef.workspace.project;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.tree.DefaultMutableTreeNode;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.raf.gef.app.exceptions.GefException;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.IDiagramTreeNode;
import edu.raf.gef.workspace.Workspace;

public class DiagramProject extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1406273266034959927L;

	protected Workspace workspace;

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

	public void saveDiagram(GefDiagram diagram) throws GefException {
		saveDiagramHelper(diagram, ensureFileSystem());
	}

	private GefDiagram loadDiagramHelper(File file) {

	}

	private void saveDiagramHelper(GefDiagram diagram, File projectFolder)
			throws GefException {
		OutputStream os;
		try {
			os = new BufferedOutputStream(new FileOutputStream(new File(
					projectFolder, diagram.getModel().getTitle() + ".dxml")));
		} catch (FileNotFoundException e) {
			throw new GefException("Error saving diagram " + diagram, e);
		}
		XStream xs = new XStream(new DomDriver());
		diagram.configureXstream(xs);
		xs.toXML(diagram, os);

		try {
			os.close();
		} catch (IOException e) {
		}
	}

	public void save() throws GefException {
		File projectFolder = ensureFileSystem();
		File metaFolder = new File(projectFolder, ".project");
		File metaFile = new File(metaFolder, "project.xml");

		OutputStream os;
		try {
			os = new BufferedOutputStream(new FileOutputStream(metaFile));
		} catch (FileNotFoundException e) {
			throw new GefException("Error saving project meta data to "
					+ metaFile.getAbsolutePath(), e);
		}

		XStream xs = configuredXstream();
		xs.toXML(this, os);

		try {
			os.close();
		} catch (IOException e) {
		}

		int count = getChildCount();
		for (int i = 0; i < count; ++i) {
			IDiagramTreeNode diagramNode = (IDiagramTreeNode) getChildAt(i);
			saveDiagramHelper(diagramNode.getDiagram(), projectFolder);
		}
	}

	private XStream configuredXstream() {
		XStream xs = new XStream(new DomDriver())
		return xs;
	}

	private File ensureFileSystem() throws GefException {
		File projectFolder = new File(this.workspace.getLocation(),
				getProjectName());
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
	 * TODO: So we are working like this now because only one type of project is
	 * available, but this should be top priority for changing in future.
	 * <p> -- srecko
	 * 
	 * @param project
	 * @return
	 */
	public static DiagramProject createFrom(File project) {
		File[] diagrams = project.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.isFile() && pathname.getName().endsWith(".dxml");
			}
		});
		return new DiagramProject(
	}
}
