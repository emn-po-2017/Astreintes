package excel_sortie;

import java.util.Calendar;

import excel_entrée.Read_Informations;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import utils.Tools;

/*
 * 
 * Cette classe permet la création d'une feuille excel
 * qui contient le calendrier général des astreintes.
 * 
 */
public class Calendrier {

	private static final String[] WEEK_DAYS = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
	
	private WritableSheet sheet; //Feuille excel
	private Read_Informations infos; //Données en entrée
	private int[] resultats; //Tableau de résultats
	private int day_number; //Numéro du jour courant (de 0 à nbDeSemaines*7 - 1)
	
	private static int COL=0; //Curseur colonne
	private static int LIN=0; //Curseur ligne
	
	public Calendrier(WritableSheet sheet, int[] resultat_solver, Read_Informations infos) {
		this.sheet = sheet;
		this.infos = infos;
		this.resultats = resultat_solver;
		this.day_number = 0;
	}
	
	/**
	 * Méthode principale : création du calendrier
	 */
    public void createCalendar()
    		throws RowsExceededException, WriteException {
    	
    	int month = infos.getStartMonth(); //mois courant (compteur de mois)
    	int year = infos.getStartYear(); //année courante (compteur d'année)
    	
    	//Ajout des mois au calendrier
    	for (int i=0; i<infos.getHorizon(); i++) {
    		_createMonth(year, month);
    		month++;
    		if (month==12) {year++; month=0;}
    		LIN++;
    	}
    	
    	//Ajout d'une légende
    	createLegend();
    }
    
    /**
     * Ajoute un mois au calendrier
     */
	public void _createMonth(
			int year, int month)
    		throws RowsExceededException, WriteException {
		
    	//Ajout du nom du mois
    	String name_month = Tools.getMonth(month);
    	sheet.addCell(new Label(COL, LIN, name_month));
    	LIN++;
    	
    	//Ajout des jours de la semaine
    	for (int i=0; i<WEEK_DAYS.length; i++) {
        	sheet.addCell(new Label(COL+i, LIN, WEEK_DAYS[i]));
        }
    	LIN++;
    	
    	//Combien de jours compte le mois courant ?
    	int nb_jours = Tools.getNumberOfDays(year, month);

    	//Quel jour tombe le premier du mois ?
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(year, month, 1, 0, 0, 0);
    	int week_day = Tools.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
    	
    	//Remplissage des jours
    	int day = 1;
    	COL = week_day;
    	
    	int offset=0; //offset utilisé que pour la représentation du premier mois
    	if (month == infos.getStartMonth()) {
    		offset = Tools.getNumeroFirstLundi(infos.getStartYear(), infos.getStartMonth()) - 1;
    	}
    	
    	while (day <= nb_jours) {
    		while (COL<7 && day <= nb_jours) {
    			if (offset > 0) { //tant qu'on a un offset, aucun médecin n'est ajouté
    				sheet.addCell(new Number(COL, LIN, day));
    				offset--;
    			}
    			else {
    				sheet.addCell(new Number(COL, LIN, day));
    				sheet.addCell(new Label(COL, LIN+1, "", getCellFormat(resultats[day_number]))); //ajout du doc
        			day_number++;
    			}
    			COL++;
    			day++;
    		}
    		COL=0;
    		LIN = LIN + 3;
    	}
    	
    	//Pour le dernier mois, on termine la dernière semaine
    	//avec les jours du mois suivant
    	if (month == (infos.getHorizon()-1+infos.getStartMonth())%12) {
    		Calendar c = Calendar.getInstance();
        	c.set(year, month, Tools.getNumberOfDays(year, month), 0, 0, 0);
        	int w_d = Tools.getWeekDay(c.get(Calendar.DAY_OF_WEEK));
        	COL = w_d + 1;
        	LIN = LIN - 3;
        	if (COL==7) { COL=0; }
        	int extra_day = 1;
        	while (COL<7) {
        		sheet.addCell(new Number(COL, LIN, extra_day));
        		sheet.addCell(new Label(COL, LIN+1, "", getCellFormat(resultats[day_number]))); //ajout du doc
        		day_number++;
        		extra_day++;
        		COL++;
        	}
        	LIN = LIN + 3;
    	}
    }
	
	  /**
	   * Permet d'ajouter une couleur à une cellule
	   */
	  public static WritableCellFormat getCellFormat(int i) throws WriteException {
		  Colour colour = getColour(i);
		  WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 10);
		  WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
		  cellFormat.setBackground(colour);
		  return cellFormat;
	  }
	  
	  /**
	   * Retourne une couleur en fonction de l'indice
	   */
	  public static Colour getColour(int i) {
		  Colour colour;
		  switch (i) {
		  case 0: colour=Colour.BLUE2; break;
		  case 1: colour=Colour.ORANGE; break;
		  case 2: colour=Colour.GREEN; break;
		  case 3: colour=Colour.PINK2; break;
		  case 4: colour=Colour.PALE_BLUE; break;
		  case 5: colour=Colour.RED; break;
		  case 6: colour=Colour.GOLD; break;
		  default: colour=Colour.VIOLET2; break;
		  }
		  return colour;
	  }
	  
	  //============================================================================
	  //====== A AMELIORER (formules pour calcul automatique ensuite dans excel)====
	  //============================================================================
	  
	  /**
	   * Ajout d'une légende (nom médecin, couleur et nb d'astreintes associés)
	   */
	  public void createLegend() throws RowsExceededException, WriteException {
		  int col = 9;
		  int lig = 3;
		  int nb_doctors = infos.getDoctors().size();
		  for (int i=0; i<nb_doctors; i++) {
			  sheet.addCell(new Label(col, lig+i, "medecin " + i));
			  sheet.addCell(new Label(col+1, lig+i, "", getCellFormat(i)));
		  }
		  
		  sheet.addCell(new Label(col+2, lig-1, "total"));
		  for (int i=0; i<nb_doctors; i++) {
			  sheet.addCell(new Number(col+2, lig + i, this.count_total(i)));
		  }
	  }
	  
	  /**
	   * Compte le nombre total d'astreintes pour un médecin
	   */
	  public int count_total(int i) {
		  int count = 0;
		  for (int k=0; k<resultats.length; k++) {
			  if (resultats[k] == i) {
				  count++;
			  }
		  }
		  return count;
	  }
}