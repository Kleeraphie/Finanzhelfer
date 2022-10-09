package de.kleeraphie.finanzhelfer.finanzhelfer;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import de.kleeraphie.finanzhelfer.main.Main;

public class FinanzhelferManager {

	public Finanzhelfer creating;
	public ArrayList<Finanzhelfer> fhList;
	private ArrayList<Kategorie> creatingCategoryList;
	// creatingCategoryList ist nur während des Erstellens nötig;
	// wird später den creating übergeben

	public FinanzhelferManager() {
		fhList = new ArrayList<>();
	}

	// TODO: später loadFromFile(config) aufrufen
	// wo liegt die?
	public static FinanzhelferManager load() {
		return Main.dataHandler.loadFromDataFile();
	}

	// TODO: durch die passenden Datei ersetzen
	public static FinanzhelferManager loadFromFile(File file) {
		return null;
	}

	// TODO: Einstellung ob creating gelöscht werden soll, wenn Fenster geschlossen
	// wird
	public void save() {

		// if (creating != null)
		// abortCreating();

		Main.dataHandler.saveInData(this);
	}

	public void newFinanzhelfer() {
		creating = new Finanzhelfer();
		creatingCategoryList = new ArrayList<>();
	}

	// public void setName(String name) {
	// creating.setName(name);
	// }
	//
	// public void setMoney(double money) {
	// creating.setMoney(money);
	// }
	//
	// public void setSpartenAmount(int amount) {
	// for (int i = 0; i < amount; i++) {
	// creatingCategoryList.add(new Kategorie());
	// }
	// }
	//
	// public int getSpartenAmount() {
	// return creatingCategoryList.size();
	// }
	//
	// public ArrayList<Kategorie> getCreatingSpartenList() {
	// return creatingCategoryList;
	// }
	//
	// public void finishCreatingSpartenList(ArrayList<Kategorie> spartenList) {
	// creating.setSparten(spartenList);
	// }

	public void abortCreating() {
		creating = null;
		creatingCategoryList = null;
	}

	public void finish() {

		if (fhList.isEmpty()) {
			creating.setCurrent(true);
			creating.setStandard(true);
		}

		fhList.add(creating);

		abortCreating();

		Main.window.refresh();
	}

	public Finanzhelfer getCurrent() {
		for (Finanzhelfer current : fhList)
			if (current.isCurrent())
				return current;
		return null;
	}

	public void setCurrent(Finanzhelfer newCurrent) {
		for (Finanzhelfer current : fhList) {
			if (current.isCurrent()) {
				current.setCurrent(false);
				break;
			}
		}

		for (Finanzhelfer current : fhList) {
			if (current.getUUID() == newCurrent.getUUID()) {
				current.setCurrent(true);
				Main.window.refresh();
				break;
			}
		}
	}

	public Finanzhelfer getStandard() {
		for (Finanzhelfer current : fhList) {
			if (current.isStandard())
				return current;
		}
		return null;
	}

	public void setStandard(Finanzhelfer newStandard) {
		for (Finanzhelfer current : fhList) {
			if (current.isStandard()) {
				current.setStandard(false);
				break;
			}
		}

		for (Finanzhelfer current : fhList) {
			if (current.getUUID() == newStandard.getUUID()) {
				current.setStandard(true);
				Main.window.refresh();
				break;
			}
		}
	}

	public Finanzhelfer getFinanzhelferByName(String name) {
		for (Finanzhelfer current : fhList)
			if (current.getName().equals(name))
				return current;
		return null;
	}

	public Finanzhelfer getFinanzhelferByUUID(UUID uuid) {
		for (Finanzhelfer current : fhList)
			if (current.getUUID().compareTo(uuid) == 0)
				return current;
		return null;
	}

	// TODO: Button um das erneut zu überprüfen in den Einstellungen hinzufügen
	public void checkRecurrentExpenditures() {

		for (Finanzhelfer cF : fhList) {
			for (Kategorie cK : cF.getCategories()) {
				
				for (Dauerauftrag cSO : cK.getStandingOrders()) {
					if (cSO.getTimer() != null) {

						while (cSO.hasHappened()) { // so oft ausführen, wie es noch nicht gemacht wurde

							cK.addPayment(cSO.getPayment());
							cSO.addDelay();

						}

					}
				}

			}

		}

	}

	@Override
	public String toString() {
		StringBuilder fhs;

		if (fhList.isEmpty())
			fhs = new StringBuilder("[]");
		else {
			fhs = new StringBuilder("[");

			for (Finanzhelfer current : fhList)
				fhs.append(current.toString()).append("; ");

			fhs = new StringBuilder(fhs.substring(0, fhs.length() - 2)); // letztes Semikolon und Leerzeichen entfernen
			fhs.append("]");
		}

		if (creating == null)
			return "FinanzhelferManager[fhList: " + fhs + "]";
		else
			return "FinanzhelferManager[fhList:" + fhs + "; creating:" + creating
					+ "; creatingCategoryList:" + creatingCategoryList.toString() + "]";
	}

}
