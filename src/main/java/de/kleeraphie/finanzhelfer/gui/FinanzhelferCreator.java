package de.kleeraphie.finanzhelfer.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import de.kleeraphie.finanzhelfer.config.DataHandler;
import de.kleeraphie.finanzhelfer.finanzhelfer.FinanzhelferManager;
import de.kleeraphie.finanzhelfer.finanzhelfer.Kategorie;
import de.kleeraphie.finanzhelfer.main.Main;

public class FinanzhelferCreator extends JFrame {

	private static final long serialVersionUID = 8129578661521034757L;
	private FinanzhelferManager fhm;
	private JTextField name, kategorien;
	private JFormattedTextField money;
	private ArrayList<JTextField> fields;
	private GridBagConstraints c;
	private Theme theme;
	private DataHandler dataHandler;
	private Locale loc;

	public FinanzhelferCreator() {

		theme = Main.theme;
		dataHandler = Main.dataHandler;
		loc = Locale.forLanguageTag(dataHandler.getText("language.locale"));

		JOptionPane.setDefaultLocale(Locale.GERMANY);

		// TODO: Werte werden nicht gespeichert aus vorheriger session wenn man nicht
		// bei CategoriesConfigurator war
		if (Main.fhm.creating != null) {
			// TODO: einzelnen Werte der Kategorien oder von dem davor gespeichert
			// & dann wieder eingetragen werden
			// TODO: Text zentrieren
			int input = JOptionPane.showConfirmDialog(Main.window,
					dataHandler.getText("windows.creating.dialogues.notFinishedExists"));

			switch (input) {
			case JOptionPane.YES_OPTION:
				if (Main.fhm.creating.getCategories() != null && Main.fhm.creating.getCategories().length > 0) {
					dispose();
					new CategoriesConfigurator();
					return;
				} else {

				}

			case JOptionPane.NO_OPTION: // Nein
				break;

			case JOptionPane.CANCEL_OPTION: // Abbrechen
				return;

			default: // wenn man auf das Kreuz drückt
				return;
			}

		}

		fields = new ArrayList<>();

		c = new GridBagConstraints();

		buildWindow();
		buildLabels();
		buildTextFields();
		buildButtons();

		fhm = Main.fhm;

		fhm.newFinanzhelfer();

	}
	private void buildWindow() {

		setTitle(dataHandler.getText("windows.creating.title"));
		setSize(800, 600);
		setLocationRelativeTo(Main.window);
		requestFocus();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new GridBagLayout());
		getContentPane().setBackground(theme.getBackgroundColor());
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Main.fhm.abortCreating();
			}
		});

		setMinimumSize(new Dimension(800, 450));

		setVisible(true);

	}

	private void buildLabels() {
		JLabel name, money, categories;

		c.insets = new Insets(50, 100, 0, 75);
		c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 1;
		c.weighty = 1;

		c.gridy = 0;
		name = new JLabel(dataHandler.getText("windows.creating.labels.name"));
		add(name, c);

		c.gridy++;
		money = new JLabel(dataHandler.getText("windows.creating.labels.money"));
		add(money, c);

		c.gridy++;
		categories = new JLabel(dataHandler.getText("windows.creating.labels.categories"));
		add(categories, c);
	}

	private void buildTextFields() {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(loc);

		// TODO: Ränder um Fields entfernen oder schwarz machen (vllt. besser)

		// TODO: In Einstellungen Währung änderbar
		Currency currency = Currency.getInstance(loc);
		String symbol = currency.getSymbol(); // Währungssymbol

		// TODO: rausfinden, wozu die einzelnen Formatter da sind
		// create the formatters, default, display, edit
		NumberFormatter defaultFormatter = new NumberFormatter(new DecimalFormat("#.##"));
		NumberFormatter displayFormatter = new NumberFormatter(new DecimalFormat("#,##0.## " + symbol, symbols));
		NumberFormatter editFormatter = new NumberFormatter(new DecimalFormat("#.##"));
		// set their value classes
		defaultFormatter.setValueClass(Double.class);
		displayFormatter.setValueClass(Double.class);
		editFormatter.setValueClass(Double.class);
		// create and set the DefaultFormatterFactory
		DefaultFormatterFactory salaryFactory = new DefaultFormatterFactory(defaultFormatter, displayFormatter,
				editFormatter);

		c.anchor = GridBagConstraints.CENTER;

		if (fields.isEmpty()) {

			name = new JTextField(24);
			name.setBackground(theme.getFieldColor());
			fields.add(name);

			c.gridx = 1;
			c.gridy = 0;

			add(name, c);

			money = new JFormattedTextField(salaryFactory);
			money.setColumns(24);
			money.setFocusLostBehavior(JFormattedTextField.COMMIT);
			money.setValue(0);
			money.setBackground(theme.getFieldColor());
			fields.add(money);

			c.gridy++;
			add(money, c);

			kategorien = new JTextField(24);
			kategorien.setBackground(theme.getFieldColor());
			fields.add(kategorien);

			c.gridy++;
			add(kategorien, c);

		} else // durch WarnMessage wieder hier
			fields.forEach(this::add);

	}

	// TODO: Abbrechen hinzufügen
	private void buildButtons() {
		JButton nextPage;

		nextPage = new JButton(dataHandler.getText("windows.creating.buttons.nextPage"));
		nextPage.addActionListener(e -> nextPage());

		nextPage.setContentAreaFilled(false);
		nextPage.setOpaque(true);
		nextPage.setBackground(theme.getButtonColor());

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(0, 0, 0, 0);

		add(nextPage, c);
	}

	private void nextPage() {
		String message;
		Double moneyValue;
		
		message = null;
		moneyValue = Double.parseDouble(money.getText().replace(".", "").replace(',', '.').replace('€', ' '));

		// Checking if any JTextField is empty
		if (name.getText().equals("") && money.getText().equals("") && kategorien.getText().equals(""))
			message = dataHandler.getText("windows.creating.dialogues.allEmpty");
		else if (name.getText().equals("") && money.getText().equals(""))
			message = dataHandler.getText("windows.creating.dialogues.nameAndMoneyEmpty");
		else if (money.getText().equals("") && kategorien.getText().equals(""))
			message = dataHandler.getText("windows.creating.dialogues.moneyAndCategoriesEmpty");
		else if (name.getText().equals("") && kategorien.getText().equals(""))
			message = dataHandler.getText("windows.creating.dialogues.nameAndCategoriesEmpty");
		else if (name.getText().equals(""))
			message = dataHandler.getText("windows.creating.dialogues.nameEmpty");
		else if (money.getText().equals(""))
			message = dataHandler.getText("windows.creating.dialogues.moneyEmpty");
		else if (kategorien.getText().equals(""))
			message = dataHandler.getText("windows.creating.dialogues.categoriesEmpty");
		// Checking if JTextField for Sparten contains an unnatural number
		else if (Double.parseDouble(kategorien.getText().replace(',', '.')) % 1 != 0d)
			message = dataHandler.getText("windows.creating.dialogues.notNatural");
		else if (Double.parseDouble(kategorien.getText()) <= 0d)
			message = dataHandler.getText("windows.creating.dialogues.notPositive");
		else if (moneyValue < 0d)
			message = dataHandler.getText("windows.creating.dialogues.moneyNegative");
		// kommt zwar Fehlermeldung in der Konsole, aber egal

		// wenn eine Nachricht vorliegt
		if (message != null) {
			JOptionPane.showMessageDialog(this, message, dataHandler.getText("windows.creating.dialogues.error"),
					JOptionPane.WARNING_MESSAGE);

			// CategoriesConfigurator von vorne aber mit allen Werten bereits eingetragen
			getContentPane().removeAll();

			buildLabels();
			buildTextFields();
			buildButtons();

			if (name.getText().equals(""))
				name.requestFocusInWindow();
			else if (money.getText().equals(""))
				money.requestFocusInWindow();
			else if (moneyValue < 0d)
				money.requestFocusInWindow();
			else if (kategorien.getText().equals(""))
				kategorien.requestFocusInWindow();
			// Checking if JTextField for Sparten contains an unnatural number
			else if (Double.parseDouble(kategorien.getText().replace(',', '.')) % 1 != 0d)
				kategorien.requestFocusInWindow();
			else if (Double.parseDouble(kategorien.getText().replace(',', '.')) <= 0d)
				kategorien.requestFocusInWindow();
			
			revalidate();
			return;
		}

		fhm.creating.setName(name.getText());
		fhm.creating.setMoney(moneyValue);
		fhm.creating.setMoneyLeft(fhm.creating.getMoney());
		fhm.creating.setCategories(new Kategorie[Integer.parseInt(kategorien.getText())]);

		dispose();

		new CategoriesConfigurator();
	}

}
