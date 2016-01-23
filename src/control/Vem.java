package control;



import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.algorithms.layout.util.Relaxer;
import edu.uci.ics.jung.algorithms.layout.util.VisRunner;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.util.Animator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.functors.ConstantTransformer;

import MyGraph.MyGraph;



import components.Question;
import components.RequestInput;
import components.fileChooser;
import components.graphChooser;
import components.sortableTable;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Toolkit;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 *
 * @author Nicholas Schiavi
 */


public class Vem implements ClipboardOwner {
	String mathOutput = "";
	static final double version = 4.0;
	static Graph<Integer, String> g;
    static boolean verified = false;
    static boolean loadgraph = false;
    static int graphToLoad = -1;
	static int nodeCount;
	static int edgeCount;
    Factory <Integer> vertexFactory;
    Factory<String> edgeFactory;
    static ArrayList  myGraphs = new ArrayList <Graph>();
    
    /** Creates a new instance of SimpleGraphView */
    public Vem() {
        // Graph<V, E> where V is the type of the vertices and E is the type of the edges
        g = new SparseMultigraph<Integer, String>();
        nodeCount = 0; edgeCount = 0;
        vertexFactory = new Factory<Integer>() { // My vertex factory
            public Integer create() {
                return nodeCount++;
            }
        };
        edgeFactory = new Factory<String>() { // My edge factory
            public String create() {
                return "E"+edgeCount++;
            }
        };
        
    }

   
    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("unchecked")
	public static void main(String[] args) {
    	final Vem sgv = new Vem();
        // Layout<V, E>, VisualizationViewer<V,E>
    	
  	  //Create file to hold saved graphs
    	  File f = new File("graphHistory.txt");
    	  boolean canload = false;
    	//ASK TO LOAD GRAPH
    	  if(f.exists())
    	  {
    		  Question lg = new Question("Would you like to load a graph?");
    		  canload = lg.ask();
    	  }else{
    		  //Create new file if necessary

              	 try{
                       FileWriter fstream = new FileWriter("graphHistory.txt");
                       BufferedWriter out = new BufferedWriter(fstream);
                       out.write("Version:"+version);
                       out.close();
                       }catch (Exception e2) {
                       	System.err.println("Error writing file: " + e2.getMessage());
                       	
                       }
                       
                      
                  
              
              
              while(!f.exists())
              {
              	//waiting...
              }
    	  }
    	
    	if(canload)
    	{
            
            if(f.exists()){
            //Parse config file
            try {
                BufferedReader in = new BufferedReader(new FileReader("graphHistory.txt"));
                String str;
                
                //Verify version
            	 if ((str = in.readLine()) != null) {
                 	double historyVersion = Double.parseDouble(str.substring(str.indexOf(':')+1,str.length()));
                     if(historyVersion == version)
                     {
                     	System.out.println("Version verified.");
                     	verified = true;
                     }else{
                     	System.out.println("Found unexpected line in file: "+str);
                     }
                 }
                
            	//Parse Graph data
                if(verified)
                {
                	
                	MyGraph tempgraph = null;
                	while ((str = in.readLine()) != null) {
                		System.out.println("Parsing line:");
                		System.out.println(str);
                		/* Data format
                		 ############
                		  Graph Name:string
                		  Created on:date
                		  Total Nodes:int
                		  Total Edges:int
                		  Vertices:0,1,2,3,4
                		  Edge Data:Edge-B[1,2] Edge-A[0,1] E2[0,4] E4[0,2] E3[0,3] E5[3,0] E6[3,3] 
                		  Mathematica Output:
                		  MatrixForm[{{0,1,1,1,1},
						 	{1,0,1,0,0},
						 	{1,1,0,0,0},
							{1,0,0,1,0},
						 	{1,0,0,0,0}
						 	}]
                		   %%%%%%%%%%%%
                		 */
                		
                		if(str.contains("############"))
                		{
                			tempgraph = new MyGraph();
                		}else                		
                		if(str.contains("Graph Name:"))
                		{
                			if(tempgraph!=null)
                			{
                				tempgraph.name = str.substring(str.indexOf(':')+1,str.length());
                			}
                			
                		}else
                		if(str.contains("Created on:"))
                		{
                			if(tempgraph!=null)
                			{
                				tempgraph.createdOn = new Date();
                			}
                		}else
                		if(str.contains("Total Nodes:"))
                		{
                			if(tempgraph!=null)
                			{
                				tempgraph.totalnodes = Integer.parseInt(str.substring(str.indexOf(':')+1,str.length()));
                			}
                		}else
                		if(str.contains("Total Edges:"))
                		{
                			if(tempgraph!=null)
                			{
                				tempgraph.totaledges = Integer.parseInt(str.substring(str.indexOf(':')+1,str.length()));
                			}
                		}else	
                		if(str.contains("Vertices:"))
                		{
                			if(tempgraph!=null)
                			{
                				
                				String parseMe = str.substring(str.indexOf(':')+1,str.length());
                				System.out.println(parseMe);
                				String delims = "[,]";
                				String[] tokens = parseMe.split(delims);
                				for(int i = 0; i < tokens.length; i++)
                				{
                					tempgraph.g.addVertex(Integer.parseInt(tokens[i]));
                				}
                				
                			}
                		}else
                		if(str.contains("Edge Data:"))
                		{
                			if(tempgraph!=null)
                			{
                				String parseMe = str.substring(str.indexOf(':')+1,str.length());
                				String delims = "[ ]";
                				String[] tokens = parseMe.split(delims);
                				for(int i = 0; i < tokens.length; i++)
                				{
                					String s1 = tokens[i].substring(0,tokens[i].indexOf('['));
                					int s2 = (Integer)(Integer.parseInt(tokens[i].substring(tokens[i].indexOf('[')+1,tokens[i].indexOf(','))));
                					int s3 = (Integer)(Integer.parseInt(tokens[i].substring(tokens[i].indexOf(',')+1,tokens[i].indexOf(']'))));
                					System.out.println("S1:"+s1+"S2:"+s2+"S3:"+s3);
                					tempgraph.g.addEdge(s1, s2, s3); 
                				}
                				
                			}
                		}else
                		if(str.contains("%%%%%%%%%%%%"))
                		{               
                			//end of element
                			if(tempgraph!=null){
                			System.out.println("Displaying graph data for verification: " + tempgraph.g.toString());
                			
                			loadgraph = true;
                			
                			}
                			else{
                				System.out.println("Problem?");
                			}
                			myGraphs.add(new MyGraph(tempgraph.name,tempgraph.createdOn,tempgraph.totalnodes,tempgraph.totaledges,tempgraph.g,tempgraph.mathematicaOutput));
                		}
                		
                		/*  Input format
                		
                		    sgv.g.addVertex((Integer)0);
                	        sgv.g.addVertex((Integer)1);
                	        sgv.g.addVertex((Integer)2); 
                	        sgv.nodeCount = 3;
                	        
                	        sgv.g.addEdge("Edge-A", 0, 1); 
                	        sgv.g.addEdge("Edge-B", 1, 2);
                	        sgv.edgeCount = 2;
                	        */
                		
                		
                		
                		
                	}
                	//Display graph information, and allow user to select a graph to load
                	System.out.println("Parse complete.");
                }
               
                in.close();
            } catch (IOException e) {
            	
            }
            
            //System.out.println("MYCONF: "+myConf.getPlayer().getName()+" "+myConf.getHandHistoryPath());
            }
    		
            
    	}
    	
    	
        Layout<Integer, String> layout = new FRLayout(sgv.g);
        layout.setSize(new Dimension(300,300));
        //final VisualizationViewer<Integer,String> vv = new VisualizationViewer<Integer,String>(layout);
        final VisualizationViewer vv = new VisualizationViewer(layout);
        vv.setPreferredSize(new Dimension(350,350));
        // Show vertex and edge labels
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        // Create a graph mouse and add it to the visualization viewer
        // Our Vertices are going to be Integer objects so we need an Integer factory
        EditingModalGraphMouse gm = new EditingModalGraphMouse(vv.getRenderContext(), 
                 sgv.vertexFactory, sgv.edgeFactory); 
        vv.setGraphMouse(gm);

        
        JFrame frame = new JFrame("Graph matrix exporter for mathematica");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        
        // Let's add a menu for changing mouse modes
        JMenuBar menuBar = new JMenuBar();
        JMenu modeMenu = gm.getModeMenu();
        
        JMenu matrixMenu = new JMenu();
        matrixMenu.setText("Matrix");
        matrixMenu.setIcon(null);
        matrixMenu.setPreferredSize(new Dimension(80,20));
        
        JMenu fileMenu = new JMenu();
        fileMenu.setText("File");
        fileMenu.setIcon(null);
        fileMenu.setPreferredSize(new Dimension(80,20));
        
        modeMenu.setText("Mouse Mode");
        modeMenu.setIcon(null); // I'm using this in a main menu
        modeMenu.setPreferredSize(new Dimension(80,20)); // Change the size so I can see the text
        
        menuBar.add(modeMenu);
        
        menuBar.add(matrixMenu);
        JMenuItem copyMatrix = new JMenuItem("Copy Matrix to clipboard",KeyEvent.VK_C);
        copyMatrix.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                ActionEvent.CTRL_MASK));
        matrixMenu.add(copyMatrix);
        
        JMenuItem copyGraphSelection = new JMenuItem("Copy graph selection to clipboard",KeyEvent.VK_X);
        copyGraphSelection.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                ActionEvent.CTRL_MASK));
        matrixMenu.add(copyGraphSelection);
        
        JMenuItem pasteGraphSelection = new JMenuItem("Paste graph selection to model",KeyEvent.VK_V);
        pasteGraphSelection.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
                ActionEvent.CTRL_MASK));
        matrixMenu.add(pasteGraphSelection);
        
        menuBar.add(fileMenu);
        
        JMenuItem saveGraph = new JMenuItem("Save graph",KeyEvent.VK_S);
        saveGraph.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                ActionEvent.CTRL_MASK));
        fileMenu.add(saveGraph);
        
        /*JMenuItem loadGraph = new JMenuItem("Load graph",KeyEvent.VK_L);
        loadGraph.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
                ActionEvent.CTRL_MASK));
        fileMenu.add(loadGraph);
        */
        frame.setJMenuBar(menuBar);
        
        //Set up Matrix
        List<Integer[]> graphMatrix = new ArrayList<Integer[]>();
        //graphMatrix.add(new Integer[]{1, 2, 3});
        //graphMatrix.add(new Integer[]{4, 5 , 6, 7});
        
        
        if(loadgraph)
        {
        	
        	//Select Graph to load
        	//sortableTable st = new sortableTable(myGraphs);
        	graphChooser gc = new graphChooser(myGraphs);
        	gc.psuedomain(gc);
        	while(gc.returnIndex==-1);
        	graphToLoad = gc.returnIndex;
   
        	
        	
        	
        	
       
        	MyGraph curGraph = ((MyGraph)(myGraphs.get(graphToLoad)));
        	System.out.println(curGraph.name);
        	Graph<Integer, String> tempG = (Graph<Integer, String>)curGraph.g ;
        	for(int i = 0; i < tempG.getVertexCount(); i++)
        	{
        		sgv.g.addVertex((Integer)i);
        	}
        	String edgeString = tempG.toString();
        	edgeString = edgeString.substring(edgeString.indexOf("Edges:"),edgeString.length());
        	
        	
        	String parseMe = edgeString.substring(edgeString.indexOf(':')+1,edgeString.length());
			String delims = "[ ]";
			String[] tokens = parseMe.split(delims);
			for(int i = 0; i < tokens.length; i++)
			{
				String s1 = tokens[i].substring(0,tokens[i].indexOf('['));
				int s2 = (Integer)(Integer.parseInt(tokens[i].substring(tokens[i].indexOf('[')+1,tokens[i].indexOf(','))));
				int s3 = (Integer)(Integer.parseInt(tokens[i].substring(tokens[i].indexOf(',')+1,tokens[i].indexOf(']'))));
				System.out.println("S1:"+s1+"S2:"+s2+"S3:"+s3);
				sgv.g.addEdge(s1, s2, s3); 
			}
			sgv.nodeCount = curGraph.totalnodes;
			sgv.edgeCount = curGraph.totaledges;
			
			
        	/*Iterator edgeInterator =  tempG.getEdges().iterator();
        	while(edgeInterator.hasNext())
        	{
        		System.out.println("TESTING:"+edgeInterator.next().toString());
        	}*/
        	
        }
        
        //******example input ******
       /* sgv.g.addVertex((Integer)0);
        sgv.g.addVertex((Integer)1);
        sgv.g.addVertex((Integer)2); 
        sgv.nodeCount = 3;
        
        sgv.g.addEdge("Edge-A", 0, 1); 
        sgv.g.addEdge("Edge-B", 1, 2);
        sgv.edgeCount = 2;
        */

        
        //System.out.println(matrixToString(graphMatrix));
       // System.out.println(matrixToMathematica(graphMatrix));
        
       
        
          saveGraph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	
            	 /* Data format
       		 ############
       		  Graph Name:string
       		  Created on:date
       		  Total Nodes:int
       		  Total Edges:int
       		  Vertices:0,1,2,3,4
       		  Edge Data:Edge-B[1,2] Edge-A[0,1] E2[0,4] E4[0,2] E3[0,3] E5[3,0] E6[3,3] 
       		  Mathematica Output:
       		  MatrixForm[{{0,1,1,1,1},
       		 	{1,0,1,0,0},
       		 	{1,1,0,0,0},
       			{1,0,0,1,0},
       		 	{1,0,0,0,0}
       		 	}]
       		   %%%%%%%%%%%%
       		 *//*
            	
                sgv.g = new SparseMultigraph<Integer, String>();//
                // Add some vertices and edges
            	
                sgv.g.addVertex((Integer)1);
                sgv.g.addVertex((Integer)2);
                sgv.g.addVertex((Integer)3); 
                sgv.g.addVertex((Integer)4); 
                sgv.g.addEdge("Edge-A", 1, 2); 
                sgv.g.addEdge("Edge-B", 2, 3);  
                sgv.g.addEdge("Edge-C", 1, 4);  
                
             */
            	//Receives a graph name and prints it to a file
            	//along with printing the date
            	/*############
         		  Graph Name:string
         		  Created on:date
         		  */
            	RequestInput graphName = new RequestInput();
            	
            	//Write the remaining elements of graph to file
            	 try{
                     FileWriter fstream = new FileWriter("graphHistory.txt",true);
                     BufferedWriter out = new BufferedWriter(fstream);
                     
                     //Total Nodes:int
                     out.newLine();
                     out.write("Total Nodes:"+sgv.g.getVertexCount());
                     //Total Edges:int
                     out.newLine();
                     out.write("Total Edges:"+sgv.g.getEdgeCount());
                     /*Vertices:0,1,2,3,4
						Edge Data:Edge-B[1,2] Edge-A[0,1] E2[0,4] E4[0,2] E3[0,3] E5[3,0] E6[3,3] 
						*/
                     String temp = sgv.g.toString();
                     temp = temp.replace("Edges:", "Edge Data:");
                     String temp2 = temp.substring(temp.indexOf("Edge Data:"),temp.length());
                     temp = temp.substring(0,temp.indexOf("Edge Data:"));
                     out.newLine();
                     out.write(temp);
                     out.newLine();
                     out.write(temp2);
                     /*
						Mathematica Output:
						MatrixForm[{{0,1,1,1,1},
						{1,0,1,0,0},
						{1,1,0,0,0},
						{1,0,0,1,0},
						{1,0,0,0,0}
						}]
						*/
                     out.newLine();
                     out.write("Mathematica Output:");
                     out.newLine();
                     out.write(graphToMathematica(sgv));
                     //%%%%%%%%%%%%
                     out.newLine();
                     out.write("%%%%%%%%%%%%");
                     out.close();
                     }catch (Exception e2) {
                     	System.err.println("Error writing file: " + e2.getMessage());
                     	
                     }
            	
            	
            	  System.out.println(" HIHI ");
            	
            	}
            });
        
        

        copyGraphSelection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	
            	
            	Collection picked = vv.getPickedVertexState().getPicked();
           	
            	Vem textTransfer = sgv;//new Vem();
            	
                //Set up Matrix
                List<Integer[]> graphMatrix = new ArrayList<Integer[]>();
                int MatrixSize = picked.size(); //sgv.g.getVertexCount(); 
                System.out.println("Matrix Size: "+ MatrixSize);
                
                                
                Integer[] activeV = new Integer[MatrixSize];
                int count = 0;
                int activeVPos = 0;
                
                while(activeVPos < MatrixSize)
                {
                	if(sgv.g.containsVertex(count) && picked.contains(count))
                	{
                		activeV[activeVPos] = count;
                		activeVPos++;
                	}
                	count++;
                }
                
               // sgv.g.getVertices().toArray()  ((Integer[])(sgv.g.getVertices().toArray()))
                for(int i = 0; i < MatrixSize; i ++)
                {
                	Integer[] tempArray = new Integer[MatrixSize];
                	for(int j = 0; j < MatrixSize; j++)
                	{	if(sgv.g.findEdge(activeV[i],activeV[j]) != null)
                		{
                		tempArray[j] = 1;
                		}else
                		{
                			tempArray[j] = 0;
                		}
                	}
                	graphMatrix.add(tempArray);
                }
                //graphMatrix.add(new Integer[]{1, 2, 3});
                //graphMatrix.add(new Integer[]{4, 5 , 6, 7});

                
                //System.out.println(matrixToString(graphMatrix));
                //System.out.println(matrixToMathematica(graphMatrix));
            	
            	textTransfer.setClipboardContents(""+matrixToMathematica(graphMatrix));
                System.out.println("Clipboard contains:" + textTransfer.getClipboardContents() );
                System.out.println("Graph to string: " + sgv.g.toString());
                
                
            }
        });
        
        
        pasteGraphSelection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	
            	
            	vv.getPickedVertexState().clear();
            	clipBoardMatrixToGraph(getClipboardContents());
            	
            	AbstractLayout<Integer,String> layout = null;
            	
            	  Dimension d = vv.getSize();//new Dimension(600,600);


                      layout = new ISOMLayout<Integer,String>(g);
                      layout.setSize(d);
                      Relaxer relaxer = new VisRunner((IterativeContext)layout);
                      relaxer.stop();
                      relaxer.prerelax();
                      StaticLayout<Integer,String> staticLayout = new StaticLayout<Integer,String>(g, layout);
                      LayoutTransition<Integer,String> lt =
                          new LayoutTransition<Integer,String>(vv, vv.getGraphLayout(),
                                  staticLayout);
                      Animator animator = new Animator(lt);
                      animator.start();
                  //  vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
                    //  vv.repaint();
   
                  
            	
            	
            	vv.repaint();
                
            	
            	
            }
        });
        
        copyMatrix.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	Vem textTransfer = sgv;//new Vem();
            	
                //Set up Matrix
                List<Integer[]> graphMatrix = new ArrayList<Integer[]>();
                int MatrixSize = sgv.g.getVertexCount(); 
                System.out.println("Matrix Size: "+ MatrixSize);
                
                                
                Integer[] activeV = new Integer[MatrixSize];
                int count = 0;
                int activeVPos = 0;
                
                while(activeVPos < MatrixSize)
                {
                	if(sgv.g.containsVertex(count))
                	{
                		activeV[activeVPos] = count;
                		activeVPos++;
                	}
                	count++;
                }
                
               // sgv.g.getVertices().toArray()  ((Integer[])(sgv.g.getVertices().toArray()))
                for(int i = 0; i < MatrixSize; i ++)
                {
                	Integer[] tempArray = new Integer[MatrixSize];
                	for(int j = 0; j < MatrixSize; j++)
                	{	if(sgv.g.findEdge(activeV[i],activeV[j]) != null)
                		{
                		tempArray[j] = 1;
                		}else
                		{
                			tempArray[j] = 0;
                		}
                	}
                	graphMatrix.add(tempArray);
                }
                //graphMatrix.add(new Integer[]{1, 2, 3});
                //graphMatrix.add(new Integer[]{4, 5 , 6, 7});

                
                //System.out.println(matrixToString(graphMatrix));
                //System.out.println(matrixToMathematica(graphMatrix));
            	
            	textTransfer.setClipboardContents(""+matrixToMathematica(graphMatrix));
                System.out.println("Clipboard contains:" + textTransfer.getClipboardContents() );
                System.out.println("Graph to string: " + sgv.g.toString());
                
            }
        });
        
        gm.setMode(ModalGraphMouse.Mode.EDITING); // Start off in editing mode
        frame.pack();
        frame.setVisible(true);  
        

    
        
    }
    public static String graphToMathematica(Vem sgv)
    {
    	Vem textTransfer = sgv;//new Vem();
    	
        //Set up Matrix
        List<Integer[]> graphMatrix = new ArrayList<Integer[]>();
        int MatrixSize = sgv.g.getVertexCount(); 
        System.out.println("Matrix Size: "+ MatrixSize);
        
        
        Integer[] activeV = new Integer[MatrixSize];
        int count = 0;
        int activeVPos = 0;
        
        while(activeVPos < MatrixSize)
        {
        	if(sgv.g.containsVertex(count))
        	{
        		activeV[activeVPos] = count;
        		activeVPos++;
        	}
        	count++;
        }
        
       // sgv.g.getVertices().toArray()  ((Integer[])(sgv.g.getVertices().toArray()))
        for(int i = 0; i < MatrixSize; i ++)
        {
        	Integer[] tempArray = new Integer[MatrixSize];
        	for(int j = 0; j < MatrixSize; j++)
        	{	if(sgv.g.findEdge(activeV[i],activeV[j]) != null)
        		{
        		tempArray[j] = 1;
        		}else
        		{
        			tempArray[j] = 0;
        		}
        	}
        	graphMatrix.add(tempArray);
        }
    	
    	return matrixToMathematica(graphMatrix);

    }
    @SuppressWarnings("unused")
	private static String matrixToString(List<Integer[]> graphMatrix) {
    	String mString = "";
		for(int i=0; i<graphMatrix.size();i++)
		{
			for (int t = 0; t < graphMatrix.get(i).length;t++)
			{
				mString += (graphMatrix.get(i))[t] + " ";
			}
			mString+="\n";
		}
		return mString;
	}
    private static String matrixToMathematica(List<Integer[]> graphMatrix) {
    	String mString = "MatrixForm[{";
		for(int i=0; i<graphMatrix.size();i++)
		{
			mString+="{";
			for (int t = 0; t < graphMatrix.get(i).length;t++)
			{
				if(t==graphMatrix.get(i).length-1)
				{
					mString += (graphMatrix.get(i))[t];
				}else
				{
					mString += (graphMatrix.get(i))[t] + ",";
				}
			}
			if(i==graphMatrix.size()-1){
				mString+="}\n";
			}else{
				mString+="},\n";
			}
			
		}
		mString+="}]";
		return mString;
	}

	/**
     * Empty implementation of the ClipboardOwner interface.
     */
     public void lostOwnership( Clipboard aClipboard, Transferable aContents) {
       //do nothing
     }

    /**
    * Place a String on the clipboard, and make this class the
    * owner of the Clipboard's contents.
    */
    public void setClipboardContents( String aString ){
      StringSelection stringSelection = new StringSelection( aString );
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      clipboard.setContents( stringSelection, this );
    }

    /**
    * Get the String residing on the clipboard.
    *
    * @return any text found on the Clipboard; if none found, return an
    * empty String.
    */
    public static String getClipboardContents() {
      String result = "";
      Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      //odd: the Object param of getContents is not currently used
      Transferable contents = clipboard.getContents(null);
      boolean hasTransferableText =
        (contents != null) &&
        contents.isDataFlavorSupported(DataFlavor.stringFlavor)
      ;
      if ( hasTransferableText ) {
        try {
          result = (String)contents.getTransferData(DataFlavor.stringFlavor);
        }
        catch (UnsupportedFlavorException ex){
          //highly unlikely since we are using a standard DataFlavor
          System.out.println(ex);
          ex.printStackTrace();
        }
        catch (IOException ex) {
          System.out.println(ex);
          ex.printStackTrace();
        }
      }
      return result;
    }
    
    public static void clipBoardMatrixToGraph(String matrixString){
 	
    	
    	System.out.println("B4: ");
    	System.out.println(matrixString);
    	
    	String curString; 
    	
    	if(matrixString.contains("["))
    	{
    		curString = matrixString.substring(matrixString.indexOf('[')+2, matrixString.indexOf(']')-1);
    	}else
    	{
    		curString = matrixString;
    	}
    	
    	
    	
    	curString = curString.replaceAll("\\s","");
    	
    	curString = curString.replaceAll(",","");
    	
    	curString = curString.replaceAll("}","");
    	//curString.replaceAll(" ","");

    	curString = curString+"{";
    	System.out.println("After: ");
    	System.out.println(curString);
    	
    	String[] rows = curString.split("\\{|}");

    	ArrayList<String> curatedRows = new ArrayList<String>();
    	
    	System.out.println("rows length: "+rows.length);
    	//System.out.println(curString);
    	
    	//System.out.println("Rows length: "+rows.length);
    	
    	for (int x= 0; x < rows.length; x++)
    	{
    		System.out.println("row: "+rows[x]);
    		if(!(rows[x].length() == 0 || rows[x].length() == 2))
    		{
    			curatedRows.add(rows[x]);
    		}

    	}
    	
    	//System.out.println("Curated List: ");
    	char[] curValues;
    	
    	int vertexNum = 0;
    	
    	int preMergeVertexCount =g.getVertexCount();
    	
    	System.out.println("curatedRows.size(): "+curatedRows.size());
    	
    	for (int x= 0; x < curatedRows.size(); x++)
    	{
    		curString = curatedRows.get(x);
    		curValues = curString.toCharArray();
    		System.out.println("cRs: "+curatedRows.get(x));
    		
    		vertexNum = g.getVertexCount();
    		g.addVertex(vertexNum);
    		nodeCount++;
    		
    		
    		
    		//System.out.println("cVs: "+curValues);
    		
    		for (int y= 0; y < curValues.length; y++)
        	{
    			//System.out.println("hi: "+curValues[y]);
        		if(curValues[y]=='1' && y <= x )
        		{
        			System.out.println(("E"+(g.getEdgeCount()))+" "+(preMergeVertexCount+x)+" "+(preMergeVertexCount+y));
        			g.addEdge("E"+(g.getEdgeCount()), (preMergeVertexCount+x), (preMergeVertexCount+y)); 
        			edgeCount++;
        		}

        	}
    		
    		
    	}
    	
    	
    
    	//g.getVertexCount()
    	
     	//g.addVertex((Integer)i);
    	//g.addEdge(s1, s2, s3); 

    }
    
    
}