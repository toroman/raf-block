package edu.raf.flowchart.gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import edu.raf.flowchart.app.ComponentDiscoveryUtils;
import edu.raf.flowchart.app.Resources;
import edu.raf.flowchart.gui.actions.ActionGroups;
import edu.raf.flowchart.gui.actions.CreateDocumentAction;
import edu.raf.flowchart.gui.actions.OpenDocumentAction;
import edu.raf.flowchart.gui.swing.ApplicationMdiFrame;
import edu.raf.flowchart.gui.swing.EditorPlugin;
import edu.raf.flowchart.gui.swing.ToolbarManager;

/**
 * Main window, defines creational functions.
 * 
 */

public class MainFrame extends ApplicationMdiFrame {
	/**
	 * ID representing this frame
	 */
	public static final String ID = "mainFrame";

	protected static final long serialVersionUID = 4040204356233038729L;

	public MainFrame() {
		super(ID);
		setResources(Resources.getGlobal());
		addMenubar();
		addStatusbar();
		addToolbar();
	}

	@Override
	protected ToolbarManager createToolbarManager() {
		ToolbarManager tbm = super.createToolbarManager();
		tbm.addAction(ActionGroups.STANDARD.name(), new OpenDocumentAction(this));
		// tbm.addAction(ActionGroups.CUT_COPY_PASTE.name(), new
		// OpenDocumentAction(this));
		return tbm;
	}

	@Override
	protected JMenuBar createMenubar() {
		JMenuBar mbar = super.createMenubar();
		{
			JMenu file = new JMenu("File!");
			{
				JMenu inew = new JMenu("New");
				{
					for (Class<? extends EditorPlugin> plugin : ComponentDiscoveryUtils
							.getPlugins()) {
						JMenuItem menuItem = new JMenuItem(new CreateDocumentAction(this, plugin));
						inew.add(menuItem);
					}
				}
				file.add(inew);

				JMenuItem open = new JMenuItem(new OpenDocumentAction(this));
				file.add(open);
			}
			mbar.add(file);
		}
		return mbar;
	}
}
