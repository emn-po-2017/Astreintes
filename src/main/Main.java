package main;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import excel_entrée.MessageErreurEntree;

import javax.swing.JOptionPane;

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
		 * 1. Lecture des données d'entrée
		 */
		String input = "Saisie.xls";
		Read_Excel excel = new Read_Excel(input);
		Read_Informations infos = excel.getInformations();
		Read_Preferences prefs = excel.getPreferences();
		Read_Conges conges = excel.getConges();
		
		//On affiche un message d'erreur si le excel Saisie a été mal rempli
		MessageErreurEntree msgErreurEntree = new MessageErreurEntree(prefs, conges);
		if (msgErreurEntree.isError()) {
			msgErreurEntree.displayError();
		}
		
		/*
		 * 2. Analyse faisabilité
		 */
		else {

			Faisabilite f = new Faisabilite(prefs.getPrefs(), conges.getConges(), infos);

			/*
			 * 3.1 Solver
			 */
			if (f.faisable()) {
				Solveur solveur = new Solveur(infos, conges, prefs);
				int[] resultats = solveur.resoudre();
				int cpt = 0;
				for(int i=0; i<resultats.length; i++ ) {
					if(resultats[i] == 0){
						cpt ++ ;
					}
				}
				if(cpt > 100){
					JOptionPane d = new JOptionPane();
					JOptionPane.showMessageDialog(
							d, 
							"Pas de solution trouvée.\n"
							+ "Piste de résolution :\n"
							+ "Pour un médecin : Evitez les couples de préférences 'Lundi-Mardi' ou 'Jeudi-Vendredi'.", 
							"Erreur",
							JOptionPane.ERROR_MESSAGE);
				}
				
				/*
				 * 4. Ecriture du fichier sortant
				 */
				else {
					String output = "Planning.xls";
					WriteOutput excel_file = new WriteOutput(output, resultats, infos);
	
					excel_file.write();
					Desktop dt = Desktop.getDesktop();
					dt.open(new File(output));
					}
				}
			
			/*
			 * 3.2 Erreur si non résolution
			 */
			else {
				JOptionPane d = new JOptionPane();
				JOptionPane.showMessageDialog(
						d, 
						f.provenence(), 
						"Erreur",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
