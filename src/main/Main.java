package main;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import excel_entrée.MessageErreurEntree;

import javax.swing.JOptionPane;
import javax.swing.plaf.synth.SynthScrollBarUI;

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
//			int[][] tabPref = new int[7][7];
//			tabPref[0][0]=1;
//			tabPref[0][1]=0;
//			tabPref[0][2]=1;
//			tabPref[0][3]=0;
//			tabPref[0][4]=1;
//			tabPref[0][5]=1;
//			tabPref[0][6]=1;
//			
//			tabPref[1][0]=1;
//			tabPref[1][1]=1;
//			tabPref[1][2]=0;
//			tabPref[1][3]=0;
//			tabPref[1][4]=1;
//			tabPref[1][5]=1;
//			tabPref[1][6]=1;
//			
//			tabPref[2][0]=1;
//			tabPref[2][1]=1;
//			tabPref[2][2]=0;
//			tabPref[2][3]=0;
//			tabPref[2][4]=1;
//			tabPref[2][5]=1;
//			tabPref[2][6]=1;
//			
//			tabPref[3][0]=1;
//			tabPref[3][1]=0;
//			tabPref[3][2]=1;
//			tabPref[3][3]=1;
//			tabPref[3][4]=0;
//			tabPref[3][5]=1;
//			tabPref[3][6]=1;
//			
//			tabPref[4][0]=0;
//			tabPref[4][1]=0;
//			tabPref[4][2]=1;
//			tabPref[4][3]=1;
//			tabPref[4][4]=1;
//			tabPref[4][5]=1;
//			tabPref[4][6]=1;
//			
//			tabPref[5][0]=1;
//			tabPref[5][1]=0;
//			tabPref[5][2]=0;
//			tabPref[5][3]=1;
//			tabPref[5][4]=1;
//			tabPref[5][5]=1;
//			tabPref[5][6]=1;
//			
//			tabPref[6][0]=0;
//			tabPref[6][1]=1;
//			tabPref[6][2]=1;
//			tabPref[6][3]=1;
//			tabPref[6][4]=0;
//			tabPref[6][5]=1;
//			tabPref[6][6]=1;
			
//			Faisabilite f = new Faisabilite(prefs.getPrefs(), conges.getConges());
			Faisabilite f = new Faisabilite(prefs.getPrefs(), conges.getConges(), infos);

			/*
			 * 3. Solver
			 */
			if (f.faisable()) {
				Solveur solveur = new Solveur(infos, conges, prefs);
				int[] resultats = solveur.resoudre();
				int cpt = 0 ;
				for(int i = 0 ; i<resultats.length ; i++ ){
					if( resultats[i] == 0){
						cpt ++ ;
					}
				}
				if( cpt > 100 ){
					JOptionPane d = new JOptionPane();
					JOptionPane.showMessageDialog(
							d, 
							"Pas de solution trouvée.\n"
							+ "Piste de résolution :\n"
							+ "Pour un médecin : Evitez les couples de préférences 'Lundi-Mardi' ou 'Jeudi-Vendredi'.", 
							"Erreur",
							JOptionPane.ERROR_MESSAGE);
				}
				else{
					
				// TODO
				// Si on trouve une solution -> on affiche le calendrier
				// Sinon on affiche un calendrier "faux"

				/*
				 * 4. Ecriture du fichier sortant
				 */
				String output = "Sortie.xls";
				WriteOutput excel_file = new WriteOutput(output, resultats, infos);
//				for (int i=0; i<prefs.getPrefs().length; i++) {
//					for (int j=0; j<prefs.getPrefs()[i].length; j++) {
//						System.out.println(prefs.getPrefs()[i][j]);
//					}
//					System.out.println();
//				}
				System.out.println();
				excel_file.write();
				Desktop dt = Desktop.getDesktop();
				dt.open(new File(output));
				}
			} else {
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
