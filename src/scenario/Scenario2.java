package scenario;

import ipaddress.SubnetUtils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import absorbingChain.utilities.Matrix3;
import absorbingChain.utilities.Fraction;
import alert.ftext.Alert;
import att.grappa.Edge;
import att.grappa.Graph;
import att.grappa.GrappaConstants;
import att.grappa.Node;
import att.grappa.Attribute;


public class Scenario2 {
	private Graph scenario;
	

	List<Node> initialAlerts;
	List<Node> objectives;
	Node recentNode;
	
	private SubnetUtils subnet;
	private int networkPrefixes = 31;
	boolean completed;
	int nbNodes;
	int nbEdges;
	List<Alert> alertsSerie;
	
	public int getNbNodes() {
		return nbNodes;
	}
	public int getNbEdges() {
		return nbEdges;
	}
	public Graph getScenario() {
		return scenario;
	}
	Scenario2(List<Alert> alerts, int netPrefixes){
		nbNodes = 0;
		nbEdges = 0;
		networkPrefixes = netPrefixes;
		subnet = new SubnetUtils(alerts.get(0).getIpDest()+"/"+networkPrefixes);
		scenario = new Graph(subnet.getInfo().getNetworkAddress());
		
		scenario.setAttribute(new Attribute(GrappaConstants.SUBGRAPH, Attribute.LABEL_ATTR, subnet.getInfo().getNetworkAddress()));
		completed = false;
		initialAlerts = new ArrayList<Node>();
		recentNode = null;
		objectives = new ArrayList<Node>();
		alertsSerie = new ArrayList<Alert>();
		updateGraph(alerts);
	}
	
	
	
	Scenario2(Alert alert, int netPrefixes){
		nbEdges = 0;
		nbNodes = 0;
		networkPrefixes = netPrefixes;
		subnet = new SubnetUtils(alert.getIpSrc()+"/"+networkPrefixes);
		scenario = new Graph(subnet.getInfo().getNetworkAddress());
		completed = false;
		initialAlerts = new ArrayList<Node>();
		recentNode = null;
		objectives = new ArrayList<Node>();
		alertsSerie = new ArrayList<Alert>();
		addAlert(alert);
	}

