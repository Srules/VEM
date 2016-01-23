package components;

import javax.swing.*;
import java.io.*;

/** Filter to work with JFileChooser to select java file types. **/
public class JavaFilter extends javax.swing.filechooser.FileFilter
{
  public boolean accept (File f) {
    return f.getName ().toLowerCase ().endsWith (".java")
          || f.isDirectory ();
  }
 
  public String getDescription () {
    return "Java files (*.java)";
  }
} // class JavaFilter