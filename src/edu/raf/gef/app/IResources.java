package edu.raf.gef.app;

import javax.swing.ImageIcon;

public interface IResources {

	/**
	 * @param key
	 * 
	 * @return traženu sliku
	 */
	public abstract ImageIcon getIcon(String key);

	public abstract String getProperty(String key);

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
	public abstract String getString(String key, Object... args);

}