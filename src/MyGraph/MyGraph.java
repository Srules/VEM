package MyGraph;

import java.util.Date;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

public class MyGraph {
	public Date createdOn;
	public String name;
	public int totalnodes;
	public int totaledges;
	public  Graph<Integer, String> g;
	public String mathematicaOutput;

public MyGraph()
{
	createdOn = new Date();
	name = "";
	totalnodes = 0;
	totaledges = 0;
	g = new SparseMultigraph<Integer, String>();
	mathematicaOutput = "";
}
public MyGraph(String n, Date d, int tn,int te, Graph<Integer,String> ge, String mo)
{
	createdOn = d;
	name = n;
	totalnodes = tn;
	totaledges = te;
	mathematicaOutput = mo;
	g = new SparseMultigraph<Integer, String>();
	//Deep copying graph

	for(int i = 0; i < ge.getVertexCount(); i++)
	{
		g.addVertex((Integer)i);
	}
	String edgeString = ge.toString();
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
		g.addEdge(s1, s2, s3); 
	}
	
}



}
