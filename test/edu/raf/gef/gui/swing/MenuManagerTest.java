package edu.raf.gef.gui.swing;

import static org.junit.Assert.fail;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.raf.gef.gui.swing.menus.MenuManager;
import edu.raf.gef.gui.swing.menus.MenuPart;

public class MenuManagerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMenuManager() throws Exception {
		// general test
		final Thread the = Thread.currentThread();
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				MenuManager manager = new MenuManager(null);
				manager.createContainer(MenuManager.ROOT, "file").getMenu().setText("File");
				MenuPart partImpExp = manager.createPart("file", "file.export_import");
				partImpExp.add(new AbstractAction("Akc1") {
					public void actionPerformed(ActionEvent e) {
					}
				});
				MenuPart partOpenSave = manager.createPart("file", "file.open_save_new");
				partOpenSave.add(new AbstractAction("Open") {
					public void actionPerformed(ActionEvent e) {
					}
				});

				partOpenSave.add(new AbstractAction("Save") {
					public void actionPerformed(ActionEvent e) {
					}
				});

				partOpenSave.add(new AbstractAction("Save as..") {
					public void actionPerformed(ActionEvent e) {
					}
				});
				JFrame fr = new JFrame("hha");
				fr.setLayout(new GridLayout(1, 2));
				fr.add(new JButton(new AbstractAction("OK") {
					@Override
					public void actionPerformed(ActionEvent e) {
						the.resume();
					}
				}));

				manager.createContainer(partImpExp.getPartId(), "file.dude").getMenu().setText(
					"Set text manually");
				manager.createPart("file.dude", "first");
				manager.addAction("first", new AbstractAction("First!") {
					public void actionPerformed(ActionEvent e) {

					}

				});

				fr.setJMenuBar((JMenuBar)manager.getMenubar());
				fr.pack();
				fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				fr.setVisible(true);
			}
		});
		the.suspend();
	}

	@Test
	public void testAddAction() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveAction() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreatePart() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetActions() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMenubar() {
		fail("Not yet implemented");
	}

}
