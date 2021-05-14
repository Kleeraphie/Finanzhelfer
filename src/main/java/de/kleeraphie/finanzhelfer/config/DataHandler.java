package de.kleeraphie.finanzhelfer.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

import de.kleeraphie.finanzhelfer.finanzhelfer.FinanzhelferManager;
import de.kleeraphie.finanzhelfer.gui.Theme;

public class DataHandler {

	private Gson gson;
	private File data, config, langDir;
	private HashMap<String, String> currentLangTexts;

	// TODO: Mehrzahl bei Währung beachten

	public DataHandler() {
		gson = new Gson();
		data = new File("files/data.json");
		config = new File("files/config.yml");
		langDir = new File("files/languages");

		currentLangTexts = loadLanguageFile(get("current_lang"));
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

	public String get(String path) {

		try {

			BufferedReader bfr = new BufferedReader(new FileReader(config));
			String line;
			String[] texts;

			while ((line = bfr.readLine()) != null) {

				if (line.startsWith(path)) {

					texts = line.split(":");
					bfr.close();

					return texts[1].trim();
				}

				inputBuffer.append(line);
				inputBuffer.append('\n');

			}

			bfr.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;

	}

	private void set(String path, String toSet) {

		try {
			BufferedReader bfr = new BufferedReader(new FileReader(config));
			StringBuffer inputBuffer = new StringBuffer();
			String line;
			String[] texts;

			while ((line = bfr.readLine()) != null) {

				if (line.startsWith(path)) {

					texts = line.split(":");
					line = texts[0] + ": " + toSet;
				}

			}

			bfr.close();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;

	}

	// TODO: Texte für Symbols & Theme sprachenunabhängig speichern

	private void setCurrentSymbols(String newSymbols) {

		try {
			BufferedReader bfr = new BufferedReader(new FileReader(config));
			StringBuffer inputBuffer = new StringBuffer();
			String line;
			String[] texts;

			while ((line = bfr.readLine()) != null) {

				if (line.startsWith("current_symbols:")) {

					texts = line.split(":");
					line = texts[0] + ": " + newSymbols;

				}

				inputBuffer.append(line);
				inputBuffer.append('\n');

			}

			bfr.close();

			FileOutputStream fileOut = new FileOutputStream("files/config.yml");
			fileOut.write(inputBuffer.toString().getBytes());
			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getCurrentSymbols() {

		try {

			BufferedReader bfr = new BufferedReader(new FileReader(config));
			String line;
			String[] texts;

			while ((line = bfr.readLine()) != null) {

				if (line.startsWith("current_symbols:")) {

					texts = line.split(":");
					bfr.close();
					return texts[1].trim();

				}

			}

			bfr.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void setCurrentTheme(String newTheme) {

		try {
			BufferedReader bfr = new BufferedReader(new FileReader(config));
			StringBuffer inputBuffer = new StringBuffer();
			String line;
			String[] texts;
			int theme = 0;
			ArrayList<String> themes;

			themes = getTextArray("themes");

			for (int i = 0; i < themes.size(); i++) {
				if (themes.get(i).equals(newTheme)) {
					theme = i;
					break;
				}
			}

			while ((line = bfr.readLine()) != null) {

				if (line.startsWith("current_theme:")) {

					texts = line.split(":");
					line = texts[0] + ": " + theme;

				}

				inputBuffer.append(line);
				inputBuffer.append('\n');

			}

			bfr.close();

			FileOutputStream fileOut = new FileOutputStream("files/config.yml");
			fileOut.write(inputBuffer.toString().getBytes());
			fileOut.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Theme getCurrentTheme() {
		int theme;

		try {

			BufferedReader bfr = new BufferedReader(new FileReader(config));
			String line;
			String[] texts;

			while ((line = bfr.readLine()) != null) {

				if (line.startsWith("current_theme:")) {

					bfr.close();

					texts = line.split(":");
					theme = Integer.parseInt(texts[1].trim());

					return getThemeByID(theme);
				}

			}

			bfr.close();

		} catch (Exception e) {
			e.printStackTrace();
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
		set("current_lang", lang);
	}

	public void changeSymbols(String symbols) {
		set("current_symbols", symbols);
	}

	public void changeTheme(String newTheme) {
		ArrayList<String> themes = getTextArray("themes");
		int theme = 0;
		
		for (int i = 0; i < themes.size(); i++) {
			if (themes.get(i).equals(newTheme)) {
				theme = i;
				break;
			}
		}
		
		set("current_theme", String.valueOf(theme));
	}

	public String getText(String key) { // Text für Sache in richtiger Sprache returnen
		return currentLangTexts.get(key);
	}

	public ArrayList<String> getTextArray(String keyBegin) {
		ArrayList<String> result = new ArrayList<>();

		for (String current : currentLangTexts.keySet()) {
			if (current.startsWith(keyBegin) && !current.equalsIgnoreCase(keyBegin))
				result.add(0, getText(current)); // 0, da sonst die Reihenfolge falschrum ist als in der lang-Datei
		}

		return result;
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

			result.add(loadLanguageFile(currentLangCode).get("language.name"));

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

				texts = line.split(":", 2);

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
					result.put(path, replace(texts[1]));

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

//	benutzt um in result den text zu packen um mehrzeilige Nachrichten zu machen -> hat nicht funktioniert
	private String replace(String string) {

		string = string.replaceAll("\\\\n", System.lineSeparator()); // \\\\n = \\n = \n; wenn nur replace() dann \\n

		return string;
	}

	public String getLangCodeByLangName(String name) throws FileNotFoundException {
		HashMap<String, String> current;

		for (File lang : langDir.listFiles()) {
			current = loadLanguageFile(lang.getName().replace(".yml", ""));

			if (current.get("language.name").equals(name))
				return lang.getName().replace(".yml", "");

		}

		throw new FileNotFoundException("Corresponding file not found for: " + name);

	}

	public Theme getThemeByID(int theme) {

		for (Theme current : Theme.values()) {
			if (current.getID() == theme)
				return current;
		}

		return null;
	}

}
