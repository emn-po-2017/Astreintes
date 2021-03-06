package solveur;

import org.chocosolver.solver.Model;

public class Contraintes {
	
	Model model;
	Param param;
	
	public Contraintes(Variables v, Model model, Param param) {
	
		this.model=model;
		this.param = param;
		
		// 1 par jour
		for (int i = 0; i < Param.joursT * param.semaines; i++) {
			model.sum(v.getMedT()[i], "=", 1).post();
		}
		
		// Astreintes min
		for (int i = 0; i < param.medecins; i++) {
			model.sum(v.getMed()[i], ">", param.nbAstreinteMin).post();
		}

		// même nombre semaine
		for (int i = 0; i < param.medecins; i++) {
			model.sum(v.getMed5()[i], "<", param.nbAstreinteSemaineMax).post();
		} // même

		// nombre weekend
		for (int i = 0; i < param.medecins; i++) {
			model.sum(v.getMed2()[i], "<", param.nbAstreinteWeekendMax).post();
		}

		// Pas plus de 3 par semaine de 7 jours
		for (int s = 0; s < param.semaines; s++) {
			for (int m = 0; m < param.medecins; m++) {
				model.sum(v.getParSemaine()[s][m], "<=", 3).post();
			}
		}
		// Pas plus de 2 par semaine de 5 jours
		for (int s = 0; s < param.semaines; s++) {
			for (int m = 0; m < param.medecins; m++) {
				model.sum(v.getParSemaine5()[s][m], "<=", 2).post();
			}
		}

		// Pas 2 le week end
		for(int s = 0 ; s<param.semaines; s++){
			for( int m = 0 ; m < param.medecins ; m++ ){
				model.sum(v.getParWeekend()[s][m], "<", 2).post();
			}
		}
		// Pas 3 d'affilée ( Dimanche - Lundi - Mardi et Jeudi-Vendredi-Samedi )
		for(int s = 0 ; s<param.semaines; s++){
			for( int m = 0 ; m < param.medecins ; m++ ){
				model.sum(v.getJVS()[s][m], "<", 3).post();
			}
		}
		for(int s = 0 ; s<param.semaines; s++){
			for( int m = 0 ; m < param.medecins ; m++ ){
				model.sum(v.getDLM()[s][m], "<", 3).post();
			}
		}
		// Congé
		for (int i = 0; i < param.medecins; i++) {
			for (int j = 0; j < param.semaines * Param.joursT; j++) {
				if (param.tabConge[i][j] == 1) {
					model.arithm(v.getMed()[i][j], "!=", 1).post();
				}
			}
		}
		// Pref
		for(int s = 0 ; s <param.semaines; s++){
			for( int m = 0 ; m < param.medecins ; m++ ){
				for(int j = 0 ; j<Param.joursT ; j++){
					if(param.tabPref[m][j] == 0){
						model.arithm(v.getParSemaine()[s][m][j], "!=", 1).post();
					}
				}
			}
		}

	}
}
