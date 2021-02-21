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

import de.kleeraphie.finanzhelfer.finanzhelfer.Kategorie;
import de.kleeraphie.finanzhelfer.main.Main;

public class AusgabenAdder extends JFrame {

	private static final long serialVersionUID = -2761622587237851L;
	private JComboBox<String> categories, types, unit;
	private JTextField name;
	private JFormattedTextField cost;
	private JTextArea info;
	private JButton finish;
	private JPanel howOftenEntries;
	private JLabel howOften;
	private GridBagConstraints c;
	private Theme theme;

	public AusgabenAdder() {
		c = new GridBagConstraints();
		theme = Main.theme;

		buildWindow();
		buildLabels();
		buildEntries();
		buildButtons();

		setVisible(true);
	}

	private void buildWindow() {
		setTitle("Neue Ausgabe hinzufügen");
		setSize(900, 650);
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
		JLabel categorie, name, info, cost, type;

		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.weighty = 1;
		c.gridx = 0;
		c.gridy = 0;

		categorie = new JLabel("Kategorie:");
		add(categorie, c);

		c.gridy++;
		name = new JLabel("Name:");
		add(name, c);

		c.gridy++;
		info = new JLabel("Informationen:");
		add(info, c);

		c.gridy++;
		cost = new JLabel("Kosten:");
		add(cost, c);

		c.gridy++;
		type = new JLabel("Typ:");
		add(type, c);

	}

	private void buildEntries() {
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 1;
		c.gridy = 0;

		// TODO: In Einstellungen Währung änderbar
		Currency currency = Currency.getInstance(Locale.GERMANY);
		String symbol = currency.getSymbol(); // Währungssymbol
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMANY);

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
		types = new JComboBox<>();
		types.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"); // 30x x (aber 2px zu klein)

		types.addItem("Einmalige Zahlung");
		types.addItem("Dauerauftrag");

		types.setUI(new BasicComboBoxUI() {
			protected JButton createArrowButton() { // TODO: eigene Farbe für den Pfeil erstellen
				return new BasicArrowButton(BasicArrowButton.SOUTH, null, null, theme.getTaskBarColor(), null);
			}
		});

		types.addActionListener(new ActionListener() { // TODO: Funktioniert noch nicht
			public void actionPerformed(ActionEvent e) {

				if (types.getSelectedItem().toString().equals("Dauerauftrag")) {
					JLabel all;
					JFormattedTextField times;

					// finish-Button entfernen
					remove(finish);

					c.gridwidth = 1;
					c.gridx = 0; // da nun ein Label kommt
					c.gridy++;

					howOftenEntries = new JPanel(new FlowLayout(FlowLayout.LEFT));
					howOftenEntries.setBackground(theme.getBackgroundColor());

					howOften = new JLabel("Wie oft");
					add(howOften, c);

					c.gridx++; // da nun ein Entry kommt
					all = new JLabel("Alle");
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

					unit.addItem("Sekunden");
					unit.addItem("Tage");
					unit.addItem("Wochen");
					unit.addItem("Monate"); // TODO: Timer setzen & dann auch Monate hinzufügen
					unit.addItem("Jahre");

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

		add(types, c);

	}

	private void buildButtons() {

		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy++;

		finish = new JButton("Fertigstellen");

		finish.setContentAreaFilled(false);
		finish.setOpaque(true);
		finish.setBackground(theme.getButtonColor());

		finish.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addAusgabe();
			}
		});
		add(finish, c);
	}

	private void addAusgabe() {
		Kategorie currentCategorie = Main.fhm.getCurrent().getCategorieByName((String) categories.getSelectedItem());
		double money = Double.parseDouble(cost.getText().replace(".", "").replace(',', '.').replace('€', ' '));

		if (money > currentCategorie.getMoneyLeft()) {
			String message = "Diese Ausgabe übersteigt das verbleibende Geld dieser Kategorie! (" + money + " € > "
					+ currentCategorie.getMoneyLeft() + " €)";

			JOptionPane.showMessageDialog(this, message, "Fehler!", JOptionPane.WARNING_MESSAGE);

			return;
		}

		String selected = String.valueOf(categories.getSelectedItem());
		Kategorie current = Main.fhm.getCurrent().getCategorieByName(selected);

		if (types.getSelectedItem().toString().equals("Dauerauftrag")) {
			int delay = ((int) ((JFormattedTextField) howOftenEntries.getComponent(1)).getValue());

			System.out.println(unit.getSelectedItem().toString().equals("Sekunden"));

			switch (unit.getSelectedItem().toString()) {

			case "Sekunden":
				current.addStandingOrder(name.getText(), info.getText(), money, delay, ChronoUnit.SECONDS);
				System.out.println("T");
				break;

			case "Tage":
				current.addStandingOrder(name.getText(), info.getText(), money, delay, ChronoUnit.DAYS);
				break;

			case "Wochen":
				current.addStandingOrder(name.getText(), info.getText(), money, delay, ChronoUnit.WEEKS);
				break;

			case "Monate":
				current.addStandingOrder(name.getText(), info.getText(), money, delay, ChronoUnit.MONTHS);
				break;

			case "Jahre":
				current.addStandingOrder(name.getText(), info.getText(), money, delay, ChronoUnit.YEARS);
				break;
			}

		}

		current.addAusgabe(name.getText(), info.getText(), money);

		dispose();
	}

}
