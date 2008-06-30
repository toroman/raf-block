package edu.raf.gef.app;

import java.awt.Image;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import edu.raf.gef.Main;
import edu.raf.gef.app.exceptions.GefException;

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
public class Resources implements IResources {
	private static Locale locale = null;

	public static Locale getLocale() {
		return locale;
	}

	private Properties properties;
	private ResourceBundle bundle;
	private HashMap<String, ImageIcon> icons;

	protected final String location;

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
		initProperties();
	}

	public Resources(Package rootPackage) {
		this(rootPackage.getName().replace('.', File.separatorChar) + File.separatorChar);
	}

	/**
	 * Loads properties / settings.
	 */
	private void initProperties() {
		properties = new Properties();
		InputStream fis = null;
		try {
			fis = new FileInputStream(getPropertiesFile());
			properties.load(fis);
		} catch (IOException ex) {
			log.log(Level.SEVERE, "Couldn't load properties!", ex);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException ioe) {
				}
			}
		}
	}

	protected File getPropertiesFile() throws IOException {
		File prop = null;
		if (location.length() == 0) {
			prop = new File(Main.config, "gef.config");
		} else {
			prop = new File(Main.config, location.replace(File.separatorChar, '.') + "config");
		}
		if (!prop.exists())
			prop.createNewFile();
		return prop;
	}

	public ResourceBundle getBundle() {
		if (bundle == null) {
			bundle = ResourceBundle.getBundle(getBundlePath(), locale);
		}
		return bundle;
	}

	protected String getBundlePath() {
		return location + "locales" + File.separator + "LocalMessagesBundle";
	}

	/**
	 * Snima promene properties-a.
	 */
	public void saveProperties() {
		// log.log(Level.INFO, "Saving properties");
		OutputStream fos = null;
		try {
			synchronized (properties) {
				fos = new BufferedOutputStream(new FileOutputStream(getPropertiesFile()));
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.raf.gef.app.IResources#getIcon(java.lang.String)
	 */
	public ImageIcon getIcon(String key) {
		try {
			ImageIcon icon = icons.get(key);
			if (icon == null) {
				icon = loadIcon(key);
			}
			return icon;
		} catch (Exception ex) {
			return null;
		}
	}

	private ImageIcon loadIcon(String key) throws IOException {
		String name;
		try {
			name = getBundle().getString(key);
		} catch (MissingResourceException ex) {
			return null;
		}
		if (name == null)
			name = key;
		URL url = getClass().getClassLoader().getResource(location + "icons/" + name);
		Image image = ImageIO.read(url);
		if (image != null) {
			ImageIcon icon = new ImageIcon(image);
			icons.put(key, icon);
			return icon;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.raf.gef.app.IResources#getProperty(java.lang.String)
	 */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public void setProperty(String key, String value) {
		this.properties.setProperty(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.raf.gef.app.IResources#getString(java.lang.String,
	 *      java.lang.Object)
	 */
	public String getString(String key, Object... args) {
		return new MessageFormat(getBundle().getString(key), locale).format(args);
	}

	/**
	 * First read the value of the <code>key</code> from the properties file,
	 * then try to return InputStream of the resource from returned path.
	 * <p>
	 * Throws exceptions if resource cannot be found.
	 * 
	 * @param key
	 * @param args
	 * @return
	 * @throws GefException
	 *             If key couldn't be read from the bundle.
	 */
	public InputStream getResource(String key, Object... args) throws GefException {
		String resource = getString(key, args);
		if (resource == null)
			throw new GefException("No resource under name: " + key);
		InputStream is = getClass().getClassLoader().getResourceAsStream(resource);
		if (is == null)
			throw new NullPointerException();
		return is;
	}

	private static final Logger log = Logger.getLogger(Resources.class.getName());
}
