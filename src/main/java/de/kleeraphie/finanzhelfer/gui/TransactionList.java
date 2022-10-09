package de.kleeraphie.finanzhelfer.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

import de.kleeraphie.finanzhelfer.finanzhelfer.Zahlung;
import de.kleeraphie.finanzhelfer.config.DataHandler;
import de.kleeraphie.finanzhelfer.finanzhelfer.Kategorie;
import de.kleeraphie.finanzhelfer.main.Main;

public class TransactionList extends JFrame {

	private static final long serialVersionUID = 8022505422069339749L;

	private int currentPage;
	private int ausgabenAmount = getAusgabenAmount();
	private int pages = (int) Math.ceil((double) ausgabenAmount / 9);
	private Kategorie currentCategorie;
	private GridBagConstraints c;
	private Theme theme;
	private DataHandler dataHandler;
	private Locale loc;

	public TransactionList() {

		// TODO: Zahlung eines Auftrags in eine Zahlung zusammenfassen
		// TODO: Anzahl der Seiten im nächsten Seite Button anzeigen (& zurück Seite)

		currentPage = 1;

		if (pages == 0)
			pages = 1; // damit man bei nur einer Seite nicht auf nächste Seite klicken kann

		c = new GridBagConstraints();
		theme = Main.theme; // TODO: vllt. wie DataHandler nie eine Instanz erstellen
		dataHandler = Main.dataHandler;
		loc = Locale.forLanguageTag(dataHandler.getText("language.locale"));

		buildWindow();
		buildCells();
		buildButtons();
	}

	// @Override
	// public void paint(Graphics g) {
	//
	// int x, y, width, height;
	//
	//
	// super.paint(g);
	//
	// for (Component current : getAllComponents(this)) {
	//
	// x = (int) current.getBounds().getMinX();
	// y = (int) current.getBounds().getMaxY();
	// width = current.getWidth();
	// height = current.getHeight();
	//
	// if (current instanceof JButton)
	// g.setColor(Color.GREEN);
	// if (current instanceof JLabel) {
	// g.setColor(Color.RED);
	// JLabel c = (JLabel) current;
	//
	// if (c.getFont().getStyle() == Font.BOLD && !(c.getText().contains("Info") ||
	// c.getText().contains("Kosten")))
	// g.setColor(Color.BLUE);
	//
	// else if (c.getText().equals("Info:"))
	// g.setColor(Color.MAGENTA);
	//
	// else if (c.getText().equals("Kosten:"))
	// g.setColor(Color.ORANGE);
	//
	// else if (!c.getText().contains("€"))
	// g.setColor(Color.YELLOW);
	// }
	//
	//
	// g.drawRect(x, y, width, height);
	//
	// }
	//
	// }
	//
	// public static List<Component> getAllComponents(final Container c) {
	// Component[] comps = c.getComponents();
	// List<Component> compList = new ArrayList<>();
	// for (Component comp : comps) {
	// compList.add(comp);
	// if (comp instanceof Container)
	// compList.addAll(getAllComponents((Container) comp));
	// }
	// return compList;
	// }

	private void buildWindow() {

		setTitle(dataHandler.getText("windows.list.title"));
		setSize(900, 650);
		setLocationRelativeTo(Main.window);
		requestFocus();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		getContentPane().setBackground(theme.getBackgroundColor());

		UIManager.put("ComboBox.background", new ColorUIResource(theme.getFieldColor()));

		setVisible(true);

	}

