package edu.raf.flowchart.diagram;

import java.security.SecureClassLoader;

import edu.raf.flowchart.app.Resources;

public class BlockDiagramResources extends Resources {

	private BlockDiagramResources() {
		super("edu/raf/flowchart/diagram/");
	}

	private static class IH {
		BlockDiagramResources instance = new BlockDiagramResources();
	}

}
