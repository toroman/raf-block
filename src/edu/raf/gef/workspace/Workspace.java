package edu.raf.gef.workspace;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import edu.raf.gef.app.Resources;
import edu.raf.gef.app.exceptions.GefRuntimeException;

public class Workspace extends DefaultTreeModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -343920936878421835L;

	private final File location;

	public Workspace(File location) {
		super(new DefaultMutableTreeNode("Workspace"));

		if (location == null)
			throw new NullPointerException();
		if (!location.exists())
			location.mkdir();
		else if (!location.isDirectory()) {
			throw new GefRuntimeException("Invalid location: " + location.getAbsolutePath());
		}
		this.location = location;
	}

	public File getLocation() {
		return location;
	}

	@Override
	public DefaultMutableTreeNode getRoot() {
		return (DefaultMutableTreeNode) super.getRoot();
	}

	public static File getWorkspaceFileFromResources(Resources resources) {
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
}
