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
			System.out.println(objectif.getObj4());
			System.out.println(objectif.getSommeCarree());
			System.out.println(objectif.getSommeNonVoulues());
		System.out.println("1: " + objectif.getNbAstreintesParMedecin()[0].getValue() + "\n" + "2: "
				+ objectif.getNbAstreintesParMedecin()[1].getValue() + "\n" + "3: " + objectif.getNbAstreintesParMedecin()[2].getValue() + "\n"
				+ "4: " + objectif.getNbAstreintesParMedecin()[3].getValue() + "\n" + "5: " + objectif.getNbAstreintesParMedecin()[4].getValue()
				+ "\n" + "6: " + objectif.getNbAstreintesParMedecin()[5].getValue() + "\n" + "7: "
				+ objectif.getNbAstreintesParMedecin()[6].getValue() + "\n");
		
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
	}
}