package de.kleeraphie.finanzhelfer.gui.settings;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

public class SaveCategory extends JPanel {

	private static final long serialVersionUID = 2614184109652521435L;

	private Theme theme;
	private GridBagConstraints c;
	private JComboBox<String> saveOpt;
	private DataHandler dataHandler;
	private int tempSaveOpt;

	public SaveCategory() {

		theme = Main.theme;
		c = new GridBagConstraints();
		dataHandler = Main.dataHandler;
		
		tempSaveOpt = Integer.parseInt(dataHandler.getFromConfig("save_option"));
		

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

		JLabel save_opt;

		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;

//		System.out.println(dataHandler.getTextArray("categories.test").get(5));
		save_opt = new JLabel(dataHandler.getText("categories.test.labels.save_option"));
		add(save_opt, c);
		
	}

	private void buildEntries() {
		ArrayList<String> save_opts = dataHandler.getTextArray("categories.test.options");

		c.gridy = 0;
		c.gridx = 1;

		saveOpt = new JComboBox<>();
		saveOpt.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"); // 30x x (aber 2px zu klein)

		save_opts.forEach(option -> saveOpt.addItem(option));

		saveOpt.setSelectedIndex(tempSaveOpt); // akt. Speicheropt.

		// TODO: px-Reihe rechts & unten von Pfeilkasten entfernen
		saveOpt.setUI(new BasicComboBoxUI() {
			protected JButton createArrowButton() { // TODO: eigene Farbe für den Pfeil erstellen
				return new BasicArrowButton(BasicArrowButton.SOUTH, null, null, theme.getTaskBarColor(), null);
			}
		});

		add(saveOpt, c);

	}

	private void buildButtons() {
		JPanel btnPanel;
		JButton save, discard;

		c.gridy = 3;
		c.gridx = 0;
		c.gridwidth = 2;

		btnPanel = new JPanel(new FlowLayout());
		btnPanel.setBackground(theme.getBackgroundColor());

		save = new JButton(dataHandler.getText("categories.test.buttons.save"));

		save.setContentAreaFilled(false);
		save.setOpaque(true);
		save.setBackground(theme.getButtonColor());

		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					dataHandler.changeSaveOption(saveOpt.getSelectedIndex());
					
					// TODO: Dateien woanders speichern
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnPanel.add(save);

		discard = new JButton(dataHandler.getText("categories.test.buttons.discard"));

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
