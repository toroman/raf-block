package edu.raf.gef.gui.standard;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeSupport;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import edu.raf.gef.app.ResourceHelper;
import edu.raf.gef.app.Resources;
import edu.raf.gef.app.errors.GraphicalErrorHandler;
import edu.raf.gef.gui.swing.StatusManager;

public abstract class ApplicationDialog {
	protected final String id;

	private GraphicalErrorHandler gErrorHandler;

	private StatusManager statusManager;

	private Component contents;

	private JDialog dialog;

	protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	private boolean createStatusbar = false;

	public ApplicationDialog(String id) {
		this.id = id;
	}

	/**
	 * Called after all parts have been initialized (see {@link #build()}
	 */
	protected abstract void init();

	public void addStatusbar() {
		createStatusbar = true;
	}

	/**
	 * The building process. (Re)create everything.
	 * <p>
	 * <ol>
	 * <li>{@link #createDialog()}</li>
	 * <li>{@link #createStatusbar()}</li>
	 * <li>{@link #createControls()}</li>
	 * </ol>
	 * 
	 * @param owner
	 *            Owning component
	 * 
	 */
	private void build(Window owner) {
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new IllegalStateException("Must be called from the EDT");
		}
		dialog = createDialog(owner);
		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				onFrameClosing(e);
			}
		});
		Container con = dialog.getContentPane();
		con.setLayout(new BorderLayout());
		if (createStatusbar) {
			statusManager = createStatusManager();
			con.add(statusManager.getStatusbar(), BorderLayout.SOUTH);
		}
		con.add(contents = createContents(), BorderLayout.CENTER);
		dialog.setIconImage(createIconImage());
		init();
	}

	protected JDialog createDialog(Window owner) {
		JDialog dlg = new JDialog(owner, createDialogTitle(), ModalityType.APPLICATION_MODAL);
		dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		return dlg;
	}

	/**
	 * Run upon closing of the frame. Should't be totally overriden.
	 * 
	 * @param e
	 */
	protected void onFrameClosing(WindowEvent e) {
		storeFrameState();
		getResources().saveProperties();
	}

	protected void restoreFrameState() {
		try {
			String state = getResources().getProperty(id + ".state");
			if (state == null)
				this.dialog.pack();
			else
				ResourceHelper.applyStateToDialog(this.dialog, state);
		} catch (Exception ex) {
			getGeh().handleError("restoreFrameState", "Couldn't restore frame state!", ex);
		}
	}

	protected void storeFrameState() {
		String state = ResourceHelper.dialogStateToString(dialog);
		getResources().setProperty(id + ".state", state);
	}

	protected Image createIconImage() {
		try {
			return getResources().getIcon(id + ".icon").getImage();
		} catch (Exception ex) {
			getGeh().handleError("createIcon", "Couldn't create icon for " + id, ex);
			return null;
		}
	}

	public void open(Window owner) {
		build(owner);
		restoreFrameState();
		dialog.setVisible(true);
	}

	protected void close() {
		onFrameClosing(null);
		dialog.setVisible(false);
		dialog.dispose();
	}

	/**
	 * will exit the program.
	 * 
	 * @return
	 */

	public Component createContents() {
		return new JPanel();
	}

	protected String createDialogTitle() {
		String title = null;
		if (id != null) {
			title = getResources().getString(id + ".title");
		}
		return title == null ? "" : title;
	}

	protected Component createStatusbar() {
		return new JLabel("Status bar");
	}

	protected StatusManager createStatusManager() {
		return new StatusManager();
	}

	public Component getContents() {
		return contents;
	}

	public StatusManager getStatusManager() {
		return statusManager;
	}

	public JDialog getDialog() {
		return dialog;
	}

	protected synchronized final GraphicalErrorHandler getGeh() {
		if (gErrorHandler == null) {
			gErrorHandler = new GraphicalErrorHandler(getClass(), dialog);
		}
		return gErrorHandler;
	}

	protected Logger getLog() {
		return Logger.getLogger(getClass().getName());
	}

	public abstract Resources getResources();
}
