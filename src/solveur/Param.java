package solveur;

import excel_entrée.Read_Conges;
import excel_entrée.Read_Informations;
import excel_entrée.Read_Preferences;

public class Param {
	
	public static final int joursS = 5;
	public static final int joursW = 2;
	public static final int joursT = 7;
	
	public int medecins; //nombre de docteurs
	public int semaines; //nombre de semaines
	public int nbAstreinteMin;
	public int nbAstreinteSemaineMax;
	public int nbAstreinteWeekendMax;
	
	//Matrice des congés : Congé = 1 ; Pas Congé = 0
	public int[][] tabConge;
	
	//Matrice des préférences : Jour désiré = 1 ; Jour non désiré = 0;
	public int[][] tabPref;
	
	public Param(Read_Informations infos, Read_Conges conges, Read_Preferences prefs){
	
		this.medecins = infos.getDoctors().size();
		this.semaines = infos.getNbSemaines();
		//this.nbAstreinteMin = (semaines*joursT/medecins)-3;
		this.nbAstreinteMin = (semaines*joursT/medecins)-2;
		//this.nbAstreinteSemaineMax = (semaines*joursS/medecins)+2;
		//this.nbAstreinteWeekendMax = (semaines*joursW/medecins)+2;
		this.nbAstreinteSemaineMax = (semaines*joursS/medecins)+2;
		this.nbAstreinteWeekendMax = (semaines*joursW/medecins)+2;
		this.tabConge = conges.getConges();
		//this.tabPref = prefs.getPrefs();
		this.tabPref = prefs.getPrefs() ;
		
		
		
		
//		for (int i = 0; i < medecins; i++) {
//			for (int j = 0; j < semaines * joursT; j++) {
//				tabConge[i][j] = 0;
//			}
//		}
//		// vacances d'hiver, 2 semaines 2 médecins
//		for (int j = 35; j < 49; j++) {
//			tabConge[0][j] = 1;
//			tabConge[1][j] = 1;
//		}
//		// vacances de paques,
//		for (int j = 63; j < 70; j++) {
//			tabConge[2][j] = 1;
//			tabConge[3][j] = 1;
//		}
//		for (int j = 70; j < 77; j++) {
//			tabConge[4][j] = 1;
//			tabConge[5][j] = 1;
//			tabConge[6][j] = 1;
//		}
//		for (int j = 77; j < 84; j++) {
//			tabConge[0][j] = 1;
//			tabConge[1][j] = 1;
//		}
	}
}
