package components;

// "Java Tech"
//  Code provided with book for educational purposes only.
//  No warranty or guarantee implied.
//  This code freely available. No copyright claimed.
//  2003
//

// Begun from StartJApp3

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
  * Demonstrate the use of a JFileChooser to open and
  * save files. The chooser filers for *.java files. Opening
  * the file results in the text filling a JTextArea component.
  * The text can be modified and saved to a new file.<br><br>
 **/
public class FileChooseApp extends JFrame
       implements ActionListener
{

  JMenuItem fMenuOpen = null;
  JMenuItem fMenuSave  = null;
  JMenuItem fMenuClose = null;

  JTextArea fTextArea;

  JavaFilter fJavaFilter = new JavaFilter ();
  File fFile = new File ("default.java");

  /** Create a frame with JTextArea and a menubar
    * with a "File" dropdown menu.
   **/
  FileChooseApp (String title) {
    super (title);

    Container content_pane = getContentPane ();

    // Create a user interface.
    content_pane.setLayout ( new BorderLayout () );
    fTextArea = new JTextArea ("");

    content_pane.add ( fTextArea, "Center");


    // Use the helper method makeMenuItem
    // for making the menu items and registering
    // their listener.
    JMenu m = new JMenu ("File");

    // Modify task names to something relevant to
    // the particular program.
    m.add (fMenuOpen  = makeMenuItem ("Open"));
    m.add (fMenuOpen  = makeMenuItem ("Save"));
    m.add (fMenuClose = makeMenuItem ("Quit"));

    JMenuBar mb = new JMenuBar ();
    mb.add (m);

    setJMenuBar (mb);
    setSize (400,400);
  } // ctor

  /** Process events from the chooser. **/
  public void actionPerformed ( ActionEvent e ) {
    boolean status = false;

    String command = e.getActionCommand ();
    if  (command.equals ("Open")) {
    // Open a file
    status = openFile ();
    if (!status)
        JOptionPane.showMessageDialog (
          null,
          "Error opening file!", "File Open Error",
          JOptionPane.ERROR_MESSAGE
        );

    } else if (command.equals ("Save")) {
        // Save a file
        status = saveFile ();
        if (!status)
            JOptionPane.showMessageDialog (
              null,
              "IO error in saving file!!", "File Save Error",
              JOptionPane.ERROR_MESSAGE
            );

    } else if (command.equals ("Quit") ) {
        dispose ();
    }
  } // actionPerformed

  /** This "helper method" makes a menu item and then
    * registers this object as a listener to it.
   **/
  private JMenuItem makeMenuItem (String name) {
    JMenuItem m = new JMenuItem (name);
    m.addActionListener (this);
    return m;
  } // makeMenuItem

  /**
    * Use a JFileChooser in Open mode to select files
    * to open. Use a filter for FileFilter subclass to select
    * for *.java files. If a file is selected then read the
    * file and place the string into the textarea.
   **/
  boolean openFile () {

      JFileChooser fc = new JFileChooser ();
      fc.setDialogTitle ("Open File");

      // Choose only files, not directories
      fc.setFileSelectionMode ( JFileChooser.FILES_ONLY);

      // Start in current directory
      fc.setCurrentDirectory (new File ("."));

      // Set filter for Java source files.
      fc.setFileFilter (fJavaFilter);

      // Now open chooser
      int result = fc.showOpenDialog (this);

      if (result == JFileChooser.CANCEL_OPTION) {
          return true;
      } else if (result == JFileChooser.APPROVE_OPTION) {

          fFile = fc.getSelectedFile ();
          // Invoke the readFile method in this class
          String file_string = readFile (fFile);

          if (file_string != null)
              fTextArea.setText (file_string);
          else
              return false;
      } else {
          return false;
      }
      return true;
   } // openFile


  /**
    * Use a JFileChooser in Save mode to select files
    * to open. Use a filter for FileFilter subclass to select
    * for "*.java" files. If a file is selected, then write the
    * the string from the textarea into it.
   **/
   boolean saveFile () {
     File file = null;
     JFileChooser fc = new JFileChooser ();

     // Start in current directory
     fc.setCurrentDirectory (new File ("."));

     // Set filter for Java source files.
     fc.setFileFilter (fJavaFilter);

     // Set to a default name for save.
     fc.setSelectedFile (fFile);

     // Open chooser dialog
     int result = fc.showSaveDialog (this);

     if (result == JFileChooser.CANCEL_OPTION) {
         return true;
     } else if (result == JFileChooser.APPROVE_OPTION) {
         fFile = fc.getSelectedFile ();
         if (fFile.exists ()) {
             int response = JOptionPane.showConfirmDialog (null,
               "Overwrite existing file?","Confirm Overwrite",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE);
             if (response == JOptionPane.CANCEL_OPTION) return false;
         }
         return writeFile (fFile, fTextArea.getText ());
     } else {
       return false;
     }
  } // saveFile

  /** Use a BufferedReader wrapped around a FileReader to read
    * the text data from the given file.
   **/
  public String readFile (File file) {

    StringBuffer fileBuffer;
    String fileString=null;
    String line;

    try {
      FileReader in = new FileReader (file);
      BufferedReader dis = new BufferedReader (in);
      fileBuffer = new StringBuffer () ;

      while ((line = dis.readLine ()) != null) {
            fileBuffer.append (line + "\n");
      }

      in.close ();
      fileString = fileBuffer.toString ();
    }
    catch  (IOException e ) {
      return null;
    }
    return fileString;
  } // readFile


  /**
    * Use a PrintWriter wrapped around a BufferedWriter, which in turn
    * is wrapped around a FileWriter, to write the string data to the
    * given file.
   **/
  public static boolean writeFile (File file, String dataString) {
    try {
       PrintWriter out =
         new PrintWriter (new BufferedWriter (new FileWriter (file)));
       out.print (dataString);
       out.flush ();
       out.close ();
    }
    catch (IOException e) {
       return false;
    }
    return true;
  } // writeFile

  /** Create the framed application and show it. **/
  public static void main (String [] args) {
    // Can pass frame title in command line arguments
    String title="Frame Test";
    if (args.length != 0) title = args[0];
    FileChooseApp f = new FileChooseApp (title);
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    f.setVisible (true);
  } // main

}// class FileChooseApp