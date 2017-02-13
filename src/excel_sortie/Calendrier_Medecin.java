package excel_sortie;

import java.util.Calendar;

import excel_entrée.Read_Informations;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
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
 * qui contient le calendrier d'un médecin.
 * 
 */
public class Calendrier_Medecin {

	private static final String[] WEEK_DAYS = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
	
	private WritableSheet sheet; //Feuille excel
	private Read_Informations infos; //Données en entrée
	private int[] resultats; //Tableau de résultats
	private int day_number; //Numéro du jour courant (de 0 à nbDeSemaines*7 - 1)
	private int id; //Id du médecin considéré
	
	private int col; //Curseur colonne
	private int lin; //Curseur ligne
	
	public Calendrier_Medecin(WritableSheet sheet, int[] resultat_solver, Read_Informations infos, int id) {
		this.sheet = sheet;
		this.infos = infos;
		this.resultats = resultat_solver;
		this.day_number = 0;
		this.id = id;
		this.col = 0;
		this.lin = 0;
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
    		lin++;
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
		
    	//1. Ajout du nom du mois
		WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 10);
		cellFont.setBoldStyle(WritableFont.BOLD);
		WritableCellFormat format_mois = new WritableCellFormat(cellFont);
		format_mois.setBorder(Border.ALL, BorderLineStyle.THIN);
		format_mois.setBackground(Colour.TAN);
    	String name_month = Tools.getMonth(month);
    	sheet.addCell(new Label(col, lin, name_month, format_mois));
    	lin++;
    	
    	//2. Ajout des jours de la semaine
    	WritableCellFormat format_jour = new WritableCellFormat();
	    format_jour.setBorder(Border.ALL, BorderLineStyle.THIN);
		format_jour.setBackground(Colour.IVORY);
    	for (int i=0; i<WEEK_DAYS.length; i++) {
        	sheet.addCell(new Label(col+i, lin, WEEK_DAYS[i], format_jour));
        }
    	lin++;
    	
    	//Combien de jours compte le mois courant ?
    	int nb_jours = Tools.getNumberOfDays(year, month);

    	//Quel jour tombe le premier du mois ?
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(year, month, 1, 0, 0, 0);
    	int week_day = Tools.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
    	
    	//3. Remplissage des jours
    	int day = 1;
    	col = week_day;
    	
    	int offset=0; //offset utilisé que pour la représentation du premier mois
    	if (month == infos.getStartMonth()) {
    		offset = Tools.getNumeroFirstLundi(infos.getStartYear(), infos.getStartMonth()) - 1;
    	}
    	
    	while (day <= nb_jours) {
    		while (col<7 && day <= nb_jours) {
    			if (offset > 0) { //tant qu'on a un offset, aucun médecin n'est ajouté
    				sheet.addCell(new Number(col, lin, day));
    				offset--;
    			}
    			else {
    				int doc = (resultats[day_number] == id) ? id : -1;
    				sheet.addCell(new Number(col, lin, day));
    				if (doc != -1) {
    					sheet.addCell(new Label(col, lin+1, "", getCellFormat(doc))); //ajout du doc
    				}
        			day_number++;
    			}
    			col++;
    			day++;
    		}
    		col=0;
    		lin = lin + 3;
    	}
    	
    	//Pour le dernier mois, on termine la dernière semaine
    	//avec les jours du mois suivant
    	if (month == (infos.getHorizon()-1+infos.getStartMonth())%12) {
    		Calendar c = Calendar.getInstance();
        	c.set(year, month, Tools.getNumberOfDays(year, month), 0, 0, 0);
        	int w_d = Tools.getWeekDay(c.get(Calendar.DAY_OF_WEEK));
        	col = w_d + 1;
        	lin = lin - 3;
        	if (col==7) { col=0; }
        	int extra_day = 1;
        	while (col<7) {
        		int doc = (resultats[day_number] == id) ? id : -1;
        		sheet.addCell(new Number(col, lin, extra_day));
        		if (doc != -1) {
        			sheet.addCell(new Label(col, lin+1, "", getCellFormat(doc))); //ajout du doc
        		}
        		day_number++;
        		extra_day++;
        		col++;
        	}
        	lin = lin + 3;
    	}
    }
	
	  /**
	   * Permet d'ajouter une couleur à une cellule
	   */
	  public static WritableCellFormat getCellFormat(int i) throws WriteException {
		  Colour colour = getColour(i);
		  WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 10);
		  WritableCellFormat cellFormat = new WritableCellFormat(cellFont);
		  cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
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
		  case 1: colour=Colour.LIME; break;
		  case 2: colour=Colour.GREEN; break;
		  case 3: colour=Colour.LAVENDER; break;
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
		  
		  //Format avec bordures pour cellule
		  WritableCellFormat format = new WritableCellFormat();
		  format.setBorder(Border.ALL, BorderLineStyle.THIN);
		  
		  //1. Nom du médecin et couleur associée
		  int length_name = infos.getDoctors().get(id).length();
		  Label c = new Label(col, lig, infos.getDoctors().get(id), format);
		  c.getCellFormat();
		  sheet.setColumnView(col, length_name); //on ajuste la taille de la colonne au nom du médecin
		  sheet.addCell(c);
		  sheet.addCell(new Label(col+1, lig, "", getCellFormat(id)));
		  
		  //2. Nombre d'astreintes totales
		  sheet.addCell(new Label(col+2, lig-1, "Total", format));
		  sheet.addCell(new Number(col+2, lig, this.count_total(id), format));
		  
		  //3. Nombre d'astreintes week-semaine
		  sheet.addCell(new Label(col+3, lig-1, "Semaine", format));
		  sheet.addCell(new Number(col+3, lig, this.count_semaine(id), format));
		  
		  //4. Nombre d'astreintes week-end
		  sheet.addCell(new Label(col+4, lig-1, "WE", format));
		  sheet.addCell(new Number(col+4, lig, this.count_we(id), format));
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
	  
	  /**
	   * Compte le nombre d'astreintes en week-end pour un médecin
	   */
	  public int count_we(int i) {
		  int count = 0;
		  int k = 5;
		  while (k < infos.getNbSemaines() * 7) {
			  if (resultats[k] == i || resultats[k+1] == i) {
				  count++;
			  }
			  k+=7;
		  }
		  return count;
	  }
	  
	  /**
	   * Compte le nombre d'astreintes en semaine pour un médecin
	   */
	  public int count_semaine(int i) {
		  return count_total(i) - count_we(i);
	  }
}