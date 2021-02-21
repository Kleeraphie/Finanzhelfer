package de.kleeraphie.finanzhelfer.finanzhelfer;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import de.kleeraphie.finanzhelfer.main.Main;

public class FinanzhelferManager {

	public Finanzhelfer creating;
	public ArrayList<Finanzhelfer> fhList;
	private ArrayList<Kategorie> creatingCategorieList;
	// creatingCategorieList ist nur während des Erstellens nötig;
	// wird später den creating übergeben

	public FinanzhelferManager() {
		fhList = new ArrayList<>();
	}

	// TODO: später loadFromFile(config) aufrufen
	// wo liegt die?
	public static FinanzhelferManager load() {
		FinanzhelferManager temp;

		temp = Main.dataHandler.loadFromDataFile();
		return temp;
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
		creatingCategorieList = new ArrayList<>();
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
	// creatingCategorieList.add(new Kategorie());
	// }
	// }
	//
	// public int getSpartenAmount() {
	// return creatingCategorieList.size();
	// }
	//
	// public ArrayList<Kategorie> getCreatingSpartenList() {
	// return creatingCategorieList;
	// }
	//
	// public void finishCreatingSpartenList(ArrayList<Kategorie> spartenList) {
	// creating.setSparten(spartenList);
	// }

	public void abortCreating() {
		creating = null;
		creatingCategorieList = null;
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

							cK.addAusgabe(cSO.getExpenditure());
							cSO.addDelay();

						}

					}
				}

			}

		}

	}

	@Override
	public String toString() {
		String fhs;

		if (fhList.isEmpty())
			fhs = "[]";
		else {
			fhs = "[";

			for (Finanzhelfer current : fhList)
				fhs += current.toString() + "; ";

			fhs = fhs.substring(0, fhs.length() - 2); // letztes Semikolon und Leerzeichen entfernen
			fhs += "]";
		}

		if (creating == null)
			return "FinanzhelferManager[fhList: " + fhs + "]";
		else
			return "FinanzhelferManager[fhList:" + fhs + "; creating:" + creating.toString()
					+ "; creatingCategorieList:" + creatingCategorieList.toString() + "]";
	}

}
