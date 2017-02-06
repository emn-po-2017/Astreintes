package solveur;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;

import excel_entrée.Read_Conges;
import excel_entrée.Read_Informations;


public class Solveur {

	private Model model;
	private Param param;
	private Variables variables;
	
	public Solveur(Read_Informations infos, Read_Conges conges) {
		this.model = new Model();
		this.param = new Param(infos, conges);
		this.variables = new Variables(this.model, this.param);
	}
	
	public int[] resoudre() {
		new Contraintes(variables, model, param);
		Objectif objectif = new Objectif(variables, model, param, 4);
		
		Solver solver = this.model.getSolver();
		solver.limitTime("10s");
		int[] resultats = new int[variables.getMed()[0].length];
		while (solver.solve()) {
			System.out.println(objectif.getObj4());
			System.out.println(objectif.getSommeCarree());
			System.out.println(objectif.getSommeNonVoulues());
		System.out.println("1: " + objectif.getNbAstreintesParMedecin()[0].getValue() + "\n" + "2: "
				+ objectif.getNbAstreintesParMedecin()[1].getValue() + "\n" + "3: " + objectif.getNbAstreintesParMedecin()[2].getValue() + "\n"
				+ "4: " + objectif.getNbAstreintesParMedecin()[3].getValue() + "\n" + "5: " + objectif.getNbAstreintesParMedecin()[4].getValue()
				+ "\n" + "6: " + objectif.getNbAstreintesParMedecin()[5].getValue() + "\n" + "7: "
				+ objectif.getNbAstreintesParMedecin()[6].getValue() + "\n");
		
		int c =0;
		for (int i=0; i<variables.getMed().length; i++) {
			for (int j=0; j<variables.getMed()[i].length; j++) {
				if (variables.getMed()[i][j].getValue() == 1) {
					resultats[j] = i;
					c++;
				}
			}
		}
		System.out.println(c);

		
		//============ AFFICHAGE
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
		for (int i=0; i<7; i++) {
			for (int j=0; j<resultats.length; j++) {
				if (resultats[j] == i) {
					compteur2++;
				}
			}
			System.out.println("medecin" + i + " : " + compteur2);
			compteur2 = 0;
			}
		}
		return resultats;
		
		/**
		 * Affichage des résultats
		 * 
		 */
		/*
		 * if (solver.solve()) {
		 * 
		 * System.out.println( objectif);
		 * 
		 * for (int m = 0; m < medecins; m++) { System.out.print("\nM" + m +
		 * " "); for (int j = 0; j < joursT * semaines; j++) {
		 * System.out.print(med[m][j].getValue() + " "); } }
		 * System.out.println(); for (int s = 0; s < joursS * semaines; s++) {
		 * System.out.print("\nJ" + s + " "); for (int m = 0; m < medecins; m++)
		 * { System.out.print(med[m][s].getValue() + " "); } }
		 * 
		 * for (int s = 0; s < semaines; s++) { System.out.print("\nS" + s); for
		 * (int m = 0; m < medecins; m++) { System.out.print("\nM" + m); for
		 * (int j = 0; j < joursT; j++) { System.out.print(" " +
		 * parSemaine[s][m][j].getValue()); } }
		 * 
		 * } System.out.println("\n\nNb d'astreintes en semaine"); for (int m =
		 * 0; m < medecins; m++) { int nbAstreintes = 0; for (int i = 0; i <
		 * joursS * semaines; i++) { nbAstreintes += med5[m][i].getValue(); }
		 * System.out.print("\nM" + m + " : " + nbAstreintes); }
		 * System.out.println("\n\nNb d'astreintes en weekend"); for (int m = 0;
		 * m < medecins; m++) { int nbAstreintes = 0; for (int i = 0; i < joursW
		 * * semaines; i++) { nbAstreintes += med2[m][i].getValue(); }
		 * System.out.print("\nM" + m + " : " + nbAstreintes); } }
		 */
	}
}