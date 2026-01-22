package scenario;


import ged.editpath.*;
import ged.editpath.editoperation.*;
import ged.graph.DecoratedGraph;
import ged.graph.GraphConverter;
import ged.graph.ParseException;
import ged.processor.CostContainer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import alert.ftext.*;
import att.grappa.Edge;
import att.grappa.Node;


public class Demo0 {

	public static void main(String[] args)  {
		
		Config config = new Config(31/*destNetworkPrefixes*/, false/*alertSeriesCheckSrcPorts*/,
				true/*alertSeriesCheckDstPorts*/, false/*scenarioCheckDstPorts*/,
				false/*saveAlertSeries*/, "-2-smia2012.alerts",
				"output.smia2012.txt", "smia2012.noping.fast", Config.SNORTFAST,
				//"output-ctf17-alerts-dataset-human-readable-10000.txt", "ctf17-alerts-dataset-human-readable-10000.txt",
				20 /*maxScenarioNodesForMatrix*/, 60/*maxScenarioNodesForGraphImage*/, 2012);
		
		//Config config = new Config(31/*destNetworkPrefixes*/, false, true, true, false, "-2-10000.alerts",
		//				"output-10000.txt", "ctf17-alerts-dataset-human-readable-10000.txt");
		
		
		//System.out.println("start parsing...");
		AlertSet parser = new AlertSet(config);
	    try {
			parser.processLineByLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    
		for(int srcNetworkPrefixes = 31; srcNetworkPrefixes>=31; srcNetworkPrefixes--){
		//for(int srcNetworkPrefixes = 31; srcNetworkPrefixes>=29; srcNetworkPrefixes--){
			
			//System.out.println("end parsing...");
	
		    TimeWindow tw = new TimeWindow(50000000);
		    
		    List<Alert> alerts;
		    boolean done = false;
		    Scenarios scenarios = new Scenarios(config, config.getDestNetworkPrefixes(), srcNetworkPrefixes); //network prefix = 31, used to aggregate IPSrc.
		    
		    int window = 1;
		    while(!done){
				//System.out.println("start windowing...");
	
		    	alerts = tw.getNextWindowAlert(parser.getAlerts(), window);
		    	if(alerts.size()==0) break;
		    	for(Alert a: alerts){		    		
		    		scenarios.addAlert(a);
		    	}
		    	window++;
		    }
		    
		    System.out.println(srcNetworkPrefixes+" "+scenarios.getNbScenarios());
		    for(Scenario s: scenarios.getScenarios()){
		    	//if(s.getNbNodes()>2){
		    		s.setAsObjectiveNode(s.getLastAlert().getName());
			    	//s.createGraphFiles(window);
			    	//s.statScenario();
		    	//}
		    }
		    PrintStream predictionOutput = null;
			try {
				predictionOutput = new PrintStream(new FileOutputStream("prediction.smia2012.txt"));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		    
		    
		    //for(BigDecimal rho = new BigDecimal("1.00"); rho.compareTo(new BigDecimal("0.5"))>=0; rho = rho.subtract(new BigDecimal("0.05"))){
			BigDecimal rho = new BigDecimal("1.00");
			
		    	/*CostContainer costs = new CostContainer(BigDecimal.valueOf(rho),
		    			new BigDecimal(1-rho, MathContext.DECIMAL64).setScale(2),
		    			new BigDecimal(Math.sqrt(rho), MathContext.DECIMAL64).setScale(2),
		    			new BigDecimal(Math.sqrt(1-rho), MathContext.DECIMAL64).setScale(2),
		    			new BigDecimal("1000000.00").setScale(2));
		    	CostContainer costs = new CostContainer(rho.setScale(2,BigDecimal.ROUND_UP),
		    			BigDecimal.valueOf(1).subtract(rho).setScale(2,BigDecimal.ROUND_UP),
		    			BigDecimal.valueOf(Math.sqrt(rho.doubleValue())).setScale(2,BigDecimal.ROUND_UP),
		    			BigDecimal.valueOf(Math.sqrt(1-rho.doubleValue())).setScale(2,BigDecimal.ROUND_UP),
		    			BigDecimal.valueOf(1000000.00));*/
		    
			CostContainer costs = new CostContainer(rho.setScale(2,BigDecimal.ROUND_UP),
					rho.setScale(2,BigDecimal.ROUND_UP),
					rho.setScale(2,BigDecimal.ROUND_UP),
					rho.setScale(2,BigDecimal.ROUND_UP),
	    			BigDecimal.valueOf(1000000.00));
			
		    	Scenarios newscenarios = new Scenarios(config, config.getDestNetworkPrefixes(), srcNetworkPrefixes);
		    	for(Scenario s: scenarios.getScenarios()){
			    		if(newscenarios.getNbScenarios()==0){
			    			newscenarios.addScenario(s);
			    			continue;
			    		}
			    		
			    		
			    		
			    		if(s.getNbNodes()<300 && s.getNbNodes()>2 && s.getNbEdges()<300){		    				
				    		for(Scenario ns: newscenarios.getScenarios()){
				    			DecoratedGraph first, second;
				    			if(ns.getNbNodes()<300 && ns.getNbNodes()>2 && ns.getNbEdges()<300){
				    				
									try {
										first = GraphConverter.parse(s.getScenario());//from
										second = GraphConverter.parse( ns.getScenario());//to
										EditPath editPath = EditPathFinder.find(first, second, costs);
										predictionOutput.print(rho.setScale(2,BigDecimal.ROUND_UP)+";"+s.getSrcNetworkPrefixes()+"-"+s.getId()+";");
										predictionOutput.print(ns.getSrcNetworkPrefixes()+"-"+ns.getId()+";");
										
										/*System.out.println(editPath);*/
										BigDecimal edgeDeletionCosts = new BigDecimal(0).setScale(3);
										BigDecimal nodeDeletionCosts = new BigDecimal(0).setScale(3);
										BigDecimal edgeInsertionCosts = new BigDecimal(0).setScale(3);
										BigDecimal nodeInsertionCosts = new BigDecimal(0).setScale(3);
										for(NodeEditPath nodeEditPath : editPath.getNodeEditPaths()) {
											for(EditOperation editOperation : nodeEditPath.getEditOperations()) {
												if (editOperation instanceof EdgeDeletion)
													edgeDeletionCosts = edgeDeletionCosts.add(editOperation.getCost());
												if (editOperation instanceof EdgeInsertion)
													edgeInsertionCosts = edgeInsertionCosts.add(editOperation.getCost());
												if (editOperation instanceof NodeDeletion)
													nodeDeletionCosts = nodeDeletionCosts.add(editOperation.getCost());
												if (editOperation instanceof NodeInsertion)
													nodeInsertionCosts = nodeInsertionCosts.add(editOperation.getCost());
											}
										}
										/*double distance = (edgeCosts.doubleValue()+nodeCosts.doubleValue())/(2*(s.getNbEdges()+ns.getNbEdges()+s.getNbNodes()+ns.getNbNodes()));
										distance/=(s.getNbEdges()+ns.getNbEdges());
										System.out.print(edgeCosts.doubleValue()+"/"+(s.getNbEdges()+ns.getNbEdges())+" "+(edgeCosts.doubleValue()/(s.getNbEdges()+ns.getNbEdges()))+" "+distance+"# ");
										distance +=nodeCosts.doubleValue()/(s.getNbNodes()+ns.getNbNodes());
										System.out.print(nodeCosts.doubleValue()+"/"+(s.getNbNodes()+ns.getNbNodes())+" "+(nodeCosts.doubleValue()/(s.getNbNodes()+ns.getNbNodes()))+"# ");
										distance /=2;
										if(distance>=1.0)
										 System.out.println(editPath.getCost() +" : "+ distance +" : "+ edgeCosts.setScale(2) +" : "+ nodeCosts.setScale(2) +" : "+ (s.getNbEdges()+ns.getNbEdges()) +" : "+ (s.getNbNodes()+ns.getNbNodes()));
										 */
										/*System.out.println(editPath.getCost() +" : "
										 +(editPath.getCost().doubleValue()/(2*(s.getNbEdges()+ns.getNbEdges()
												 +s.getNbNodes()+ns.getNbNodes()))) +" : "+ s.getNbEdges()
												 +"+"+ns.getNbEdges() +"("+(s.getNbEdges()+ns.getNbEdges())+") : "
												 + s.getNbNodes()+"+"+ns.getNbNodes()+"("+(s.getNbNodes()
														 +ns.getNbNodes())+")");
										*/
										predictionOutput.println(nodeDeletionCosts+";"+nodeInsertionCosts
												+";"+edgeInsertionCosts+";"+edgeDeletionCosts+";"+editPath.getCost() +";"
												+round(editPath.getCost().doubleValue()/(2*(s.getNbEdges()+ns.getNbEdges()
												+s.getNbNodes()+ns.getNbNodes())), 2) +";"+ s.getNbNodes()+";"+ s.getNbEdges()
												+";"+ns.getNbNodes()+";"+ns.getNbEdges());
										
						    			// Form the combined result graph
						    			//Graph graph = GraphConverter.combine(editPath);
						    			//graph.printGraph(System.out);
									
									} catch (ParseException | CostLimitExceededException e) {
										e.printStackTrace();
									}
				    			}
				    		}
				    		newscenarios.addScenario(s);
			    		}
			    }
		    //} end for rho
	    }
		config.closeOutputFile();
	}
	public static double round(double num, int places) {
		 if (places < 0) throw new IllegalArgumentException();

		    BigDecimal bd = new BigDecimal(num);
		    bd = bd.setScale(places, RoundingMode.HALF_UP);
		    return bd.doubleValue();
	}
	
}
