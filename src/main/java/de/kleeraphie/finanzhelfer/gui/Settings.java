package de.kleeraphie.finanzhelfer.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import de.kleeraphie.finanzhelfer.main.Main;

public class Settings extends JFrame {
	
	private static final long serialVersionUID = -4853302493411791563L;
	
	private Theme theme;
	
	public Settings() {
		theme = Main.theme;
		
		buildWindow();
		buildCells();
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

	private void buildCells() {
		// TODO Auto-generated method stub
		
	}

	private void buildButtons() {
		// TODO Auto-generated method stub
		
	}

}
