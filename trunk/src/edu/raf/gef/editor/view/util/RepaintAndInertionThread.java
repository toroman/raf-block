package edu.raf.gef.editor.view.util;

import java.awt.geom.Point2D;

import edu.raf.gef.editor.view.DiagramView;

/**
 * Does the repainting. If you don't want to use inertion, state so in the
 * constructor. This thread does the repaint-if-needed, and bundles multiple
 * invocations of repaint into one repaint done in the end.
 */
public class RepaintAndInertionThread implements Runnable {

	private final static int lambda = 7; // do not modify

	private Boolean repaintNeeded;
	private final DiagramView view;
	private Thread thread = null;
	private final boolean useInertion;
	double uservx = 0, uservy = 0, devicevx = 0, devicevy = 0;

	private boolean radi;

	private boolean updateVelocities;

	public RepaintAndInertionThread(DiagramView view) {
		this(view, true);
	}

	public RepaintAndInertionThread(DiagramView view, boolean useInertion) {
		this.view = view;
		this.useInertion = useInertion;
		repaintNeeded = true;
		radi = true;
		updateVelocities = false;
	}

	public synchronized void start() {
		if (thread != null)
			throw new IllegalStateException("Thread already running");
		else {
			thread = new Thread(this);
			thread.start();
		}
	}

	public synchronized void stop() {
		if (thread == null)
			throw new IllegalStateException("Thread already not running");
		else {
			thread.interrupt();
			thread = null;
		}
	}

	public synchronized void setRepaintNeeded(final boolean bool) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				synchronized (repaintNeeded) {
					repaintNeeded = bool;
				}
			}
		});
	}

	public synchronized boolean isRepaintNeeded() {
		synchronized (repaintNeeded) {
			return repaintNeeded;
		}
	}

	public void setRadi(boolean radi) {
		this.radi = radi;
		updateVelocities = true;
	}

	public boolean getRadi() {
		return radi;
	}

	/**
	 * repaints and does the indertion, if set.
	 */
	
	@Override
	public void run() {
		AffineTransformManager afm = view.getAffineTransformManager();
		try {
			Point2D oldUserSpaceLocation = (Point2D) afm.getUserSpaceLocation().clone();
			Point2D oldDeviceSpaceLocation = (Point2D) afm.getDeviceSpaceLocation().clone();
			long newTime = System.currentTimeMillis();
			while (true) {
				long oldTime = newTime;
				newTime = System.currentTimeMillis();
				long deltaTime = newTime - oldTime;

				boolean doRepaint = false;

				if (useInertion)
					synchronized (afm) {

						Point2D newUserSpaceLocation = (Point2D) afm.getUserSpaceLocation().clone();
						Point2D newDeviceSpaceLocation = (Point2D) afm.getDeviceSpaceLocation()
								.clone();

						if (updateVelocities) {
							updateVelocities = false;
							uservx = 60
									* (newUserSpaceLocation.getX() - oldUserSpaceLocation.getX())
									/ deltaTime;
							uservy = 60
									* (newUserSpaceLocation.getY() - oldUserSpaceLocation.getY())
									/ deltaTime;
							devicevx = 60
									* (newDeviceSpaceLocation.getX() - oldDeviceSpaceLocation
											.getX()) / deltaTime;
							devicevy = 60
									* (newDeviceSpaceLocation.getY() - oldDeviceSpaceLocation
											.getY()) / deltaTime;
						}

						if (getRadi()) {
							afm.setAutoMatchTransform(false);
							
							double factor = Math.exp(-1d * deltaTime * lambda / 1000);

							if (Math.abs(uservx) > 0.1d || Math.abs(uservy) > 0.1d) {
								double x = newUserSpaceLocation.getX();
								x += uservx * (1 - factor);
								double y = newUserSpaceLocation.getY();
								y += uservy * (1 - factor);
								afm.setUserSpaceLocation(x, y);
								uservx *= factor;
								uservy *= factor;
								doRepaint = true;
							}

							if (Math.abs(devicevx) > 0.1d || Math.abs(devicevy) > 0.1d) {
								double x = newDeviceSpaceLocation.getX();
								x += devicevx * (1 - factor);
								double y = newDeviceSpaceLocation.getY();
								y += devicevy * (1 - factor);
								afm.setDeviceSpaceLocation(x, y);
								devicevx *= factor;
								devicevy *= factor;
								doRepaint = true;
							}
							
							afm.matchTransform();
							if (!doRepaint)
								setRadi(false);
						}

						oldUserSpaceLocation = newUserSpaceLocation;
						oldDeviceSpaceLocation = newDeviceSpaceLocation;
					}

				if (repaintNeeded)
					view.getCanvas().repaint();
				Thread.sleep(10);
			}
		} catch (InterruptedException e) {
			return;
		}
	}
}
