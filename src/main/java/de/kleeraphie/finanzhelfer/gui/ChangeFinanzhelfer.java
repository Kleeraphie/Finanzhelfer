package de.kleeraphie.finanzhelfer.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.kleeraphie.finanzhelfer.config.DataHandler;
import de.kleeraphie.finanzhelfer.finanzhelfer.Finanzhelfer;
import de.kleeraphie.finanzhelfer.main.Main;

public class ChangeFinanzhelfer extends JFrame {

	private static final long serialVersionUID = 4164556038899643742L;

	// TODO: vllt. final
	private Theme theme;
	private int currentPage;
	private int fhAmount = Main.fhm.fhList.size();
	private int pages = (int) Math.ceil((double) fhAmount / 9);
	private GridBagConstraints c;
	private JPanel left, middle, right;
	private JPanel southPanel; // f�r die Btns von buildButtons()
	private Finanzhelfer selected; // TODO: vllt. irgendwie ohne
	private DataHandler dataHandler;
	private Locale loc;

	public ChangeFinanzhelfer() {

		// TODO: BUG: wenn man t (2.) zum standard macht wird erstes zum standard
		// (irgendwo noch mit Namen verglichen?)

		// TODO: BUG: wenn man auf das Info-Feld klickt, dann wird das nicht als Klick
		// auf den Btn registriert

		currentPage = 1;
		c = new GridBagConstraints();
		theme = Main.theme;
		dataHandler = Main.dataHandler;
		loc = Locale.forLanguageTag(dataHandler.getText("language.locale"));

		buildWindow();
		buildCells();
		setFocusToCurrentFH();
		buildButtons();

		setVisible(true);
	}

	private void buildWindow() {
		setTitle(dataHandler.getText("windows.change.title"));
		setSize(900, 650);
		setLocationRelativeTo(Main.window);
		requestFocus();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		getContentPane().setBackground(theme.getBackgroundColor());
	}

