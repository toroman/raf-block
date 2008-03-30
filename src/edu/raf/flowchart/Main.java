package edu.raf.flowchart;

import javax.swing.UIManager;

import edu.raf.flowchart.gui.MainFrame;

public class Main {
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable () {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				new MainFrame().setVisible(true);
			}
		});
	}
}
