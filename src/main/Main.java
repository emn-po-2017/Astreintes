package main;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import excel_entrée.Read_Informations;
import excel_sortie.WriteOutput;
import jxl.write.WriteException;
import solveur.Solveur;

public class Main {

	public static void main(String[] args) throws IOException, WriteException {
		/*
		 * Lecture des données d'entrée
		 */
		//String chemin = "C:/Users/David/Documents/Mines de Nantes/A3S1/Projet Option/Excel/";
		String input = "Saisie.xls";
        Read_Informations infos = new Read_Informations(input);
        
        /*
         * Solver
         */
        Solveur solveur = new Solveur(infos);
        //solveur.resoudre();
        //int[] resultats = solveur.getResultats();
        int[] resultats = solveur.resoudre();
        
        //TODO
        //Si on trouve une solution -> on affiche le calendrier
        //Sinon on affiche un calendrier "faux"
        
        /*
         * Ecriture du fichier sortant
         */
        String output = "Sortie.xls";
        WriteOutput excel_file = new WriteOutput(output, resultats, infos);
        excel_file.write();
        Desktop dt = Desktop.getDesktop();
        dt.open(new File(output));
	}
}
