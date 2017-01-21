package excel_sortie;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import excel_entrée.Read_Informations;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


public class WriteOutput {

	private String path; //chemin d'accès
	private int[] resultat_solver; //tableau résultant du solver (médecin/jour)
	private Read_Informations infos; //informations données en entrée

	public WriteOutput(String path, int[] resultat_solver, Read_Informations infos) {
		this.path = path;
		this.resultat_solver = resultat_solver;
		this.infos = infos;
	}

    public void write() throws IOException, WriteException {
    	//Création du document final
        File file = new File(this.path);
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            
        //Ajout et écriture d'une feuille "Calendrier"
        workbook.createSheet("Calendrier", 0);
        WritableSheet _calendrier = workbook.getSheet(0);
        Calendrier calendrier = new Calendrier(_calendrier, resultat_solver, infos);
        calendrier.createCalendar();

        workbook.write();
        workbook.close();
    }
}