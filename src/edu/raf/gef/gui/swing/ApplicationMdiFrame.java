package edu.raf.gef.gui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import edu.raf.gef.app.ResourceHelper;
import edu.raf.gef.app.Resources;
import edu.raf.gef.app.errors.GraphicalErrorHandler;
import edu.raf.gef.app.exceptions.GefException;
import edu.raf.gef.gui.swing.menus.MenuManager;
import edu.raf.gef.gui.swing.menus.SAXMenuImporter;

/**
 * Class for representing general MDI (Multiple Document Interface) frame.
 * Toolbar and status bar are optional. All creation methods can be overriden
 * but be careful.
 * 
 * 
 */
public abstract class ApplicationMdiFrame implements InternalFrameListener {
	protected final String id;
	private GraphicalErrorHandler gErrorHandler;
	private JFrame frame;

	private ToolbarManager toolbarManager;

	private StatusManager statusManager;

	private MenuManager menuManager;

	private JDesktopPane desktop;

	private boolean createToolbar = false;
	private boolean createStatusbar = false;
	private boolean createMenubar = false;
	private Resources resources;

	public ApplicationMdiFrame(String id) {
		this.id = id;
	}

	/**
	 * Called after all parts have been initialized (see {@link #build()}
	 */
	protected abstract void init();

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
			menuManager = createMenuManager();
			frame.setJMenuBar(menuManager.getMenubar());
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

		init();
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
			if (state == null)
				state = "0 133 54 800 600"; // default state
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
		JDesktopPane pane = new JDesktopPane();
		return pane;
	}

	protected String createFrameTitle() {
		String title = null;
		if (id != null) {
			title = resources.getString(id + ".title");
		}
		return title == null ? "" : title;
	}

	protected MenuManager createMenuManager() {
		MenuManager menu = new MenuManager();
		// try restore configuration from XML
		InputStream is = null;
		try {
			is = getResources().getResource(id + ".MenuConfigurationFile");
		} catch (GefException e) {
			// no configuration
			getLog().log(Level.FINEST, "Menu not configured");
			return menu;
		} catch (Throwable t) {
			getGeh().handleErrorBlocking("createMenuManager", "Couldn't read menu configuration!",
				t);
			System.exit(-1);
		}

		try {
			SAXParserFactory saxFactory = SAXParserFactory.newInstance();

			try {
				SAXParser parser = saxFactory.newSAXParser();
				parser.parse(is, new SAXMenuImporter(menu, getResources()));
			} finally {
				try {
					is.close();
				} catch (Throwable ex) {
				}
			}
		} catch (Throwable e) {
			try {
				getGeh().handleErrorBlocking("createMenuManager",
					"Menu couldn't be restored! Application will now shutdown.", e);
			} finally {
				System.exit(-1);
			}
		}
		return menu;
	}

	public MenuManager getMenuManager() {
		return menuManager;
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

	protected Logger getLog() {
		return Logger.getLogger(getClass().getName());
	}

	protected void setResources(Resources resources) {
		this.resources = resources;
	}

	public Resources getResources() {
		return resources;
	}

	public abstract void internalFrameActivated(InternalFrameEvent e);

	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent e) {

	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent e) {

	}

	@Override
	public void internalFrameIconified(InternalFrameEvent e) {

	}

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {

	}

}
