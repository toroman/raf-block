package edu.raf.gef.services.mime;

import java.io.File;

import edu.raf.gef.workspace.project.DiagramProject;

/**
 * All plugins that can open some file should register their ExtensionHandler.
 * 
 * @author Srecko Toroman
 * 
 */
public interface FileHandler {
	public boolean canHandleFile(File file);

	public void handleFile(File file, DiagramProject owner);
}
