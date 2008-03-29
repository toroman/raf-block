package edu.raf.flowchart.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Singleton klasa koja čuva sve opcije vezane za aplikaciju uopšteno, ali i globalcu Locale
 * instancu, i jezik. Po kreiranju čita podatke o jeziku, pa čita i sve ostalo. Pravo učitavanje se
 * dešava tek kada se prvi put zatraži instanca klase. Što će se verovatno desiti kada se bude
 * kreirao prozor, zbog title-a ili pozicije na ekranu ili šta ja znam.
 * <p>
 * Ako nam ne treba lazy čitanje ima i preloadGlobalResources() metodu.
 * <p>
 * Locale-u i LanguageBundle-u se može pristupiti samo preko ne-static metoda za pristup. Na primer,
 * da bi pronašao tekst za labelu <i>Djoksi</i>, linija glasi
 * <i>GlobalResources.getInstance().getLanguageBundle().getString("DjoksiLabel")</i>
 * 
 * @author Boca
 */

@SuppressWarnings("serial")
public class GlobalProperties extends Properties {
	private static volatile GlobalProperties GLOBAL_PROPERTIES = null;
	private static volatile Locale APP_LOCALE = null;
	private static volatile ResourceBundle LANGUAGE_BUNDLE = null;

	private GlobalProperties() {
		loadProperties();
		APP_LOCALE = GlobalPropertiesHelper.makeLocaleFromString(getProperty("currentLocale"));
		LANGUAGE_BUNDLE = ResourceBundle.getBundle("languages/LocalMessagesBundle", APP_LOCALE);
	}

	/**
	 * Pri pozivu ove metode se otpočinje učitavanje svega ovoga. Služi da se izbegne lazy
	 * učitavanje, ako bude potrebe.
	 */

	public void preloadGlobalProperties() {
		getInstance();
	}

	/**
	 * Glavna singleton metoda. Otpočinje lazy učitavanje, ako treba.
	 * 
	 * @return instancu GlobalProperties klase
	 */

	public static GlobalProperties getInstance() {
		if (GLOBAL_PROPERTIES == null)
			synchronized (GlobalProperties.class) {
				if (GLOBAL_PROPERTIES == null)
					GLOBAL_PROPERTIES = new GlobalProperties();
			}
		return GLOBAL_PROPERTIES;
	}

	/**
	 * Snima promene properties-a.
	 */

	public void saveProperties() {
		try {
			super.store(new FileOutputStream(DEFAULT_PROPERTIES_FILE), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public final Locale getLocale() {
		return APP_LOCALE;
	}

	public final ResourceBundle getLanguageBundle() {
		return LANGUAGE_BUNDLE;
	}

	/**
	 * Čita properties iz fajla. Nema veze sa locale-om i jezicima, ova metoda čita samo podatke
	 * koji se mogu menjati u toku rada programa. Locale i jezik se inicijalizuju prvi put, i ne
	 * promenjivi su.
	 */

	public void loadProperties() {
		try {
			super.load(new FileInputStream(DEFAULT_PROPERTIES_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Path do glavnog properties fajla.
	 */

	public static final File DEFAULT_PROPERTIES_FILE = new File("res/properties.properties");
}
