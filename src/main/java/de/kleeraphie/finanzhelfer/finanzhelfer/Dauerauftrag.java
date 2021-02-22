package de.kleeraphie.finanzhelfer.finanzhelfer;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Dauerauftrag {

	private Zahlung payment;
	private int delay;
	private LocalDateTime timer;
	private ChronoUnit unit;

	public Dauerauftrag(Zahlung payment, int delay, ChronoUnit unit) {
		this.payment = payment;
		this.delay = delay;
		this.unit = unit;

		this.timer = LocalDateTime.now().plus(delay, unit);
	}

	public void addDelay() {
		timer = timer.plus(delay, unit);
	}

	public boolean hasHappened() {
		return ChronoUnit.DAYS.between(timer, LocalDateTime.now()) >= 0 ? true : false;
	}

	public Zahlung getPayment() {
		return payment;
	}

	public void setPayment(Zahlung expenditure) {
		this.payment = expenditure;
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
	
	@Override
	public String toString() {
		return "Dauerauftrag[payment:" + payment + "; timer:" + timer + "]";
	}

}