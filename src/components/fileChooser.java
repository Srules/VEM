package components;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;

/*
 * fileChooser.java requires these files:
 *   ImageFileView.java
 *   ImageFilter.java
 *   ImagePreview.java
 *   Utils.java
 *   images/jpgIcon.gif (required by ImageFileView.java)
 *   images/gifIcon.gif (required by ImageFileView.java)
 *   images/tiffIcon.gif (required by ImageFileView.java)
 *   images/pngIcon.png (required by ImageFileView.java)
 */
public class fileChooser extends JPanel
                              implements ActionListener {
    static private String newline = "\n";
    private JTextArea log;
    private JFileChooser fc;
    File file = null;
    int count =0;
    public fileChooser() {
        super(new BorderLayout());

        //Create the log first, because the action listener
        //needs to refer to it.
        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        JButton sendButton = new JButton("Attach...");
        sendButton.addActionListener(this);

        add(sendButton, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        //Set up the file chooser.
        if (fc == null) {
            fc = new JFileChooser();
            

	    //Add a custom file filter and disable the default
	    //(Accept All) file filter.
            //fc.addChoosableFileFilter(new ImageFilter());
           fc.setAcceptAllFileFilterUsed(true);

	    //Add custom icons for file types.
           // fc.setFileView(new ImageFileView());

	    //Add the preview pane.
           // fc.setAccessory(new ImagePreview(fc));
        }

        //Show it.
        int returnVal = fc.showDialog(fileChooser.this,
                                      "Attach");

        //Process the results.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            System.out.println(file);
            log.append("Attaching file: " + file.getName()
                       + "." + newline);
        } else {
            log.append("Attachment cancelled by user." + newline);
        }
        log.setCaretPosition(log.getDocument().getLength());

        System.out.println(file);
        System.out.println(getFile());
        System.out.println(fcNULL());
        //Reset the file chooser for the next time it's shown.
        //fc.setSelectedFile(null);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public File getFile()
    {
    	return file;
    }
    public boolean fcNULL()
    {
    	
    	//System.out.print("Performing check number.."+count);
    	//count++;
    	if(file == null)
    	{
    		//System.out.print(" t ");
    		//System.out.println();
    		return true;
    	}
    	//System.out.print(" f ");
    	//System.out.println();
    	return false;
    }
    public static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("fileChooser");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        //Add content to the window.
        frame.add(new fileChooser());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}