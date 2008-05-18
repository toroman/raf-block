package test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import edu.raf.gef.app.Resources;
import edu.raf.gef.gui.MainFrame;

/**
 * Kreira prozor u određenom jeziku, omogućava promenu jezika i proverava sistem
 * za lociranje prozora pre ponovnom paljenju. Ono, da li je maksimizovan i to.
 * 
 * @author Boca
 * 
 */
@SuppressWarnings("serial")
public class ResourcesTest extends MainFrame implements ActionListener {

	@Override
	public void open() {
		super.open();
		JInternalFrame in = new JInternalFrame("HIho");
		in.setLayout(new BorderLayout());
		in.add(createPanel(), BorderLayout.CENTER);
		getDesktop().add(in);
	}

	private JPanel createPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		JTextArea textArea = new JTextArea(Resources.getGlobal().getString("testCompositeMessage",
			Resources.getLocale().toString(), new Date())
				+ "\n\n" + Resources.getGlobal().getString("testMessage"));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		mainPanel.add(textArea, BorderLayout.CENTER);

		JButton btn;

		JPanel jezikPanel = new JPanel();

		btn = new JButton("English");
		btn.addActionListener(this);
		jezikPanel.add(btn);
		btn = new JButton("Srpski");
		btn.addActionListener(this);
		jezikPanel.add(btn);
		btn = new JButton("Српски");
		btn.addActionListener(this);
		jezikPanel.add(btn);

		mainPanel.add(jezikPanel, BorderLayout.SOUTH);
		return mainPanel;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (((JButton) event.getSource()).getText().equals("English")) {
			Resources.getGlobal().setProperty("currentLocale", "en");
			Resources.getGlobal().saveProperties();
		} else if (((JButton) event.getSource()).getText().equals("Srpski")) {
			Resources.getGlobal().setProperty("currentLocale", "sr SR LATIN");
			Resources.getGlobal().saveProperties();
		} else {
			Resources.getGlobal().setProperty("currentLocale", "sr SR CYRILLIC");
			Resources.getGlobal().saveProperties();
		}
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				new ResourcesTest().open();
			}
		});
	}

}
