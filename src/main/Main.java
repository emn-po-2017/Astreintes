package main;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import excel_entrée.Read_Conges;
import excel_entrée.Read_Excel;
import excel_entrée.Read_Informations;
import excel_entrée.Read_Preferences;
import excel_sortie.WriteOutput;
import jxl.write.WriteException;
import solveur.Solveur;

public class Main {

	public static void main(String[] args) throws IOException, WriteException {
		/*
		 * Lecture des données d'entrée
		 */
		String input = "Saisie.xls";
		Read_Excel excel = new Read_Excel(input);
		Read_Informations infos = excel.getInformations();
		Read_Preferences prefs = excel.getPreferences();
		Read_Conges conges = excel.getConges();
		int[][] tabPref = new int[7][7];
		tabPref[0][0] = 1;
		tabPref[0][1] = 0;
		tabPref[0][2] = 1;
		tabPref[0][3] = 0;
		tabPref[0][4] = 0;
		tabPref[0][5] = 1;
		tabPref[0][6] = 1;

		tabPref[1][0] = 1;
		tabPref[1][1] = 0;
		tabPref[1][2] = 1;
		tabPref[1][3] = 0;
		tabPref[1][4] = 1;
		tabPref[1][5] = 1;
		tabPref[1][6] = 1;

		tabPref[2][0] = 0;
		tabPref[2][1] = 1;
		tabPref[2][2] = 0;
		tabPref[2][3] = 1;
		tabPref[2][4] = 0;
		tabPref[2][5] = 1;
		tabPref[2][6] = 1;

		tabPref[3][0] = 0;
		tabPref[3][1] = 1;
		tabPref[3][2] = 0;
		tabPref[3][3] = 1;
		tabPref[3][4] = 0;
		tabPref[3][5] = 1;
		tabPref[3][6] = 1;

		tabPref[4][0] = 0;
		tabPref[4][1] = 0;
		tabPref[4][2] = 1;
		tabPref[4][3] = 0;
		tabPref[4][4] = 1;
		tabPref[4][5] = 1;
		tabPref[4][6] = 1;

		tabPref[5][0] = 1;
		tabPref[5][1] = 0;
		tabPref[5][2] = 0;
		tabPref[5][3] = 0;
		tabPref[5][4] = 1;
		tabPref[5][5] = 1;
		tabPref[5][6] = 1;

		tabPref[6][0] = 0;
		tabPref[6][1] = 0;
		tabPref[6][2] = 1;
		tabPref[6][3] = 1;
		tabPref[6][4] = 0;
		tabPref[6][5] = 1;
		tabPref[6][6] = 1;
		
		Faisabilite f = new Faisabilite(tabPref, conges.getConges());

		/*
		 * Solver
		 */
		if (f.faisable()) {
			Solveur solveur = new Solveur(infos, conges);
			int[] resultats = solveur.resoudre();
			// TODO
			// Si on trouve une solution -> on affiche le calendrier
			// Sinon on affiche un calendrier "faux"

			/*
			 * Ecriture du fichier sortant
			 */
			String output = "Sortie.xls";
			WriteOutput excel_file = new WriteOutput(output, resultats, infos);
			excel_file.write();
			Desktop dt = Desktop.getDesktop();
			dt.open(new File(output));
		} else {
			System.out.println(f.provenence());
		}
	}
}
