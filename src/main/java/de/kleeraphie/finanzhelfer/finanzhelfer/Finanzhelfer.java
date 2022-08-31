package de.kleeraphie.finanzhelfer.finanzhelfer;

import java.util.UUID;

public class Finanzhelfer {

	private String name, info;
	private double money, moneyLeft;
	private Kategorie[] categories;
	private boolean current, standard;
	private final UUID UUID;
	// money = Gehalt; moneyLeft = übriges Geld (nach ausgaben)

	public Finanzhelfer(String name, String info, double money, Kategorie[] categories, boolean current, boolean standard) {
		this.name = name;
		this.info = info;
		this.money = money;
		this.moneyLeft = money;
		this.categories = categories;
		this.current = current;
		this.standard = standard;
		this.UUID = java.util.UUID.randomUUID();
	}

	public Finanzhelfer() {
		this(null, null, 0, null);
	}

	public Finanzhelfer(String name, String info, double money, Kategorie[] categories) {
		this(name, info, money, categories, false, false);
	}

	public Finanzhelfer(String name, double money, Kategorie[] categories) {
		this(name, null, money, categories);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
		updateMoneyLeft();
	}

	public double getMoneyLeft() {
		updateMoneyLeft();
		return moneyLeft;
	}

	public void setMoneyLeft(double moneyLeft) {
		this.moneyLeft = moneyLeft;
	}

	public void updateMoneyLeft() {
		this.moneyLeft = 0d;

		if (categories != null)
			for (Kategorie current : categories)
				this.moneyLeft += current.getMoneyLeft();

	}

	public Kategorie[] getCategories() {
		return categories;
	}

	public Kategorie getCategoryByName(String name) {

		for (Kategorie current : categories) {
			if (current.getName().equals(name))
				return current;
		}

		return null;
	}

	public void setCategories(Kategorie[] categories) {
		this.categories = categories;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public boolean isStandard() {
		return standard;
	}

	public void setStandard(boolean standard) {
		this.standard = standard;
	}

	@Override
	public String toString() {

		String categoriesString;

		// TODO: prüfen ob beide nötig sind
		if (categories == null || categories[0] == null)
			categoriesString = "[]";
		else {

			categoriesString = "[";

			for (Kategorie current : categories) {
				categoriesString += current.toString() + "; ";
			}

			categoriesString = categoriesString.substring(0, categoriesString.length() - 2); // letztes Semikolon und Leerzeichen
																					// entfernen
			categoriesString += "]";
			// TODO: Klammern nur dann, wenn mehr als 1 Kategorie vorhanden ist; dann bei
			// Finanzhelfer auch nur Kategorie statt Sparten schreiben

		}

		return "Finanzhelfer[name:" + name + "; info:" + info + "; Kategorien:" + categoriesString + "; current:" + current
				+ "; standard:" + standard + "]";
	}

	// private String arrayToString(Kategorie[] array) {
	// String result;
	//
	// if (array == null || array[0] == null)
	// result = "[]";
	// else {
	// result = "[";
	//
	// for (Kategorie current : array)
	// result += current.toString() + "; ";
	//
	// result = result.substring(0, result.length() - 2); //letztes Semikolon und
	// Leerzeichen entfernen
	// result += "]";
	// }
	//
	// return result;
	// }
	
	public UUID getUUID() {
		return UUID;
	}

}
