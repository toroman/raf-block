package edu.raf.flowchart.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import edu.raf.flowchart.app.ResourceHelper;
import edu.raf.flowchart.app.Resources;
import edu.raf.flowchart.diagram.control.action.AddTestObject;

/**
 * Glavni prozor, vodi računa o perzistentnosti lokacije i stanja prozora.
 * @author Boca
 *
 */

public class MainFrame extends JFrame implements WindowListener {

	protected static final long serialVersionUID = 4040204356233038729L;
	
	private DiagramPanel diagramPanel;
	
	protected JPanel createMainPanel() {
		JPanel mainPanel = new JPanel();
		diagramPanel = new DiagramPanel();
		
		mainPanel.setLayout(new BorderLayout ());
		mainPanel.add(createToolBar(), BorderLayout.NORTH);
		mainPanel.add(diagramPanel, BorderLayout.CENTER);		
		
		return mainPanel;
	}
	
	protected JToolBar createToolBar() {
		JToolBar toolBar = new JToolBar();
		JButton createButton = new JButton (new AddTestObject(diagramPanel.getDiagram(), "Add Test Object"));
		toolBar.add(createButton);
		createButton.addActionListener(new ButtonListener ());
		return toolBar;
	}
	
	protected class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
	}

	public MainFrame() {
		super();
		super.setIconImage(Resources.getInstance().getImageIcon("MainFrameIcon 16x16.PNG").getImage());
		
		String frameState = Resources.getInstance().getProperty("frameState");
		String mainFrameTitle = Resources.getInstance().getLanguageBundle().getString("mainFrameTitle");
		
		setTitle(mainFrameTitle);
		ResourceHelper.applyStateToFrame(this, frameState);
		setContentPane(createMainPanel());
		pack();		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(this);
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosing(WindowEvent event) {
		Resources.getInstance().setProperty("frameState",
				ResourceHelper.frameStateToString(this));
		Resources.getInstance().saveProperties();
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		
	}
}