	private void buildCells() {
		JLabel currentMoneyLabel, currentInfoLabel; // nur der Name
		JLabel currentName, currentMoney; // die Infos
		JTextArea currentInfo;
		Finanzhelfer currentFH;
		Font fett;
		DecimalFormat moneyFormat;
		DecimalFormatSymbols symbols;
		JPanel cell;
		JButton cellBtn, emptyBtn;
//		ButtonGroup btnGroup;

		// TODO: btns vllt. durch togglebtns mit btnGroup ersetzen

		fett = new Font(new JLabel().getFont().getFontName(), Font.BOLD, new JLabel().getFont().getSize());

		symbols = new DecimalFormatSymbols(loc);

		// TODO: wenn z.B. 5,6�, dann 5,60� ausgeben
		moneyFormat = new DecimalFormat("#,##0.##", symbols);
		moneyFormat.setGroupingSize(3);

		ArrayList<Finanzhelfer> allFHs = Main.fhm.fhList;

		left = new JPanel(new GridLayout(3, 1));
		middle = new JPanel(new GridLayout(3, 1));
		right = new JPanel(new GridLayout(3, 1));

		left.setBackground(theme.getBackgroundColor());
		middle.setBackground(theme.getBackgroundColor());
		right.setBackground(theme.getBackgroundColor());

		// Placeholder so each JPanel is only using one third of the ContentPane
		// TODO: zum funktionieren bringen
		emptyBtn = new JButton();
		emptyBtn.setVisible(false);

		left.add(emptyBtn);
		middle.add(emptyBtn);
		right.add(emptyBtn);

//		btnGroup = new ButtonGroup();

		for (int i = 0; i < 9; i++) {

			// TODO: Zeichen wenn er standard ist und eins wenn er current ist (oder nur das er am Anfang umrahmt ist?)

			if ((9 * (currentPage - 1) + i) >= fhAmount)
				break;// damit nicht 9 auf einer Seite, obwohl es nicht so viele gibt

			cell = new JPanel(new GridBagLayout());
			cell.setBackground(theme.getBackgroundColor());

			cellBtn = new JButton();

			c.anchor = GridBagConstraints.CENTER;

			currentFH = allFHs.get(9 * (currentPage - 1) + i);

			currentMoneyLabel = new JLabel();
			currentInfoLabel = new JLabel();
			currentName = new JLabel();
			currentMoney = new JLabel();

			currentInfo = new JTextArea(2, 10);

			currentInfo.setBackground(theme.getFieldColor());

			// if (currentFH.getInfo() != null)
			currentInfo.setEditable(false);
			currentInfo.setLineWrap(true);
			currentInfo.setWrapStyleWord(true);
			currentInfo.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) {
				}

				@Override
				public void focusGained(FocusEvent e) {
					e.getComponent().getParent().getParent().requestFocus();
				}
			});
			JScrollPane sp = new JScrollPane(currentInfo);
			sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

			currentName.setFont(new Font(fett.getFontName(), fett.getStyle(), 12));
			currentMoneyLabel.setFont(fett);
			currentInfoLabel.setFont(fett);

			currentMoneyLabel.setText(dataHandler.getText("windows.change.labels.money"));
			currentInfoLabel.setText(dataHandler.getText("windows.change.labels.info"));

			currentName.setText(currentFH.getName());
			// TODO: W�hrungssymbol in Config �nderbar
			currentMoney.setText(moneyFormat.format(currentFH.getMoneyLeft()) + " "
					+ dataHandler.getText("currency.symbol1") + " / " + currentFH.getMoney() + " "
					+ dataHandler.getText("currency.symbol1")); // TODO: vllt. zu currency.getSymbol �ndern wie
																// sonst �berall
			currentInfo.setText(currentFH.getInfo());

			c.gridwidth = 2;
			c.gridx = 0;
			c.gridy = 0;
			c.insets = new Insets(0, 0, 0, 0); // TODO: pr�fen welche wirklich n�tig; wenn nur 1, dann nur das
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

			c.gridx = 1;
			c.gridy = 1;

			c.insets.left = 10;

			if (i >= 6)
				c.insets.right = 50;

			cell.add(currentMoney, c);

			c.gridy++;
			cell.add(currentInfo, c);

			cellBtn.add(cell);
			// TODO: UUID durch Nummer des Btns ersetzen da es so unsicher ist
			cellBtn.setName(currentFH.getUUID().toString());
			cellBtn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					selected = getFinanzhelferFromButton(getSelectedBtn());
					reloadSouthernButtons();
