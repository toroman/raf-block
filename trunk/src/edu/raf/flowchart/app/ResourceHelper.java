package edu.raf.flowchart.app;

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
 * Utility gluposti vezane za Resources.
 * 
 * @author Boca
 * 
 */
public class ResourceHelper {
	/**
	 * Parsira string i od njega pravi Locale instancu. String mora biti jednog od formata
	 * "&#060;lang&#062;", "&#060;lang&#062; &#060;country&#062;" ili "&#060;lang&#062;
	 * &#060;country&#062; &#060;variant&#062;". Postoji tačno jedan blanko između tagova, ako se ne
	 * vidi.
	 * 
	 * @param locale
	 *            String koji treba parsirati
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
	 * Prebacuje poziciju i stanje prozora u string.
	 * 
	 * @param frame
	 *            Prozor čiji se stanje konvertuje
	 * @return opis stanja prozora
	 */

	public static String frameStateToString(JFrame frame) {
		int exState = frame.getExtendedState();
		frame.setVisible(false);
		frame.setExtendedState(JFrame.NORMAL);
		frame.setVisible(true);
		return exState + " " + (int) frame.getX() + " "
				+ (int) frame.getY() + " " + frame.getWidth() + " " + frame.getHeight();
	}

	/**
	 * Ime govori čemu služi.
	 * 
	 * @param frame
	 *            Prozor koji treba namestiti
	 * @param state
	 *            String formatiran kao u frameStateToString() metodi.
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
	 * Ovaj kod ti je poznat, Srećko :))
	 * 
	 * @param im slika koja treba da dobije transparentnu boju
	 * @param color transparentna boja 
	 * @return nova slika sa transparentnom bojom (НЕ СЕРИ, РЕЈУЗАБИЛНОСТ)
	 */
	
	public static Image makeColorTransparent(Image im, final Color color) {
		ImageFilter filter = new RGBImageFilter() {

			public int markerRGB = color.getRGB() | 0xFF000000;

			@Override
			public final int filterRGB(int x, int y, int rgb) {
				if ((rgb | 0xFF000000) == markerRGB) {
					return 0x00FFFFFF & rgb;
				} else {
					return rgb;
				}
			}
		};
		ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	public static Color stringToColor(String colorText) {
		String [] parts = colorText.split(" ");
		int r = Integer.parseInt(parts[0]);
		int g = Integer.parseInt(parts[1]);
		int b = Integer.parseInt(parts[2]);
		int alpha = Integer.parseInt(parts[3]);
		return new Color (r, g, b, alpha);
	}
}
