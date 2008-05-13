package edu.raf.flowchart.app;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Resource holder / wrapper.
 * <p>
 * Has a static getGlobal() method which returns the lazy initialized global
 * resources (which should have the default locale, main settings and other
 * stuff - possibly everything - but some other parts may actually have their
 * own resources...).
 * <p>
 * Strings are accessed via {@link #getLanguageBundle()}
 * <p>
 * Composite messages are obtained via
 * {@link #createCompositeMessage(String, Object...)}
 * <p>
 * Image icons are buffered, use {@link #getIcon(String)} to fetch
 * 
 */

@SuppressWarnings("serial")
public class Resources {
	private static Locale locale = null;

	public static Locale getLocale() {
		return locale;
	}

	private Properties properties;
	private ResourceBundle bundle;
	private HashMap<String, ImageIcon> icons;

	private String location;

	private static class GlobalInstanceHolder {
		static final Resources instance = new Resources("");
		static {
			locale = ResourceHelper.makeLocaleFromString(instance.getProperty("currentLocale"));
		}
	}

	/**
	 * @return Global resources.
	 */
	public static Resources getGlobal() {
		return GlobalInstanceHolder.instance;
	}

	/**
	 * Construct and initialize this the Resources instance.
	 * 
	 * @param location
	 *            Root directory for settings/properties, images and strings.
	 *            For example "" means for root, but something like "./foo/" can
	 *            be passed too.
	 */
	public Resources(String location) {
		this.location = location;
		this.icons = new HashMap<String, ImageIcon>();
		try {
			initProperties();
		} catch (Exception ex) {
			// TODO: handle exception
			ex.printStackTrace();
		}
	}

	/**
	 * Loads properties / settings.
	 */
	private void initProperties() throws IOException {
		properties = new Properties();
		InputStream fis = null;
		try {
			// fis = new FileInputStream(new File("properties.properties"));
			URL url = getClass().getClassLoader().getResource(location + "properties.properties");
			fis = new BufferedInputStream(new FileInputStream(url.getPath()));
			properties.load(fis);
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
	}

	private ResourceBundle getBundle() {
		if (bundle == null) {
			bundle = ResourceBundle.getBundle(location + "locales/LocalMessagesBundle", locale);
		}
		return bundle;
	}

	/**
	 * Snima promene properties-a.
	 */
	public void saveProperties() {
		OutputStream fos = null;
		try {
			synchronized (properties) {
				URL url = getClass().getClassLoader().getResource(
					location + "properties.properties");
				fos = new BufferedOutputStream(new FileOutputStream(url.getPath()));
				properties.store(fos, null);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * @param key
	 * 
	 * @return traženu sliku
	 */
	public ImageIcon getIcon(String key) {
		try {
			ImageIcon icon = icons.get(key);
			if (icon == null) {
				icon = loadIcon(key);
			}
			return icon;
		} catch (Exception ex) {
			System.err.println("No icon: " + key);
			ex.printStackTrace();
			return null;
		}
	}

	private ImageIcon loadIcon(String key) throws IOException {
		String name = getBundle().getString(key);
		URL url = getClass().getClassLoader().getResource(location + "icons/" + name);
		Image image = ImageIO.read(url);
		if (image != null) {
			ImageIcon icon = new ImageIcon(image);
			icons.put(key, icon);
			return icon;
		}
		return null;
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public void setProperty(String key, String value) {
		this.properties.setProperty(key, value);
	}

	/**
	 * Kreira string od templejta koji se nalazi pod imenom <i>patternName</i>,
	 * i ubacuje odgovarajuće argumente na odgovarajuća mesta u odgovarajućem
	 * formatu. Koristi globalni Locale za odabir svih pod-formata.
	 * 
	 * @param key
	 *            ldentifikator tog paterna u LanguageBundle-u
	 * @param args
	 *            lista argumenata
	 * @return sklopljena poruka
	 */
	public String getString(String key, Object... args) {
		return new MessageFormat(getBundle().getString(key), locale).format(args);
	}

}
