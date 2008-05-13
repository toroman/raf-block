package edu.raf.flowchart;

import javax.swing.UIManager;

import edu.raf.flowchart.app.ComponentDiscoveryUtils;
import edu.raf.flowchart.app.Resources;
import edu.raf.flowchart.app.errors.GraphicalErrorHandler;
import edu.raf.flowchart.gui.MainFrame;

public final class Main {
	private static MainFrame mainFrame;

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				} catch (Exception e) {
					geh.handleError("main", "Couldn't set look and feel (aka skin)", e);
				}

				String components = Resources.getGlobal().getProperty("components");
				if (components != null) {
					String[] klasses = components.split(" ");
					ComponentDiscoveryUtils.discover(klasses);
				}
				mainFrame = new MainFrame();
				mainFrame.open();
			}
		});
	}

	private static GraphicalErrorHandler geh = new GraphicalErrorHandler(Main.class, null);

}
