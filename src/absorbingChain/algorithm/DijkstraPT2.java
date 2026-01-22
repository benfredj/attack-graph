package absorbingChain.algorithm;

import java.util.*;

import att.grappa.Graph;
//import dijkstra.*;
import ged.graph.DecoratedGraph;
import att.grappa.Graph;
import att.grappa.Edge;
import att.grappa.Node;
import att.grappa.Subgraph;

public class DijkstraPT2 {
	public DijkstraPT2(){
		
	}
	/*
	// assumes Nodes are numbered 0, 1, ... n and that the source Node is 0
	   ArrayList<Node> findShortestPath(Node[] nodes, Edge[] edges, int source, int target) {
		   
	       double[][] Weight = initializeWeight(nodes, edges);
	       double[] D = new double[nodes.length];
	       double[] I = new double[nodes.length];
	       Node[] P = new Node[nodes.length];
	       ArrayList<Node> C = new ArrayList<Node>();

	       // initialize:
	       // (C)andidate set,
	       // (D)yjkstra special path length, and
	       // (P)revious Node along shortest path
	       for(int i=0; i<nodes.length; i++){
	           C.add(nodes[i]);
	           D[i] = Weight[source][i];
	           if(D[i] != Double.MAX_VALUE){
	               P[i] = nodes[source];
	           }
	       }

	       // crawl the graph
	       for(int i=0; i<nodes.length; i++){
	           // find the lightest Edge among the candidates
	    	   double l = Double.MAX_VALUE;
	           Node n = nodes[source];
	           for(Node j : C){
	               if(D[j.getIndex()] < l){
	                   n = j;
	                   l = D[j.getIndex()];
	               }
	           }
	           C.remove(n);

	           // see if any Edges from this Node yield a shorter path than from source->that Node
	           for(int j=0; j<nodes.length; j++){
	               if(D[n.getIndex()] != Integer.MAX_VALUE && Weight[n.getIndex()][j] != Integer.MAX_VALUE && D[n.getIndex()]+Weight[n.getIndex()][j] < D[j]){
	                   // found one, update the path
	                   D[j] = D[n.getIndex()] + Weight[n.getIndex()][j];
	                   P[j] = n;
	               }
	           }
	       }
	       
	       System.out.print("P: ");
	       for (Node i:P){
			   System.out.print(i.getIndex() + " ");
		   }
		   System.out.println();
		   
	       // we have our path. reuse C as the result list
	       C.clear();
	       int loc = nodes[target].getIndex();
	       C.add(nodes[target]);
	       // backtrack from the target by P(revious), adding to the result list
	       while(P[loc] != nodes[source]){
	           if(P[loc] == null){
	               // looks like there's no path from source to target
	               return null;
	           }
	           C.add(0, P[loc]);
	           loc = P[loc].getIndex();
	       }
	       C.add(0, nodes[source]);
	       
	       System.out.print("C: ");
	       for (Node i:C){
			   System.out.print(i.getIndex() + " ");
		   }
		   System.out.println();
		   
	       return C;
	   }

	   private double[][] initializeWeight(Node[] nodes, Edge[] edges){
		   double[][] Weight = new double[nodes.length][nodes.length];
	       for(int i=0; i<nodes.length; i++){
	           Arrays.fill(Weight[i], Double.MAX_VALUE);
	       }
	       for(Edge e : edges){
	           Weight[e.getFrom().getIndex()][e.getTo().getIndex()] = e.getWeight();
	       }
	       return Weight;
	   }
	   ArrayList<Node> shortestpathPassthrough(Node[] nodes, Edge[] edges, Node source, Node target, Node through){
		   ArrayList<Node> path,path1,path2;
		   path = new ArrayList<Node>();
		   path1 = new ArrayList<Node>();
		   path2 = new ArrayList<Node>();
		   
		   path.clear();
		   path1.clear();
		   path2.clear();
		   
		   path1 = findShortestPath(nodes, edges, source.getIndex(), through.getIndex());
		   path2 = findShortestPath(nodes, edges, through.getIndex(), target.getIndex());
		   path2.remove(0);
		   
		   path.addAll(path1);
		   path.addAll(path2);
		   
		   return path;
	   }
	   public static void main(String args[])  {
		   
		   DecoratedGraph dgraph;
		   Graph graph;
		   Node newNode;
		   Edge  newEdge;
		   //Subgraph subgraph = new Subgraph();
		   
		   
		   graph.addNode(new Node(null, "10"));
		   graph.addNode(new Node(null, "8"));
		   graph.addNode(new Node(null, "3"));
		   graph.addNode(new Node(null, "5"));
		   graph.addNode(new Node(null, "9"));
		   graph.addNode(new Node(null, "17"));
		   graph.addNode(new Node(null, "4"));
		   graph.addNode(new Node(null, "14"));
		   graph.addNode(new Node(null, "12"));
		   
		   graph.addEdge(new Edge());
		   
		   
		   Node[] nodes;
		   Edge[] edges;
		   
		   Node source, target;
		   int n=9, m=14;
		   ArrayList<Node> path;
		   path = new ArrayList<Node>();
		   
		   nodes = new Node[n];
		   edges = new Edge[m];
		  
		   
		   //for (int i=0; i<n; i++){
			//   nodes[i] = new Node(i);
		   //}
		   nodes[0] = new Node(10,0);
		   nodes[1] = new Node(8,1);
		   nodes[2] = new Node(3,2);
		   nodes[3] = new Node(5,3);
		   nodes[4] = new Node(9,4);
		   nodes[5] = new Node(17,5);
		   nodes[6] = new Node(4,6);
		   nodes[7] = new Node(14,7);
		   nodes[8] = new Node(12,8);
			   
		   edges[0] = new Edge(nodes[0],nodes[1],0.5);   
		   edges[1] = new Edge(nodes[0],nodes[2],0.5);   
		   edges[2] = new Edge(nodes[2],nodes[2],0.5);   
		   edges[3] = new Edge(nodes[2],nodes[3],0.25);   
		   edges[4] = new Edge(nodes[3],nodes[0],1);   
		   edges[5] = new Edge(nodes[2],nodes[5],0.25);   
		   edges[6] = new Edge(nodes[1],nodes[4],1);   
		   edges[7] = new Edge(nodes[4],nodes[4],0.67);   
		   edges[8] = new Edge(nodes[5],nodes[6],1);   
		   edges[9] = new Edge(nodes[4],nodes[6],0.33);   
		   edges[10] = new Edge(nodes[6],nodes[7],0.67);   
		   edges[11] = new Edge(nodes[6],nodes[8],0.33);   
		   edges[12] = new Edge(nodes[7],nodes[6],0.5);   
		   edges[13] = new Edge(nodes[7],nodes[2],0.5);
		  
		   
		   source = nodes[0];
		   target = nodes[8];
		   
		   DijkstraPT2 dijkstra = new DijkstraPT2();
		   		   
		   for(Node i: nodes){
			   if(i.getName()!=source.getName() && i.getName()!=target.getName()){
				  
				   path = dijkstra.shortestpathPassthrough(nodes, edges, source, target,i);
				   
				   
				   System.out.print("path from "+source.getName()+" to "+target.getName()+" throught "+i.getName()+": ");
				   for (Node k:path){
					   System.out.print(k.getName() + " ");
				   }
				   System.out.println();
			   }
		   }
		   

	   }
	   public static void main2(String args[])  {
		   Node[] nodes;
		   Edge[] edges;
		   int source, target;
		   int n=10, m=75;
		   ArrayList<Node> path;
		   
		   nodes = new Node[n];
		   edges = new Edge[m];
		   Random  random = new Random(55);
		   
		   for (int i=0; i<n; i++){
			   nodes[i] = new Node(i);
		   }
		   for (int i=0; i<m; i++){
			   
			   edges[i] = new Edge(nodes[random.nextInt(n)],nodes[random.nextInt(n)],random.nextInt(100));
		   }
		   
		   target = random.nextInt(n);
		   source = random.nextInt(n);
		   
		   DijkstraPT2 dijkstra = new DijkstraPT2();
		   path = dijkstra.findShortestPath(nodes, edges, source, target);
		   
		   System.out.println("nodes: ");
		   for (int i=0; i<nodes.length; i++){
			   System.out.print(nodes[i].getName() + " ");
		   }
		   System.out.println();
		   
		   System.out.println("edges: ");
		   for (int i=0; i<edges.length; i++){
			   System.out.println(edges[i].getTo().getName() + " - "+ edges[i].getWeight() +" -> " +edges[i].getFrom().getName());
		   }
		   
		   System.out.println("target: "+nodes[target].getName());

		 
		   System.out.print("path: ");
		   for (Node i:path){
			   System.out.print(i.getName() + " ");
		   }
		   System.out.println();
	   }
*/
	
}
