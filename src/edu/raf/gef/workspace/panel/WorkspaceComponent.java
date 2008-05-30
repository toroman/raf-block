package edu.raf.gef.workspace.panel;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.workspace.Workspace;
import edu.raf.gef.workspace.diagram.DiagramProject;

public class WorkspaceComponent extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3164075486971114416L;
	private Workspace workspace;

	private JTree trWorkspace;

	public WorkspaceComponent(Workspace workspace) {
		initComponents();
		setWorkspace(workspace);
	}

	private void initComponents() {
		trWorkspace = new JTree();
		trWorkspace.setRootVisible(false);
		Container con = this;
		con.setLayout(new BorderLayout());
		con.add(trWorkspace, BorderLayout.CENTER);
	}

	public void setWorkspace(Workspace workspace) {
		if (workspace == null)
			throw new NullPointerException();
		trWorkspace.setModel(workspace);
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	public static void main(final String[] args) {
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					main(args);
				}
			});
			return;
		}
		JFrame frame = new JFrame("Dude");
		frame.setBounds(100, 100, 700, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final Workspace ws = new Workspace(new File(File.listRoots()[0], "workspace_test"));
		WorkspaceComponent wsc = new WorkspaceComponent(ws);

		Container con = frame.getContentPane();
		con.setLayout(new BorderLayout());
		con.add(wsc, BorderLayout.CENTER);
		frame.setVisible(true);
		// filling workspace
		GefDiagram dg = new GefDiagram();
		DiagramProject p1 = new DiagramProject(ws, dg);

		Timer t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				System.out.println("Running free");
				GefDiagram dg = new GefDiagram();
				dg.getModel().setTitle("NEWWWWWW");
				DiagramProject p2 = new DiagramProject(ws, dg);
			}
		}, 5000);
	}
}
