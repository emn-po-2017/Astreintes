package solveur;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Objectif {
	
	Model model; 
	Param param;

	IntVar[] nbAstreintesParMedecin ;
	IntVar nbAstreintesMoyen;;
	IntVar obj2;

	
	public Objectif(Variables v, Model model, Param param) {
		
		this.model= model;
		
		this.nbAstreintesParMedecin = model.intVarArray(param.medecins, 0, 999);
		this.nbAstreintesMoyen = model.intVar("somme", 0, 999);
		this.obj2 = model.intVar("obj", 0, 9999);
		
		// Asi

		for (int m = 0; m < param.medecins; m++) {
			model.sum(v.getMed()[m], "=", nbAstreintesParMedecin[m]).post();
		}
		// Asi moyen

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
		IntVar[] inter = model.intVarArray(param.medecins, 0, 9999);
		for (int m = 0; m < inter.length; m++) {
			model.square(inter[m], nbAstreintesParMedecin[m]).post();
		}
		
		model.sum(inter, "=", obj2).post();
		model.setObjective(Model.MINIMIZE, obj2);
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
}
