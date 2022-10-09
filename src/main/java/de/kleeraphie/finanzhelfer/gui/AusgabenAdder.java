package de.kleeraphie.finanzhelfer.gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import de.kleeraphie.finanzhelfer.config.DataHandler;
import de.kleeraphie.finanzhelfer.finanzhelfer.Kategorie;
import de.kleeraphie.finanzhelfer.finanzhelfer.Zahlung;
import de.kleeraphie.finanzhelfer.main.Main;

public class AusgabenAdder extends JFrame {

	private static final long serialVersionUID = -2761622587237851L;
	private JComboBox<String> categories, timesTypes, unit, types;
	private JTextField name;
	private JFormattedTextField cost;
	private JTextArea info;
	private JButton finish;
	private JPanel howOftenEntries;
	private JLabel howOften;
	private GridBagConstraints c;
	private Theme theme;
	private DataHandler dataHandler;
	private Locale loc;

	public AusgabenAdder() {
		c = new GridBagConstraints();
		theme = Main.theme;
		dataHandler = Main.dataHandler;
		loc = Locale.forLanguageTag(dataHandler.getText("language.locale"));

		buildWindow();
		buildLabels();
		buildEntries();
		buildButtons();

		setVisible(true);
	}

	private void buildWindow() {
		setTitle(dataHandler.getText("windows.add.title"));
		setSize(900, 650); // TODO: restliche Größen aller Fenster entfernen
		setLocationRelativeTo(null);
		requestFocus();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new GridBagLayout());
		getContentPane().setBackground(theme.getBackgroundColor());

