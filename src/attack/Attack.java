package attack;

import ipaddress.SubnetUtils;

import java.util.ArrayList;
import java.util.List;

import alert.ftext.Alert;
import att.grappa.Edge;
import att.grappa.Graph;
import att.grappa.GrappaConstants;
import att.grappa.Node;
import att.grappa.Attribute;


import ged.graph.DecoratedGraph;

public class Attack {
	private Graph scenario;
	

	List<Node> initialAlerts;
	Node recentNode;
	
	private SubnetUtils subnet;
	private String networkPrefixes = "23";
	boolean completed;
	
	public Graph getScenario() {
		return scenario;
	}
	Attack(List<Alert> alerts){
		subnet = new SubnetUtils(alerts.get(0).getIpDest()+"/"+networkPrefixes);
		scenario = new Graph(subnet.getInfo().getNetworkAddress());
		
		scenario.setAttribute(new Attribute(GrappaConstants.SUBGRAPH, Attribute.LABEL_ATTR, subnet.getInfo().getNetworkAddress()));
		completed = false;
		initialAlerts = new ArrayList<Node>();
		recentNode = null;
		updateGraph(alerts);
	}
	Attack(Alert alert){
		subnet = new SubnetUtils(alert.getIpSrc()+"/"+networkPrefixes);
		scenario = new Graph(subnet.getInfo().getNetworkAddress());
		completed = false;
		initialAlerts = new ArrayList<Node>();
		recentNode = null;
		addAlert(alert);
	}
	public void addAlert(Alert alert){
		Node n1 = null;
		Edge e=null;
		
		n1= scenario.findNodeByName(String.valueOf(alert.getId()));
		if(n1==null){
			n1 = new Node(scenario, String.valueOf(alert.getId()));
			//String s = "<<FONT COLOR='blue'>"+alert.getId()+"</FONT><FONT COLOR='green'>O</FONT>>";
			String s = "<<FONT COLOR='blue'>"+alert.getId()+"</FONT>>";
			n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.LABEL_ATTR, s));
			//n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.SHAPE_ATTR, "plaintext"));
			scenario.addNode(n1);
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
	public void setAsObjectiveNode(String nodeName){
		Node n1= scenario.findNodeByName(nodeName);
		if(n1!=null){
			
			n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.SHAPE_ATTR, "Msquare"));
			n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.COLOR_ATTR, "red"));
			//n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.SHAPE_ATTR, "polygon"));
			//n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.SIDES_ATTR, "5"));
			//n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.STYLE_ATTR, "filled"));
		}
	}
	
	public void setAsObjectiveNode(Node n){
		
		if(n!=null){
			n.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.SHAPE_ATTR, "Msquare"));
			n.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.COLOR_ATTR, "red"));
			//n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.SHAPE_ATTR, "polygon"));
			//n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.SIDES_ATTR, "5"));
			//n1.setAttribute(new Attribute(GrappaConstants.NODE, Attribute.STYLE_ATTR, "filled"));
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
			}
			if(recentNode==null){//The scenario is empty
				recentNode = n1;				
			}else{
				e = scenario.findEdgeByName(recentNode.getName()+"-"+n1.getName());
				if(e==null){
					 e = new Edge(scenario,recentNode,n1,recentNode.getName()+"-"+n1.getName());
					 e.setAttribute(new Attribute(GrappaConstants.EDGE, Attribute.WEIGHT_ATTR, "1"));
					scenario.addEdge(e);
				}else{
					int w = Integer.parseInt(e.getAttributeValue(Attribute.WEIGHT_ATTR).toString());
					w++;
					e.setAttribute(new Attribute(GrappaConstants.EDGE, Attribute.WEIGHT_ATTR, w));
				}
			}
						
		}
		
		
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
	public boolean aggregatable(String ip){
		return subnet.getInfo().isInRange(ip);
	}
}
