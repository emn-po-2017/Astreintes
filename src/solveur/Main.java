package solveur;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.objective.ParetoOptimizer;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

public class Main {

	public static void main(String[] args) {

		/**
		 * Paramètres
		 * 
		 */

		Model model = new Model();
		int semaines = 5*6;
		int joursS = 5;
		int joursW = 2;
		int joursT = 7;
		int medecins = 7;
		int nbAstreinteMin = (semaines*joursT/medecins)-3;
		int nbAstreinteSemaineMax = (semaines*joursS/medecins)+2;
		int nbAstreinteWeekendMax = (semaines*joursW/medecins)+2;;
		// Matrice congé : Congé = 1 ; Pas Congé = 0
		// Sera lue depuis un fichier Excel
		int[][] tabConge = new int[medecins][semaines * joursT];
		for (int i = 0; i < medecins; i++) {
			for (int j = 0; j < semaines * joursT; j++) {
				tabConge[i][j] = 0;
			}
		}
		// vacances d'hiver, 2 semaines 2 médecins
		for (int j = 35; j < 49; j++) {
			tabConge[0][j] = 1;
			tabConge[1][j] = 1;
		}
		// vacances de paques,
		for (int j = 63; j < 70; j++) {
			tabConge[2][j] = 1;
			tabConge[3][j] = 1;
		}
		for (int j = 70; j < 77; j++) {
			tabConge[4][j] = 1;
			tabConge[5][j] = 1;
			tabConge[6][j] = 1;
		}
		for (int j = 77; j < 84; j++) {
			tabConge[0][j] = 1;
			tabConge[1][j] = 1;
		}

		/**
		 * Variables
		 * 
		 */

		BoolVar[][] med = model.boolVarMatrix(medecins, joursT * semaines);
		BoolVar[][] medT = model.boolVarMatrix(joursT * semaines, medecins);

		/**
		 * Contraintes
		 * 
		 */

		// 2 tableaux identiques
		for (int i = 0; i < joursT * semaines; i++) {
			for (int j = 0; j < medecins; j++) {
				model.arithm(medT[i][j], "=", med[j][i]).post();
			}
		}
		// tableau des jours de semaine
		BoolVar[][] med5 = model.boolVarMatrix(medecins, joursS * semaines);
		for (int m = 0; m < medecins; m++) {
			for (int s = 0; s < semaines; s++) {
				for (int j = 0; j < joursS; j++) {
					model.arithm(med5[m][joursS * s + j], "=", med[m][joursT * s + j]).post();
				}
			}
		}
		// tableau des jours de weekend
		BoolVar[][] med2 = model.boolVarMatrix(medecins, joursW * semaines);
		for (int m = 0; m < medecins; m++) {
			for (int s = 0; s < semaines; s++) {
				for (int j = 0; j < joursW; j++) {
					model.arithm(med2[m][joursW * s + j], "=", med[m][joursT * s + j + joursS]).post();
				}
			}
		}
		// 1 tableau/semaine
		BoolVar[][][] parSemaine = new BoolVar[semaines][medecins][joursT];
		for (int s = 0; s < semaines; s++) {
			parSemaine[s] = model.boolVarMatrix(medecins, joursT);
		}

		for (int m = 0; m < medecins; m++) {
			for (int s = 0; s < semaines; s++) {
				for (int j = 0; j < joursT; j++) {
					model.arithm(med[m][joursT * s + j], "=", parSemaine[s][m][j]).post();

				}
			}
		}
		// 1 tableau/semaine de 5
		BoolVar[][][] parSemaine5 = new BoolVar[semaines][medecins][joursS];
		for (int s = 0; s < semaines; s++) {
			parSemaine5[s] = model.boolVarMatrix(medecins, joursS);
		}

		for (int m = 0; m < medecins; m++) {
			for (int s = 0; s < semaines; s++) {
				for (int j = 0; j < joursS; j++) {
					model.arithm(med[m][joursT * s + j], "=", parSemaine5[s][m][j]).post();

				}
			}
		}
		// 1 tableau/weekend
		BoolVar[][][] parWeekend = new BoolVar[semaines][medecins][joursW];
		for (int s = 0; s < semaines; s++) {
			parWeekend[s] = model.boolVarMatrix(medecins, joursW);
		}

		for (int m = 0; m < medecins; m++) {
			for (int s = 0; s < semaines; s++) {
				for (int j = 0; j < joursW; j++) {
					model.arithm(med[m][joursT * s + j], "=", parWeekend[s][m][j]).post();

				}
			}
		}

		// 1 par jour
		for (int i = 0; i < joursT * semaines; i++) {
			model.sum(medT[i], "=", 1).post();
		}
		// Astreintes min

		for (int i = 0; i < medecins; i++) {
			model.sum(med[i], ">", nbAstreinteMin).post();
		}

		// même nombre semaine
		for (int i = 0; i < medecins; i++) {
			model.sum(med5[i], "<", nbAstreinteSemaineMax).post();
		} // même

		// nombre weekend
		for (int i = 0; i < medecins; i++) {
			model.sum(med2[i], "<", nbAstreinteWeekendMax).post();
		}

		// Pas plus de 3 par semaine de 7 jours
		for (int s = 0; s < semaines; s++) {
			for (int m = 0; m < medecins; m++) {
				model.sum(parSemaine[s][m], "<=", 3).post();
			}
		}
		// Pas plus de 2 par semaine de 5 jours
		for (int s = 0; s < semaines; s++) {
			for (int m = 0; m < medecins; m++) {
				model.sum(parSemaine5[s][m], "<=", 2).post();
			}
		}
		// Pas 2 d'affilée
		for (int m = 0; m < medecins; m++) {
			for (int j = 0; j < semaines * joursT - 1; j++) {
				model.arithm(med[m][j], "+", med[m][j + 1], "<", 2).post();
			}
		}
		// Congé
		for (int i = 0; i < medecins; i++) {
			for (int j = 0; j < semaines * joursT; j++) {
				if (tabConge[i][j] == 1) {
					// model.arithm(med[i][j], "!=", 1).post();
				}
			}
		}
		// Asi
		IntVar[] nbAstreintesParMedecin = model.intVarArray(medecins, 0, 999);
		for (int m = 0; m < medecins; m++) {
			model.sum(med[m], "=", nbAstreintesParMedecin[m]).post();
		}
		// Asi moyen
		IntVar nbAstreintesMoyen = model.intVar("somme", 0, 999);
		model.sum(nbAstreintesParMedecin, "=", nbAstreintesMoyen).post();
		// model.arithm(nbAstreintesMoyen, "/", medecins);
		/*
		 * IntVar nbAstreintesMoyen2 = model.intVar("moyenne", 0, 999);
		 * model.scalar(new IntVar[] {nbAstreintesMoyen} , new int[]
		 * {1/medecins} ,"=", nbAstreintesMoyen2);
		 * //System.out.println(nbAstreintesMoyen);
		 * //System.out.println(nbAstreintesMoyen2); // Asi - As IntVar[]
		 * DiffAstreintesParMedecin = model.intVarArray(medecins, 0, 999); for
		 * (int m = 0; m < medecins; m++) {
		 * model.arithm(nbAstreintesParMedecin[m], "-", nbAstreintesMoyen2, "=",
		 * DiffAstreintesParMedecin[m]).post(); } // sum IntVar objectif =
		 * model.intVar("objectif", 0, 999); model.sum(DiffAstreintesParMedecin,
		 * "=", objectif).post(); model.setObjective(Model.MINIMIZE, objectif);
		 */

		// autre fonction objective

		// ParetoOptimizer po = new ParetoOptimizer(Model.MINIMIZE,
		// nbAstreintesParMedecin);
		// Solver solver = model.getSolver();
		// solver.plugMonitor(po);
		IntVar[] inter = model.intVarArray(medecins, 0, 9999);
		for (int m = 0; m < inter.length; m++) {
			model.square(inter[m], nbAstreintesParMedecin[m]).post();
		}
		IntVar obj2 = model.intVar("obj", 0, 9999);
		model.sum(inter, "=", obj2).post();
		model.setObjective(Model.MINIMIZE, obj2);
		/**
		 * Résolution
		 * 
		 */
		Solver solver = model.getSolver();
		while (solver.solve()) {
			System.out.println(nbAstreintesMoyen);
			// System.out.println(nbAstreintesMoyen2);
			System.out.println(obj2);
			System.out.println("1: " + nbAstreintesParMedecin[0].getValue() + "\n" + "2: "
					+ nbAstreintesParMedecin[1].getValue() + "\n" + "3: " + nbAstreintesParMedecin[2].getValue() + "\n"
					+ "4: " + nbAstreintesParMedecin[3].getValue() + "\n" + "5: " + nbAstreintesParMedecin[4].getValue()
					+ "\n" + "6: " + nbAstreintesParMedecin[5].getValue() + "\n" + "7: "
					+ nbAstreintesParMedecin[6].getValue() + "\n");
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