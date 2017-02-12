package excel_entrée;

import jxl.Sheet;
import solveur.Param;
import utils.Tools;

public class Read_Preferences {

	private int[][] preferences; //[nbMedecins][nbDeJoursDansLaSemaine] = 0 ou 1
	private String log; //message si erreur
	
	public Read_Preferences(Sheet sheet, Read_Informations infos) {
		this.log = "";
		
		this.preferences = new int[infos.getDoctors().size()][Param.joursT];
		for (int i=0; i<preferences.length; i++) {
			for (int j=0; j<preferences[i].length; j++) {
				if( j<Param.joursS){
					preferences[i][j] = 0;
				}
				else{
					preferences[i][j] = 1;
				}
				
			}
		}
		
		for (int l=2; l < sheet.getRows(); l++) {
			String doc = sheet.getCell(0, l).getContents();
			if (doc != infos.getDoctors().get(l-2)) {
				log += "Les docteurs n'ont pas été rentrés "
						+ "dans le même ordre que dans l'onglet Informations"
						+ "\n";
			}
			
			else {
				/*
				 * col = 1 -> préférence semaine 1
				 * col = 2 -> préférence semaine 2
				 * col = 3 -> préférence semaine 3
				 */
				for (int col=1; col<4; col++) {
					String day = sheet.getCell(col, l).getContents();
					int dayweek = Tools.getDayWeek(day); //numéro du jour de la semaine
					if (dayweek != -1) { 
						preferences[l-2][dayweek] = 1;
					}
				}
				
				//Si le medecin n'a pas specifie 3 preferences distinctes
				if (!this.isEnoughPref(preferences, l-2)) {
					log += doc + " : vous devez spécifier 3 jours distincts dans vos préférences. \n";
				}
			}
		}
	}
	
	public int[][] getPrefs() {
		return this.preferences;
	}
	
	public String getLogPrefs() {
		return this.log;
	}
	
	/**
	 * Verifie que chaque medecin ait 3 specifie 3 jours differents
	 */
	public boolean isEnoughPref(int[][] prefs, int medecin) {
		int compteur = 0;
		for (int i=0; i<Param.joursS; i++) {
			if (prefs[medecin][i] == 1) {
				compteur++;
			}
		}
		return compteur == 3;
	}
}
