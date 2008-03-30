package edu.raf.flowchart.app;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

/**
 * Singleton klasa koja čuva sve opcije vezane za aplikaciju uopšteno, ali i globalcu Locale
 * instancu, i jezik. Po kreiranju čita podatke o jeziku, pa čita i sve ostalo. Pravo učitavanje se
 * dešava tek kada se prvi put zatraži instanca klase. Što će se verovatno desiti kada se bude
 * kreirao prozor, zbog title-a ili pozicije na ekranu ili šta ja znam.
 * <p>
 * Ako nam ne treba lazy čitanje ima i preloadResources() metodu.
 * <p>
 * Locale-u i LanguageBundle-u se može pristupiti samo preko ne-static metoda za pristup. Na primer,
 * da bi pronašao tekst za labelu <i>Djoksi</i>, linija glasi
 * <i>Resources.getInstance().getLanguageBundle().getString("DjoksiLabel")</i>
 * <p>
 * Formatiranje kompozitne poruke se obavlja preko metode getInstance().createCompositeMessage
 * (String patternName, Object... args).
 * <p>
 * Slike su baferovane, i za to postoji metoda getInstance.getImageIcon(String fileName). Na primer,
 * getImageIcon ("MainFrameIcon.PNG")
 * 
 * @author Boca
 */

@SuppressWarnings("serial")
public class Resources extends Properties {
	private static volatile Resources GLOBAL_RESOURCES = null;
	private static volatile Locale APP_LOCALE = null;
	private static volatile ResourceBundle LANGUAGE_BUNDLE = null;
	private static volatile MessageFormat MSG_FORMATTER = null;
	private static volatile HashMap<String, ImageIcon> IMAGES = null;

	public static final File DEFAULT_PROPERTIES_FILE = new File("res/properties.properties");
	public static final Color TRANSPARRENT_COLOR = new Color(255, 0, 255);

	private Resources() {
		loadProperties();
		APP_LOCALE = ResourceHelper.makeLocaleFromString(getProperty("currentLocale"));
		LANGUAGE_BUNDLE = ResourceBundle.getBundle("languages/LocalMessagesBundle", APP_LOCALE);
		MSG_FORMATTER = new MessageFormat("");
		MSG_FORMATTER.setLocale(APP_LOCALE);

		IMAGES = new HashMap<String, ImageIcon>();

		File[] imageFiles = new File("res/images").listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				String filename = pathname.getAbsolutePath();
				String extension = (filename.lastIndexOf(".") == -1) ? null : filename.substring(
						filename.lastIndexOf(".") + 1, filename.length());
				if (extension != null && extension.toLowerCase().equals("png"))
					return true;
				return false;
			}
		});
		for (File file : imageFiles) {
			String path = "\\images\\" + file.getName();
			URL url = Resources.class.getClassLoader().getResource(path);
			Image image = Toolkit.getDefaultToolkit().getImage(url);
			IMAGES.put(file.getName(), new ImageIcon(ResourceHelper.makeColorTransparent(image,
					TRANSPARRENT_COLOR)));
		}
	}

	/**
	 * Pri pozivu ove metode se otpočinje učitavanje svega ovoga. Služi da se izbegne lazy
	 * učitavanje, ako bude potrebe.
	 */

	public static void preloadResources() {
		getInstance();
	}

	/**
	 * Glavna singleton metoda. Otpočinje lazy učitavanje, ako treba.
	 * 
	 * @return instancu Resources klase
	 */

	public static Resources getInstance() {
		if (GLOBAL_RESOURCES == null)
			synchronized (Resources.class) {
				if (GLOBAL_RESOURCES == null)
					GLOBAL_RESOURCES = new Resources();
			}
		return GLOBAL_RESOURCES;
	}

	/**
	 * Kreira string od templejta koji se nalazi pod imenom <i>patternName</i>, i ubacuje
	 * odgovarajuće argumente na odgovarajuća mesta u odgovarajućem formatu. Koristi globalni Locale
	 * za odabir svih pod-formata.
	 * 
	 * @param patternName
	 *            ldentifikator tog paterna u LanguageBundle-u
	 * @param args
	 *            lista argumenata
	 * @return sklopljena poruka
	 */

	public synchronized String createCompositeMessage(String patternName, Object... args) {
		String pattern = getLanguageBundle().getString(patternName);
		MSG_FORMATTER.applyPattern(pattern);
		return MSG_FORMATTER.format(args);
	}

	public synchronized final Locale getLocale() {
		return APP_LOCALE;
	}

	public synchronized final ResourceBundle getLanguageBundle() {
		return LANGUAGE_BUNDLE;
	}

	/**
	 * Snima promene properties-a.
	 */

	public synchronized void saveProperties() {
		try {
			super.store(new FileOutputStream(DEFAULT_PROPERTIES_FILE), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Čita properties iz fajla. Nema veze sa locale-om i jezicima, ova metoda čita samo podatke
	 * koji se mogu menjati u toku rada programa. Locale i jezik se inicijalizuju prvi put, i ne
	 * promenjivi su.
	 */

	public synchronized void loadProperties() {
		try {
			super.load(new FileInputStream(DEFAULT_PROPERTIES_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Vraća ImageIcon objekat koji je učitan sa fajla <i>imageName</i>. Na primer, getImageIcon
	 * ("MainFrameIcon 16x16.PNG"). On ne čita tu sliku sa fajla, ona je baferovana a može joj se
	 * pristupiti preko tog ključa.
	 * 
	 * @param imageName
	 *            ime fajla
	 * @return traženu sliku
	 */

	public synchronized ImageIcon getImageIcon(String imageName) {
		return IMAGES.get(imageName);
	}
}
