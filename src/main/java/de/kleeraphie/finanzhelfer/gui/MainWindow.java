package de.kleeraphie.finanzhelfer.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.kleeraphie.finanzhelfer.config.DataHandler;
//import ChartDirector.ChartViewer;
//import de.kleeraphie.finanzhelfer.charts.donut;
import de.kleeraphie.finanzhelfer.main.Main;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1211837376461487581L;
	private Theme theme;
	private GridBagConstraints c;
	private DataHandler dataHandler;

	public MainWindow() {
		
		// TODO: alle Fenster gleich groß

		theme = Main.theme;
		dataHandler = Main.dataHandler;
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.NORTHWEST;

		buildWindow();
		setVisible(true);
		buildTaskBar();

		// ChartViewer viewer = new ChartViewer();
		// donut.createChart(viewer, 0);
		// getContentPane().add(viewer);

	}

	private void buildWindow() {

		setTitle(dataHandler.getText("windows.main.title"));
		setExtendedState(MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		requestFocus();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridBagLayout());
		getContentPane().setBackground(theme.getBackgroundColor());

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Main.fhm.save();
			}
		});
		
		setMinimumSize(new Dimension(800,450));

	}

	private void buildTaskBar() {
		JPanel taskBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton createFH, switchFH, settings, newTransaction, listTransactions;

		// TODO: vllt. andere Aufteilung der Btns wenn kein FH vorhanden
		// TODO: taskBar height wieder so wie früher
		// TODO: Symbole für die Buttons erstellen und einfügen
		// TODO: bei list auch Möglichkeit zum Bearbeiten einbauen
		// mit eckigen Pfeil davor oder dahinter um Info noch anzuzeigen

		taskBar.setBackground(theme.getTaskBarColor());

		createFH = new JButton("+");
		createFH.setPreferredSize(new Dimension(50, 50));
		createFH.setToolTipText(dataHandler.getText("windows.main.buttons.createFH"));
		createFH.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new FinanzhelferCreator();
			}
		});

		createFH.setContentAreaFilled(false);
		createFH.setOpaque(true);
		createFH.setBackground(theme.getButtonColor());

		taskBar.add(createFH);

		if (Main.fhm.fhList.size() >= 2) {
			switchFH = new JButton("W");
			switchFH.setPreferredSize(new Dimension(50, 50));
			switchFH.setToolTipText(dataHandler.getText("windows.main.buttons.switchFH"));
			switchFH.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					new ChangeFinanzhelfer();
				}
			});
			switchFH.setContentAreaFilled(false);
			switchFH.setOpaque(true);
			switchFH.setBackground(theme.getButtonColor());
			taskBar.add(switchFH);
		}

		settings = new JButton();
		settings.setPreferredSize(new Dimension(50, 50));

		settings.setToolTipText(dataHandler.getText("windows.main.buttons.settings"));
		settings.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new Settings();
			}
		});

		Image icon;
		try {
			icon = ImageIO.read(new File("src/main/java/de/kleeraphie/finanzhelfer/graphics/settings.png"));

			settings.setIcon(new ImageIcon(icon));

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		settings.setContentAreaFilled(false);
		settings.setOpaque(true);
		settings.setBackground(theme.getButtonColor());

		taskBar.add(settings);

		if (Main.fhm.getStandard() != null) {

			newTransaction = new JButton("N");
			newTransaction.setPreferredSize(new Dimension(50, 50));

			if (Main.fhm.fhList.size() >= 2)
				newTransaction.setLocation(250, 25);
			else
				newTransaction.setLocation(175, 25);

			newTransaction.setToolTipText(dataHandler.getText("windows.main.buttons.newTransaction"));
			newTransaction.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					new AusgabenAdder();
				}
			});

			newTransaction.setContentAreaFilled(false);
			newTransaction.setOpaque(true);
			newTransaction.setBackground(theme.getButtonColor());
			taskBar.add(newTransaction);

			listTransactions = new JButton("L");
			listTransactions.setPreferredSize(new Dimension(50, 50));

			if (Main.fhm.fhList.size() >= 2)
				listTransactions.setLocation(325, 25);
			else
				listTransactions.setLocation(250, 25);

			listTransactions.setToolTipText(dataHandler.getText("windows.main.buttons.listTransactions"));
			listTransactions.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					new AusgabenList();
				}
			});

			listTransactions.setContentAreaFilled(false);
			listTransactions.setOpaque(true);
			listTransactions.setBackground(theme.getButtonColor());
			taskBar.add(listTransactions);

		}

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 1;
		
		add(taskBar, c);

		revalidate();
		repaint();

	}

	public void refresh() {

		getContentPane().removeAll();

		revalidate();
		repaint();

		buildTaskBar();

	}

}
