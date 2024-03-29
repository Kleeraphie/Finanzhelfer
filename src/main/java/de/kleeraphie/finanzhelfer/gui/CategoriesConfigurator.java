package de.kleeraphie.finanzhelfer.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import de.kleeraphie.finanzhelfer.config.DataHandler;
import de.kleeraphie.finanzhelfer.finanzhelfer.Kategorie;
import de.kleeraphie.finanzhelfer.main.Main;

public class CategoriesConfigurator extends JFrame {

	private static final long serialVersionUID = 2410388183366385549L;

	private final int spartenAmount = Main.fhm.creating.getCategories().length;
	private final int pages = (int) Math.ceil((double) spartenAmount / 8);
	private int currentPage;
	private ArrayList<JTextField> spartenName = new ArrayList<>();
	private ArrayList<JTextField> spartenMoney = new ArrayList<>();
	// ArrayLists speichern die eingegebene Wert f�r jede Kategorie
	private GridBagConstraints c;
	private Theme theme;
	private DataHandler dataHandler;
	private Locale loc;

	// TODO: Fertigstellen-Btn auf gleiche H�he wie Weiter-Btn auf Seite davor (?)
	// TODO: build-Fkt: Text & Size nicht nach if sondern vorher(s. buildTextFields)

	public CategoriesConfigurator() {

		currentPage = 1;
		c = new GridBagConstraints();
		theme = Main.theme;
		dataHandler = Main.dataHandler;
		loc = Locale.forLanguageTag(dataHandler.getText("language.locale"));

		buildWindow();
		buildCells();
		buildButtons();

	}