	public void addAlert(Alert alert){
		Node n1 = null;
		Edge e=null;
		alertsSerie.add(alert);
		n1= scenario.findNodeByName(String.valueOf(alert.getId()));
		if(n1==null){
			n1 = new Node(scenario, String.valueOf(alert.getId()));
			//String s = "<<FONT COLOR='blue'>"+alert.getId()+"</FONT><FONT COLOR='green'>O</FONT>>";
			String s = "<<FONT COLOR='blue'>"+alert.getId()+"</FONT>>";
			//String s = ""+alert.getId()+"";
			n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.LABEL_ATTR, s));
			//n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.SHAPE_ATTR, "plaintext"));
			scenario.addNode(n1);
			nbNodes++;
			
		}
		if(recentNode==null){//The scenario is empty
			setAsInitialNode(n1);
			recentNode = n1;				
		}else{
			e = scenario.findEdgeByName(recentNode.getName()+"-"+n1.getName());
			if(e==null){
				 e = new Edge(scenario,recentNode,n1,recentNode.getName()+"-"+n1.getName());
				 e.setAttribute(new Attribute(GrappaConstants.EDGE, Attribute.LABEL_ATTR, "1"));
				scenario.addEdge(e);
				nbEdges++;
			}else{
//System.out.println(e.getAttributeValue(Attribute.WEIGHT_ATTR).toString());
				//double w = Double.parseDouble(e.getAttributeValue(Attribute.LABEL_ATTR).toString());
				int w = Integer.parseInt(e.getAttributeValue(Attribute.LABEL_ATTR).toString());
				w++;
				
				e.setAttribute(new Attribute(GrappaConstants.EDGE, Attribute.LABEL_ATTR, Integer.toString(w)));
				//e.setAttribute(new Attribute(GrappaConstants.EDGE, Attribute.LABEL_ATTR, w));
			}
		}
		recentNode = n1;
	}
	public Node getLastAlert(){
		return recentNode;
	}
	public Boolean isObjective(Node o){
		Iterator<Edge> outs = o.outEdgeElements();
		
		Boolean res = false;
		if(outs.hasNext()){
			Edge e =  outs.next();
			if(!outs.hasNext() && e.getHead()==e.getTail()){
				return true;
			}
		}else
			res = true;
		return res;
	}
	public void setAsObjectiveNode(String nodeName){
		Node n1= scenario.findNodeByName(nodeName);
		if(n1!=null){
			
			//n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.SHAPE_ATTR, "Msquare"));
			//n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.COLOR_ATTR, "red"));
			
			//n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.SHAPE_ATTR, "polygon"));
			//n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.SIDES_ATTR, "5"));
			//n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.STYLE_ATTR, "filled"));
			
			
				if(!isObjective(n1)){
				Node newobjective;								
				Edge e=null;
				
				newobjective = new Node(scenario, String.valueOf(n1.getName())+".");
				String s = "<<FONT COLOR='blue'>"+n1.getName()+".</FONT>>";
				newobjective.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.LABEL_ATTR, s));
				newobjective.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.SHAPE_ATTR, "Msquare"));
				newobjective.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.COLOR_ATTR, "red"));
				scenario.addNode(newobjective);
				nbNodes++;
					
				 e = new Edge(scenario,recentNode,newobjective,recentNode.getName()+"-"+newobjective.getName());
				 e.setAttribute(new Attribute(GrappaConstants.EDGE, Attribute.LABEL_ATTR, "1"));
				scenario.addEdge(e);
				nbEdges++;				
			
				recentNode = newobjective;
				objectives.add(newobjective);
				
			}else{
				n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.SHAPE_ATTR, "Msquare"));
				n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.COLOR_ATTR, "red"));
				objectives.add(n1);
			}
				
			
		}
	}
	
	public void setAsObjectiveNode(Node n){
		
		if(n!=null){
			n.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.SHAPE_ATTR, "Msquare"));
			n.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.COLOR_ATTR, "red"));
			//n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.SHAPE_ATTR, "polygon"));
			//n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.SIDES_ATTR, "5"));
			//n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.STYLE_ATTR, "filled"));
			objectives.add(n);
		}
	}
	
	public void setAsInitialNode(Node n){
		
		if(n!=null){
			n.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.SHAPE_ATTR, "Mdiamond"));
			n.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.COLOR_ATTR, "green"));
			//n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.SHAPE_ATTR, "polygon"));
			//n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.SIDES_ATTR, "5"));
			//n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.STYLE_ATTR, "filled"));
		}
	}
	
	
	public void updateGraph(List<Alert> alerts){
		Node n1 = null;
		//Node n2 = null;
		Edge e=null;
		int i =0;
		if(alerts.size()==0) return;		
		
		for(i=0;i<alerts.size();i++){
			n1= scenario.findNodeByName(String.valueOf(alerts.get(i).getId()));
			if(n1==null){
				n1 = new Node(scenario, String.valueOf(alerts.get(i).getId()));
				scenario.addNode(n1);
				nbNodes++;
			}
			if(recentNode==null){//The scenario is empty
				recentNode = n1;				
			}else{
				e = scenario.findEdgeByName(recentNode.getName()+"-"+n1.getName());
				if(e==null){
					 e = new Edge(scenario,recentNode,n1,recentNode.getName()+"-"+n1.getName());
					 e.setAttribute(new Attribute(GrappaConstants.EDGE, Attribute.WEIGHT_ATTR, "1"));
					scenario.addEdge(e);
					nbEdges++;
				}else{
					int w = Integer.parseInt(e.getAttributeValue(Attribute.WEIGHT_ATTR).toString());
					w++;
					e.setAttribute(new Attribute(GrappaConstants.EDGE, Attribute.WEIGHT_ATTR, w));
				}
			}
						
		}
	}
	public boolean aggregatable(String ip){
		return subnet.getInfo().isInRange(ip);
	}
	
	public void printObjectives(){
		System.out.print("Objectives:");
		for(Node o: objectives){
			System.out.print(o.getName()+"/"+o.getId()+ " ");
		}
		System.out.println();
	}	
	
	
	public void printNodes(){
		System.out.print("Nodes:");
		Node nodes[] = scenario.nodeElementsAsArray();
		for(Node n: nodes){
			System.out.print(n.getName()+"/"+n.getId()+ " ");
		}
		System.out.println();
	}
	public void printAlertsSerie(PrintStream ps){
		ps.println("Alerts:");
		
		for(Alert a: alertsSerie){
			ps.println(a.getId());
		}
		
	}
	public void toMatrix() {
		Fraction[][] Q, R;
		
		if(objectives.isEmpty()) return; //todo: throw exception
		Node nodes[] = scenario.nodeElementsAsArray();
		Edge edges[] = scenario.edgeElementsAsArray();
		int trans = nodes.length-1;
		int absorb = 1;
		Q = new Fraction[trans][trans];
		R = new Fraction[trans][absorb];
		int objective=objectives.get(0).getId();
		
		for(int i=0;i<edges.length;i++){
			//System.out.println("Head:"+edges[i].getHead().getId());
			//System.out.println("Tail:"+edges[i].getTail().getId());
			//System.out.println("Head:"+objectives.get(0).getId());
			int head, tail, newhead, newtail;
			head = edges[i].getHead().getId();
			tail = edges[i].getTail().getId();
			
			newhead = head;
			if(head>=objective)
				newhead = head-1;
			newtail = tail;
			if(tail>objective)
				newtail = tail-1;
			//System.out.println("head:"+head+ ":"+newhead+", tail:"+tail+":"+newtail+", objective:"+objective);
			if(head != objective && tail != objective)
			Q[newtail][newhead] = new Fraction(Integer.parseInt(String.valueOf(edges[i].getAttributeValue(Attribute.LABEL_ATTR))));
			
			//if(tail == objective && head!=objective)
			if(head == objective && tail!=objective)
			R[newtail][0] = new Fraction(Integer.parseInt(String.valueOf(edges[i].getAttributeValue(Attribute.LABEL_ATTR))));
			
		}
		
		Matrix3 qMat = new Matrix3(Q);
		Matrix3 rMat = new Matrix3(R);
		qMat.display("Q");
		rMat.display("R");	
		
		Fraction[][] qArr = qMat.toFractionArray();
		Fraction[][] rArr = rMat.toFractionArray();
		Matrix3 iqMat, nMat, cMat, tMat, bMat;
		
		Fraction total;
		for (int r = 0; r < qArr.length; r++) {
			total = new Fraction(0);
			for (int c = 0; c < qArr[0].length; c++) {
				total = total.add(qArr[r][c]);
			}	
			for (int c = 0; c < rArr[0].length; c++) {
				total = total.add(rArr[r][c]);
			}
			for (int c = 0; c < qArr[0].length; c++) {
				//qArr[r][c] = round(qArr[r][c] / total, .001);
				qArr[r][c] = qArr[r][c].divideBy(total);
			}	
			for (int c = 0; c < rArr[0].length; c++) {
				//rArr[r][c] = round(rArr[r][c] / total, .001);
				rArr[r][c] = rArr[r][c].divideBy(total);
			}		
		}
		
		for (int r = 0; r < qArr.length; r++) {
			for (int c = 0; c < qArr[0].length; c++) {
				Q[r][c] = qArr[r][c];
			}	
			for (int c = 0; c < rArr[0].length; c++) {
				R[r][c] = rArr[r][c];
			}
		}
		
		//Q.toString();
	
		
		qMat = new Matrix3(qArr);
		rMat = new Matrix3(rArr);
		//qMat.display("qMat");
		//rMat.display("rMat");
		
		iqMat = Matrix3.subtract(Matrix3.identity(trans), qMat);
		//iqMat.display("iqMat");
		nMat = iqMat.inverse();
		//nMat.display("nMat");
		//nMat.round(2);
		
	
		cMat = Matrix3.con(trans, 1);
		
		//cMat.display("cMat");
		tMat = Matrix3.multiply(nMat, cMat);
		//tMat.round(2);
		
			
		bMat = Matrix3.multiply(nMat, rMat);
		//bMat.round(2);
	
		qMat.display("Q");			
		rMat.display("R");	
		
		nMat.display("N");			
		tMat.display("T");	
		bMat.display("B");	
		
		/*while(nodes.hasNext()) {
			Node n = nodes.next();
			System.out.println(n.getName() + " " +n.getId()+ " " +n.getAttributeValue( Attribute.LABEL_ATTR));
		}*/
		


}
	public double round(double num, double accuracy) {
		return accuracy * Math.round(num / accuracy);
	}	
	
	/*public void updateGraph(List<Alert> alerts){
		Node n1 = null;
		Node n2 = null;
		Edge e=null;
		int i =1;
		if(alerts.size()==0) return;
		
		
		if(recentNode==null){
			n1 = new Node(scenario, String.valueOf(alerts.get(0).getId()));
			if(alerts.size()>=2){
				n2 = new Node(scenario, String.valueOf(alerts.get(1).getId()));
				i=3;
			}else{
				i=2;
			}
		}else{
			n1 = recentNode;
			n2 = new Node(scenario, String.valueOf(alerts.get(0).getId()));
			i=1;
		}
		if(n2==null){
			if(scenario.findNodeByName(n1.getName())==null)
				scenario.addNode(n1);
			else{
				e = scenario.findEdgeByName(n1.getName()+"-"+n1.getName());
				if(e==null){
					e = new Edge(scenario,n1,n1,n1.getName()+"-"+n1.getName());
					scenario.addEdge(e);
				}else{
					e.setAttribute(new Attribute(GrappaConstants.EDGE, Attribute.WEIGHT_ATTR, ""));
				}
			}
			return;
		}else{
			if(scenario.findNodeByName(n1.getName())==null)
				scenario.addNode(n1);
			if(scenario.findNodeByName(n2.getName())==null)
				scenario.addNode(n2);
			
			if(scenario.findEdgeByName(n1.getName()+"-"+n2.getName())==null){
				e = new Edge(scenario,n1,n2,n1.getName()+"-"+n2.getName());
				scenario.addEdge(e);
			}				
			
		}
		
		if(scenario.findEdgeByName(n1.getName()+"-"+n2.getName())==null){
			e = new Edge(scenario,n1,n2,n1.getName()+"-"+n2.getName());
			scenario.addEdge(e);
		}
		
		
		for(;i<alerts.size();i++){
			n2= scenario.findNodeByName(String.valueOf(alerts.get(i).getId()));
			if(n2==null)
				n1 = new Node(scenario, String.valueOf(alerts.get(i).getId()));
			
			
			//Attribute a = new Attribute();
			if(scenario.findNodeByName(n.getName())==null)
				scenario.addNode(n);
			else{
				if(scenario.findEdgeByName(n.getName()+"-"+n.getName())==null){
					Edge e = new Edge(scenario,n,n,n.getName()+"-"+n.getName());
					scenario.addEdge(e);
				}				
			}
			
		}
		
		
	}*/
}
