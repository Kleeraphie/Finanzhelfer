package de.kleeraphie.finanzhelfer.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import de.kleeraphie.finanzhelfer.finanzhelfer.FinanzhelferManager;
import de.kleeraphie.finanzhelfer.finanzhelfer.Kategorie;
import de.kleeraphie.finanzhelfer.main.Main;

public class FinanzhelferCreator extends JFrame {

	private static final long serialVersionUID = 8129578661521034757L;
	private FinanzhelferManager fhm;
	private JTextField name, kategrien;
	private JFormattedTextField money;
	private ArrayList<JTextField> fields;
	private GridBagConstraints c;
	private Theme theme;

	public FinanzhelferCreator() {
		
		theme = Main.theme;

		// TODO: Werte werden nicht gespeichert aus vorheriger session wenn man nicht
		// bei CategoriesConfigurator war
		if (Main.fhm.creating != null) {
			// TODO: einzelnen Werte der Kategorien oder von dem davor gespeichert
			// & dann wieder eingetragen werden
			int input = JOptionPane.showConfirmDialog(Main.window,
					"Ein neuer Finanzhelfer wurde nicht fertig erstellt. Wollen Sie diesen weiter bearbeiten?");

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

			default:
				break;
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

		setTitle("Neuen Finanzhelfer erstellen");
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

		name = new JLabel("Name:");

		c.gridx = 0;
		c.gridy = 0;

		add(name, c);

		money = new JLabel("Gehalt:");

		c.gridx = 0;
		c.gridy = 1;

		add(money, c);

		categories = new JLabel("Anzahl an Kategorien:");

		c.gridx = 0;
		c.gridy = 2;

		add(categories, c);
	}

	private void buildTextFields() {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMANY);

		//TODO: Ränder um Fields entfernen oder schwarz machen (vlt. besser)
		
		// TODO: In Einstellungen Währung änderbar
		Currency currency = Currency.getInstance(Locale.GERMANY);
		String symbol = currency.getSymbol(); // Währungssymbol

		// TODO: rausfinden, wozu die einzelenen Formatter da sind
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

			kategrien = new JTextField(24);
			kategrien.setBackground(theme.getFieldColor());
			fields.add(kategrien);
			
			c.gridy++;
			add(kategrien, c);

		} else { // durch WarnMessage wieder hier
			for (JTextField field : fields)
				add(field);
		}

	}

	private void buildButtons() {
		JButton nextPage;

		nextPage = new JButton("Weiter");
		nextPage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				nextPage();
			}
		});
		
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

		String message = null;

		// Checking if any JTextField is empty
		if (name.getText().equals("") && money.getText().equals("") && kategrien.getText().equals(""))
			message = "In keinem der Felder ist kein Wert eingetragen. \nAlle Felder benötigen einen Wert!";
		else if (name.getText().equals("") && money.getText().equals(""))
			message = "In den Feldern \"Name\" & \"Gehalt\" ist kein Wert eingetragen. \nDiese Felder benötigen einen Wert!";
		else if (money.getText().equals("") && kategrien.getText().equals(""))
			message = "In den Feldern \"Gehalt\" & \"Anzahl der Sparten\" ist kein Wert eingetragen. \nDiese Felder benötigen einen Wert!";
		else if (name.getText().equals("") && kategrien.getText().equals(""))
			message = "In den Feldern \"Name\" & \"Anzahl der Sparten\" ist kein Wert eingetragen. \nDiese Felder benötigen einen Wert!";
		else if (name.getText().equals(""))
			message = "In dem Feld \"Name\" ist kein Wert eingetragen. \nDieses Feld benötigt einen Wert!";
		else if (money.getText().equals(""))
			message = "In dem Feld \"Gehalt\" ist kein Wert eingetragen. \nDieses Feld benötigt einen Wert!";
		else if (kategrien.getText().equals(""))
			message = "In dem Feld \"Anzahl der Sparten\" ist kein Wert eingetragen. \nDieses Feld benötigt einen Wert ( > 0)!";
		// Checking if JTextField for Sparten contains an unnatural number
		else if (Double.parseDouble(kategrien.getText().replace(',', '.')) % 1 != 0d)
			message = "Das Feld \"Anzahl der Sparten\" muss eine natürliche Zahl beinhalten!";
		else if (Double.parseDouble(kategrien.getText()) <= 0d)
			message = "In dem Feld \"Anzahl der Sparten\" muss ein Wert > 0 eingetragen werden!";

		// wenn eine Nachricht vorliegt
		if (message != null) {
			JOptionPane.showMessageDialog(this, message, "Fehler!", JOptionPane.WARNING_MESSAGE);

			// CategoriesConfigurator von vorne aber mit allen Werten bereits eingetragen
			getContentPane().removeAll();

			buildLabels();
			buildTextFields();
			buildButtons();

			if (name.getText().equals(""))
				name.requestFocusInWindow();
			else if (money.getText().equals(""))
				money.requestFocusInWindow();
			else if (kategrien.getText().equals(""))
				kategrien.requestFocusInWindow();
			// Checking if JTextField for Sparten contains an unnatural number
			else if (Double.parseDouble(kategrien.getText().replace(',', '.')) % 1 != 0d)
				kategrien.requestFocusInWindow();
			else if (Double.parseDouble(kategrien.getText()) <= 0d)
				kategrien.requestFocusInWindow();
			// TODO: Button funktioniert nicht mehr
			return;
		}

		fhm.creating.setName(name.getText());
		fhm.creating.setMoney(Double.parseDouble(money.getText().replace(".", "").replace(',', '.').replace('€', ' ')));
		fhm.creating.setMoneyLeft(fhm.creating.getMoney());
		fhm.creating.setCategories(new Kategorie[Integer.parseInt(kategrien.getText())]);

		dispose();

		new CategoriesConfigurator();
	}

}
