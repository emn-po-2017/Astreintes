package excel_entrée;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Read_Excel {

	private String path; //chemin d'accès au fichier Excel
	private Read_Informations infos; //informations générales
	private Read_Preferences prefs; //informations avec les préférences
	private Read_Conges conges; //informations avec les conges
	
    public Read_Excel(String path) throws IOException {
    	this.path = path;
    	this.read();
    }
  
    public void read() throws IOException  {
        File inputWorkbook = new File(path);
        if(!inputWorkbook.getAbsoluteFile().exists()){
        	JOptionPane d = new JOptionPane();
			JOptionPane.showMessageDialog(
					d, 
					"Fichier Saisie.xls introuvable.\n"
					+ "Vérifier que Saisie.xls est dans le même répertoire.", 
					"Erreur",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
        }
        else{
        Workbook w;
        try {
        	w = Workbook.getWorkbook(inputWorkbook);
            Sheet infos_sheet = w.getSheet(0); //Feuille excel "Informations"
            Sheet pref_sheet = w.getSheet(1); //Feuille excel "Préférences"
            Sheet conges_sheet = w.getSheet(2); //Feuille excel "Congés"
            this.infos = new Read_Informations(infos_sheet);
            this.prefs = new Read_Preferences(pref_sheet, this.infos);
            this.conges = new Read_Conges(conges_sheet, this.infos);
        } 
        catch (BiffException e) {
                e.printStackTrace();
        }
        }
    }
    
    public Read_Informations getInformations() {
    	return this.infos;
    }
    
    public Read_Preferences getPreferences() {
    	return this.prefs;
    }
    
    public Read_Conges getConges() {
    	return this.conges;
    }
    
}