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
        
        Faisabilite f = new Faisabilite(new int[0][] , conges.getConges());
       
        /*
         * Solver
         */
        if( f.faisable()){
	        Solveur solveur = new Solveur(infos, conges);
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
        else{
        	System.out.println(f.provenence());
        }
	}
}
