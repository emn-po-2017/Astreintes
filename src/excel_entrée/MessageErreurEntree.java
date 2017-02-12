package excel_entrée;

import javax.swing.JOptionPane;

public class MessageErreurEntree {

	private boolean error;
	private String msg;
	
	public MessageErreurEntree(Read_Conges conges) {
		this.error = false;
		this.msg = "";
		String log_conges = conges.getLogConges();
		if (log_conges != "") {
			error = true;
			msg += log_conges;
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
