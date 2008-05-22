package edu.raf.flowchart;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;

import javax.swing.JPanel;

import edu.raf.flowchart.view.FlowChartEditor;
import edu.raf.gef.app.Resources;
import edu.raf.gef.app.framework.EditorPlugin;

public class FlowChartPlugin implements EditorPlugin {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4600587151373699739L;

	static final private Resources resources = new Resources(FlowChartPlugin.class.getPackage()
			.getName().replace('.', File.separatorChar)
			+ "/res/");

	public FlowChartPlugin() {

	}

	public JPanel createEditor() {
		return new FlowChartEditor();
	}

	public static Resources getResources() {
		return resources;
	}
}
