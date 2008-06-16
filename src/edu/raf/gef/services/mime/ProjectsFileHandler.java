package edu.raf.gef.services.mime;

import java.io.File;

import edu.raf.gef.workspace.project.DiagramProject;

/**
 * All plugins that can open some file should register their ExtensionHandler.
 * 
 * @author Srecko Toroman
 * 
 */
public interface ProjectsFileHandler {
	/**
	 * 
	 * @param file
	 * @return Return true if you can open the file
	 */
	public boolean canHandleFile(File file);

	/**
	 * This is executed by workspace tree usually, telling you to open the given
	 * file.
	 * 
	 * @param file
	 * @param owner
	 */
	public void openFile(File file, DiagramProject owner);
}
