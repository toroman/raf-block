package edu.raf.gef.gui.help;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class HelpFrame extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = -8663925647486921998L;
	
	private JEditorPane editorPane;
	private JButton closeButton;
	
	public HelpFrame() {
		super ("Uputstvo & O Autoru");
		
		editorPane = new JEditorPane();		
		closeButton = new JButton("Zatvori");
		closeButton.addActionListener(this);
		
		JPanel mainPanel = new JPanel();
		JPanel lowerPanel = new JPanel();
		
		mainPanel.setLayout(new BorderLayout());
		lowerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		lowerPanel.add(closeButton);
		mainPanel.add(lowerPanel, BorderLayout.SOUTH);
		mainPanel.add(new JScrollPane (editorPane), BorderLayout.CENTER);
		
		editorPane.setEditable(false);
		editorPane.setContentType("text/html");
		
		InputStream iStream = getClass().getClassLoader().getResourceAsStream("help.html");
		Reader reader = new InputStreamReader(iStream, Charset.forName("UTF-8"));
		
		try {
			editorPane.read(reader, editorPane.getDocument());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setContentPane(mainPanel);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setPreferredSize(new Dimension (800, 600));
		pack();
		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		setVisible(false);
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable () {
			@Override
			public void run() {
				new HelpFrame().setVisible(true);
			}
		});
	}
}
