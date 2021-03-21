package de.kleeraphie.finanzhelfer.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

import de.kleeraphie.finanzhelfer.finanzhelfer.FinanzhelferManager;
import de.kleeraphie.finanzhelfer.main.Main;

public class DataHandler {

	private Gson gson;
	private File data, config, langDir;
	private HashMap<String, String> currentLangTexts;

	public DataHandler() {
		gson = new Gson();
		data = new File("files/data.json");
		config = new File("files/config.yml");
		langDir = new File("files/languages");

		currentLangTexts = loadLanguageFile(getCurrentLanguage());
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

	public void setCurrentLanguage(String newLanguage) {

		try {
			BufferedReader bfr = new BufferedReader(new FileReader(config));
			StringBuffer inputBuffer = new StringBuffer();
			String line;
			String[] texts;

			while ((line = bfr.readLine()) != null) {

				if (line.startsWith("current_lang:")) {

					texts = line.split(":");
					line = texts[0] + ": " + newLanguage;
					inputBuffer.append(line);
					inputBuffer.append('\n');

				}

			}

			bfr.close();

			FileOutputStream fileOut = new FileOutputStream("files/config.yml");
			fileOut.write(inputBuffer.toString().getBytes());
			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getCurrentLanguage() {

		try {

			BufferedReader bfr = new BufferedReader(new FileReader(config));
			String line;
			String[] texts;

			while ((line = bfr.readLine()) != null) {

				if (line.startsWith("current_lang:")) {

					texts = line.split(":");
					bfr.close();
					return texts[1].trim();

				}

			}

			bfr.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;

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
		return currentLangTexts.get(key);
	}

	public void loadConfig() {

	}

	public void saveInConfig(String key, String value) {

	}

	public ArrayList<String> allLangNames() {
		ArrayList<String> result = new ArrayList<>(); // TODO: vllt. durch Array ersetzen
		String currentLangCode;

		for (File currentLang : langDir.listFiles()) {

			currentLangCode = currentLang.getName().replace(".yml", "");

			result.add(loadLanguageFile(currentLangCode).get("language." + currentLangCode));

		}

		return result;
	}

	public HashMap<String, String> loadLanguageFile(String code) {
		HashMap<String, String> result = new HashMap<>();
		File langFile = new File(langDir.getAbsolutePath() + "/" + code + ".yml");

		try {
			BufferedReader bfr = new BufferedReader(new FileReader(langFile, StandardCharsets.UTF_8));
			String line;
			String path = "";
			int oldTabs = 0, newTabs = 0;
			String[] texts;

			while ((line = bfr.readLine()) != null) {

				if (line.trim().equals("")) { // letzten Teil ersetzen kommt erst, wenn es was neues gibt
					continue;
				}

				texts = line.split(":");

				if (texts.length == 2)
					texts[1] = texts[1].trim(); // da am Anfang ein Leerzeichen ist

				newTabs = countFrontTabs(line);
				texts[0] = texts[0].trim();

				if (newTabs <= oldTabs) { // letzten Teil von path neu machen

					while (newTabs <= oldTabs) {
						path = path.replace(path.substring(indexOfLastDot(path)), "");
						oldTabs--;
					}
				}

				if (!path.equals(""))
					path += "." + texts[0];
				else
					path = texts[0];

				if (texts.length > 1) // == 2
					result.put(path, texts[1]);

				oldTabs = newTabs;

			}

			bfr.close();
			return result;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private int countFrontTabs(String word) {
		int result = 0;
		String wordPart = "";

		for (char c : word.toCharArray()) {

			wordPart += c;

			if (wordPart.equals("    ")) {
				result++;
				wordPart = "";
			} else if (wordPart.chars().allMatch(Character::isLetter))
				break;

		}

		return result;
	}

	private int indexOfLastDot(String word) {
		int index = 0;

		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == '.')
				index = i;
		}

		return index;
	}

}
