package components;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class RequestInput {
  public RequestInput(){

	  JFrame frame = new JFrame("Project VEM");
      frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
      
	  JTextField graphName = new JTextField();
      Object [] msg = {"Graph Name:", graphName};
      JOptionPane op = new JOptionPane(msg,JOptionPane.QUESTION_MESSAGE,JOptionPane.OK_CANCEL_OPTION, null, null);
      JDialog dialog = op.createDialog(frame,("Enter graph name please."));
      dialog.setVisible(true);
      int uresult = JOptionPane.OK_OPTION;
      String graph_Name = "";
      
      try
		{
		    uresult = ((Integer)op.getValue()).intValue();
		}
		catch(Exception uninitializedValue)
		{}

		if(uresult == JOptionPane.OK_OPTION)
		{
			System.out.println(graphName.getText());
			graph_Name = graphName.getText();
		}
		else
		{
			System.out.println("Canceled");
		}
      
			
      
     	
     	 try{
             FileWriter fstream = new FileWriter("graphHistory.txt",true);
             BufferedWriter out = new BufferedWriter(fstream);
             
           
             out.newLine();
             out.write("############");
             out.newLine();
             out.write("Graph Name:"+graph_Name);
             out.newLine();
             out.write("Created on:"+(new Date()).toString());
             
             
             out.close();
             }catch (Exception e2) {
             	System.err.println("Error writing file: " + e2.getMessage());
             	
             }
  }
}