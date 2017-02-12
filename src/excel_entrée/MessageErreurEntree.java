package excel_entrée;

import javax.swing.JOptionPane;

/**
 * Collecter les erreurs d'entrées dans le excel Saisie
 * Afficher les erreurs à l'utilisateur
 *
 */
public class MessageErreurEntree {

	private boolean error;
	private String msg;
	
	public MessageErreurEntree(Read_Preferences prefs, Read_Conges conges) {
		this.error = false;
		this.msg = "";
		String log_conges = conges.getLogConges();
		String log_prefs = prefs.getLogPrefs();
		if (log_conges != "") {
			error = true;
			msg += log_conges + "\n";
		}
		if (log_prefs != "") {
			error = true;
			msg += log_prefs + "\n";
		}
		
	}
	
	public boolean isError() {
		return this.error;
	}
	
	public String getErrorMsg() {
		return this.msg;
	}
	
	public void displayError() {
		JOptionPane d = new JOptionPane();
		JOptionPane.showMessageDialog(
				d, 
				getErrorMsg(), 
				"Erreur",
				JOptionPane.ERROR_MESSAGE);
	}
}
