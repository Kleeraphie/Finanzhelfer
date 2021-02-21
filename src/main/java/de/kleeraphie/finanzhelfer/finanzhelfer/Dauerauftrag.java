package de.kleeraphie.finanzhelfer.finanzhelfer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class Dauerauftrag {

	// TODO: Eine Oberklasse für Einnahmen und Ausgaben machen und diese dann hier
	// nehmen
	// dann kann man noch eins davon übergeben (implements oder extends?)

	private Ausgabe expenditure;
	private int delay;
	private LocalDateTime creation, timer;
	private ChronoUnit unit;

	public Dauerauftrag(Ausgabe expenditure, int delay, ChronoUnit unit) {
		this.expenditure = expenditure;
		this.delay = delay;
		this.unit = unit;

		this.creation = LocalDateTime.now(ZoneId.systemDefault());

		this.timer = LocalDateTime.now().plus(delay, unit);
	}

	public void addDelay() {
		timer = timer.plus(delay, unit);
	}

	public boolean hasHappened() {
		return ChronoUnit.NANOS.between(timer, LocalDateTime.now()) >= 0 ? true : false;
	}

	public Ausgabe getExpenditure() {
		return expenditure;
	}

	public void setExpenditure(Ausgabe expenditure) {
		this.expenditure = expenditure;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public ChronoUnit getUnit() {
		return unit;
	}

	public void setUnit(ChronoUnit unit) {
		this.unit = unit;
	}

	public LocalDateTime getTimer() {
		return timer;
	}

	public void setTimer(LocalDateTime timer) {
		this.timer = timer;
	}

	public LocalDateTime getCreation() {
		return creation;
	}
	
	@Override
	public String toString() {
		return "Dauerauftrag[name:" + expenditure.getName() + "; info:" + expenditure.getInfo() + "; cost:" + expenditure.getCost() + "]";
	}

}
