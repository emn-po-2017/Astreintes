package utils;

import java.util.Calendar;

public class Tools {

	/**
	 * Les WEEK_DAY de java.util.Calendar ne sont pas "bien" ordonnés.
	 * Par exemple avec Calendar Lundi = 2 -> on voudrait que Lundi = 0
	 * 2 : Lundi -> 0
	 * 3 : Mardi -> 1
	 * 4 : Mercredi -> 2
	 * 5 : Jeudi -> 3
	 * 6 : Vendredi -> 4
	 * 7 : Samedi -> 5
	 * 1 : Dimanche -> 6
	 */
	public static int getWeekDay(int d) {
		return (d==1) ? 6 : d-2; 
	}

	/**
	 * Retourne le nombre de jours d'un mois
	 */
	public static int getNumberOfDays(int year, int month) {
        int nb_days;
        switch (month) {
            case 0: nb_days=31; break;
            case 1: nb_days=(year%4==0 && year%100!=0)?29:28; break;
            case 2: nb_days=31; break;
            case 3: nb_days=30; break;
            case 4: nb_days=31; break;
            case 5: nb_days=30; break;
            case 6: nb_days=31; break;
            case 7: nb_days=31; break;
            case 8: nb_days=30; break;
            case 9: nb_days=31; break;
            case 10: nb_days=30; break;
            case 11: nb_days=31; break;
            default: nb_days=-1; break;
        }
        return nb_days;
    }
 
	/**
	 * Retourne le nom du mois correspondant à l'entier donné en entrée
	 */
    public static String getMonth(int month) {
        String string_month;
        switch (month) {
            case 0: string_month="Janvier"; break;
            case 1: string_month="Février"; break;
            case 2: string_month="Mars"; break;
            case 3: string_month="Avril"; break;
            case 4: string_month="Mai"; break;
            case 5: string_month="Juin"; break;
            case 6: string_month="Juillet"; break;
            case 7: string_month="Aout"; break;
            case 8: string_month="Septembre"; break;
            case 9: string_month="Octobre"; break;
            case 10: string_month="Novembre"; break;
            case 11: string_month="Décembre"; break;
            default: string_month="Invalid month"; break;
        }
        return string_month;
    }
    
    /**
	 * Retourne l'entier correspondant à un mois (String en entrée)
	 */
    public static int getMonth(String string_month) {
        int month;
        switch (string_month) {
            case "Janvier": month=0; break;
            case "Février": month=1; break;
            case "Mars": month=2; break;
            case "Avril": month=3; break;
            case "Mai": month=4; break;
            case "Juin": month=5; break;
            case "Juillet": month=6; break;
            case "Aout": month=7; break;
            case "Septembre": month=8; break;
            case "Octobre": month=9; break;
            case "Novembre": month=10; break;
            case "Décembre": month=11; break;
            default: month=-1; break;
        }
        return month;
    }
    
    /**
     * Retourne la date du premier lundi du mois
     */
    public static int getNumeroFirstLundi(int year, int month) {
    	Calendar c = Calendar.getInstance();
    	c.set(year, month, 1, 0, 0, 0); //1er MM YYYY
    	int week_day = Tools.getWeekDay(c.get(Calendar.DAY_OF_WEEK));
    	if (week_day==0) {return 1;} //Month start with a Monday -> 1 
    	if (week_day==1) {return 7;} //Month start with a Tuesday -> 7
    	if (week_day==2) {return 6;} //Month start with a Wednesday -> 6
    	if (week_day==3) {return 5;} //Month start with a Thursday -> 5
    	if (week_day==4) {return 4;} //Month start with a Friday -> 4
    	if (week_day==5) {return 3;} //Month start with a Saturday -> 3
    	if (week_day==6) {return 2;} //Month start with a Sunday -> 2
    	return week_day;
    }
    
    public static void main(String[] args) {
		for (int i=0; i<12; i++) {
			
			int a = (i+6-1)%12;
			System.out.println("départ :" + i + "  fin :" + a);
		}
	}
}
