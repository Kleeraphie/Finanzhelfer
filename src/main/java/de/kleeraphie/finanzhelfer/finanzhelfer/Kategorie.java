package de.kleeraphie.finanzhelfer.finanzhelfer;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import de.kleeraphie.finanzhelfer.main.Main;

public class Kategorie {

	private String name, info;
	private double totalMoney, moneyLeft;
	private ArrayList<Ausgabe> expenditures;
	private ArrayList<Dauerauftrag> standingOrders;
	// TODO: vielleicht FH parent hinzufügen
	
	public Kategorie() {
		this(null, null, 0);
	}
	
	public Kategorie(String name, String info, double money) {
		this.name = name;
		this.info = info;
		this.totalMoney = money;
		this.moneyLeft = this.totalMoney;
		
		this.expenditures = new ArrayList<>();
		this.standingOrders = new ArrayList<>();
	}
	
	public Kategorie(String name, double money) {
		this(name, null, money);
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

	public double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
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
		this.moneyLeft = this.totalMoney;
		
		for (Ausgabe expenditure : this.expenditures)
			this.moneyLeft -= expenditure.getCost();
		
	}

	public ArrayList<Ausgabe> getExpenditures() {
		return expenditures;
	}

	public void setExpenditures(ArrayList<Ausgabe> expenditures) {
		this.expenditures = expenditures;
	}
	
	public void addAusgabe(String name, String info, double cost) {
		expenditures.add(new Ausgabe(name, info, cost));
		this.moneyLeft -= cost;
		Main.fhm.getCurrent().updateMoneyLeft();
	}
	
	public void addAusgabe(String name, double cost) {
		addAusgabe(name, null, cost);
	}
	
	public void addAusgabe(Ausgabe expenditure) {
		addAusgabe(expenditure.getName(), expenditure.getInfo(), expenditure.getCost());
	}
	
	public ArrayList<Dauerauftrag> getStandingOrders() {
		return standingOrders;
	}

	public void setStandingOrders(ArrayList<Dauerauftrag> standingOrders) {
		this.standingOrders = standingOrders;
	}
	
	public void addStandingOrder(String name, String info, double cost, int delay, ChronoUnit unit) {
		System.out.println("oldSize: " + standingOrders.size());
		standingOrders.add(new Dauerauftrag(new Ausgabe(name, info, cost), delay, unit));
		this.moneyLeft -= cost;
		Main.fhm.getCurrent().updateMoneyLeft();
		System.out.println("newSize: " + standingOrders.size());
	}

	@Override
	public String toString() {
		
		String ausgaben;
		
		if (expenditures.isEmpty())
			ausgaben = "[]";
		else {
			
			ausgaben = "[";
			
			for (Ausgabe current : expenditures) {
				ausgaben += current.toString() + "; ";
			}
			
			ausgaben = ausgaben.substring(0, ausgaben.length() - 2); //letztes Semikolon und Leerzeichen entfernen
			ausgaben += "]";
		}
		
		return "Kategorie[name:" + name + "; info:" + info + "; totalMoney:" + totalMoney + "; moneyLeft:" + moneyLeft + "; Ausgaben: " + ausgaben + "]";
	}
	
}
