package scenario;


import java.util.ArrayList;
import java.util.List;

import alert.ftext.AlertSet;
import alert.ftext.Alert;

public class Scenarios {

	private List<Scenario> scenarios;
	private int destNetworkPrefixes;
	private int srcNetworkPrefixes;
	private int nbScenarios;
	private AlertSet alerts;
	private Config config;
	
	Scenarios(Config config, int destNetPrefixes, int srcNetPrefixes){
		scenarios = new ArrayList<Scenario>();
		destNetworkPrefixes = destNetPrefixes;
		srcNetworkPrefixes = srcNetPrefixes;
		this.config = config;
		nbScenarios = 0;
	}
	public void addScenario(Scenario s){
		scenarios.add(new Scenario(s));
		nbScenarios++;
	}
	public List<Scenario> getScenarios() {
		return scenarios;
	}
	public void addAlert(Alert alert){
		boolean done = false;
		if(scenarios.size() > 0)
		for(Scenario scenario : scenarios) {
			//if(scenario.aggregatable(alert.getIpDest(),alert.getIpSrc())){
			if(scenario.aggregatable(alert)){
				scenario.addAlert(alert);
				done = true;
				break;
			}
			//scenario.getScenario().
		}
		if(!done){
			nbScenarios++;
			Scenario scenario = new Scenario(nbScenarios, config, alert, destNetworkPrefixes, srcNetworkPrefixes);
			scenarios.add(scenario);
		}
	}
	public int getNbScenarios(){
		return nbScenarios;
	}

/*
	public static void parse(AlertSet alertset) {
		List<Alert> alerts = alertset.getAlerts();
		if(alerts == null) {
			System.out.println("Empty alert set input!");
		}
		
				
		InputStream inputStream = new ByteArrayInputStream(alerts.getBytes());
		
		Parser grappaParser = new Parser(inputStream);
		
		Graph grappaGraph = null;
		try {
			grappaParser.parse();
			grappaGraph = grappaParser.getGraph();
		} catch (Exception e) {
			throw new DotParseException(e.getMessage());
		}		
		
		// Build node adjacency lists and wrap the Grappa elements into decorated elements.
		
		Enumeration<Node> grappaNodes = grappaGraph.nodeElements();
		
		Set<DecoratedNode> nodes = new HashSet<DecoratedNode>();
		Map<Node, DecoratedNode> nodeMapping = new HashMap<Node, DecoratedNode>();
		
		while(grappaNodes.hasMoreElements()) {
			Node grappaNode = grappaNodes.nextElement();
			
			DecoratedNode node = new DecoratedNode(grappaNode);
						
			nodeMapping.put(grappaNode, node);
			nodes.add(node);
		}
		
		if(nodes.isEmpty()) {
			throw new DotParseException("The graph has no nodes!");
		}
		
		for(DecoratedNode node : nodes) {
			Node grappaNode = node.getGrappaNode();
			
			Enumeration<Edge> grappaEdges = grappaGraph.isDirected() ? 
					grappaNode.outEdgeElements() : grappaNode.edgeElements();
			
			while(grappaEdges.hasMoreElements()) {
				Edge grappaEdge = grappaEdges.nextElement();
				
				Node head = grappaEdge.getHead();
				Node tail = grappaEdge.getTail();
				
				Node adjacentGrappaNode = head.equals(grappaNode) ? tail : head;
								
				node.addAdjacent(nodeMapping.get(adjacentGrappaNode));
			}
			
			if(grappaGraph.isDirected()) {
				Enumeration<Edge> grappaInEdges = grappaNode.inEdgeElements();
				
				while(grappaInEdges.hasMoreElements()) {
					Edge grappaInEdge = grappaInEdges.nextElement();
					
					Node head = grappaInEdge.getHead();
					Node tail = grappaInEdge.getTail();
					
					Node adjacentGrappaNode = head.equals(grappaNode) ? tail : head;
					
					node.addAccessedBy(nodeMapping.get(adjacentGrappaNode));
				}
			}
		}
		
		return new DecoratedGraph(grappaGraph.getName(), nodes, grappaGraph);
	}
	*/
}
