package main;

import java.util.ArrayList;

import excel_entrée.Read_Informations;
import solveur.Param;

public class Faisabilite {
	
	int[][] tabConge ;
	int[][] tabPref ;
	String Erreur = "Type Erreur : \n";
	String[] jours = {"Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi","Dimanche"} ;
	Read_Informations infos ;
	public Faisabilite(int[][] tabPref, int[][] tabConge , Read_Informations infos){
		this.tabConge = tabConge ;
		this.tabPref = tabPref ;
		this.infos = infos ;
	}

	public boolean faisable() {
		boolean un = this.testConge() ;
		boolean deux = this.testPrefSemaine() ;
		boolean trois = this.testPrefMedecin() ;
		boolean quatre = this.testPrefConge() ;
		return un && deux && trois && quatre ;
	}

	private boolean testPrefConge() {
		boolean ok = true ;
		ArrayList<ArrayList<Integer>> dispo = new ArrayList<ArrayList<Integer>>(); //)[this.tabConge[0].length];
		for (int i = 0; i<this.tabConge[0].length; i++){
			dispo.add(new ArrayList<Integer>());
		}
		// Insertion préférences
		for( int j = 0 ; j<this.tabConge[0].length ; j++){
			for(int m = 0 ; m< this.tabPref.length ; m++){
				if(this.tabPref[m][j%7] == 1 ){
					dispo.get(j).add(m);
				}
			}
		}
		// Soustraction conge
		for(int j = 0 ; j<this.tabConge[0].length ; j++ ){
			ArrayList<Integer> ms = new ArrayList<Integer>();
			for( int r = 0 ; r<dispo.get(j).size() ; r++){
				ms.add(dispo.get(j).get(r));
			}
			for(int i = 0 ; i<dispo.get(j).size() ; i++ ){
				if(this.tabConge[dispo.get(j).get(i)][j] == 1){
					dispo.get(j).remove(i);
					i--;
				}
				if(ok){
					ok = !dispo.get(j).isEmpty() ;
				}
				if(dispo.get(j).isEmpty()){				
					System.out.println(ms.get(0));
					this.Erreur = this.Erreur +"Les médecins " + this.infos.getDoctors().get(ms.get(0));
					for(int k = 1 ; k<ms.size() ; k++){
						this.Erreur = this.Erreur + ", "+ this.infos.getDoctors().get(ms.get(k)); 
					}
					this.Erreur = this.Erreur + " sont en congé le jour "+ utils.Tools.getDate(j+1,infos.getStartMonth(), infos.getStartYear()) + " et sont les seuls désirant travailler le "+ this.jours[j%7]+".\n";
				}
			}
		}
		return ok;
	}
	
	private boolean testPrefMedecin(){
		boolean ok = true ;
		int j = 0 ;
		while( j < tabPref.length && ok ){
			int cpt = 0 ;
			for(int m = 0 ; m< Param.joursS ; m++ ){
				if( this.tabPref[j][m] == 1){
					cpt++;
				}
			}
			ok = (cpt>=3);
			if( !ok){
				this.Erreur = this.Erreur + "Le médecin "+this.infos.getDoctors().get(j)+" doit choisir au minimum 3 préférences en semaine.\n";
			}
			j++;
		}
		return ok ;
	}

	private boolean testPrefSemaine() {
		boolean ok = true ;
		int j = 0 ;
		while( j< tabPref[0].length && ok ){
			int cpt = 0 ;
			for(int m = 0 ; m < tabPref.length ; m++){
				if( tabPref[m][j] == 1){
					cpt++;
				}
			}
			if( j < 5){
				ok = cpt >= 3 ;
				if(!ok){
					Erreur = Erreur + "Il manque "+ (3-cpt)+" préférence(s) le " + this.jours[j]+".\nRappel : 3 mininimum par jour en semaine.\n" ; 
				}
			}
			j++ ;
		}
		return ok ;
	}

	private boolean testConge() {		
		// test journalier
		int j = 0 ;
		boolean ok = true ;
		int cpt = 0 ;
		while( j<this.tabConge[0].length && ok ){
			for(int m = 0 ; m < this.tabConge.length ; m++ ){
				if(this.tabConge[m][j] == 1){
					cpt++;
				}
			}
			ok = !(cpt == this.tabConge[0].length);
			if(!ok){
				this.Erreur = this.Erreur + " Tous les médecins sont en congés le jour "+utils.Tools.getDate(j+1,infos.getStartMonth(), infos.getStartYear())+ ".\n";
			}
			j++ ;
		}
		return ok;
	}

	public String provenence() {
		return this.Erreur;
	}
}
