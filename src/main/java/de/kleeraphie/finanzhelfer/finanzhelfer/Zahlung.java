package de.kleeraphie.finanzhelfer.finanzhelfer;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Zahlung {

	private String name, info;
	private double cost;
	private final LocalDateTime CREATION_DATE;

	public Zahlung(String name, String info, double cost) {
		this.name = name;
		this.info = info;
		this.cost = cost;

		CREATION_DATE = LocalDateTime.now(ZoneId.systemDefault());
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
		return CREATION_DATE;
	}

	@Override
	public String toString() {
		// TODO: Zahlungen == 0 verbieten
		if (cost > 0)
			return "Einnahme[name:" + name + "; info:" + info + "; cost:" + cost + "; creation:" + CREATION_DATE + "]";
		else
			return "Ausgabe[name:" + name + "; info:" + info + "; cost:" + cost + "; creation:" + CREATION_DATE + "]";
	}

}
