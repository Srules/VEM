
package components;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.*;


import MyGraph.MyGraph;



/* graphChooser.java requires no other files. */
public class graphChooser extends JPanel
                      implements ListSelectionListener {
    private JList list;
    private DefaultListModel listModel;

    private static final String SearchString = "Search";
    private static final String showString = "Show";

    private JButton showButton;
    private JButton searchButton;

    private ArrayList <MyGraph>mG;
    private JFrame frame;
    public int returnIndex = -1;
    
    
    private JTextField graphName;

    public graphChooser(ArrayList <MyGraph> mGraphs) {
        super(new BorderLayout());

        mG = mGraphs;
        listModel = new DefaultListModel();
        
        for (int i = 0; i < mGraphs.size(); i++)
        {
        	listModel.addElement(mGraphs.get(i).name + " | " +mGraphs.get(i).createdOn);
        }


        
        
        
        //Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);

        JButton SearchButton = new JButton(SearchString);
        SearchListener SearchListener = new SearchListener(SearchButton);
        SearchButton.setActionCommand(SearchString);
        SearchButton.addActionListener(SearchListener);
        SearchButton.setEnabled(false);
        
        showButton = new JButton(showString);
        showButton.setActionCommand(showString);
        showButton.addActionListener(new showListener());


        graphName = new JTextField(10);
        graphName.addActionListener(SearchListener);
        graphName.getDocument().addDocumentListener(SearchListener);
        String name = listModel.getElementAt(
                              list.getSelectedIndex()).toString();

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));

        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(showButton);
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(graphName);
        buttonPane.add(SearchButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    class showListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int index = list.getSelectedIndex();
            String gName = listModel.get(index).toString();

            for(int i = 0; i <mG.size();i++)
            {
            	
            	String compareMe = mG.get(i).name+ " | " +mG.get(i).createdOn;
            	System.out.println("TEST:"+compareMe);
            	if(compareMe.equals(gName))
            	{
            		returnIndex = i;
            		//frame.setVisible(false);
            		break;
            	}
            }
            
            
            
        }
    }

    //This listener is shared by the text field and the Search button.
    class SearchListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public SearchListener(JButton button) {
            this.button = button;
        }

        //Required by ActionListener.
        public void actionPerformed(ActionEvent e) {
            String name = graphName.getText();
            ArrayList <MyGraph> mGs = new ArrayList<MyGraph>();
            int found = 0;
            int loc = 0;
            for (int i = 0; i < mG.size(); i++)
            {
            	if(mG.get(i).name.toLowerCase().contains(name.toLowerCase()))
            	{
            		mGs.add(mG.get(i));
            		found++;
            		loc = i;
            	}
            }
            System.out.println("Found:"+found);
            if(found == 1)
            {
            	returnIndex = loc;
            	//frame.setVisible(false);
            }
            if(found > 1)
            {	listModel.removeAllElements();
            	for (int i = 0; i < mGs.size(); i++)
                {
            		
                	listModel.addElement(mGs.get(i).name + " | " +mGs.get(i).createdOn);
                }
            }
            if(found == 0)
            {
            	System.out.println("Nothing found..");
            }
            
           
            //Reset the text field.
            graphName.requestFocusInWindow();
            graphName.setText("");

            //Select the new item and make it visible.
            list.setSelectedIndex(loc);
            list.ensureIndexIsVisible(loc);
        }

        //This method tests for string equality. You could certainly
        //get more sophisticated about the algorithm.  For example,
        //you might want to ignore white space and capitalization.
        protected boolean alreadyInList(String name) {
            return listModel.contains(name);
        }

        //Required by DocumentListener.
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        //Required by DocumentListener.
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        //Required by DocumentListener.
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (list.getSelectedIndex() == -1) {
            //No selection, disable fire button.
                //fireButton.setEnabled(false);

            } else {
            //Selection, enable the fire button.
                //fireButton.setEnabled(true);
            }
        }
    }

    /**
     * Create the GUI and Search it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     * @param gc 
     */
    private  void createAndShowGUI(graphChooser gc) {
        //Create and set up the window.
        frame = new JFrame("graphChooser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        JComponent newContentPane = gc;
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public void psuedomain(final graphChooser gc) {
        //Schedule a job for the event-dispatching thread:
        //creating and Searching this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(gc);
            }
        });
    }
}
