package de.kleeraphie.finanzhelfer.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.kleeraphie.finanzhelfer.main.Main;

public class Settings extends JFrame {
	
	private static final long serialVersionUID = -4853302493411791563L;
	
	private Theme theme;
	private GridBagConstraints c;
	
	public Settings() {
		c = new GridBagConstraints();
		theme = Main.theme;
		
		buildWindow();
		buildCategories();
		buildButtons();
	}

	private void buildWindow() {

		setTitle("Einstellungen");
		setSize(900, 650);
		setLocationRelativeTo(Main.window);
		requestFocus();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		getContentPane().setBackground(theme.getBackgroundColor());

		setVisible(true);
		
	}

	private void buildCategories() {
		JPanel categories;
		JButton general, save;
		
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		
		categories = new JPanel();
		categories.setLayout(new BoxLayout(categories, BoxLayout.Y_AXIS));
		categories.setBackground(theme.getBackgroundColor());
		
		general = new JButton("Allgemein");
		general.setOpaque(true);
		general.setContentAreaFilled(false);
		general.setBackground(theme.getButtonColor());
		categories.add(general);
		
		save = new JButton("Speicheroptionen");
		save.setOpaque(true);
		save.setContentAreaFilled(false);
		save.setBackground(theme.getButtonColor());
		categories.add(save);
		
		
		add(categories, BorderLayout.WEST);
	}

	private void buildButtons() {
		JPanel southPanel, btnPanel;
		JButton save, discard;
		
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0, 0, 20, 0);
		
		southPanel = new JPanel(new GridBagLayout());
		southPanel.setBackground(theme.getBackgroundColor());
		
		btnPanel = new JPanel(new FlowLayout());
		btnPanel.setBackground(theme.getBackgroundColor());
		
		save = new JButton("Speichern");
		save.setOpaque(true);
		save.setContentAreaFilled(false);
		save.setBackground(theme.getButtonColor());
		btnPanel.add(save);
		
		discard = new JButton("Abbrechen");
		discard.setOpaque(true);
		discard.setContentAreaFilled(false);
		discard.setBackground(theme.getButtonColor());
		btnPanel.add(discard);
		
		southPanel.add(btnPanel, c);
		add(southPanel, BorderLayout.SOUTH);
	}

}
