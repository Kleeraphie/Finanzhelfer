package de.kleeraphie.finanzhelfer.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.RingPlot;
import org.jfree.data.general.DefaultPieDataset;

import de.kleeraphie.finanzhelfer.config.DataHandler;
import de.kleeraphie.finanzhelfer.finanzhelfer.Kategorie;
import de.kleeraphie.finanzhelfer.finanzhelfer.Zahlung;
//import ChartDirector.ChartViewer;
//import de.kleeraphie.finanzhelfer.charts.donut;
import de.kleeraphie.finanzhelfer.main.Main;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1211837376461487581L;
	private Theme theme;
	private GridBagConstraints c;
	private DataHandler dataHandler;
	private Dimension screenSize;

	public MainWindow() {

		// TODO: alle Fenster gleich groß
		theme = Main.theme;
		dataHandler = Main.dataHandler;

		c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTHWEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 1;
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // nur für Übungszwecke

		buildWindow();
		buildTaskBar();
		buildGraphs();
		setVisible(true);

		// ChartViewer viewer = new ChartViewer();
		// donut.createChart(viewer, 0);
		// getContentPane().add(viewer);

	}

	private void buildWindow() {

		GridBagLayout layout = new GridBagLayout();

		setTitle(dataHandler.getText("windows.main.title"));
		setExtendedState(MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		requestFocus();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(layout);
		getContentPane().setBackground(theme.getBackgroundColor());

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Main.fhm.save();
			}
		});

		setMinimumSize(new Dimension(800, 450));

	}

	private void buildTaskBar() {
		JPanel taskBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton createFH, switchFH, settings, newTransaction, listTransactions;

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;

		// TODO: vllt. andere Aufteilung der Btns wenn kein FH vorhanden
		// TODO: taskBar height wieder so wie früher
		// TODO: Symbole für die Buttons erstellen und einfügen
		// TODO: bei list auch Möglichkeit zum Bearbeiten einbauen
		// mit eckigen Pfeil davor oder dahinter um Info noch anzuzeigen
		// TODO: ToolTips durch getTextArray("windows.main.buttons") machen
		// TODO: BUG: nach refresh() ist die Taskbar zu tief

		taskBar.setBackground(theme.getTaskBarColor());
		add(taskBar, c);

		createFH = new JButton("+");
		createFH.setPreferredSize(new Dimension(50, 50));
		createFH.setToolTipText(dataHandler.getText("windows.main.buttons.createFH"));
		createFH.addActionListener(e -> new FinanzhelferCreator());
		createFH.setContentAreaFilled(false);
		createFH.setOpaque(true);
		createFH.setBackground(theme.getButtonColor());

		taskBar.add(createFH);

		if (Main.fhm.fhList.size() >= 2) {
			switchFH = new JButton("W");
			switchFH.setPreferredSize(new Dimension(50, 50));
			switchFH.setToolTipText(dataHandler.getText("windows.main.buttons.switchFH"));
			switchFH.addActionListener(e -> new ChangeFinanzhelfer());
			switchFH.setContentAreaFilled(false);
			switchFH.setOpaque(true);
			switchFH.setBackground(theme.getButtonColor());
			taskBar.add(switchFH);
		}

		settings = new JButton();
		settings.setPreferredSize(new Dimension(50, 50));
		settings.setToolTipText(dataHandler.getText("windows.main.buttons.settings"));
		settings.addActionListener(e -> new Settings());

		try {
			Image icon = ImageIO.read(new File("src/main/java/de/kleeraphie/finanzhelfer/graphics/settings.png"));
			settings.setIcon(new ImageIcon(icon));
		} catch (IOException e) {
			e.printStackTrace();
		}

		settings.setContentAreaFilled(false);
		settings.setOpaque(true);
		settings.setBackground(theme.getButtonColor());

		taskBar.add(settings);

		if (Main.fhm.getCurrent() == null)
			return;

		newTransaction = new JButton("N");
		newTransaction.setPreferredSize(new Dimension(50, 50));
		newTransaction.setToolTipText(dataHandler.getText("windows.main.buttons.newTransaction"));
		newTransaction.addActionListener(e -> new AusgabenAdder());
		newTransaction.setContentAreaFilled(false);
		newTransaction.setOpaque(true);
		newTransaction.setBackground(theme.getButtonColor());
		taskBar.add(newTransaction);

		listTransactions = new JButton("L");
		listTransactions.setPreferredSize(new Dimension(50, 50));
		listTransactions.setToolTipText(dataHandler.getText("windows.main.buttons.listTransactions"));
		listTransactions.addActionListener(e -> new TransactionList());
		listTransactions.setContentAreaFilled(false);
		listTransactions.setOpaque(true);
		listTransactions.setBackground(theme.getButtonColor());
		taskBar.add(listTransactions);

	}

	private void buildGraphs() {
		JPanel minorGraphs;
		ChartPanel result;
		DefaultPieDataset<String> dataset;
		double used;
		GridBagConstraints cGraphs;
		JLabel name;

		c.gridwidth = 1;
		c.insets = new Insets(100, 100, 0, 200);
		
		cGraphs = new GridBagConstraints();

		// Main graph
		dataset = new DefaultPieDataset<>();

		dataset.setValue("Übrig", Main.fhm.getCurrent().getMoneyLeft());

		used = 0d;

		for (Kategorie c : Main.fhm.getCurrent().getCategories()) {
			for (Zahlung p : c.getPayments()) {
				used += p.getCost();
			}
		}
		result = createRingChart(dataset, used);
		result.setPreferredSize(new Dimension(1000, 800));
		
		add(result, c);

		// Minor graphs
		minorGraphs = new JPanel(new GridBagLayout());
		minorGraphs.setBackground(theme.getBackgroundColor());
		// TODO: mit JGridBagLayout machen, um Namen drunter zu machen
		JScrollPane scrollPanel = new JScrollPane(minorGraphs);
		scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//      scrollPane.setBounds(0, 0, 300, 200); TODO: nötig?
		scrollPanel.setPreferredSize(new Dimension(600, 450)); // TODO: Tabelle soll bis nach ganz unten gehen
		
		scrollPanel.setBorder(BorderFactory.createEmptyBorder());

		c.gridx++;
		c.insets = new Insets(100, 0, 0, 0);

		// TODO: funktioniert noch nicht
//		//set gridheight of c to the number of rows so it can use the whole screen
//				int rows = layout.getLayoutDimensions()[1].length;
//				c.gridheight = rows;
		
		add(scrollPanel, c);

		cGraphs.gridx = 0;
		cGraphs.gridy = 0;
		
		for (Kategorie cat : Main.fhm.getCurrent().getCategories()) {
			used = 0d;

			name = new JLabel(cat.getName(), SwingConstants.CENTER);
			name.setPreferredSize(new Dimension(300, 20));
			// TODO: Text soll ein paar Pixel nach unten
			
			dataset = new DefaultPieDataset<>();
			dataset.setValue("Übrig", cat.getMoneyLeft());

			for (Zahlung p : cat.getPayments())
				used += p.getCost();

			result = createRingChart(dataset, used);
			result.setPreferredSize(new Dimension(300, 200));

			int top = cGraphs.gridy == 0 ? 2 : 0;
			int left = cGraphs.gridx == 0 ? 2 : 0;

			name.setBorder(BorderFactory.createMatteBorder(top, left, 0, 2, theme.getTaskBarColor()));
			result.setBorder(BorderFactory.createMatteBorder(0, left, 2, 2, theme.getTaskBarColor()));
			
			minorGraphs.add(name, cGraphs);
			cGraphs.gridy++;
			minorGraphs.add(result, cGraphs);
			cGraphs.gridy--;
			
			// go to the next grid
			cGraphs.gridx = (++cGraphs.gridx) % 3;
			if (cGraphs.gridx == 0)
				cGraphs.gridy += 2;
			
		}

	}

	private ChartPanel createRingChart(DefaultPieDataset<String> dataset, double used) {
		JFreeChart chart;
		RingPlot plot;
		ChartPanel result;
		dataset.setValue("Verbraucht", Math.abs(used));

		chart = ChartFactory.createRingChart(null, dataset, false, true, false);
		plot = (RingPlot) chart.getPlot();

		plot.setShadowPaint(Color.BLACK);
		plot.setSimpleLabels(true);

		plot.setLabelBackgroundPaint(theme.getBackgroundColor());
		plot.setBackgroundPaint(theme.getBackgroundColor());
		plot.setLabelOutlinePaint(null);
		plot.setLabelShadowPaint(null);

		plot.setSectionDepth(0.50);
		plot.setSectionOutlinesVisible(false);
		plot.setSeparatorsVisible(false);

		plot.setIgnoreZeroValues(true);

		plot.setNoDataMessage("No data available");
		plot.setSectionDepth(0.08);
		plot.setCircular(true);
		plot.setLabelGap(0.50);

		plot.setOutlineVisible(false);

		result = new ChartPanel(chart);
		return result;
	}

	public void refresh() {

		Main.dataHandler = new DataHandler();
		dataHandler = Main.dataHandler;

		Main.theme = dataHandler.getThemeByID(Integer.parseInt(dataHandler.getFromConfig("current_theme")));
		theme = Main.theme;

		getContentPane().removeAll();

		// TODO: Bug: Fenster nicht mehr maximiert
		buildTaskBar();
		buildGraphs();

		revalidate();
		repaint();

	}

}
