package de.kleeraphie.finanzhelfer.config;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import de.kleeraphie.finanzhelfer.finanzhelfer.FinanzhelferManager;
import de.kleeraphie.finanzhelfer.main.Main;

public class DataHandler {

	private Gson gson;
	private File data, config, lang;

	public DataHandler() {
		gson = new Gson();
		data = new File("files/data.json");
		config = new File("files/config.yml");
//		lang = new File()
		// TODO: Dateien später in einen Ordner "files" packen
	}

	public void saveInData(FinanzhelferManager toSave) {

		try {

			if (!data.getParentFile().exists())
				createFilesDir();

			if (!data.exists())
				data.createNewFile();

			FileWriter writer = new FileWriter(data);

			writer.write(gson.toJson(toSave));
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public FinanzhelferManager loadFromDataFile() {
		FinanzhelferManager fhm;

		try {

			if (data.exists()) {
				FileReader reader = new FileReader(data);
				fhm = gson.fromJson(reader, FinanzhelferManager.class);

				return fhm;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public File getDataFile() {
		return data;
	}

	private void createFilesDir() {
		File dir = new File("files");

		dir.mkdir();
	}

	public void changeLanguage(String lang) {
		Main.window.refresh();
	}

	public String getText(String key) { // Text für Sache in richtiger Sprache returnen
		return "";
	}

	public void loadConfig() {

	}

	public void saveInConfig(String key, String value) {

	}

}
