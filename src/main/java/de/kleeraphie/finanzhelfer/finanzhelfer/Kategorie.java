package de.kleeraphie.finanzhelfer.finanzhelfer;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import de.kleeraphie.finanzhelfer.main.Main;

public class Kategorie {

	private String name, info;
	private double totalMoney, moneyLeft;
	private ArrayList<Zahlung> payments;
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

		this.payments = new ArrayList<>();
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
		moneyLeft = totalMoney;
		payments.forEach(payment -> moneyLeft += payment.getCost());
	}

	public ArrayList<Zahlung> getPayments() {
		return payments;
	}

	public void setPayments(ArrayList<Zahlung> payment) {
		this.payments = payment;
	}

	public void addPayment(String name, String info, double cost) {
		payments.add(new Zahlung(name, info, cost));
		this.moneyLeft += cost;
		Main.fhm.getCurrent().updateMoneyLeft();
	}

	public void addPayment(String name, double cost) {
		addPayment(name, null, cost);
	}

	public void addPayment(Zahlung payment) {
		addPayment(payment.getName(), payment.getInfo(), payment.getCost());
	}

	public ArrayList<Dauerauftrag> getStandingOrders() {
		return standingOrders;
	}

	public void setStandingOrders(ArrayList<Dauerauftrag> standingOrders) {
		this.standingOrders = standingOrders;
	}

	public void addStandingOrder(Zahlung payment, int delay, ChronoUnit unit) {
		standingOrders.add(new Dauerauftrag(payment, delay, unit));
		this.moneyLeft += payment.getCost();
		Main.fhm.getCurrent().updateMoneyLeft();
	}

	@Override
	public String toString() {

		StringBuilder paymentsToString;

		if (payments.isEmpty())
			paymentsToString = new StringBuilder("[]");
		else {

			paymentsToString = new StringBuilder("[");

			for (Zahlung current : payments) {
				paymentsToString.append(current.toString()).append("; ");
			}

			paymentsToString = new StringBuilder(paymentsToString.substring(0, paymentsToString.length() - 2)); // letztes "; " entfernen
			paymentsToString.append("]");
		}

		return "Kategorie[name:" + name + "; info:" + info + "; totalMoney:" + totalMoney + "; moneyLeft:" + moneyLeft
				+ "; payments: " + paymentsToString + "]";
	}

}
