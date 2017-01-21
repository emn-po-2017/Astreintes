package solveur;

public class Param {
	
	public static final int semaines = 5*6;
	public static final int joursS = 5;
	public static final int joursW = 2;
	public static final int joursT = 7;
	public static final int medecins = 7;
	public static final int nbAstreinteMin = (semaines*joursT/medecins)-3;
	public static final int nbAstreinteSemaineMax = (semaines*joursS/medecins)+2;
	public static final int nbAstreinteWeekendMax = (semaines*joursW/medecins)+2;;
	// Matrice congé : Congé = 1 ; Pas Congé = 0
	// Sera lue depuis un fichier Excel
	public static int[][] tabConge = new int[medecins][semaines * joursT];
	
	public Param(){
	
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
	}
}