	private void buildWindow() {

		setTitle(dataHandler.getText("windows.config.title"));
		setSize(900, 650);
		setLocationRelativeTo(Main.window);
		requestFocus();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
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

	private void buildCells() {
		JPanel left, right, cell;
		JLabel currentNumberLabel, currentNameLabel, currentMoneyLabel;
		JTextField currentName;
		JFormattedTextField currentMoney;

		// f�r currentMoney
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(loc);

		// TODO: In Einstellungen W�hrung �nderbar
		Currency currency = Currency.getInstance(loc);
		String symbol = currency.getSymbol(); // W�hrungssymbol

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

		// ab hier wieder f�r die Zelle
		left = new JPanel();
		left.setLayout(new GridLayout(4, 1));
		left.setBackground(theme.getBackgroundColor());

		right = new JPanel(); // TODO: erst erstellen wenn mehr als 4 Zellen
		right.setLayout(new GridLayout(4, 1));
		right.setBackground(theme.getBackgroundColor());

		for (int i = 0; i < 8; i++) {

			if ((8 * (currentPage - 1) + i) >= spartenAmount)
				break;// damit er nicht 8 auf eine Seite macht, obwohl man nicht so viel braucht

			cell = new JPanel();
			cell.setLayout(new GridBagLayout());
			cell.setBackground(theme.getBackgroundColor());

			currentNumberLabel = new JLabel(String.format(dataHandler.getText("windows.config.labels.category"),
					(8 * (currentPage - 1) + (i + 1))));
			currentNameLabel = new JLabel(dataHandler.getText("windows.config.labels.name"));
			currentMoneyLabel = new JLabel(dataHandler.getText("windows.config.labels.money"));

			c.anchor = GridBagConstraints.CENTER;
			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy = 0;

			if (i == 0)
				c.insets = new Insets(20, 100, 0, 0);
			else if (i < 4)
				c.insets = new Insets(20, 100, 0, 0);
			else if (i == 4)
				c.insets = new Insets(20, 0, 0, 100);
			else // i > 4
				c.insets = new Insets(20, 0, 0, 100);

			cell.add(currentNumberLabel, c);

			c.anchor = GridBagConstraints.LINE_END;
			c.gridwidth = 1;
			c.insets.top = 10;
			c.insets.right = 0;
			c.gridy++;
			cell.add(currentNameLabel, c);

			c.gridy++;
			cell.add(currentMoneyLabel, c);

			c.gridx = 1;
			c.gridy = 1;
			c.insets.left = 20;

			if (i >= 4)
				c.insets.right = 100;

			currentName = new JTextField(24);
			currentMoney = new JFormattedTextField(salaryFactory);

			currentMoney.setColumns(24);

			currentName.setBackground(theme.getFieldColor());
			currentMoney.setBackground(theme.getFieldColor());

			if (spartenAmount == 0)
				currentMoney.setValue(Main.fhm.creating.getMoney());
			currentMoney.setValue(0);

			cell.add(currentName, c);

			c.gridy++;
			cell.add(currentMoney, c);

			if (spartenName.size() == spartenAmount) { // damit auch spartenMoney
				currentName.setText(spartenName.get((8 * (currentPage - 1) + i)).getText());
				currentMoney.setText(spartenMoney.get((8 * (currentPage - 1) + i)).getText());

				spartenName.remove((8 * (currentPage - 1) + i));
				spartenMoney.remove((8 * (currentPage - 1) + i));
			}

			spartenName.add((8 * (currentPage - 1) + i), currentName);
			spartenMoney.add((8 * (currentPage - 1) + i), currentMoney);
			// Zelle nun fertig

			if (i < 4)
				left.add(cell);
			else
				right.add(cell);

		}

		add(left, BorderLayout.WEST);
		add(right, BorderLayout.EAST);
	}

	private void buildButtons() { // TODO: southPanel einbauen
		JButton finish, nextPage;
		JPanel btnPanel;

		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 0;
		c.insets.bottom = 20;

		btnPanel = new JPanel();
		btnPanel.setLayout(new GridBagLayout());
		btnPanel.setBackground(theme.getBackgroundColor());

		if (currentPage == pages) { // funktioniert auch wenn beide 1 sind (wenn <= 8 Sparten konfiguriert werden)

			finish = new JButton(dataHandler.getText("windows.config.buttons.finish"));
			finish.addActionListener(e -> finish());

			finish.setContentAreaFilled(false);
			finish.setOpaque(true);
			finish.setBackground(theme.getButtonColor());

			btnPanel.add(finish, c);

		} else {

			nextPage = new JButton(dataHandler.getText("windows.config.buttons.nextPage"));
			nextPage.setSize(100, 20);
			nextPage.setLocation(400, 580);
			nextPage.addActionListener(e -> changePage(1));

			nextPage.setContentAreaFilled(false);
			nextPage.setOpaque(true);
			nextPage.setBackground(theme.getButtonColor());

			btnPanel.add(nextPage, c);
		}

		add(btnPanel, BorderLayout.SOUTH);
	}

	private void finish() { // vom Fertigstellen-Button ausgef�hrt
		String name;
		double money;
		Kategorie[] categorieList;
		Kategorie current;
		double c, all;

		c = 0d;
		all = Main.fhm.creating.getMoney();

		// Dezimalzahl-Format mit mind. 1 Stelle vor Komma & ggf. 2 Nachkommastellen
		// TODO: wozu sind die ### bei Vorkomma (ggf. max. 3 Vorkommastellen?)
		DecimalFormat moneyFormat = new DecimalFormat("###0.##");

		for (JTextField t : spartenMoney)
			c += Double.parseDouble(t.getText().replace(".", "")
											   .replace(',', '.')
											   .replace('�', ' '));

		if (c != all) {
			String message;

			if (c < all)
				message = String.format(dataHandler.getText("windows.config.dialogues.notAllMoney"),
						moneyFormat.format(c), dataHandler.getText("currency.symbol1"), moneyFormat.format(all),
						dataHandler.getText("currency.symbol1")); // TODO: auch auf currency.getSymbol umschreiben
			else // c > all
				message = String.format(dataHandler.getText("windows.config.dialogues.toMuchMoney"),
						moneyFormat.format(c), dataHandler.getText("currency.symbol1"), moneyFormat.format(all),
						dataHandler.getText("currency.symbol1")); // TODO: auch auf currency.getSymbol umschreiben

			JOptionPane.showMessageDialog(this, message, dataHandler.getText("windows.config.dialogues.error"),
					JOptionPane.WARNING_MESSAGE);

			// CategoriesConfigurator von vorne aber mit allen Werten bereits eingetragen
			changePage(-(currentPage - 1)); // damit man auf Seite 1 ist
			return;
		}

		categorieList = Main.fhm.creating.getCategories().clone();

		for (int i = 0; i < spartenAmount; i++) {
			name = spartenName.get(i).getText();
			money = Double.parseDouble(spartenMoney.get(i).getText().replace(".", "")
																	.replace(',', '.')
																	.replace('�', ' '));

			current = new Kategorie();
			current.setName(name);
			current.setTotalMoney(money);
			current.setMoneyLeft(money);

			categorieList[i] = current;
		}

		Main.fhm.creating.setCategories(categorieList);
		Main.fhm.finish();

		dispose();
	}

	private void changePage(int add) {
		currentPage += add;
		rebuildWindow();
	}

	private void rebuildWindow() {
		getContentPane().removeAll();

		buildCells();
		buildButtons();

		revalidate();
		repaint();
	}

}
