package excel_entrée;

import java.util.ArrayList;

import jxl.Cell;
import jxl.Sheet;
import utils.Tools;

public class Read_Informations {
	
	private int horizon; //nombre de mois à considérer
	private int start_month; //mois de départ (numéroté de 0 à 11)
	private int start_year; //année de départ
	private int nb_semaines; //nombre de semaines
	private ArrayList<String> doctors = 
			new ArrayList<String>(); //liste avec le nom des médecins

	
    public Read_Informations(Sheet sheet) {
        //Lecture de l'horizon
        Cell horizon = sheet.getCell(1, 1);
        this.horizon = Integer.parseInt(horizon.getContents());
        
        //Lecture du mois de départ
        Cell start_month = sheet.getCell(1,2);
        this.start_month = Tools.getMonth(start_month.getContents());
        
        //Lecture de l'année de départ
        Cell start_year = sheet.getCell(1,3);
        this.start_year = Integer.parseInt(start_year.getContents());
        
        //Calcul du nombre de semaines
        this.calculeNbSemaines();
        
        //Lecture des noms des docteurs
        for (int l=6; l<sheet.getRows(); l++) {
        	Cell d = sheet.getCell(0, l);
        	this.doctors.add(d.getContents());
        }
    }
    
    public int getHorizon() {
    	return this.horizon;
    }
    
    public int getStartMonth() {
    	return this.start_month;
    }
    
    public int getStartYear() {
    	return this.start_year;
    }
    
    public int getNbSemaines() {
    	return this.nb_semaines;
    }
    
    public ArrayList<String> getDoctors() {
    	return this.doctors;
    }
    
    public void calculeNbSemaines() {
    	int total_days = 0; //Nombre de jours dans la période considéré
    	int current_month = this.getStartMonth(); //Numéro du mois courant
    	int current_year = this.getStartYear(); //Année courante
    	
    	//Nombre de jours dans le premier mois
    	int first_lundi = Tools.getNumeroFirstLundi(this.getStartYear(), this.getStartMonth());
    	int days_in_first_month = Tools.getNumberOfDays(this.getStartYear(), this.getStartMonth());
    	total_days = days_in_first_month - (first_lundi-1);
    	
    	//Nombre de jours dans les mois suivants
    	current_month++;
    	if (current_month == 12) { current_month=0; current_year=this.getStartYear()+1; }
    	for (int i=1; i<horizon; i++) {
    		total_days+=Tools.getNumberOfDays(current_year, current_month);
    		current_month++;
    		if (current_month == 12) { current_month=0; current_year=this.getStartYear()+1; }
    	}
    	
    	//Calcule un nombre de semaines
    	this.nb_semaines = (int)(Math.ceil(total_days/7.0));
    }
}