	private void buildCells() {
		JLabel currentMoneyLabel, currentInfoLabel, created; // nur der Name
		JLabel textFor, currentName, currentMoney, creationDate; // die Infos
		JTextArea currentInfo;
		JComboBox<String> categories;
		Zahlung currentAusgabe;
		Font fett;
		DecimalFormat moneyFormat;
		DecimalFormatSymbols symbols;
		JPanel top, left, middle, right, cell;

		// TODO: Bug bei dem currentFH nicht geändert wird obwohl in Config anders

		fett = new Font(new JLabel().getFont().getFontName(), Font.BOLD, new JLabel().getFont().getSize());

		symbols = new DecimalFormatSymbols(loc);

		// TODO: wenn z.B. 5,6€, dann 5,60€ ausgeben
		moneyFormat = new DecimalFormat("#,##0.##", symbols);
		moneyFormat.setGroupingSize(3);

		// moneyFormat.setGroupingFormatSymbols(symbols);

		top = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 20));
		top.setBackground(theme.getBackgroundColor());

		textFor = new JLabel();
		textFor.setFont(new Font(fett.getFontName(), Font.BOLD, 20));
		textFor.setText(dataHandler.getText("windows.list.labels.expenditure"));

		top.add(textFor);

		// top.setAlignmentY(20); // TODO: zum funktionieren kriegen top zum rand 50px,
		// zwischen den Komponenten 20px

		Kategorie[] allCategories = Main.fhm.getCurrent().getCategories();
		String[] categorieList = new String[allCategories.length];

		for (int i = 0; i < allCategories.length; i++)
			categorieList[i] = allCategories[i].getName();

		categories = new JComboBox<>(categorieList);
		categories.setSelectedItem(currentCategorie.getName());
		categories.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxxxxxx"); // 24x x

		// TODO: px-Reihe rechts & unten von Pfeilkasten entfernen
		categories.setUI(new BasicComboBoxUI() {
			protected JButton createArrowButton() { // TODO: eigene Farbe für den Pfeil erstellen
				return new BasicArrowButton(BasicArrowButton.SOUTH, null, null, theme.getTaskBarColor(), null);
			}
		});

		categories.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				currentCategorie = Main.fhm.getCurrent().getCategoryByName(categories.getSelectedItem().toString());

				// Werte neu berechnen, damit sie mit den Werten der neuen Kategorie übereinstimmen
				ausgabenAmount = getAusgabenAmount();
				pages = (int) Math.ceil((double) ausgabenAmount / 9);

				rebuildWindow();
			}
		});

		top.add(categories);

		add(top, BorderLayout.NORTH);

		left = new JPanel(new GridLayout(3, 1));
		middle = new JPanel(new GridLayout(3, 1));
		right = new JPanel(new GridLayout(3, 1));

		left.setBackground(theme.getBackgroundColor());
		middle.setBackground(theme.getBackgroundColor());
		right.setBackground(theme.getBackgroundColor());

		for (int i = 0; i < 9; i++) {
			if ((9 * (currentPage - 1) + i) >= ausgabenAmount) break;// damit nur so viele auf der Seite wie nötig sind

			cell = new JPanel(new GridBagLayout());
			cell.setBackground(theme.getBackgroundColor());

			c.anchor = GridBagConstraints.CENTER;

			currentAusgabe = currentCategorie.getPayments().get(9 * (currentPage - 1) + i);

			currentMoneyLabel = new JLabel();
			currentInfoLabel = new JLabel();
			created = new JLabel();

			currentName = new JLabel();
			currentMoney = new JLabel();
			creationDate = new JLabel();

			currentInfo = new JTextArea(2, 10);

			currentInfo.setBackground(theme.getFieldColor());
			currentInfo.setEditable(false);
			currentInfo.setLineWrap(true);
			currentInfo.setWrapStyleWord(true);
			JScrollPane sp = new JScrollPane(currentInfo);
			sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

			currentName.setFont(new Font(fett.getFontName(), fett.getStyle(), 12));
			currentMoneyLabel.setFont(fett);
			currentInfoLabel.setFont(fett);
			created.setFont(fett);

			currentMoneyLabel.setText(dataHandler.getText("windows.list.labels.money"));
			currentInfoLabel.setText(dataHandler.getText("windows.list.labels.info"));
			created.setText(dataHandler.getText("windows.list.labels.date"));

			currentName.setText(currentAusgabe.getName());
			// TODO: Währungssymbol in Config änderbar
			currentMoney.setText(moneyFormat.format(currentAusgabe.getCost()) + " "
					+ dataHandler.getText("currency.symbol1")); // TODO: auch zu currency.getSymbol ändern
			currentInfo.setText(currentAusgabe.getInfo());

			// TODO: Locale aus der Config nehmen
			DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(loc);

			creationDate.setText(currentAusgabe.getCreationDate().format(formatter));

			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy = 0;
			c.insets = new Insets(0, 0, 0, 0); // TODO: prüfen welche wirklich nötig; wenn nur 1, dann nur das
			// TODO: currentName mgl. nicht ganz mittig
			if (i < 3)
				c.insets.left = 50;
			else if (i >= 6)
				c.insets.right = 50;

			cell.add(currentName, c);

			c.anchor = GridBagConstraints.LINE_END;
			c.gridwidth = 1;

			if (i >= 6)
				c.insets.right = 0;

			c.gridy++;
			cell.add(currentMoneyLabel, c);

			c.anchor = GridBagConstraints.NORTHEAST;

			c.gridy++;
			cell.add(currentInfoLabel, c);

			c.gridy++;
			cell.add(created, c);

			c.gridx = 1;
			c.gridy = 1;

			c.insets.left = 10;

			if (i >= 6)
				c.insets.right = 50;

			cell.add(currentMoney, c);

			c.gridy++;
			cell.add(currentInfo, c);

			c.gridy++;
			cell.add(creationDate, c);

			if (i < 3) // TODO: vllt. durch (int) i/3 (switchcase) kürzer
				left.add(cell);
			else if (i < 6)
				middle.add(cell);
			else
				right.add(cell);

		}

		add(left, BorderLayout.WEST);
		add(middle, BorderLayout.CENTER);
		add(right, BorderLayout.EAST);

	}

	private void buildButtons() {
		JButton pageBefore, nextPage, finish;
		JPanel southPanel, btnPanel;

		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 20, 0);

		southPanel = new JPanel(new GridBagLayout());
		southPanel.setBackground(theme.getBackgroundColor());

		btnPanel = new JPanel(new FlowLayout());
		btnPanel.setBackground(theme.getBackgroundColor());

		if (currentPage == 1) {
			pageBefore = new JButton(dataHandler.getText("windows.list.buttons.pageBefore"));

			pageBefore.setContentAreaFilled(false);
			pageBefore.setOpaque(true);
			pageBefore.setBackground(theme.getButtonColor());
			pageBefore.addActionListener(e -> changePage(-1));
			pageBefore.setEnabled(false);

			btnPanel.add(pageBefore);
		}

		if (currentPage == pages) {
			nextPage = new JButton(dataHandler.getText("windows.list.buttons.nextPage"));

			nextPage.setContentAreaFilled(false);
			nextPage.setOpaque(true);
			nextPage.setBackground(theme.getButtonColor());
			nextPage.addActionListener(e -> changePage(1));
			nextPage.setEnabled(false);

			btnPanel.add(nextPage);
		}

		finish = new JButton(dataHandler.getText("windows.list.buttons.finish"));

		finish.setContentAreaFilled(false);
		finish.setOpaque(true);
		finish.setBackground(theme.getButtonColor());
		finish.addActionListener(e -> dispose());
		btnPanel.add(finish);

		southPanel.add(btnPanel, c);
		add(southPanel, BorderLayout.SOUTH);
	}

	private void changePage(int add) {
		currentPage += add;
		rebuildWindow();
	}

	private int getAusgabenAmount() {
		int ausgabenAmount;

		if (currentCategorie == null)
			currentCategorie = Main.fhm.getCurrent().getCategories()[0];

		ausgabenAmount = currentCategorie.getPayments().size();

		return ausgabenAmount;
	}

	private void rebuildWindow() {
		getContentPane().removeAll();

		buildCells();
		buildButtons();

		revalidate();
		repaint();
	}

}
