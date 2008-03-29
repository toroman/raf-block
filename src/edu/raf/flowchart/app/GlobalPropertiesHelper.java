package edu.raf.flowchart.app;

import java.awt.Dimension;
import java.util.Locale;

import javax.swing.JFrame;

/**
 * Utility gluposti vezane za GlobalProperties.
 * 
 * @author Boca
 * 
 */
public class GlobalPropertiesHelper {
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
		return frame.getExtendedState() + " " + (int) frame.getBounds().getX() + " "
				+ (int) frame.getBounds().getY() + " " + frame.getWidth() + " " + frame.getHeight();
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
}
