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
		this.tabPref=new int[this.medecins][joursT];
		
		tabPref[0][0]=1;
		tabPref[0][1]=0;
		tabPref[0][2]=1;
		tabPref[0][3]=0;
		tabPref[0][4]=1;
		tabPref[0][5]=1;
		tabPref[0][6]=1;
		
		tabPref[1][0]=1;
		tabPref[1][1]=1;
		tabPref[1][2]=0;
		tabPref[1][3]=0;
		tabPref[1][4]=1;
		tabPref[1][5]=1;
		tabPref[1][6]=1;
		
		tabPref[2][0]=1;
		tabPref[2][1]=1;
		tabPref[2][2]=0;
		tabPref[2][3]=0;
		tabPref[2][4]=1;
		tabPref[2][5]=1;
		tabPref[2][6]=1;
		
		tabPref[3][0]=1;
		tabPref[3][1]=0;
		tabPref[3][2]=1;
		tabPref[3][3]=1;
		tabPref[3][4]=0;
		tabPref[3][5]=1;
		tabPref[3][6]=1;
		
		tabPref[4][0]=0;
		tabPref[4][1]=0;
		tabPref[4][2]=1;
		tabPref[4][3]=1;
		tabPref[4][4]=1;
		tabPref[4][5]=1;
		tabPref[4][6]=1;
		
		tabPref[5][0]=1;
		tabPref[5][1]=0;
		tabPref[5][2]=0;
		tabPref[5][3]=1;
		tabPref[5][4]=1;
		tabPref[5][5]=1;
		tabPref[5][6]=1;
		
		tabPref[6][0]=0;
		tabPref[6][1]=1;
		tabPref[6][2]=1;
		tabPref[6][3]=1;
		tabPref[6][4]=0;
		tabPref[6][5]=1;
		tabPref[6][6]=1;
		
		
		
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
