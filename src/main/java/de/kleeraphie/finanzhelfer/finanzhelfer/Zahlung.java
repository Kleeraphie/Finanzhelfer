package de.kleeraphie.finanzhelfer.finanzhelfer;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Zahlung {

	private String name, info;
	private double cost;
	private LocalDateTime creation;

	public Zahlung(String name, String info, double cost) {
		this.name = name;
		this.info = info;
		this.cost = cost;

		creation = LocalDateTime.now(ZoneId.systemDefault());
	}

	public Zahlung(String name, double cost) {
		this(name, null, cost);
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

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public LocalDateTime getCreationDate() {
		return creation;
	}

	@Override
	public String toString() {
		// TODO: Zahlungen == 0 verbieten
		if (cost > 0)
			return "Einnahme[name:" + name + "; info:" + info + "; cost:" + cost + "; creation:" + creation + "]";
		else
			return "Ausgabe[name:" + name + "; info:" + info + "; cost:" + cost + "; creation:" + creation + "]";
	}

}