package main;

import java.io.IOException;

import excel_entrée.Read_Informations;
import excel_sortie.WriteOutput;
import jxl.write.WriteException;

public class Main {

	public static void main(String[] args) throws IOException, WriteException {
		/*
		 * Lecture des données d'entrée
		 */
		String chemin = "C:/Users/David/Documents/Mines de Nantes/A3S1/Projet Option/Excel/";
		String input = chemin + "Saisie.xls";
        Read_Informations infos = new Read_Informations(input);
        
        /*
         * Solver
         */
//        boolean solution = false;
//        System.out.println(solution);
//        //Si on trouve une solution -> on affiche le calendrier
//        //Sinon on affiche un calendrier "faux"
        
        /*
         * Ecriture du fichier sortant
         */
        String output = chemin + "Sortie.xls";
        WriteOutput excel_file = new WriteOutput(output, new int[0], infos);
        excel_file.write();
	}
}