		UIManager.put("ComboBox.background", new ColorUIResource(theme.getFieldColor()));
		// TODO: JOptionPane soll sich auch theme anpassen
//		UIManager.put("OptionPane.background", new ColorUIResource(theme.getBackgroundColor()));
//		UIManager.put("pane.background", new ColorUIResource(theme.getBackgroundColor()));
	}

	private void buildLabels() {
		JLabel category, name, info, cost, times, transactionType;

		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;

		category = new JLabel(dataHandler.getText("windows.add.labels.category"));
		add(category, c);

		c.gridy++;
		transactionType = new JLabel(dataHandler.getText("windows.add.labels.transactionType"));
		add(transactionType, c);
		
		c.gridy++;
		name = new JLabel(dataHandler.getText("windows.add.labels.name"));
		add(name, c);

		c.gridy++;
		info = new JLabel(dataHandler.getText("windows.add.labels.info"));
		add(info, c);

		c.gridy++;
		cost = new JLabel(dataHandler.getText("windows.add.labels.cost"));
		add(cost, c);

		c.gridy++;
		times = new JLabel(dataHandler.getText("windows.add.labels.type"));
		add(times, c);

	}

	private void buildEntries() {
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 1;
		c.gridy = 0;

		// TODO: In Einstellungen Währung änderbar
		Currency currency = Currency.getInstance(loc);
		String symbol = currency.getSymbol(); // Währungssymbol
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(loc);

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

		// TODO: Mindestgröße setzen (wenn mgl. ohne px)
		categories = new JComboBox<>();
		categories.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"); // 30x x (aber 2px zu klein)

		for (Kategorie current : Main.fhm.getCurrent().getCategories())
			categories.addItem(current.getName());

		// TODO: px-Reihe rechts & unten von Pfeilkasten entfernen
		categories.setUI(new BasicComboBoxUI() {
			protected JButton createArrowButton() { // TODO: eigene Farbe für den Pfeil erstellen
				return new BasicArrowButton(BasicArrowButton.SOUTH, null, null, theme.getTaskBarColor(), null);
			}
		});

		add(categories, c);
		
		c.gridy++;
		types = new JComboBox<>();
		types.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		
		types.addItem(dataHandler.getText("windows.add.types.expenditure"));
		types.addItem(dataHandler.getText("windows.add.types.earning"));
		
		types.setUI(new BasicComboBoxUI() {
			protected JButton createArrowButton() { // TODO: eigene Farbe für den Pfeil erstellen
				return new BasicArrowButton(BasicArrowButton.SOUTH, null, null, theme.getTaskBarColor(), null);
			}
		});
		
		add(types, c);

		c.gridy++;
		name = new JTextField(24);
		name.setBackground(theme.getFieldColor());
		add(name, c);

		c.gridy++;
		info = new JTextArea(6, 24);
		info.setLineWrap(true);
		info.setWrapStyleWord(true);
		info.setBackground(theme.getFieldColor());
		JScrollPane sp = new JScrollPane();
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		sp.getViewport().add(info);
		sp.setBackground(theme.getTaskBarColor());
		add(sp, c);
		// TODO: bg-Color vom sp ändern

		c.gridy++;
		cost = new JFormattedTextField(salaryFactory);
		cost.setColumns(24);
		cost.setValue(0);
		cost.setBackground(theme.getFieldColor());
		add(cost, c);

		c.gridy++;
		timesTypes = new JComboBox<>();
		timesTypes.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"); // 30x x (aber 2px zu klein)

		timesTypes.addItem(dataHandler.getText("windows.add.types.once"));
		timesTypes.addItem(dataHandler.getText("windows.add.types.order"));

		timesTypes.setUI(new BasicComboBoxUI() {
			protected JButton createArrowButton() { // TODO: eigene Farbe für den Pfeil erstellen
				return new BasicArrowButton(BasicArrowButton.SOUTH, null, null, theme.getTaskBarColor(), null);
			}
		});

		timesTypes.addActionListener(new ActionListener() { // TODO: Funktioniert noch nicht
			public void actionPerformed(ActionEvent e) {

				if (timesTypes.getSelectedItem().toString().equals(dataHandler.getText("windows.add.types.order"))) {
					JLabel all;
					JFormattedTextField times;

					// finish-Button entfernen
					remove(finish);

					c.gridwidth = 1;
					c.gridx = 0; // da nun ein Label kommt
					c.gridy++;

					howOftenEntries = new JPanel(new FlowLayout(FlowLayout.LEFT));
					howOftenEntries.setBackground(theme.getBackgroundColor());

					howOften = new JLabel(dataHandler.getText("windows.add.labels.howOften"));
					add(howOften, c);

					c.gridx++; // da nun ein Entry kommt
					all = new JLabel(dataHandler.getText("windows.add.labels.all"));
					howOftenEntries.add(all);

					NumberFormat format = NumberFormat.getInstance();
					NumberFormatter formatter = new NumberFormatter(format);
					formatter.setValueClass(Integer.class);
					formatter.setMinimum(0);
					formatter.setMaximum(Integer.MAX_VALUE);
					formatter.setAllowsInvalid(false);
					formatter.setCommitsOnValidEdit(true);

					times = new JFormattedTextField(formatter);
					times.setValue(0);
					times.setColumns(2);
					howOftenEntries.add(times);

					unit = new JComboBox<>();

					unit.addItem(dataHandler.getText("windows.add.units.days"));
					unit.addItem(dataHandler.getText("windows.add.units.weeks"));
					unit.addItem(dataHandler.getText("windows.add.units.months"));
					unit.addItem(dataHandler.getText("windows.add.units.years"));

					howOftenEntries.add(unit);

					add(howOftenEntries, c);

					buildButtons(); // finish-Button neu hinzufügen

				} else {

					if (howOften != null && howOftenEntries != null) {
						remove(howOften);
						remove(howOftenEntries);
					}

				}

				revalidate();
				repaint();

			}
		});

		add(timesTypes, c);

	}

	private void buildButtons() {

		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy++;

		finish = new JButton(dataHandler.getText("windows.add.buttons.finish"));

		finish.setContentAreaFilled(false);
		finish.setOpaque(true);
		finish.setBackground(theme.getButtonColor());

		finish.addActionListener(e -> addAusgabe());
		add(finish, c);
	}

	private void addAusgabe() {
		Kategorie currentCategory = Main.fhm.getCurrent().getCategoryByName((String) categories.getSelectedItem());
		double money = Double.parseDouble(cost.getText().replace(".", "").replace(',', '.').replace('€', ' '));
		Zahlung payment = new Zahlung(name.getText(), info.getText(), money);
		String selected = String.valueOf(categories.getSelectedItem());
		Kategorie current = Main.fhm.getCurrent().getCategoryByName(selected);

		if (types.getSelectedItem().toString().equals("Ausgabe"))
			money  *= -1;
		
		if (money > currentCategory.getMoneyLeft()) {
			String message = String.format(dataHandler.getText("windows.add.dialogues.toExpensive"), money, dataHandler.getText("currency.symbol1"), currentCategory.getMoneyLeft(), dataHandler.getText("currency.symbol1"));
			JOptionPane.showMessageDialog(this, message, dataHandler.getText("windows.add.dialogues.error"), JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (timesTypes.getSelectedItem().toString().equals(dataHandler.getText("windows.add.types.order"))) {
			int delay = ((int) ((JFormattedTextField) howOftenEntries.getComponent(1)).getValue());

			if (delay <= 0) {
				String message = String.format(dataHandler.getText("windows.add.dialogues.notPositive"), dataHandler.getText("windows.add.labels.howOften").replace(":", ""));
				JOptionPane.showMessageDialog(this, message, dataHandler.getText("windows.add.dialogues.error"), JOptionPane.WARNING_MESSAGE);
				return;
			}

			switch (unit.getSelectedItem().toString()) {

			case "Tage":
				current.addStandingOrder(payment, delay, ChronoUnit.DAYS);
				break;

			case "Wochen":
				current.addStandingOrder(payment, delay, ChronoUnit.WEEKS);
				break;

			case "Monate":
				current.addStandingOrder(payment, delay, ChronoUnit.MONTHS);
				break;

			case "Jahre":
				current.addStandingOrder(payment, delay, ChronoUnit.YEARS);
				break;
			}

		}

		current.addPayment(payment);

		dispose();
		Main.window.refresh();
	}

}
