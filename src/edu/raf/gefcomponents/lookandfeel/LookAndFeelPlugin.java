package edu.raf.gefcomponents.lookandfeel;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import edu.raf.gef.app.IResources;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.swing.menus.MenuPart;
import edu.raf.gef.gui.swing.menus.StandardMenuParts;
import edu.raf.gef.plugin.AbstractPlugin;

/**
 * This plugins adds default look and feel changing to our program.
 * 
 */
public class LookAndFeelPlugin implements AbstractPlugin, IResources {

	@Override
	public IResources getResources() {
		return this;
	}

	@Override
	public void setMainFrame(MainFrame mf) {
		MenuPart part = mf.getMenuManager().getPart(StandardMenuParts.WINDOW_LOOK_AND_FEEL);
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			part.add(new ActionSetLookAndFeel(info.getClassName(), mf.getFrame()));
		}
	}

	@Override
	public ImageIcon getIcon(String key) {
		return null;
	}

	@Override
	public String getProperty(String key) {
		if ("name".equals(key))
			return "Look and feel";
		else
			return null;
	}

	@Override
	public String getString(String key, Object... args) {
		return null;
	}

}
