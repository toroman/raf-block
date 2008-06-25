package edu.raf.gef.editor.actions;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;

import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.editor.model.object.Drawable;
import edu.raf.gef.gui.MainFrame;
import edu.raf.gef.gui.actions.ResourceConfiguredAction;

public class ActionExportDiagram extends ResourceConfiguredAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2644033083557256902L;

	private MainFrame mainFrame;

	public ActionExportDiagram(MainFrame mf) {
		super(mf.getFrame(), "ActionExportDiagram");
		this.mainFrame = mf;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GefDiagram dg = mainFrame.getSelectedDiagram();
		if (dg == null)
			return;
		JFileChooser fc = new JFileChooser(new File("."));
		fc.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".png");
			}

			@Override
			public String getDescription() {
				return "PNG Images";
			}
		});
		if (fc.showSaveDialog(mainFrame.getFrame()) != JFileChooser.APPROVE_OPTION)
			return;
		final File out = fc.getSelectedFile();
		int x1, y1, x2, y2;
		x1 = y1 = Integer.MAX_VALUE;
		x2 = y2 = Integer.MIN_VALUE;

		for (Drawable d : dg.getModel().getDrawables()) {
			Rectangle2D r = d.getBoundingRectangle();
			x1 = (int) Math.min(x1, r.getMinX());
			y1 = (int) Math.min(y1, r.getMinY());
			x2 = (int) Math.max(x2, r.getMaxX());
			y2 = (int) Math.max(y2, r.getMaxY());
		}
		x1 -= 50;
		y1 -= 50;
		x2 += 50;
		y2 += 50;

		int w = x2 - x1;
		int h = y2 - y1;

		final BufferedImage bimb = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) bimb.createGraphics();
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, w, h);
		g.translate(-x1, -y1);
		g.setClip(x1, y1, w, h);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		dg.getView().drawDiagram(g);
		g.dispose();

		new SwingWorker<Exception, Void>() {
			@Override
			protected Exception doInBackground() throws Exception {
				try {
					ImageIO.write(bimb, "PNG", out);
				} catch (IOException e1) {
					return e1;
				}
				return null;
			}

			@Override
			protected void done() {
				Exception ex;
				try {
					ex = get();
				} catch (Exception e) {
					ex = e;
				}
				if (ex != null) {
					getGeh().handleError("Export", "Error in exporting. Sorry, try again.", ex);
				}
			}
		}.execute();

	}

}
