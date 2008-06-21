package edu.raf.gef;

import javax.swing.UIManager;

import edu.raf.gef.app.Resources;
import edu.raf.gef.app.errors.GraphicalErrorHandler;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.plugin.ComponentDiscoveryUtils;

public final class Main {
	private static MainFrame mainFrame;
	private static final ComponentDiscoveryUtils components = new ComponentDiscoveryUtils();

	/**
	 * Well, we should change this :)
	 */
	public static Object simpleClipboard = null;

	/**
	 * Or use IoC instead.
	 * 
	 * @return Currently used instance of the component discovery class
	 */
	public static ComponentDiscoveryUtils getComponentDiscoveryUtils() {
		return components;
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				try {
					// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				} catch (Exception e) {
					geh.handleError("main", "Couldn't set look and feel (aka skin)", e);
				}

				String components = Resources.getGlobal().getProperty("components");
				String off = Resources.getGlobal().getProperty("disabled_components");
				if (components != null) {
					if (off == null)
						off = "";
					Main.components.discover(components, off);
				}

				mainFrame = new MainFrame();
				mainFrame.open();
			}
		});
	}

	private static GraphicalErrorHandler geh = new GraphicalErrorHandler(Main.class, null);

}
