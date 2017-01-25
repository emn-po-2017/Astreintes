package excel_entrée;

import java.io.IOException;

import jxl.Sheet;
import utils.Tools;

public class Read_Conges {
	
	private Read_Informations infos;
	
	private int[][] conges;
	
    public Read_Conges(Sheet sheet, Read_Informations infos) throws IOException {
    	this.infos = infos;
    	this.conges = new int[infos.getDoctors().size()][infos.getNbSemaines() * 7];
    	for (int i=0; i<conges.length; i++) {
    		for (int j=0; j<conges[i].length; j++) {
    			conges[i][j] = 0;
    		}
    	}
    	
    	int col_doc = 0;
    	int col_start = 1;
    	int col_end = 2;
    	for (int l=1; l<sheet.getRows(); l++) {
    		String doc = sheet.getCell(col_doc, l).getContents(); //docteur concerné par le congé
    		int id = infos.getDoctors().indexOf(doc); //id du docteur
    		
    		String start = sheet.getCell(col_start, l).getContents(); //date de début du congé
    		String end = sheet.getCell(col_end, l).getContents(); //date de fin de congé
    		for (int i=this.toNumberOfDays(start); i<=this.toNumberOfDays(end); i++) {
    			conges[id][i] = 1;
    		}
      }
    }
    
    
    /**
     * On retourne le nombre de jours entre :
     * - le mois de départ
     * - le paramètre d'entrée (de la forme d'une String "DD/MM/YYYY")
     */
    public int toNumberOfDays(String date) {
    	String[] decomposed = date.split("/"); //date de la forme 03/12/2017
    	int day = Integer.parseInt(decomposed[0]);
    	int month = Integer.parseInt(decomposed[1]) - 1;
    	int year = Integer.parseInt(decomposed[2]);
    	int compteur = day;
    	if(infos.getStartMonth() <= month) {
    		for (int i=infos.getStartMonth(); i<month; i++) {
    			compteur += Tools.getNumberOfDays(year, i);
    		}
    	}
    	else {
    		month = month + 12;
    		for (int i=infos.getStartMonth(); i<month; i++) {
    			if (month==12) {
    				year++;
    			}
    			compteur += Tools.getNumberOfDays(year, i%12);
    		}
    	}
    	compteur -= (Tools.getNumeroFirstLundi(infos.getStartYear(), infos.getStartMonth()) - 1);
    	return compteur - 1;
    }
    
    public int[][] getConges() {
    	return this.conges;
    }
    
}
