package test;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import edu.raf.flowchart.app.Resources;
import edu.raf.flowchart.app.ResourceHelper;

/**
 * Kreira prozor u određenom jeziku, omogućava promenu jezika i proverava sistem za lociranje
 * prozora pre ponovnom paljenju. Ono, da li je maksimizovan i to.
 * 
 * @author Boca
 * 
 */
public class ResourcesTest extends WindowAdapter implements ActionListener {
	JFrame prozor;

	private ResourcesTest() {

		String prozorState = Resources.getInstance().getProperty("frameState");
		String testString = Resources.getInstance().getLanguageBundle().getString(
				"testMessage");

		prozor = new JFrame(testString);

		ResourceHelper.applyStateToFrame(prozor, prozorState);
		prozor.add(new JLabel(testString));
		prozor.setLayout(new FlowLayout());
		JButton btn;

		btn = new JButton("English");
		btn.addActionListener(this);
		prozor.add(btn);
		btn = new JButton("Srpski");
		btn.addActionListener(this);
		prozor.add(btn);
		btn = new JButton("Српски");
		btn.addActionListener(this);
		prozor.add(btn);

		prozor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		prozor.pack();
		prozor.addWindowListener(this);
		prozor.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (((JButton) event.getSource()).getText().equals("English")) {
			Resources.getInstance().setProperty("currentLocale", "en");
			Resources.getInstance().saveProperties();
		} else if (((JButton) event.getSource()).getText().equals("Srpski")) {
			Resources.getInstance().setProperty("currentLocale", "sr SR LATIN");
			Resources.getInstance().saveProperties();
		} else {
			Resources.getInstance().setProperty("currentLocale", "sr SR CYRILLIC");
			Resources.getInstance().saveProperties();
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		Resources.getInstance().setProperty("frameState",
				ResourceHelper.frameStateToString(prozor));
		Resources.getInstance().saveProperties();
		super.windowClosing(e);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ResourcesTest();
			}
		});
	}

}
