package components;

import javax.swing.JOptionPane;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class Question {
	String question = "";
	public Question(String q)
	{
		question = q;
	}
  public boolean ask () {
    JDialog.setDefaultLookAndFeelDecorated(true);
    int response = JOptionPane.showConfirmDialog(null, question, "Project VEM",
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    if (response == JOptionPane.NO_OPTION) {
      return false;
    } else if (response == JOptionPane.YES_OPTION) {
      return true;
    } else if (response == JOptionPane.CLOSED_OPTION) {
      return false;
    }
	return false;
  }
  
  
}