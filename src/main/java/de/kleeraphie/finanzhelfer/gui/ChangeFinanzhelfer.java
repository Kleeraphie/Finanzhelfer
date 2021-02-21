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
	private JPanel southPanel; // für die Btns von buildButtons()
	private Finanzhelfer selected; // TODO: vllt. irgendwie ohne

	public ChangeFinanzhelfer() {

		// TODO: BUG: wenn man t (2.) zum standard macht wird erstes zum standard
		// (irgendwo noch mit Namen verglichen?)

		currentPage = 1;
		c = new GridBagConstraints();
		theme = Main.theme;

		buildWindow();
		buildCells();
		setFocusToCurrentFH();
		buildButtons();

		setVisible(true);
	}

	private void buildWindow() {
		setTitle("Finanzhelfer wechseln");
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

		symbols = new DecimalFormatSymbols(Locale.GERMANY);

		// TODO: wenn z.B. 5,6€, dann 5,60€ ausgeben
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

			// TODO: Zeichen wenn er standard ist und eins wenn er current ist (oder nur das
			// er am Anfang umrahmt ist?)

			if ((9 * (currentPage - 1) + i) < fhAmount) { // damit nicht 9 auf einer Seite, obwohl es nicht so
															// viele gibt

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

				currentMoneyLabel.setText("Geld:");
				currentInfoLabel.setText("Info:");

				currentName.setText(currentFH.getName());
				// TODO: Währungssymbol in Config änderbar
				currentMoney.setText(String.valueOf(moneyFormat.format(currentFH.getMoneyLeft())) + " € / "
						+ currentFH.getMoney() + " €");
				currentInfo.setText(currentFH.getInfo());

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
						System.out.println(selected.getUUID());
					}
				});

				// btnGroup.add(cellBtn);

				if (i < 3) // TODO: vllt. durch (int) i/3 (switchcase) kürzer
					left.add(cellBtn);

				else if (i < 6)
					middle.add(cellBtn);

				else
					right.add(cellBtn);

			} else
				break;

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

		setCurrent = new JButton("Zu \"" + selected.getName() + "\" wechseln"); // TODO: Namen überarbeiten

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

		btnPanel.add(setCurrent);

		setStandard = new JButton("Als Standard setzen");

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

		if (selected.getUUID() == (Main.fhm.getStandard().getUUID()))
			setStandard.setEnabled(false);

		btnPanel.add(setStandard);

		pageBefore = new JButton("Seite zurück");

		pageBefore.setContentAreaFilled(false);
		pageBefore.setOpaque(true);
		pageBefore.setBackground(theme.getButtonColor());

		pageBefore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pageBefore();
			}
		});

		if (currentPage == 1)
			pageBefore.setEnabled(false);

		btnPanel.add(pageBefore);

		nextPage = new JButton("Nächste Seite");

		nextPage.setContentAreaFilled(false);
		nextPage.setOpaque(true);
		nextPage.setBackground(theme.getButtonColor());

		nextPage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nextPage();
			}
		});

		if (currentPage == pages)
			nextPage.setEnabled(false);

		btnPanel.add(nextPage);

		finish = new JButton("OK");

		finish.setContentAreaFilled(false);
		finish.setOpaque(true);
		finish.setBackground(theme.getButtonColor());

		finish.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnPanel.add(finish);

		southPanel.add(btnPanel, c);
		add(southPanel, BorderLayout.SOUTH);
	}

	private void nextPage() {
		currentPage++;

		getContentPane().removeAll();

		revalidate();
		repaint();

		buildCells();
		buildButtons();
	}

	private void pageBefore() {
		currentPage--;

		getContentPane().removeAll();

		revalidate();
		repaint();

		buildCells();
		buildButtons();
	}

	private void setFocusToCurrentFH() {
		List<JButton> allButtons = new ArrayList<JButton>();

		allButtons.addAll(getAllButtons(left));
		allButtons.addAll(getAllButtons(middle));
		allButtons.addAll(getAllButtons(right));

		for (int i = 0; i < Main.fhm.fhList.size(); i++) {

			if (i < 3) { // Klammern müssen da sonst else zum if darunter gehört (glaube ich)
				// TODO: vllt. fixen
				if (((JButton) left.getComponent(i)).isSelected()) {
					System.out.println(left.getComponent(i));
					((JButton) left.getComponent(i)).setSelected(false);
				}

			} else if (i < 6) {
				if (((JButton) middle.getComponent(i - 3)).isSelected())
					((JButton) middle.getComponent(i - 3)).setSelected(false);

			} else // i < 9
			if (((JButton) right.getComponent(i - 6)).isSelected())
				((JButton) right.getComponent(i - 6)).setSelected(false);

		}

		for (int i = 0; i < Main.fhm.fhList.size(); i++) {

			if (i < 3) {
				if (getFinanzhelferFromButton((JButton) left.getComponent(i)).isCurrent()) {
					left.getComponent(i).requestFocus();
					selected = getFinanzhelferFromButton((JButton) left.getComponent(i));
					break;
				}
			} else if (i < 6) {
				if (getFinanzhelferFromButton((JButton) middle.getComponent(i - 3)).isCurrent()) {
					middle.getComponent(i - 3).requestFocus();
					selected = getFinanzhelferFromButton((JButton) middle.getComponent(i - 3));
					break;
				}
			} else { // i < 9
				if (getFinanzhelferFromButton((JButton) right.getComponent(i - 6)).isCurrent()) {
					right.getComponent(i - 6).requestFocus();
					selected = getFinanzhelferFromButton((JButton) right.getComponent(i - 6));
					break;
				}
			}

		}

	}

	private List<JButton> getAllButtons(final Container c) {
		Component[] comps = c.getComponents();
		List<JButton> compList = new ArrayList<JButton>();
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
		List<UUID> allFhIDs = new ArrayList<UUID>();

		for (Finanzhelfer current : Main.fhm.fhList)
			allFhIDs.add(current.getUUID());

		if (allFhIDs.contains(UUID.fromString(btn.getName())))
			return Main.fhm.getFinanzhelferByUUID(UUID.fromString(btn.getName()));

		return null;
	}

	private JButton getSelectedBtn() {
		List<JButton> allButtons = new ArrayList<JButton>();

		allButtons.addAll(getAllButtons(left));
		allButtons.addAll(getAllButtons(middle));
		allButtons.addAll(getAllButtons(right));

		for (JButton btn : allButtons)
			if (btn.hasFocus())
				return btn;

		return null;
	}

	// TODO: sowas wie zählen welcher btn und wenn pos mit der fhlist gleich ist (s.
	// setFocusToCurrentFH())

}
