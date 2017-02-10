package excel_entrée;

import jxl.Sheet;
import utils.Tools;

public class Read_Preferences {

	private int[][] preferences; //[nbMedecins][nbDeJours] = 0 ou 1
	
	public Read_Preferences(Sheet sheet, Read_Informations infos) {
		this.preferences = new int[infos.getDoctors().size()][7];
		for (int i=0; i<preferences.length; i++) {
			for (int j=0; j<preferences[i].length; j++) {
				preferences[i][j] = 1;
			}
		}
		
		for (int l=2; l < sheet.getRows(); l++) {
			String doc = sheet.getCell(0, l).getContents();
			if (doc != infos.getDoctors().get(l-2)) {
				throw new Error("Les docteurs n'ont pas été rentrés"
						+ "dans l'ordre dans le excel des préférences");
			}

			/*
			 * col = 1 -> préférence semaine 1
			 * col = 2 -> préférence semaine 2
			 * col = 3 -> préférence week-end
			 */
			for (int col=1; col<4; col++) {
				String day = sheet.getCell(col, l).getContents();
				int dayweek = Tools.getDayWeek(day); //numéro du jour de la semaine
				if (dayweek != -1) { 
					preferences[l-2][dayweek] = 0;
				}
			}
		}
	}
	
	public int[][] getPrefs() {
		return this.preferences;
	}
	
}
