package edu.raf.gef.gui;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class WorkspaceDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5915679143303261378L;

	private JLabel lblPath = new JLabel("Workspace:");
	private JTextField txtPath = new JTextField(new File(".").getAbsolutePath());
	private JButton btnOk = new JButton(new AbstractAction("OK") {
		public void actionPerformed(ActionEvent e) {
			onOK();
		}
	});

	public WorkspaceDialog() {
		super();
		this.setModal(true);
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 200, 400, 250);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		initComponents();
	}

	protected void onOK() {
		
	}

	private void initComponents() {
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		c.add(lblPath);
		c.add(txtPath);
		c.add(btnOk);
	}

	public static void main(String[] args) {
		new WorkspaceDialog().setVisible(true);
	}
}
