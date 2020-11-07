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

//import ChartDirector.ChartViewer;
//import de.kleeraphie.finanzhelfer.charts.donut;
import de.kleeraphie.finanzhelfer.main.Main;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1211837376461487581L;
	private Theme theme;
	private GridBagConstraints c;

	public MainWindow() {
		
		// TODO: alle Fenster gleich groß

		theme = Main.theme;
		
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

		setTitle("Finanzhelfer");
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
		JButton createFH, switchFH, settings, newExpenditure, listExpenditures;

		// TODO: vllt. andere Aufteilung der Btns wenn kein FH vorhanden
		// TODO: taskBar height wieder so wie früher
		// TODO: Symbole für die Buttons erstellen und einfügen
		// TODO: bei list auch Möglichkeit zum Bearbeiten einbauen
		// mit eckigen Pfeil davor oder dahinter um Info noch anzuzeigen

		taskBar.setBackground(theme.getTaskBarColor());

		createFH = new JButton("+");
		createFH.setPreferredSize(new Dimension(50, 50));
		createFH.setToolTipText("Neuen Finanzhelfer erstellen");
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
			switchFH.setToolTipText("Finanzhelfer wechseln");
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

		settings.setToolTipText("Einstellungen");
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

			newExpenditure = new JButton("N");
			newExpenditure.setPreferredSize(new Dimension(50, 50));

			if (Main.fhm.fhList.size() >= 2)
				newExpenditure.setLocation(250, 25);
			else
				newExpenditure.setLocation(175, 25);

			newExpenditure.setToolTipText("Neue Ausgabe hinzufügen");
			newExpenditure.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					new AusgabenAdder();
					System.out.println(Main.fhm.getCurrent());
				}
			});

			newExpenditure.setContentAreaFilled(false);
			newExpenditure.setOpaque(true);
			newExpenditure.setBackground(theme.getButtonColor());
			taskBar.add(newExpenditure);

			listExpenditures = new JButton("L");
			listExpenditures.setPreferredSize(new Dimension(50, 50));

			if (Main.fhm.fhList.size() >= 2)
				listExpenditures.setLocation(325, 25);
			else
				listExpenditures.setLocation(250, 25);

			listExpenditures.setToolTipText("Alle Ausgaben auflisten");
			listExpenditures.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					new AusgabenList();
				}
			});

			listExpenditures.setContentAreaFilled(false);
			listExpenditures.setOpaque(true);
			listExpenditures.setBackground(theme.getButtonColor());
			taskBar.add(listExpenditures);

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
