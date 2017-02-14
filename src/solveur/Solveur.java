package solveur;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;

import excel_entrée.Read_Conges;
import excel_entrée.Read_Informations;
import excel_entrée.Read_Preferences;


public class Solveur {

	private Model model;
	private Param param;
	private Variables variables;
	
	public Solveur(Read_Informations infos, Read_Conges conges, Read_Preferences prefs) {
		this.model = new Model();
		this.param = new Param(infos, conges, prefs);
		this.variables = new Variables(this.model, this.param);
	}
	
	public int[] resoudre() {
		new Contraintes(variables, model, param);
		Objectif objectif = new Objectif(variables, model, param, 2);
		
		Solver solver = this.model.getSolver();
		solver.limitTime("10s");
		int[] resultats = new int[variables.getMed()[0].length];
		while (solver.solve()) {
	        System.out.println();
			System.out.println(objectif.getObj2());
		
		//Ecriture du resultat
 		for (int i=0; i<variables.getMed().length; i++) {
 			for (int j=0; j<variables.getMed()[i].length; j++) {
 				if (variables.getMed()[i][j].getValue() == 1) {
 					resultats[j] = i;
 				}
 			}
 		}
	 		
		//============ AFFICHAGE CONSOLE
		int compteur = 0;
		for (int m=0; m<variables.getMed().length; m++) {
			for (int i=0; i<variables.getMed()[m].length; i++) {
				if (variables.getMed()[m][i].getValue() == 1) {
					compteur++;
				}
			}
			System.out.println("medecin" + m + " : " + compteur);
			compteur = 0;
		}
        System.out.println();
		int compteur2 = 0;
		for (int i=0; i<variables.getMed().length; i++) {
			for (int j=0; j<resultats.length; j++) {
				if (resultats[j] == i) {
					compteur2++;
				}
			}
			System.out.println("medecin" + i + " : " + compteur2);
			compteur2 = 0;
			}
		}
        System.out.println();
		return resultats;
	}
}