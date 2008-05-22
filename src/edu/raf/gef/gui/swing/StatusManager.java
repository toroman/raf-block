package edu.raf.gef.gui.swing;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class StatusManager {
	private JPanel statusbar;
	private JLabel statusText;

	public StatusManager() {
	}

	private void buildStatusbar() {
		if (statusbar != null)
			throw new IllegalStateException("Statusbar should be built once and only once");
		statusbar = new JPanel();
		statusbar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		statusbar.setLayout(new BorderLayout());
		statusbar.add(statusText = new JLabel("Status bar"));
	}

	public Component getStatusbar() {
		if (statusbar == null)
			buildStatusbar();
		return statusbar;
	}

	public void setStatusMessage(String statusMessage) {
		statusText.setText(statusMessage);
	}
}
