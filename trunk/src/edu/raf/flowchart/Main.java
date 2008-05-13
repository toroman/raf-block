package edu.raf.flowchart;

import edu.raf.flowchart.app.ComponentDiscoveryUtils;
import edu.raf.flowchart.app.Resources;
import edu.raf.flowchart.gui.MainFrame;

public class Main {
	private static MainFrame mainFrame;

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
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
}
