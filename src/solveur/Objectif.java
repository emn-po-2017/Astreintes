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
	//somme carrée + préf
	IntVar obj4;

	IntVar[] inter;

	IntVar sommeSemaine;
	IntVar[] DiffAstreintesParMedecin ;
	IntVar[] nbAstreintesNonVoulues;
	IntVar[][] medNonVouluBis;
	IntVar sommeNonVoulues;
	IntVar sommeCarree;

	
	public Objectif(Variables v, Model model, Param param, int numerObjectif) {
		//initialisation des variables
		this.model = model;
		this.nbAstreintesParMedecin = model.intVarArray(param.medecins, 0, 9999);
		this.nbAstreintesSemaine = model.intVarArray(param.medecins, 0, 9999);
		this.nbAstreintesWeekend = model.intVarArray(param.medecins, 0, 9999);
		this.nbAstreintesMoyen = model.intVar("somme2", 0, 9999);
		this.inter = model.intVarArray(param.medecins, 0, 9999);
		this.obj2 = model.intVar("obj2", 0, 99999);
		this.obj1 = model.intVar("obj1", 0, 99999);
		this.obj3=model.intVar("obj3", 0, 99999);
		this.obj4=model.intVar("obj4", 0, 9999999);
		this.sommeSemaine = model.intVar("sommeSemaine", 0, 99999);
		this.DiffAstreintesParMedecin= model.intVarArray(param.medecins, 0, 99999);
		this.nbAstreintesNonVoulues=model.intVarArray(param.medecins, 0, 99999);
		this.sommeNonVoulues= model.intVar("sommeSemaine", 0, 99999);
		this.medNonVouluBis=model.boolVarMatrix(param.medecins, Param.joursT * param.semaines);
		sommeCarree = model.intVar("somme", 0, 99999);
		
		//calcul du nombre d'astreintes non voulues par medecin et au total
		for (int m = 0; m < param.medecins; m++) {
			for (int j = 0; j < param.semaines * Param.joursT; j++) {
				if (param.tabPref[m][j%7] == 0){
					model.arithm(v.getMed()[m][j], "=", medNonVouluBis[m][j]).post();
				}
				else {
					model.arithm(medNonVouluBis[m][j], "=", 0).post();
				}
			}
		}
		for (int m = 0; m < param.medecins; m++) {
			model.sum(medNonVouluBis[m], "=", nbAstreintesNonVoulues[m]).post();
		}
		model.sum(nbAstreintesNonVoulues, "=", sommeNonVoulues).post();
		
		//Calcul du nombre d'astreintes par medecin Asi
		for (int m = 0; m < param.medecins; m++) {
			model.sum(v.getMed()[m], "=", nbAstreintesParMedecin[m]).post();
		}
		
		//minimise la somme des carrés de tous les jours
		if (numerObjectif == 1) {
			IntVar[] intero = model.intVarArray(param.medecins, 0, 9999);
			for (int m = 0; m < intero.length; m++) {
				model.square(intero[m], nbAstreintesParMedecin[m]).post();
			}

			model.sum(intero, "=", obj1).post();

			model.setObjective(Model.MINIMIZE, obj1);
		}
		
		//minimise la somme des carrés en semaine et en weekend séparemment
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
			model.scalar(new IntVar[] { sommeSemaine, sommeWeekend }, new int[] { 0, 1 }, "=", obj2).post();
			model.setObjective(Model.MINIMIZE, obj2);
		}
		
		//minimise l'écart type 
		if (numerObjectif == 3) {
			//calcul du nombre d'astreintes moyen As
			model.sum(nbAstreintesParMedecin, "=", nbAstreintesMoyen).post();
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
		
		if(numerObjectif == 4){
			IntVar[] intero = model.intVarArray(param.medecins, 0, 9999);
			for (int m = 0; m < intero.length; m++) {
				model.square(intero[m], nbAstreintesParMedecin[m]).post();
			}
					
			model.sum(intero, "=", sommeCarree).post();
	
			model.scalar(new IntVar[] { sommeNonVoulues, sommeCarree }, new int[] { 0, 1 }, "=", obj4).post();
			model.setObjective(Model.MINIMIZE, obj4);
			
		}

	}
	public IntVar getSommeNonVoulues() {
		return sommeNonVoulues;
	}

	public IntVar getSommeCarree() {
		return sommeCarree;
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
	public IntVar getObj4() {
		return obj4;
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
