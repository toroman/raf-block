package edu.raf.gefcomponents.lookandfeel;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import edu.raf.gef.app.errors.GraphicalErrorHandler;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.swing.menus.MenuPart;
import edu.raf.gef.gui.swing.menus.StandardMenuParts;
import edu.raf.gef.plugin.AbstractPlugin;
import edu.raf.gefcomponents.lookandfeel.res.LFResources;

/**
 * This plugins adds default look and feel changing to our program.
 * 
 */
public class LookAndFeelPlugin implements AbstractPlugin {
	private MainFrame mainFrame;
	private GraphicalErrorHandler geh;

	private static class ResourcesInstanceHolder {
		static final LFResources resources = new LFResources();
	}

	@Override
	public LFResources getResources() {
		return ResourcesInstanceHolder.resources;
	}

	@Override
	public void setMainFrame(MainFrame mf) {
		this.mainFrame = mf;
		this.geh = new GraphicalErrorHandler(getClass(), mainFrame.getFrame());
		String lf = getResources().getProperty("lookAndFeel");
		if (!"".equals(lf)) {
			activateLookAndFeel(lf);
		}
		MenuPart part = mf.getMenuManager().getPart(StandardMenuParts.WINDOW_LOOK_AND_FEEL);
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			part.add(new ActionSetLookAndFeel(info.getClassName(), this));
		}
	}

	public void activateLookAndFeel(String className) {
		try {
			UIManager.setLookAndFeel(className);
			SwingUtilities.updateComponentTreeUI(mainFrame.getFrame());
		} catch (Throwable t) {
			geh
					.handleErrorBlocking("activateLookAndFeel", "Couldn't activate skin "
							+ className, t);
		}
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}

}
