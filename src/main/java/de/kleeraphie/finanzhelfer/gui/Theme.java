package de.kleeraphie.finanzhelfer.gui;

import java.awt.Color;

public enum Theme {
	// TODO: btnColor bei Dark ändern (sieht so aus wie ein Loch)
	LIGHT_THEME("#ffffff", "#e3e3e3", "#f0f0f0", "#f0f0f0"),
	DARK_THEME("#858585", "#6e6e6e", "#858585", "#858585");
	
	private String backgroundColor, taskBarColor, buttonColor, fieldColor;
	
	private Theme(String backgroundColor, String taskBarColor, String buttonColor, String fieldColor) {
		this.backgroundColor = backgroundColor;
		this.taskBarColor = taskBarColor;
		this.buttonColor = buttonColor;
		this.fieldColor = fieldColor;
	}
	
	public String getBackgroundColorString() {
		return backgroundColor;
	}
	
	public String getTaskBarColorString() {
		return taskBarColor;
	}
	
	public String getButtonColorString() {
		return buttonColor;
	}
	
	public String getFieldColorString() {
		return fieldColor;
	}
	
	public Color getBackgroundColor() {
		return Color.decode(backgroundColor);
	}
	
	public Color getTaskBarColor() {
		return Color.decode(taskBarColor);
	}
	
	public Color getButtonColor() {
		return Color.decode(buttonColor);
	}
	
	public Color getFieldColor() {
		return Color.decode(fieldColor);
	}

}
