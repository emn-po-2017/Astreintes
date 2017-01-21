package solveur;

import org.chocosolver.solver.Model;

public class Contraintes {
	
	Model model;
	
	public Contraintes(Variables v, Model model) {
		
		this.model=model;
		
		// 1 par jour
		for (int i = 0; i < Param.joursT * Param.semaines; i++) {
			model.sum(v.getMedT()[i], "=", 1).post();
		}
		// Astreintes min
		

		for (int i = 0; i < Param.medecins; i++) {
			model.sum(v.getMed()[i], ">", Param.nbAstreinteMin).post();
		}

		// même nombre semaine
		for (int i = 0; i < Param.medecins; i++) {
			model.sum(v.getMed5()[i], "<", Param.nbAstreinteSemaineMax).post();
		} // même

		// nombre weekend
		for (int i = 0; i < Param.medecins; i++) {
			model.sum(v.getMed2()[i], "<", Param.nbAstreinteWeekendMax).post();
		}

		// Pas plus de 3 par semaine de 7 jours
		for (int s = 0; s < Param.semaines; s++) {
			for (int m = 0; m < Param.medecins; m++) {
				model.sum(v.getParSemaine()[s][m], "<=", 3).post();
			}
		}
		// Pas plus de 2 par semaine de 5 jours
		for (int s = 0; s < Param.semaines; s++) {
			for (int m = 0; m < Param.medecins; m++) {
				model.sum(v.getParSemaine5()[s][m], "<=", 2).post();
			}
		}
		// Pas 2 d'affilée
		for (int m = 0; m < Param.medecins; m++) {
			for (int j = 0; j < Param.semaines * Param.joursT - 1; j++) {
				model.arithm(v.getMed()[m][j], "+", v.getMed()[m][j + 1], "<", 2).post();
			}
		}
		// Congé
		for (int i = 0; i < Param.medecins; i++) {
			for (int j = 0; j < Param.semaines * Param.joursT; j++) {
				if (Param.tabConge[i][j] == 1) {
					// model.arithm(med[i][j], "!=", 1).post();
				}
			}
		}

	}
}
