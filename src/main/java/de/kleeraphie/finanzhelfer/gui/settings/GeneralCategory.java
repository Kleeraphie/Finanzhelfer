package de.kleeraphie.finanzhelfer.gui.settings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

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

	private Theme theme;
	private GridBagConstraints c;
	private JComboBox<String> langs;
	private DataHandler dataHandler;

	public GeneralCategory() {

		theme = Main.theme;
		c = new GridBagConstraints();
		dataHandler = Main.dataHandler;

		buildPanel();
		buildLabels();
		buildEntries();
		setVisible(true);

	}

	private void buildPanel() {

		requestFocus();
		setLayout(new GridBagLayout());
		setBackground(theme.getBackgroundColor());

	}

	private void buildLabels() {

		JLabel language, currency;

		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;

		language = new JLabel(dataHandler.getText("categories.general.labels.language"));
		add(language, c);
		
		c.gridy++;
		currency = new JLabel(dataHandler.getText("categories.general.labels.language"));
		add(currency, c);

	}

	private void buildEntries() {
		ArrayList<String> languages = Main.dataHandler.allLangNames();

		c.gridy = 0;
		c.gridx = 1;

		langs = new JComboBox<String>();
		langs.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"); // 30x x (aber 2px zu klein)

		for (String current : languages)
			langs.addItem(current);

		// TODO: px-Reihe rechts & unten von Pfeilkasten entfernen
		langs.setUI(new BasicComboBoxUI() {
			protected JButton createArrowButton() { // TODO: eigene Farbe für den Pfeil erstellen
				return new BasicArrowButton(BasicArrowButton.SOUTH, null, null, theme.getTaskBarColor(), null);
			}
		});
		
		add(langs, c);

	}

}
