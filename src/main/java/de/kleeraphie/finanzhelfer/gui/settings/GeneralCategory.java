package de.kleeraphie.finanzhelfer.gui.settings;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

import de.kleeraphie.finanzhelfer.config.DataHandler;
import de.kleeraphie.finanzhelfer.gui.Theme;
import de.kleeraphie.finanzhelfer.main.Main;

public class GeneralCategory extends JPanel {

	private static final long serialVersionUID = 2614184109652521435L;

	// TODO: er speichert das falsche Theme (die ID-Findung ist falsch)
	
	private Theme theme;
	private GridBagConstraints c;
	private JComboBox<String> langs;
	private JComboBox<String> currs;
	private JComboBox<String> themes;
	private DataHandler dataHandler;

	public GeneralCategory() {

		theme = Main.theme;
		c = new GridBagConstraints();
		dataHandler = Main.dataHandler;

		buildPanel();
		buildLabels();
		buildEntries();
		buildButtons();
		setVisible(true);

	}

	private void buildPanel() {

		requestFocus();
		setLayout(new GridBagLayout());
		setBackground(theme.getBackgroundColor());

	}

	private void buildLabels() {

		JLabel language, currency, theme;

		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;

		language = new JLabel(dataHandler.getText("categories.general.labels.language"));
		add(language, c);

		c.gridy++;
		currency = new JLabel(dataHandler.getText("categories.general.labels.currency"));
		add(currency, c);

		c.gridy++;
		theme = new JLabel(dataHandler.getText("categories.general.labels.theme"));
		add(theme, c);

	}

	private void buildEntries() {
		ArrayList<String> languages = dataHandler.allLangNames();
		ArrayList<String> themeList = dataHandler.getTextArray("themes");
		HashMap<String, String> currentLangMap;

		c.gridy = 0;
		c.gridx = 1;

		langs = new JComboBox<String>();
		langs.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"); // 30x x (aber 2px zu klein)

		for (String current : languages)
			langs.addItem(current);

		langs.setSelectedItem(dataHandler.getText("language.name")); // aktuelle Sprache

		// TODO: px-Reihe rechts & unten von Pfeilkasten entfernen
		langs.setUI(new BasicComboBoxUI() {
			protected JButton createArrowButton() { // TODO: eigene Farbe für den Pfeil erstellen
				return new BasicArrowButton(BasicArrowButton.SOUTH, null, null, theme.getTaskBarColor(), null);
			}
		});

		add(langs, c);

		c.gridy++;
		currs = new JComboBox<String>();
		currs.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"); // 30x x (aber 2px zu klein)

		for (String current : languages) {
			try {
				currentLangMap = dataHandler.loadLanguageFile(dataHandler.getLangCodeByLangName(current));
				currs.addItem(currentLangMap.get("currency.symbol1") + ", " + currentLangMap.get("currency.symbol2"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		currs.setSelectedItem(dataHandler.getCurrentSymbols());

		// TODO: px-Reihe rechts & unten von Pfeilkasten entfernen
		currs.setUI(new BasicComboBoxUI() {
			protected JButton createArrowButton() { // TODO: eigene Farbe für den Pfeil erstellen
				return new BasicArrowButton(BasicArrowButton.SOUTH, null, null, theme.getTaskBarColor(), null);
			}
		});

		add(currs, c);

		c.gridy++;
		themes = new JComboBox<String>();
		themes.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"); // 30x x (aber 2px zu klein)

		for (String theme : themeList)
			themes.addItem(theme);

		themes.setSelectedItem(themeList.get(dataHandler.getCurrentTheme().getID()));

		// TODO: px-Reihe rechts & unten von Pfeilkasten entfernen
		themes.setUI(new BasicComboBoxUI() {
			protected JButton createArrowButton() { // TODO: eigene Farbe für den Pfeil erstellen
				return new BasicArrowButton(BasicArrowButton.SOUTH, null, null, theme.getTaskBarColor(), null);
			}
		});

		add(themes, c);

	}

	private void buildButtons() {
		JPanel btnPanel;
		JButton save, discard;

		c.gridy = 3;
		c.gridx = 0;
		c.gridwidth = 2;

		btnPanel = new JPanel(new FlowLayout());
		btnPanel.setBackground(theme.getBackgroundColor());

		save = new JButton(dataHandler.getText("categories.general.buttons.save"));

		save.setContentAreaFilled(false);
		save.setOpaque(true);
		save.setBackground(theme.getButtonColor());

		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dataHandler.changeLanguage(dataHandler.getLangCodeByLangName((String) langs.getSelectedItem()));
					dataHandler.changeSymbols((String) currs.getSelectedItem());
					dataHandler.changeTheme((String) themes.getSelectedItem());

					((Window) getRootPane().getParent()).dispose();
					Main.window.refresh();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnPanel.add(save);

		discard = new JButton(dataHandler.getText("categories.general.buttons.discard"));

		discard.setContentAreaFilled(false);
		discard.setOpaque(true);
		discard.setBackground(theme.getButtonColor());

		discard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				((Window) getRootPane().getParent()).dispose();
			}
		});
		btnPanel.add(discard);

		add(btnPanel, c);

	}

}
