package de.kleeraphie.finanzhelfer.main;

import javax.swing.UIManager;

import de.kleeraphie.finanzhelfer.config.DataHandler;
import de.kleeraphie.finanzhelfer.finanzhelfer.FinanzhelferManager;
import de.kleeraphie.finanzhelfer.gui.MainWindow;
import de.kleeraphie.finanzhelfer.gui.Theme;

public class Main {

	public static MainWindow window;
	public static FinanzhelferManager fhm;
	public static DataHandler dataHandler;
	public static Theme theme;

	public static void main(String[] args) {

		// Change LookAndFeel for the whole runtime
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		dataHandler = new DataHandler();
		
//		dataHandler.loadConfig();

		// Trying to load fhm from config
		// if fhm is null, then new fhm will be created
		fhm = FinanzhelferManager.load();

		if (fhm == null)
			fhm = new FinanzhelferManager();
		else // damit er das nicht bei einen leeren fhm macht
			fhm.checkRecurrentExpenditures();

		// TODO: später in Config einstellbar und aus ConfigManager mit getter lesen
		theme = Theme.LIGHT_THEME;
		
		window = new MainWindow();

	}

}
