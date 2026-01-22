package absorbingChain.algorithm;

import java.util.*;
import dijkstra.*;

public class Dijkstra {
	public Dijkstra(){
		
	}
	
	// assumes Nodes are numbered 0, 1, ... n and that the source Node is 0
	   ArrayList<Node> findShortestPath(Node[] nodes, Edge[] edges, Node target) {
		   
	       double[][] Weight = initializeWeight(nodes, edges);
	       double[] D = new double[nodes.length];
	       Node[] P = new Node[nodes.length];
	       ArrayList<Node> C = new ArrayList<Node>();

	       // initialize:
	       // (C)andidate set,
	       // (D)yjkstra special path length, and
	       // (P)revious Node along shortest path
	       for(int i=0; i<nodes.length; i++){
	           C.add(nodes[i]);
	           D[i] = Weight[0][i];
	           if(D[i] != Double.MAX_VALUE){
	               P[i] = nodes[0];
	           }
	       }

	       // crawl the graph
	       for(int i=0; i<nodes.length; i++){
	           // find the lightest Edge among the candidates
	    	   double l = Double.MAX_VALUE;
	           Node n = nodes[0];
	           for(Node j : C){
	               if(D[j.getName()] < l){
	                   n = j;
	                   l = D[j.getName()];
	               }
	           }
	           C.remove(n);

	           // see if any Edges from this Node yield a shorter path than from source->that Node
	           for(int j=0; j<nodes.length; j++){
	               if(D[n.getName()] != Integer.MAX_VALUE && Weight[n.getName()][j] != Integer.MAX_VALUE && D[n.getName()]+Weight[n.getName()][j] < D[j]){
	                   // found one, update the path
	                   D[j] = D[n.getName()] + Weight[n.getName()][j];
	                   P[j] = n;
	               }
	           }
	       }
	       
	       /*System.out.print("P: ");
	       for (Node i:P){
			   System.out.print(i.getName() + " ");
		   }
		   System.out.println();*/
		   
	       // we have our path. reuse C as the result list
	       C.clear();
	       int loc = target.getName();
	       C.add(target);
	       // backtrack from the target by P(revious), adding to the result list
	       while(P[loc] != nodes[0]){
	           if(P[loc] == null){
	               // looks like there's no path from source to target
	               return null;
	           }
	           C.add(0, P[loc]);
	           loc = P[loc].getName();
	       }
	       C.add(0, nodes[0]);
	       
	       /*System.out.print("C: ");
	       for (Node i:C){
			   System.out.print(i.getName() + " ");
		   }
		   System.out.println();*/
		   
	       return C;
	   }

	   private double[][] initializeWeight(Node[] nodes, Edge[] edges){
		   double[][] Weight = new double[nodes.length][nodes.length];
	       for(int i=0; i<nodes.length; i++){
	           Arrays.fill(Weight[i], Double.MAX_VALUE);
	       }
	       for(Edge e : edges){
	           Weight[e.getFrom().getName()][e.getTo().getName()] = e.getWeight();
	       }
	       return Weight;
	   }
	   
	   public static void main(String args[])  {
		   Node[] nodes;
		   Edge[] edges;
		   Node target;
		   int n=9, m=14;
		   ArrayList<Node> path;
		   
		   nodes = new Node[n];
		   edges = new Edge[m];
		  
		   
		   for (int i=0; i<n; i++){
			   nodes[i] = new Node(i);
		   }
		   
			   
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
		  
		   
		   target = nodes[8];
		   
		   Dijkstra dijkstra = new Dijkstra();
		   path = dijkstra.findShortestPath(nodes, edges, target);
		   
		   System.out.println("nodes: ");
		   for (int i=0; i<nodes.length; i++){
			   System.out.print(nodes[i].getName() + " ");
		   }
		   System.out.println();
		   
		   System.out.println("edges: ");
		   for (int i=0; i<edges.length; i++){
			   System.out.println(edges[i].getTo().getName() + " - "+ edges[i].getWeight() +" -> " +edges[i].getFrom().getName());
		   }
		   
		   System.out.println("target: "+target.getName());

		 
		   System.out.print("path: ");
		   for (Node i:path){
			   System.out.print(i.getName() + " ");
		   }
		   System.out.println();
	   }
	   public static void main2(String args[])  {
		   Node[] nodes;
		   Edge[] edges;
		   Node target;
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
		   
		   target = nodes[random.nextInt(n)];
		   
		   Dijkstra dijkstra = new Dijkstra();
		   path = dijkstra.findShortestPath(nodes, edges, target);
		   
		   System.out.println("nodes: ");
		   for (int i=0; i<nodes.length; i++){
			   System.out.print(nodes[i].getName() + " ");
		   }
		   System.out.println();
		   
		   System.out.println("edges: ");
		   for (int i=0; i<edges.length; i++){
			   System.out.println(edges[i].getTo().getName() + " - "+ edges[i].getWeight() +" -> " +edges[i].getFrom().getName());
		   }
		   
		   System.out.println("target: "+target.getName());

		 
		   /*System.out.print("path: ");
		   for (Node i:path){
			   System.out.print(i.getName() + " ");
		   }
		   System.out.println();*/
	   }

	
}
