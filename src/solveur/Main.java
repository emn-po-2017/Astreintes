package solveur;


import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;


public class Main {

	public static void main(String[] args) {
		/**
		 * Résolution
		 * 
		 */
		
		Model model = new Model();
		Param param = new Param();
		Variables variables=new Variables(model);
		new Contraintes(variables, model);
		Objectif objectif=new Objectif(variables, model);
		
		Solver solver = model.getSolver();
		
		solver.limitTime("10s");
		
		while (solver.solve()) {
			//System.out.println(nbAstreintesMoyen);
			// System.out.println(nbAstreintesMoyen2);
			System.out.println(objectif.getObj2());
			System.out.println("1: " + objectif.getNbAstreintesParMedecin()[0].getValue() + "\n" + "2: "
					+ objectif.getNbAstreintesParMedecin()[1].getValue() + "\n" + "3: " + objectif.getNbAstreintesParMedecin()[2].getValue() + "\n"
					+ "4: " + objectif.getNbAstreintesParMedecin()[3].getValue() + "\n" + "5: " + objectif.getNbAstreintesParMedecin()[4].getValue()
					+ "\n" + "6: " + objectif.getNbAstreintesParMedecin()[5].getValue() + "\n" + "7: "
					+ objectif.getNbAstreintesParMedecin()[6].getValue() + "\n");
		}
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