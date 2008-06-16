package edu.raf.gef.workspace;

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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;

import edu.raf.gef.app.IResources;
import edu.raf.gef.app.Resources;
import edu.raf.gef.app.exceptions.GefException;
import edu.raf.gef.app.exceptions.GefRuntimeException;
import edu.raf.gef.workspace.project.DiagramProject;

public class Workspace extends DefaultTreeModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -343920936878421835L;

	private final transient File location;

	public static Workspace createWorkspace(final File location) throws GefException {
		log.info("Workspace opened at " + location.getAbsolutePath());
		if (location == null) {
			throw new NullPointerException();
		}
		if (!location.exists()) {
			location.mkdir();
		} else if (!location.isDirectory()) {
			throw new GefRuntimeException("Invalid location: " + location.getAbsolutePath());
		}

		File metadir = new File(location, ".meta");
		if (!metadir.exists()) {
			metadir.mkdir();
		}

		File workspaceXml = new File(metadir, "workspace.xml");
		if (!workspaceXml.exists()) {
			try {
				workspaceXml.createNewFile();
			} catch (IOException e) {
				throw new GefException("Couldn't initialize workspace, check for correct rights!",
						e);
			}
			// return empty workspace
			return new Workspace(location);
		}

		XStream xs = configuredXStream(location);
		InputStream is;
		try {
			is = new BufferedInputStream(new FileInputStream(workspaceXml));
		} catch (FileNotFoundException e) {
			throw new GefException(
					"Couldn't open workspace configuration for reading, workspace may be corrupted!",
					e);
		}
		Object restoredWs = xs.fromXML(is);
		try {
			is.close();
		} catch (IOException e) {
		}
		if (restoredWs instanceof Workspace) {
			return (Workspace) restoredWs;
		}
		throw new GefException("Expected Workspace, got " + restoredWs);
	}

	private static XStream configuredXStream(final File location) {
		XStream xs = new XStream(new DomDriver());
		xs.registerConverter(new Converter() {
			public void marshal(Object wsObj, HierarchicalStreamWriter writer,
					MarshallingContext context) {
				writer.addAttribute("serialVersionUID", String.valueOf(Workspace.serialVersionUID));
			}

			@Override
			public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
				String v = reader.getAttribute("serialVersionUID");
				long uid = Long.valueOf(v);
				if (uid != serialVersionUID) {
					throw new GefRuntimeException(
							"Saved workspace is not compatible with this version of the software.");
				}
				try {
					return new Workspace(location);
				} catch (Exception ex) {
					throw new GefRuntimeException(ex);
				}
			}

			@Override
			public boolean canConvert(Class cl) {
				return cl == Workspace.class;
			}

		});
		return xs;
	}

	/**
	 * Create new workspace.
	 * 
	 * @param location
	 *            Where is it located on the filesystem
	 * @throws GefException
	 *             If any project couldn't be loaded
	 */
	private Workspace(File location) throws GefException {
		// title is not important
		super(new DefaultMutableTreeNode("Workspace"));
		this.location = location;
		File[] projects = this.location.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				if (!pathname.isDirectory() || pathname.getName().startsWith(".")
						|| pathname.isHidden())
					return false;
				File meta = new File(pathname, ".project");
				return meta.exists() && meta.isDirectory();
			}
		});
		ArrayList<Exception> exceptions = new ArrayList<Exception>();
		for (File project : projects) {
			try {
				addProject(DiagramProject.createFrom(this, project));
			} catch (GefException e) {
				exceptions.add(e);
			}
		}
		if (exceptions.size() > 0) {
			throw new GefException("Some projects couldn't have been loaded. "
					+ exceptions.toString());
		}
	}

	@SuppressWarnings("unchecked")
	public void save() throws GefException {
		XStream xs = configuredXStream(location);
		OutputStream os;
		try {
			os = new BufferedOutputStream(new FileOutputStream(new File(
					new File(location, ".meta"), "workspace.xml")));
		} catch (FileNotFoundException e) {
			throw new GefException("Couldn't open workspace for writing!");
		}
		xs.toXML(this, os);
		try {
			os.close();
		} catch (IOException e) {
		}

		Enumeration<? extends TreeNode> children = ((TreeNode) getRoot()).children();
		while (children.hasMoreElements()) {
			DiagramProject project = (DiagramProject) children.nextElement();
			project.save();
		}
	}

	public File getLocation() {
		return location;
	}

	public static File getWorkspaceFileFromResources(IResources resources) {
		String value = resources.getProperty("workspace");
		File dir;
		if (value != null)
			dir = new File(value);
		else
			dir = new File(".", "default_workspace");
		if (!dir.exists()) {
			dir.mkdirs();
		} else if (!dir.isDirectory()) {
			throw new GefRuntimeException("Invalid workspace location: " + dir.getAbsolutePath());
		}
		return dir;
	}

	public void setWorkspaceLocationToProperties(Resources resources) {
		String value = location.getAbsolutePath();
		resources.setProperty("workspace", value);
	}

	public void addProject(DiagramProject project) {
		project.setWorkspace(this);
		insertNodeInto(project, (MutableTreeNode) getRoot(), 0);
	}

	protected static transient final Logger log = Logger.getLogger(Workspace.class.getName());
}
