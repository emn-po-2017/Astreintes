package utils;

import excel_entrée.Read_Informations;

public class Legend {

	/**
	   * Compte le nombre total d'astreintes pour un médecin
	   */
	  public static int count_total(int i, int[] resultats) {
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
	  public static int count_we(int i, int[] resultats, Read_Informations infos) {
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
	  public static int count_semaine(int i, int[] resultats, Read_Informations infos) {
		  return count_total(i, resultats) - count_we(i, resultats, infos);
	  }
}