//					System.out.println(selected.getUUID());
				}
			});

			// btnGroup.add(cellBtn);

			if (i < 3) // TODO: vllt. durch (int) i/3 (switchcase) k�rzer
				left.add(cellBtn);
			else if (i < 6)
				middle.add(cellBtn);
			else
				right.add(cellBtn);

		}

		add(left, BorderLayout.WEST);
		add(middle, BorderLayout.CENTER);
		add(right, BorderLayout.EAST);

	}

	private void buildButtons() {
		JButton setCurrent, setStandard, pageBefore, nextPage, finish;
		JPanel btnPanel;

		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 20, 0);

		southPanel = new JPanel(new GridBagLayout());
		southPanel.setBackground(theme.getBackgroundColor());

		btnPanel = new JPanel(new FlowLayout());
		btnPanel.setBackground(theme.getBackgroundColor());

		if (selected != null) {
			setCurrent = new JButton(
					String.format(dataHandler.getText("windows.change.buttons.current"), selected.getName()));
			// TODO: Namen �berarbeiten

			setCurrent.setContentAreaFilled(false);
			setCurrent.setOpaque(true);
			setCurrent.setBackground(theme.getButtonColor());

			setCurrent.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Main.fhm.setCurrent(selected);
					dispose();
				}
			});

			if (selected.getUUID() == (Main.fhm.getCurrent().getUUID()))
				setCurrent.setEnabled(false);

			btnPanel.add(setCurrent);
		}

		setStandard = new JButton(dataHandler.getText("windows.change.buttons.standard"));

		setStandard.setContentAreaFilled(false);
		setStandard.setOpaque(true);
		setStandard.setBackground(theme.getButtonColor());

		setStandard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Main.fhm.setStandard(selected);
				reloadSouthernButtons();
				System.out.println("new: " + Main.fhm.getStandard().getUUID());
			}
		});

		if (selected != null && selected.getUUID() == (Main.fhm.getStandard().getUUID()))
			setStandard.setEnabled(false);

		btnPanel.add(setStandard);

		pageBefore = new JButton(dataHandler.getText("windows.change.buttons.pageBefore"));

		pageBefore.setContentAreaFilled(false);
		pageBefore.setOpaque(true);
		pageBefore.setBackground(theme.getButtonColor());

		pageBefore.addActionListener(e -> changePage(-1));

		if (currentPage == 1)
			pageBefore.setEnabled(false);

		btnPanel.add(pageBefore);

		nextPage = new JButton(dataHandler.getText("windows.change.buttons.nextPage"));

		nextPage.setContentAreaFilled(false);
		nextPage.setOpaque(true);
		nextPage.setBackground(theme.getButtonColor());
		nextPage.addActionListener(e -> changePage(1));

		if (currentPage == pages)
			nextPage.setEnabled(false);

		btnPanel.add(nextPage);

		finish = new JButton(dataHandler.getText("windows.change.buttons.finish"));

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

	private void rebuildWindow() {
		getContentPane().removeAll();

		buildCells();
		buildButtons();

		revalidate();
		repaint();
	}

	private void setFocusToCurrentFH() {
		List<JButton> allButtons = new ArrayList<>();

		allButtons.addAll(getAllButtons(left));
		allButtons.addAll(getAllButtons(middle));
		allButtons.addAll(getAllButtons(right));

		// alten Button nicht mehr selecten
		for (int i = 0; i < Main.fhm.fhList.size(); i++) {

				// TODO: vllt. fixen
				if (allButtons.get(i).isSelected()) {
					System.out.println(allButtons.get(i));
					allButtons.get(i).setSelected(false);
				}

		}

		// neuen Button selecten und diesen FH als selected speichern
		for (int i = 0; i < Main.fhm.fhList.size(); i++) {

			System.out.println(allButtons.get(i));

			if (getFinanzhelferFromButton(allButtons.get(i)).isCurrent()) {
				allButtons.get(i).requestFocus();
				selected = getFinanzhelferFromButton(allButtons.get(i));
				break;
			}

		}

	}

	private List<JButton> getAllButtons(final Container c) {
		Component[] comps = c.getComponents();
		List<JButton> compList = new ArrayList<>();
		for (Component comp : comps) {
			if (comp instanceof JButton)
				compList.add((JButton) comp);
			else if (comp instanceof Container)
				compList.addAll(getAllButtons((Container) comp));
		}
		return compList;
	}

	private void reloadSouthernButtons() {
		southPanel.removeAll();
		buildButtons();
		revalidate();
	}

	private Finanzhelfer getFinanzhelferFromButton(JButton btn) {
		List<UUID> allFhIDs = new ArrayList<>();

		Main.fhm.fhList.forEach(fh -> allFhIDs.add(fh.getUUID()));

		if (allFhIDs.contains(UUID.fromString(btn.getName())))
			return Main.fhm.getFinanzhelferByUUID(UUID.fromString(btn.getName()));

		return null;
	}

	private JButton getSelectedBtn() {
		List<JButton> allButtons = new ArrayList<>();

		allButtons.addAll(getAllButtons(left));
		allButtons.addAll(getAllButtons(middle));
		allButtons.addAll(getAllButtons(right));

		for (JButton btn : allButtons)
			if (btn.hasFocus())
				return btn;

		return null;
	}

	// TODO: sowas wie z�hlen welcher btn und wenn pos mit der fhlist gleich ist (s.
	// setFocusToCurrentFH())

}
