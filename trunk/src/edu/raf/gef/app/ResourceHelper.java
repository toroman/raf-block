package edu.raf.gef.app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.util.Locale;

import javax.swing.JFrame;

/**
 * Resources utilities.
 */
public class ResourceHelper {
	/**
	 * Parse a string representing the locale and return the Locale instance.
	 * 
	 * @param locale
	 * 
	 * @return
	 */
	public static Locale makeLocaleFromString(String locale) {
		String[] parts = locale.split(" ");
		switch (parts.length) {
		case 1:
			return new Locale(parts[0]);
		case 2:
			return new Locale(parts[0], parts[1]);
		default:
			return new Locale(parts[0], parts[1], parts[2]);
		}
	}

	/**
	 * Serialize frame's state to a String
	 * 
	 * @param frame
	 * @return
	 */
	public static String frameStateToString(JFrame frame) {
		boolean exVisible = frame.isVisible();
		int exState = frame.getExtendedState();
		frame.setVisible(true);
		frame.setExtendedState(JFrame.NORMAL);
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		}
		String state = exState + " " + (int) frame.getX() + " " + (int) frame.getY() + " "
				+ frame.getWidth() + " " + frame.getHeight();

		frame.setVisible(exVisible);
		frame.setExtendedState(exState);
		return state;
	}

	/**
	 * Apply the serialized state to the frame.
	 * 
	 * @param frame
	 * @param state
	 */
	public static void applyStateToFrame(JFrame frame, String state) {
		String[] coords = state.split(" ");
		frame.setExtendedState(Integer.parseInt(coords[0]));
		int x = Integer.parseInt(coords[1]);
		int y = Integer.parseInt(coords[2]);
		int width = Integer.parseInt(coords[3]);
		int height = Integer.parseInt(coords[4]);
		frame.setBounds(x, y, width, height);
		frame.setPreferredSize(new Dimension(width, height));
	}

	/**
	 * Use for eliminating "pink" transparent colors from images.
	 * 
	 * @param im
	 * @param colorToReplace
	 * @return
	 */
	public static Image makeTransparent(Image im, final Color colorToReplace) {
		ImageFilter filter = new RGBImageFilter() {
			private final int transparentRgb = colorToReplace.getRGB() & 0x00FFFFFF;

			@Override
			public final int filterRGB(int x, int y, int rgb) {
				int noAlpha = rgb & 0x00FFFFFF;
				if (noAlpha == transparentRgb) {
					return noAlpha;
				} else {
					return rgb;
				}
			}
		};
		ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	/**
	 * Converts a string given in format "R G B A" to a Color instance.
	 * 
	 * @param colorText
	 *            "R G B A"
	 * @return Teh color
	 */
	public static Color stringToColor(String colorText) {
		String[] parts = colorText.split(" ");
		int r = Integer.parseInt(parts[0]);
		int g = Integer.parseInt(parts[1]);
		int b = Integer.parseInt(parts[2]);
		int alpha = Integer.parseInt(parts[3]);
		return new Color(r, g, b, alpha);
	}

}
