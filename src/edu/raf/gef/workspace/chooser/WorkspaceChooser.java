package edu.raf.gef.workspace.chooser;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import edu.raf.gef.app.Resources;
import edu.raf.gef.gui.standard.ApplicationDialog;

public class WorkspaceChooser extends ApplicationDialog {
	private JTextField txtLocation;
	private String location = null;

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	private AbstractAction browse = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5384406827728205044L;

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser(new File("."));
			fc.setFileFilter(new FileFilter() {
				@Override
				public boolean accept(File f) {
					return f.isDirectory();
				}

				@Override
				public String getDescription() {
					return "Workspace folder";
				}
			});
			if (fc.showOpenDialog(getDialog()) != JFileChooser.APPROVE_OPTION)
				return;
			txtLocation.setText(fc.getSelectedFile().getAbsolutePath());
		}
	};

	private AbstractAction aciContinue = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5384406827728205044L;

		@Override
		public void actionPerformed(ActionEvent e) {
			setLocation(txtLocation.getText());
			close();
		}
	};

	public WorkspaceChooser() {
		super("WorkspaceChooser");
		browse.putValue(Action.NAME, getResources().getString("browse"));
		aciContinue.putValue(Action.NAME, "OK");
	}

	@Override
	public Resources getResources() {
		return Resources.getGlobal();
	}

	@Override
	public Component createContents() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		txtLocation = new JTextField(location);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(txtLocation, gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.EAST;
		panel.add(new JButton(aciContinue), gbc);
		// panel.add(new JButton(browse), gbc);
		return panel;
	}

	@Override
	protected void init() {
		getDialog().addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {

			}
		});
	}

	@Override
	public String open(Window owner) {
		super.open(owner);
		return getLocation();
	}
}
