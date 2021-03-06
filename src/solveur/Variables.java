package solveur;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;

public class Variables {
	/**
	 * Variables
	 * 
	 */
	Model model;
	Param param;

	BoolVar[][] medT;
	BoolVar[][] med;
	BoolVar[][] med5;
	BoolVar[][] med2;
	BoolVar[][][] parSemaine;
	BoolVar[][][] parSemaine5;
	BoolVar[][][] parWeekend;
	BoolVar[][][] JVS ;
	BoolVar[][][] DLM ;
	

	public Variables(Model model, Param param) {

		this.param = param;
		this.model = model;

		this.medT = model.boolVarMatrix(Param.joursT * param.semaines, param.medecins);
		this.med = model.boolVarMatrix(param.medecins, Param.joursT * param.semaines);
		this.med5 = model.boolVarMatrix(param.medecins, Param.joursS * param.semaines);
		this.med2 = model.boolVarMatrix(param.medecins, Param.joursW * param.semaines);
		this.parSemaine = new BoolVar[param.semaines][param.medecins][Param.joursT];
		this.parSemaine5 = new BoolVar[param.semaines][param.medecins][Param.joursS];
		this.parWeekend = new BoolVar[param.semaines][param.medecins][Param.joursW];
		this.JVS = new BoolVar[param.semaines][param.medecins][3];
		this.DLM = new BoolVar[param.semaines][param.medecins][3];
		
		// 2 tableaux identiques
		for (int i = 0; i < Param.joursT * param.semaines; i++) {
			for (int j = 0; j < param.medecins; j++) {
				model.arithm(medT[i][j], "=", med[j][i]).post();
			}
		}
		// tableau des jours de semaine

		for (int m = 0; m < param.medecins; m++) {
			for (int s = 0; s < param.semaines; s++) {
				for (int j = 0; j < Param.joursS; j++) {
					model.arithm(med5[m][Param.joursS * s + j], "=", med[m][Param.joursT * s + j]).post();
				}
			}
		}
		// 1 tableau/semaine

		for (int s = 0; s < param.semaines; s++) {
			parSemaine[s] = model.boolVarMatrix(param.medecins, Param.joursT);
		}

		for (int m = 0; m < param.medecins; m++) {
			for (int s = 0; s < param.semaines; s++) {
				for (int j = 0; j < Param.joursT; j++) {
					model.arithm(med[m][Param.joursT * s + j], "=", parSemaine[s][m][j]).post();

				}
			}
		}
		// 1 tableau/semaine de 5

		for (int s = 0; s < param.semaines; s++) {
			parSemaine5[s] = model.boolVarMatrix(param.medecins, Param.joursS);
		}

		for (int m = 0; m < param.medecins; m++) {
			for (int s = 0; s < param.semaines; s++) {
				for (int j = 0; j < Param.joursS; j++) {
					model.arithm(med[m][Param.joursT * s + j], "=", parSemaine5[s][m][j]).post();

				}
			}
		}
		// 1 tableau/weekend

		for (int s = 0; s < param.semaines; s++) {
			parWeekend[s] = model.boolVarMatrix(param.medecins, Param.joursW);
		}

		for (int m = 0; m < param.medecins; m++) {
			for (int s = 0; s < param.semaines; s++) {
				for (int j = 0; j < Param.joursW; j++) {
					model.arithm(parSemaine[s][m][5+j], "=", parWeekend[s][m][j]).post();

				}
			}
		}
		// 1 tableau JVS
		for (int s = 0; s < param.semaines; s++) {
			JVS[s] = model.boolVarMatrix(param.medecins, 3);
		}

		for (int m = 0; m < param.medecins; m++) {
			for (int s = 0; s < param.semaines; s++) {
				for (int j = 0; j < 3 ; j++) {
					model.arithm(parSemaine[s][m][3+j], "=", JVS[s][m][j]).post();

				}
			}
		}
		
		// 1 tableau DLM
		for (int s = 0; s < param.semaines; s++) {
			DLM[s] = model.boolVarMatrix(param.medecins, 3);
		}

		for (int m = 0; m < param.medecins; m++) {
			for (int s = 1; s < param.semaines; s++) {
				for (int j = 0; j < 3 ; j++) {
					if(j==0){
						model.arithm(parSemaine[s-1][m][6], "=", DLM[s-1][m][j]).post();
					}
					else{
						model.arithm(parSemaine[s][m][j-1], "=", DLM[s-1][m][j]).post();
					}

				}
			}
		}
		// tableau des jours de weekend

		for (int m = 0; m < param.medecins; m++) {
			for (int s = 0; s < param.semaines; s++) {
				for (int j = 0; j < Param.joursW; j++) {
					model.arithm(med2[m][Param.joursW*s +j], "=", parWeekend[s][m][j]).post();
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
	public BoolVar[][][] getJVS() {
		return JVS;
	}
	public BoolVar[][][] getDLM() {
		return DLM;
	}

	public void setParWeekend(BoolVar[][][] parWeekend) {
		this.parWeekend = parWeekend;
	}
}
