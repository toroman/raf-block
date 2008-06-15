package edu.raf.gef.gui.swing;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class ActionMapTesting extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 327750911496101438L;

	public ActionMapTesting() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 300);
		JMenuBar dude = new JMenuBar();
		setJMenuBar(dude);

		JMenu file = new JMenu("File!");
		dude.add(file);
		{
			AbstractAction aci = new AbstractAction() {

				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(ActionMapTesting.this, "Akcija!");
				}

			};
			aci.setEnabled(false);
			aci.putValue(AbstractAction.NAME, "Cut title");
			aci.putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke("F2"));
			file.add(aci);
			file.getActionMap().put("Cut", aci); // ovo je novo
			file.getInputMap().put((KeyStroke) aci.getValue(AbstractAction.ACCELERATOR_KEY), "Cut");
		}

		JTextField field = new JTextField("HIho");
		getContentPane().setLayout(new FlowLayout());
		getContentPane().add(field);
		field.getInputMap().setParent(dude.getInputMap());
		field.getActionMap().put("Cut", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(ActionMapTesting.this, "Overriden!");
			}
		});
		// field.getInputMap().put(KeyStroke.getKeyStroke("F2"), "Cut");
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new ActionMapTesting().setVisible(true);
			}
		});
	}

}
