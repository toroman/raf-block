package edu.raf.gef.editor.actions;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.SwingWorker;

import edu.raf.gef.app.exceptions.GefRuntimeException;
import edu.raf.gef.editor.GefDiagram;
import edu.raf.gef.gui.actions.ResourceConfiguredAction;
import edu.raf.gef.gui.swing.menus.StandardMenuActions;

public class ActionPrintDiagram extends ResourceConfiguredAction {

	private GefDiagram diagram;

	public ActionPrintDiagram(Component onErrorComponent, GefDiagram diagram) {
		super(onErrorComponent, StandardMenuActions.PRINT);
		this.diagram = diagram;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				doPrint();
				return null;
			}
		}.execute();
	}

	protected void doPrint() {
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(OrientationRequested.LANDSCAPE);
		// aset.add(new Copies(1));
		// aset.add(new JobName("Diagram print", null));

		/* Create a print job */
		PrinterJob pj = PrinterJob.getPrinterJob();
		pj.setPrintable(new Printable() {
			public int print(Graphics g, PageFormat pf, int pageIndex) {
				if (pageIndex == 0) {
					Graphics2D g2d = (Graphics2D) g;
					g2d.translate(pf.getImageableX(), pf.getImageableY());
					diagram.getView().drawDiagram(g2d);
					return Printable.PAGE_EXISTS;
				} else {
					return Printable.NO_SUCH_PAGE;
				}
			}
		});
		PrintService[] services = PrinterJob.lookupPrintServices();

		if (services.length > 0) {
			try {
				pj.setPrintService(services[0]);
				pj.pageDialog(aset);
				if (pj.printDialog(aset)) {
					pj.print(aset);
				}
			} catch (PrinterException pe) {
				throw new GefRuntimeException("Couldn't print!", pe);
			}
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1399246630146065524L;

}
