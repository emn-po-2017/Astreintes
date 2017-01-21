package solveur;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;

public class Variables {
	/**
	 * Variables
	 * 
	 */
	Model model;
	
	BoolVar[][] medT;
	BoolVar[][] med;
	BoolVar[][] med5;
	BoolVar[][] med2;
	BoolVar[][][] parSemaine;
	BoolVar[][][] parSemaine5;
	BoolVar[][][] parWeekend;
	
	public Variables(Model model) {
		
		this.model = model;
		
		this. medT = model.boolVarMatrix(Param.joursT * Param.semaines, Param.medecins);
		this.med = model.boolVarMatrix(Param.medecins, Param.joursT * Param.semaines);
		this.med5 = model.boolVarMatrix(Param.medecins, Param.joursS * Param.semaines);
		this.med2 = model.boolVarMatrix(Param.medecins, Param.joursW * Param.semaines);
		this.parSemaine = new BoolVar[Param.semaines][Param.medecins][Param.joursT];
		this.parSemaine5 = new BoolVar[Param.semaines][Param.medecins][Param.joursS];
		this.parWeekend = new BoolVar[Param.semaines][Param.medecins][Param.joursW];

		// 2 tableaux identiques
		for (int i = 0; i < Param.joursT * Param.semaines; i++) {
			for (int j = 0; j < Param.medecins; j++) {
				model.arithm(medT[i][j], "=", med[j][i]).post();
			}
		}
		// tableau des jours de semaine
		
		for (int m = 0; m < Param.medecins; m++) {
			for (int s = 0; s < Param.semaines; s++) {
				for (int j = 0; j < Param.joursS; j++) {
					model.arithm(med5[m][Param.joursS * s + j], "=", med[m][Param.joursT * s + j]).post();
				}
			}
		}
		// tableau des jours de weekend
		
		for (int m = 0; m < Param.medecins; m++) {
			for (int s = 0; s < Param.semaines; s++) {
				for (int j = 0; j < Param.joursW; j++) {
					model.arithm(med2[m][Param.joursW * s + j], "=", med[m][Param.joursT * s + j + Param.joursS])
							.post();
				}
			}
		}
		// 1 tableau/semaine
		
		for (int s = 0; s < Param.semaines; s++) {
			parSemaine[s] = model.boolVarMatrix(Param.medecins, Param.joursT);
		}

		for (int m = 0; m < Param.medecins; m++) {
			for (int s = 0; s < Param.semaines; s++) {
				for (int j = 0; j < Param.joursT; j++) {
					model.arithm(med[m][Param.joursT * s + j], "=", parSemaine[s][m][j]).post();

				}
			}
		}
		// 1 tableau/semaine de 5
		
		for (int s = 0; s < Param.semaines; s++) {
			parSemaine5[s] = model.boolVarMatrix(Param.medecins, Param.joursS);
		}

		for (int m = 0; m < Param.medecins; m++) {
			for (int s = 0; s < Param.semaines; s++) {
				for (int j = 0; j < Param.joursS; j++) {
					model.arithm(med[m][Param.joursT * s + j], "=", parSemaine5[s][m][j]).post();

				}
			}
		}
		// 1 tableau/weekend
		
		for (int s = 0; s < Param.semaines; s++) {
			parWeekend[s] = model.boolVarMatrix(Param.medecins, Param.joursW);
		}

		for (int m = 0; m < Param.medecins; m++) {
			for (int s = 0; s < Param.semaines; s++) {
				for (int j = 0; j < Param.joursW; j++) {
					model.arithm(med[m][Param.joursT * s + j], "=", parWeekend[s][m][j]).post();

				}
			}
		}
	}
	public BoolVar[][] getMed() {
		return med;
	}

	public void setMed(BoolVar[][] med) {
		this.med = med;
	}

	

	public BoolVar[][] getMedT() {
		return medT;
	}

	public void setMedT(BoolVar[][] medT) {
		this.medT = medT;
	}
	
	public BoolVar[][] getMed5() {
		return med5;
	}

	public void setMed5(BoolVar[][] med5) {
		this.med5 = med5;
	}

	public BoolVar[][] getMed2() {
		return med2;
	}

	public void setMed2(BoolVar[][] med2) {
		this.med2 = med2;
	}

	public BoolVar[][][] getParSemaine() {
		return parSemaine;
	}

	public void setParSemaine(BoolVar[][][] parSemaine) {
		this.parSemaine = parSemaine;
	}

	public BoolVar[][][] getParSemaine5() {
		return parSemaine5;
	}

	public void setParSemaine5(BoolVar[][][] parSemaine5) {
		this.parSemaine5 = parSemaine5;
	}

	public BoolVar[][][] getParWeekend() {
		return parWeekend;
	}

	public void setParWeekend(BoolVar[][][] parWeekend) {
		this.parWeekend = parWeekend;
	}
}
