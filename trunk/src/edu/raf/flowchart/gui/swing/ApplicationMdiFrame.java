package edu.raf.flowchart.gui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

import edu.raf.flowchart.app.ResourceHelper;
import edu.raf.flowchart.app.Resources;
import edu.raf.flowchart.app.errors.GraphicalErrorHandler;

/**
 * Class for representing general MDI (Multiple Document Interface) frame.
 * Toolbar and status bar are optional. All creation methods can be overriden
 * but be careful.
 * 
 * @author toroman
 * 
 */
public class ApplicationMdiFrame {
	private String id;
	private GraphicalErrorHandler gErrorHandler;
	private JFrame frame;

	private ToolbarManager toolbarManager;

	private StatusManager statusManager;

	private JDesktopPane desktop;

	private boolean createToolbar = false;
	private boolean createStatusbar = false;
	private boolean createMenubar = false;
	private Resources resources;

	public ApplicationMdiFrame(String id) {
		this.id = id;
	}

	public void addToolbar() {
		createToolbar = true;
	}

	public void addStatusbar() {
		createStatusbar = true;
	}

	public void addMenubar() {
		createMenubar = true;
	}

	/**
	 * The building process. (Re)create everything.
	 * <p>
	 * <ol>
	 * <li>{@link #createFrame()}</li>
	 * <li>{@link #createMenubar()}</li>
	 * <li>{@link #createToolbar()}</li>
	 * <li>{@link #createStatusbar()}</li>
	 * <li>{@link #createControls()}</li>
	 * </ol>
	 * 
	 */
	private void build() {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("Must be called from the EDT");
		}
		frame = createFrame();
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				onFrameClosing(e);
			}
		});
		if (createMenubar) {
			frame.setJMenuBar(createMenubar());
		}
		Container con = frame.getContentPane();
		con.setLayout(new BorderLayout());
		if (createToolbar) {
			toolbarManager = createToolbarManager();
			con.add(toolbarManager.getToolbar(), BorderLayout.NORTH);
		}
		if (createStatusbar) {
			statusManager = createStatusManager();
			con.add(statusManager.getStatusbar(), BorderLayout.SOUTH);
		}
		con.add(desktop = createDesktop(), BorderLayout.CENTER);
		frame.setIconImage(createIconImage());
	}

	/**
	 * Run upon closing of the frame. Should't be totally overriden.
	 * 
	 * @param e
	 */
	protected void onFrameClosing(WindowEvent e) {
		storeFrameState();
		resources.saveProperties();
	}

	protected void restoreFrameState() {
		try {
			String state = resources.getProperty(id + ".state");
			ResourceHelper.applyStateToFrame(this.frame, state);
		} catch (Exception ex) {
			getGeh().handleError("restoreFrameState", "Couldn't restore frame state!", ex);
		}
	}

	protected void storeFrameState() {
		String state = ResourceHelper.frameStateToString(frame);
		resources.setProperty(id + ".state", state);
	}

	protected Image createIconImage() {
		try {
			return resources.getIcon(id + ".icon").getImage();
		} catch (Exception ex) {
			getGeh().handleError("createIcon", "Couldn't create icon!", ex);
			return null;
		}
	}

	public void open() {
		build();
		restoreFrameState();
		frame.setVisible(true);
	}

	public void close() {
		frame.setVisible(false);
	}

	/**
	 * This will return the underlying JFrame. By default, closing of the frame
	 * will exit the program.
	 * 
	 * @return
	 */
	protected JFrame createFrame() {
		JFrame frame = new JFrame(createFrameTitle());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return frame;
	}

	protected JDesktopPane createDesktop() {
		return new JDesktopPane();
	}

	protected String createFrameTitle() {
		String title = null;
		if (id != null) {
			title = resources.getString(id + ".title");
		}
		return title == null ? "" : title;
	}

	protected JMenuBar createMenubar() {
		return new JMenuBar();
	}

	protected Component createStatusbar() {
		return new JLabel("Status bar");
	}

	protected ToolbarManager createToolbarManager() {
		return new ToolbarManager();
	}

	protected StatusManager createStatusManager() {
		return new StatusManager();
	}

	public final JDesktopPane getDesktop() {
		return desktop;
	}

	public StatusManager getStatusManager() {
		return statusManager;
	}

	public ToolbarManager getToolbarManager() {
		return toolbarManager;
	}

	public JFrame getFrame() {
		return frame;
	}

	protected synchronized final GraphicalErrorHandler getGeh() {
		if (gErrorHandler == null) {
			gErrorHandler = new GraphicalErrorHandler(getClass(), frame);
		}
		return gErrorHandler;
	}

	protected void setResources(Resources resources) {
		this.resources = resources;
	}

	public Resources getResources() {
		return resources;
	}

}
