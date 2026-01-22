package scenario;

import ipaddress.SubnetUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import absorbingChain.utilities.Matrix;
import alert.ftext.Alert;
import att.grappa.Edge;
import att.grappa.Graph;
import att.grappa.Subgraph;
import att.grappa.GrappaConstants;
import att.grappa.Node;
import att.grappa.Attribute;


public class Scenario {
	private Graph scenario;

	private int id;
	private List<Node> initialAlerts;
	private List<Node> objectives;
	private Node recentNode;

	private SubnetUtils destSubnet;
	private SubnetUtils srcSubnet;
	private int destNetworkPrefixes = 31;
	private int srcNetworkPrefixes = 31;
	private boolean completed;
	private int nbNodes;
	private int nbEdges;
	private List<Alert> alertsSerie;
	private List<String> IPs;
	private int destPort;
	private int step;
	private Config config;
	private boolean destPortSet;
	private double startTimestamp;
	private double endTimestamp;

	public long getScenarioDuration(){ //return scenario duration in seconds
		if(alertsSerie!=null && !alertsSerie.isEmpty()){
			double duration = alertsSerie.get(alertsSerie.size()-1).getTimestamp() - alertsSerie.get(0).getTimestamp();
			return (long)duration;
		}else
			return 0;
	}
	public void printSummary(){

		System.out.println("###################");
		System.out.println(getSrcNetworkPrefixes()+"-"+getId()+"-2");
		if(getNbNodes()>1)
			System.out.format("scenario:%d nodes:%d edges:%d density:%4.2f%n", getId(), getNbNodes(), getNbEdges(), 1.0*getNbEdges()/(getNbNodes()*(getNbNodes()-1)));
		else
			System.out.println("scenario:"+ getId() + " nodes:" + getNbNodes()+ " edges:" + getNbEdges()+ " density:0");
	}

