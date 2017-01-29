package solveur;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Objectif {

	Model model;
	Param param;

	IntVar[] nbAstreintesParMedecin;
	IntVar[] nbAstreintesSemaine;
	IntVar[] nbAstreintesWeekend;
	IntVar nbAstreintesMoyen;
	// somme carrée des semaines et des weekends séparées
	IntVar obj2;
	// somme carrée de tous les jours réunis
	IntVar obj1;
	// écart types
	IntVar obj3;

	IntVar[] inter;

	IntVar sommeSemaine;
	IntVar[] DiffAstreintesParMedecin ;

	
	public Objectif(Variables v, Model model, Param param, int numerObjectif) {

		this.model = model;

		this.nbAstreintesParMedecin = model.intVarArray(param.medecins, 0, 999);
		this.nbAstreintesSemaine = model.intVarArray(param.medecins, 0, 999);
		this.nbAstreintesWeekend = model.intVarArray(param.medecins, 0, 999);
		this.nbAstreintesMoyen = model.intVar("somme", 0, 999);
		this.inter = model.intVarArray(param.medecins, 0, 9999);
		this.obj2 = model.intVar("obj2", 0, 9999);
		this.obj1 = model.intVar("obj1", 0, 9999);
		this.obj3=model.intVar("obj3", 0, 9999);
		this.sommeSemaine = model.intVar("sommeSemaine", 0, 9999);
		this.DiffAstreintesParMedecin= model.intVarArray(param.medecins, 0, 999);

		for (int m = 0; m < param.medecins; m++) {
			model.sum(v.getMed()[m], "=", nbAstreintesParMedecin[m]).post();
		}
		model.sum(nbAstreintesParMedecin, "=", nbAstreintesMoyen).post();

		if (numerObjectif == 2) {

			for (int m = 0; m < param.medecins; m++) {
				model.sum(v.getMed5()[m], "=", nbAstreintesSemaine[m]).post();
			}
			for (int m = 0; m < param.medecins; m++) {
				model.sum(v.getMed2()[m], "=", nbAstreintesWeekend[m]).post();
			}
			for (int m = 0; m < inter.length; m++) {
				model.square(inter[m], nbAstreintesSemaine[m]).post();
			}
			model.sum(inter, "=", sommeSemaine).post();
			IntVar[] interBis = model.intVarArray(param.medecins, 0, 9999);
			for (int m = 0; m < interBis.length; m++) {
				model.square(interBis[m], nbAstreintesWeekend[m]).post();
			}
			IntVar sommeWeekend = model.intVar("somme", 0, 9999);
			model.sum(interBis, "=", sommeWeekend).post();
			model.scalar(new IntVar[] { sommeSemaine, sommeWeekend }, new int[] { 1, 10 }, "=", obj2).post();
			model.setObjective(Model.MINIMIZE, obj2);
		}
		if (numerObjectif == 1) {
			for (int m = 0; m < param.medecins; m++) {
				model.sum(v.getMed()[m], "=", nbAstreintesParMedecin[m]).post();
			}
			IntVar[] intero = model.intVarArray(param.medecins, 0, 9999);
			for (int m = 0; m < intero.length; m++) {
				model.square(intero[m], nbAstreintesParMedecin[m]).post();
			}

			model.sum(intero, "=", obj1).post();

			model.setObjective(Model.MINIMIZE, obj1);
		}
		if (numerObjectif == 3) {
			IntVar nbAstreintesMoyen2 = model.intVar("moyenne", 0, 999);
			model.scalar(new IntVar[] { nbAstreintesMoyen }, 
					new int[] { 1 / param.medecins }, "=", nbAstreintesMoyen2);
			// Asi - As
			
			for (int m = 0; m < param.medecins; m++) {
				model.arithm(nbAstreintesParMedecin[m], "-", 
						nbAstreintesMoyen2, "=", DiffAstreintesParMedecin[m]).post();
			}
			// sum
			model.sum(DiffAstreintesParMedecin, "=", obj3).post();
			model.setObjective(Model.MINIMIZE, obj3);
		}

		// ParetoOptimizer po = new ParetoOptimizer(Model.MINIMIZE,
		// nbAstreintesParMedecin);
		// Solver solver = model.getSolver();
		// solver.plugMonitor(po);

	}

	public IntVar[] getNbAstreintesParMedecin() {
		return nbAstreintesParMedecin;
	}

	public void setNbAstreintesParMedecin(IntVar[] nbAstreintesParMedecin) {
		this.nbAstreintesParMedecin = nbAstreintesParMedecin;
	}

	public IntVar getNbAstreintesMoyen() {
		return nbAstreintesMoyen;
	}

	public void setNbAstreintesMoyen(IntVar nbAstreintesMoyen) {
		this.nbAstreintesMoyen = nbAstreintesMoyen;
	}

	public IntVar getObj2() {
		return obj2;
	}

	public void setObj2(IntVar obj2) {
		this.obj2 = obj2;
	}

	public IntVar[] getNbAstreintesSemaine() {
		return nbAstreintesSemaine;
	}

	public IntVar[] getNbAstreintesWeekend() {
		return nbAstreintesWeekend;
	}

	public IntVar[] getInter() {
		return inter;
	}

	public IntVar getSommeSemaine() {
		return sommeSemaine;
	}

	public IntVar getObj1() {
		return obj1;
	}

	public IntVar getObj3() {
		return obj3;
	}
	public IntVar[] getDiffAstreintesParMedecin() {
		return DiffAstreintesParMedecin;
	}

}