	public void printSummary(PrintStream ps){

		ps.println("###################");
		ps.println(getSrcNetworkPrefixes()+"-"+getId()+"-2");
		if(getNbNodes()>1)
			ps.format("scenario:%d nodes:%d edges:%d density:%4.2f%n", getId(), getNbNodes(), getNbEdges(), 1.0*getNbEdges()/(getNbNodes()*(getNbNodes()-1)));
		else
			ps.println("scenario:"+ getId() + " nodes:" + getNbNodes()+ " edges:" + getNbEdges()+ " density:0");
	}
	public int getDestNetworkPrefixes(){
		return destNetworkPrefixes;
	}
	public int getSrcNetworkPrefixes(){
		return srcNetworkPrefixes;
	}
	public int getNbNodes() {
		return nbNodes;
	}
	public int getNbEdges() {
		return nbEdges;
	}
	public Graph getScenario() {
		return scenario;
	}
	public List<Node> getInitialAlerts(){
		return initialAlerts;
	}
	/*Scenario(int id, Config config, List<Alert> alerts, int destNetPrefixes, int srcNetPrefixes){
		nbNodes = 0;
		nbEdges = 0;
		destNetworkPrefixes = destNetPrefixes;
		srcNetworkPrefixes = srcNetPrefixes;
		destSubnet = new SubnetUtils(alerts.get(0).getIpDest()+"/"+destNetworkPrefixes);
		srcSubnet = new SubnetUtils(alerts.get(0).getIpSrc()+"/"+srcNetworkPrefixes);
		startTimestamp = alerts.get(0).getTimestamp();
		destPort=0;
		this.id = id;
		int i=0;
		while(destPort==0){
			destPort = alerts.get(i).getPortDest();
			i++;
		}
		destPortSet = (destPort!=0);

		scenario = new Graph(destSubnet.getInfo().getNetworkAddress());

		scenario.setAttribute(new Attribute(GrappaConstants.SUBGRAPH, Attribute.LABEL_ATTR, destSubnet.getInfo().getNetworkAddress()));
		completed = false;
		initialAlerts = new ArrayList<Node>();
		recentNode = null;
		objectives = new ArrayList<Node>();
		alertsSerie = new ArrayList<Alert>();
		IPs = new ArrayList<String>();
		step = 1;
		this.config = config;

		updateGraph(alerts);
	} */
	public Scenario(Scenario s){
		nbEdges = s.nbEdges;
		nbNodes = s.nbNodes;
		destNetworkPrefixes = s.destNetworkPrefixes;
		srcNetworkPrefixes = s.srcNetworkPrefixes;
		destSubnet = new SubnetUtils(s.destSubnet);
		srcSubnet = new SubnetUtils(s.srcSubnet);
		startTimestamp = s.startTimestamp;

		this.id = s.id;

		destPort = s.destPort;
		destPortSet = s.destPortSet;		

		scenario = s.scenario.getGraph();

		completed = s.completed;
		initialAlerts = new ArrayList<Node>(s.initialAlerts);
		recentNode = s.recentNode;
		objectives = new ArrayList<Node>(s.objectives);
		alertsSerie = new ArrayList<Alert>(s.alertsSerie);
		IPs = new ArrayList<String>(s.IPs);
		step = s.step;
		this.config = s.config;		
	}
	Scenario(int id, Config config, Alert alert, int destNetPrefixes, int srcNetPrefixes){
		nbEdges = 0;
		nbNodes = 0;
		destNetworkPrefixes = destNetPrefixes;
		srcNetworkPrefixes = srcNetPrefixes;
		destSubnet = new SubnetUtils(alert.getIpDest()+"/"+destNetworkPrefixes);
		srcSubnet = new SubnetUtils(alert.getIpSrc()+"/"+srcNetworkPrefixes);
		startTimestamp = alert.getTimestamp();

		this.id = id;

		destPort = alert.getPortDest();
		destPortSet = (destPort!=0);		

		scenario = new Graph(destSubnet.getInfo().getNetworkAddress());

		completed = false;
		initialAlerts = new ArrayList<Node>();
		recentNode = null;
		objectives = new ArrayList<Node>();
		alertsSerie = new ArrayList<Alert>();
		IPs = new ArrayList<String>();
		step = 1;
		this.config = config;

		addAlert(alert);
	}
	public int getId(){
		return id;
	}
	public void addAlertToAlertsSerie(Alert alert){

		if(!alertsSerie.isEmpty()){
			if(alert.equals(alertsSerie.get(alertsSerie.size()-1), 
					config.isAlertSeriesCheckSrcPorts(), config.isAlertSeriesCheckDstPorts())){
				alertsSerie.get(alertsSerie.size()-1).incrementRepetition();
			}else{
				Alert a = new Alert(alert);
				alertsSerie.add(a);
			}
		}else{
			Alert a = new Alert(alert);
			alertsSerie.add(a);
		}

	}
	public Node addAlertToNetCluster(Alert alert){
		Node alertNode;
		Subgraph ipDestSubgraph = scenario.findSubgraphByName("clusterh"+alert.getIpDest());
		if( ipDestSubgraph != null){
			Subgraph portSubgraph = ipDestSubgraph.findSubgraphByName("clusterh"+alert.getIpDest()+"p"+alert.getPortDest());
			if( portSubgraph != null){
				alertNode = portSubgraph.findNodeByName(alert.getIpDest()+":"+alert.getPortDest()+"-"+ alert.getId());
				if(alertNode == null){
					alertNode = new Node(portSubgraph, alert.getIpDest()+":"+alert.getPortDest()+"-"+ alert.getId());
					String s = "<<FONT COLOR='blue'>"+alert.getId()+"</FONT>>";
					alertNode.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.LABEL_ATTR, s));
					scenario.addNode(alertNode);
					nbNodes++;
				}
			}else{
				portSubgraph = new Subgraph(ipDestSubgraph, "clusterh"+alert.getIpDest()+"p"+alert.getPortDest());
				portSubgraph.setAttribute(new Attribute(GrappaConstants.SUBGRAPH, Attribute.LABEL_ATTR, "port: "+alert.getPortDest()));
				alertNode = new Node(portSubgraph, alert.getIpDest()+":"+alert.getPortDest()+"-"+ alert.getId());
				String s = "<<FONT COLOR='blue'>"+alert.getId()+"</FONT>>";
				alertNode.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.LABEL_ATTR, s));
				scenario.addNode(alertNode);
				nbNodes++;
			}
		}else{
			//System.out.println("creating clusterhost"+alert.getIpDest());
			ipDestSubgraph = new Subgraph(scenario, "clusterh"+alert.getIpDest());
			ipDestSubgraph.setAttribute(new Attribute(GrappaConstants.SUBGRAPH, Attribute.LABEL_ATTR, "IP: "+alert.getIpDest()));
			/*for(int i=0; i<scenario.subgraphElementsAsArray().length;i++){
				System.out.println("#"+scenario.subgraphElementsAsArray()[i].getAttributeValue(Attribute.LABEL_ATTR)+"#");
				
				for(int j=0; j<scenario.subgraphElementsAsArray()[i].subgraphElementsAsArray().length;j++)
					System.out.println("\t#"+scenario.subgraphElementsAsArray()[i].subgraphElementsAsArray()[j].getAttributeValue(Attribute.LABEL_ATTR)+"#");
			}
			scenario.printGraph(System.out);
			if(ipDestSubgraph.findSubgraphByName("clusterport"+alert.getPortDest())!=null)System.out.println("KO");
			
			System.out.println("creating clusterhost"+alert.getIpDest()+" : "+"clusterport"+alert.getPortDest()+" in "+ipDestSubgraph.getName());
			*/
			Subgraph portSubgraph = new Subgraph(ipDestSubgraph, "clusterh"+alert.getIpDest()+"p"+alert.getPortDest());
			portSubgraph.setAttribute(new Attribute(GrappaConstants.SUBGRAPH, Attribute.LABEL_ATTR, "port: "+alert.getPortDest()));
			alertNode = new Node(portSubgraph, alert.getIpDest()+":"+alert.getPortDest()+"-"+ alert.getId());
			String s = "<<FONT COLOR='blue'>"+alert.getId()+"</FONT>>";
			alertNode.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.LABEL_ATTR, s));
			scenario.addNode(alertNode);
			nbNodes++;
		}
		
		return alertNode;
	}
	public void addAlert(Alert alert){
		Node n1 = null;
		Edge e=null;

		if(!destPortSet && alert.getPortDest()!=0){
			destPortSet = true;
			destPort = alert.getPortDest();
		}
		addAlertToAlertsSerie(alert);
		if(!IPs.contains(alert.getIpSrc()))
			IPs.add(alert.getIpSrc());
		
		n1 = addAlertToNetCluster(alert);
		
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
	public void addAlert2(Alert alert){
		Node n1 = null;
		Edge e=null;

		if(!destPortSet && alert.getPortDest()!=0){
			destPortSet = true;
			destPort = alert.getPortDest();
		}
		addAlertToAlertsSerie(alert);
		if(!IPs.contains(alert.getIpSrc()))
			IPs.add(alert.getIpSrc());

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
				
				newobjective = new Node(n1.getSubgraph(), String.valueOf(n1.getName())+".");
				String s = "<<FONT COLOR='blue'>"+n1.getName()+".</FONT>>";
				newobjective.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.LABEL_ATTR, n1.getAttributeValue( Attribute.LABEL_ATTR)));
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

			initialAlerts.add(n);
		}
	}


	public void updateGraph(List<Alert> alerts){
		Node n1 = null;
		//Node n2 = null;
		Edge e=null;
		int i =0;
		if(alerts.size()==0) return;		

		for(i=0;i<alerts.size();i++){

			if(!destPortSet && alerts.get(i).getPortDest()!=0){
				destPortSet = true;
				destPort = alerts.get(i).getPortDest();
			}
			if(!IPs.contains(alerts.get(i).getIpSrc()))
				IPs.add(alerts.get(i).getIpSrc());

			n1= scenario.findNodeByName(String.valueOf(alerts.get(i).getId()));
			if(n1==null){
				n1 = new Node(scenario, String.valueOf(alerts.get(i).getId()));
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
	/*public boolean aggregatable(String destIp, String srcIp){
		if(destSubnet.getInfo().isInRange(destIp) && srcSubnet.getInfo().isInRange(srcIp))
			return true;
		else
			return false;
	}*/

	public boolean aggregatable(Alert newAlert){
		boolean res = false;
		if(destSubnet.getInfo().isInRange(newAlert.getIpDest()) 
				&& srcSubnet.getInfo().isInRange(newAlert.getIpSrc())){
			res = true;
			if(config.isScenarioCheckDstPorts() && destPortSet && newAlert.getPortDest()!=0 && destPort!=newAlert.getPortDest())
				res = false;
		}else
			res = false;
		return res;
	}

	public void printObjectives(){
		System.out.print("Objectives:");
		for(Node o: objectives){
			System.out.print(o.getName()+":"+o.getId()+ " ");
		}
		System.out.println();
	}	
	public void printObjectives(PrintStream ps){
		ps.print("Objectives:");
		for(Node o: objectives){
			ps.print(o.getName()+":"+o.getId()+ " ");
		}
		ps.println();
	}	

	public void printNodes(){
		System.out.print("Nodes: ");
		Node nodes[] = scenario.nodeElementsAsArray();
		for(Node n: nodes){
			System.out.print(n.getName()+":"+n.getId()+ " ");
		}
		System.out.println();
	}
	public void printNodes(PrintStream ps){
		ps.print("Nodes: ");
		Node nodes[] = scenario.nodeElementsAsArray();
		for(Node n: nodes){
			ps.print(n.getName()+":"+n.getId()+ " ");
		}
		ps.println();
	}

	public void printAlertsSerie(String filename){
		PrintStream als = null;
		try {
			als = new PrintStream(new FileOutputStream(filename));
			//als.println("Alerts: ");
			for(Alert a: alertsSerie){
				als.println(a);
			}
			als.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
	}
	public void printAlertsSerie(PrintStream ps){
		//ps.println("Alerts: ");

		for(Alert a: alertsSerie){
			ps.println(a);
		}

	}

	public void printInitialAlerts(PrintStream ps){
		ps.print("Initial Alerts: ");

		for(Node a: initialAlerts){
			ps.print(a.getName()+":"+a.getId()+ " ");
		}
		ps.println();
	}
	public void printIPs(PrintStream ps){
		ps.print("IPs: ");

		for(String ip: IPs){
			ps.print(ip+ " ");
		}
		ps.println();
	}

	public void toMatrix(PrintStream ps) {
		double[][] Q, R;

		if(objectives.isEmpty()) return; //todo: throw exception
		Node nodes[] = scenario.nodeElementsAsArray();
		Edge edges[] = scenario.edgeElementsAsArray();
		int trans = nodes.length-1;
		int absorb = 1;
		Q = new double[trans][trans];
		R = new double[trans][absorb];
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
				Q[newtail][newhead] = Integer.parseInt(String.valueOf(edges[i].getAttributeValue(Attribute.LABEL_ATTR)));

			//if(tail == objective && head!=objective)
			if(head == objective && tail!=objective)
				R[newtail][0] = Integer.parseInt(String.valueOf(edges[i].getAttributeValue(Attribute.LABEL_ATTR)));

		}
		if(trans<=1) return;
		Matrix qMat = new Matrix(Q);
		Matrix rMat = new Matrix(R);
		qMat.display("Q",0, ps);
		rMat.display("R",0, ps);	

		double[][] qArr = qMat.toArray();
		double[][] rArr = rMat.toArray();
		Matrix iqMat, nMat, cMat, tMat, bMat;

		double total;
		for (int r = 0; r < qArr.length; r++) {
			total = 0;
			for (int c = 0; c < qArr[0].length; c++) {
				total = total + qArr[r][c];
			}	
			for (int c = 0; c < rArr[0].length; c++) {
				total = total + rArr[r][c];
			}
			for (int c = 0; c < qArr[0].length; c++) {
				//qArr[r][c] = round(qArr[r][c] / total, .001);
				qArr[r][c] = qArr[r][c]/total;
			}	
			for (int c = 0; c < rArr[0].length; c++) {
				//rArr[r][c] = round(rArr[r][c] / total, .001);
				rArr[r][c] = rArr[r][c]/total;
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


		qMat = new Matrix(qArr);
		rMat = new Matrix(rArr);
		//qMat.display("qMat");
		//rMat.display("rMat");

		iqMat = Matrix.subtract(Matrix.identity(trans), qMat);
		//iqMat.display("iqMat");
		nMat = iqMat.inverse();
		//nMat.display("nMat");
		//nMat.round(2);


		cMat = Matrix.con(trans, 1);

		//cMat.display("cMat");
		tMat = Matrix.multiply(nMat, cMat);
		//tMat.round(2);


		bMat = Matrix.multiply(nMat, rMat);
		//bMat.round(2);

		qMat.display("Q",2, ps);			
		rMat.display("R",2, ps);	

		nMat.display("N",2, ps);			
		tMat.display("T",2, ps);	
		bMat.display("B",2, ps);

		/*while(nodes.hasNext()) {
			Node n = nodes.next();
			System.out.println(n.getName() + " " +n.getId()+ " " +n.getAttributeValue( Attribute.LABEL_ATTR));
		}*/



	}
	public void createGraphFiles(int window){
		//System.out.println("------------Scenario "+i+"------------------");
		//System.out.println(getDot(s.getScenario()));  
		//System.out.println("-----------------------------------");


		save2DotFile(getScenario(), srcNetworkPrefixes+"-"+getId()+"-"+window+".gv");
		if(nbNodes<=config.getMaxScenarioNodesForGraphImage())
			save2PngFile(srcNetworkPrefixes+"-"+getId()+"-"+window+".gv", srcNetworkPrefixes+"-"+getId()+"-"+window+".png");
		//System.out.println(srcNetworkPrefixes+"-"+i+"-"+window);

	}

	public void statScenario(){



		//System.out.println("------------Scenarios------------------");
		//System.out.println("scenario  nodes  edges density");

		printSummary(config.getOutputFile());
		//s.printAlertsSerie(System.out);
		printAlertsSerie(getSrcNetworkPrefixes()+"-"+getId()+config.getAlertSeriesFileExt());

		config.getOutputFile().println("duration in s: "+getScenarioDuration());
		printNodes(config.getOutputFile());

		printInitialAlerts(config.getOutputFile());
		printIPs(config.getOutputFile());
		printObjectives(config.getOutputFile());
		if(nbNodes<=config.getMaxScenarioNodesForMatrix())
			toMatrix(config.getOutputFile());
		
		config.getOutputFile().println("###################");
		//if(getNbNodes()>1)
		//	System.out.println(i + " " + getNbNodes()+ " " + getNbEdges()+ " " + 1.0*getNbEdges()/(getNbNodes()*(getNbNodes()-1)));
		//else
		//	System.out.println(i + " " + getNbNodes()+ " " + getNbEdges()+ " 0");
	}

	public static void save2PngFile(String dotFile, String filename){

		try {
			//System.out.println(ctx.outputFormat);
			Runtime.getRuntime( ).exec( new String[]{ "dot", "-Tpng", dotFile, "-o", filename } ).waitFor( );
		} catch( InterruptedException | IOException e ) { 
			e.printStackTrace();
		}finally {

		}		 

	}
	public static void save2DotFile(Graph g, String filename){
		try {

			OutputStream f0 = new FileOutputStream(filename);
			g.printGraph(f0);


		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	public static String getDot(Graph g){
		String content = null;
		//dotOutputPanel = new DotOutputPanel(GraphConverter.toDot(grappaGraph));
		try {
			StringWriter theGraph = new StringWriter();
			g.printGraph(theGraph);
			theGraph.flush();
			content = theGraph.toString();
			theGraph.close();
		} catch(Exception ex) {
			ex.printStackTrace();

		}
		return content;
	}


	public static double round(double num, double accuracy) {
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
