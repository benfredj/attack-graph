package ged.processor;

import ged.editpath.CostLimitExceededException;
import ged.editpath.EditPath;
import ged.editpath.EditPathFinder;
import ged.graph.DecoratedGraph;
import ged.graph.DotParseException;
import ged.graph.GraphConverter;
import ged.graph.ParseException;
import att.grappa.Graph;

/**
 * Component responsible for evaluating the entire edit distance computation
 * procedure together with related pre- and post-activities.
 * 
 * @author Roman Tekhov
 */
public class Processor {
	
	/**
	 * Converts the input data to graph structures, executes the edit distance
	 * finding algorithm, prepares and returns the output.
	 * 
	 * @param inputContainer input data 
	 * @return output data
	 * 
	 * @throws DotParseException in case of DOT to graph parsing errors
	 * @throws CostLimitExceededException in case when an acceptable cost
	 * 			limit has been exceeded
	 */
	public static OutputContainer process(InputContainer inputContainer) 
				throws DotParseException, CostLimitExceededException {
		
		// Parse the input graphs
		DecoratedGraph first = GraphConverter.parse(inputContainer.getFromDot());
		DecoratedGraph second = GraphConverter.parse(inputContainer.getToDot());
		
		if(first.isDirected() && !second.isDirected() || 
				!first.isDirected() && second.isDirected()) {
			throw new DotParseException("Can't compare directed and undirected graphs!");
		}
		
		// Calculate the edit path
		EditPath editPath = EditPathFinder.find(first, second, inputContainer.getCostContainer());
		
		// Form the combined result graph
		Graph graph = GraphConverter.combine(editPath);
		
		System.out.println(inputContainer.getFromDot());
		System.out.println(inputContainer.getToDot());
		System.out.println(editPath);
		graph.printGraph(System.out);
		// Display result
		return new OutputContainer(graph, editPath);
	}
	
	public static void process(String fromDot, String toDot,CostContainer costs) 
			throws DotParseException, CostLimitExceededException {
	
		// Parse the input graphs
		DecoratedGraph first = GraphConverter.parse(fromDot);
		DecoratedGraph second = GraphConverter.parse(toDot);
		
		if(first.isDirected() && !second.isDirected() || 
				!first.isDirected() && second.isDirected()) {
			throw new DotParseException("Can't compare directed and undirected graphs!");
		}
		
		// Calculate the edit path
		EditPath editPath = EditPathFinder.find(first, second, costs);
		System.out.println(editPath);
		
		// Form the combined result graph
		Graph graph = GraphConverter.combine(editPath);
		graph.printGraph(System.out);
		// Display result
		//return new OutputContainer(graph, editPath);
	}
	public static void process(Graph from, Graph to,CostContainer costs) 
			throws CostLimitExceededException, ParseException {
	
		// Parse the input graphs
		DecoratedGraph first = GraphConverter.parse(from);
		DecoratedGraph second = GraphConverter.parse(to);
		
		if(first.isDirected() && !second.isDirected() || 
				!first.isDirected() && second.isDirected()) {
			throw new ParseException("Can't compare directed and undirected graphs!");
		}
		
		// Calculate the edit path
		EditPath editPath = EditPathFinder.find(first, second, costs);
		System.out.println(editPath);
		
		// Form the combined result graph
		Graph graph = GraphConverter.combine(editPath);
		graph.printGraph(System.out);
		// Display result
		//return new OutputContainer(graph, editPath);
	}
	
	public static void process(DecoratedGraph first, DecoratedGraph second,CostContainer costs) 
			throws DotParseException, CostLimitExceededException {
	
		if(first.isDirected() && !second.isDirected() || 
				!first.isDirected() && second.isDirected()) {
			throw new DotParseException("Can't compare directed and undirected graphs!");
		}
		
		// Calculate the edit path
		EditPath editPath = EditPathFinder.find(first, second, costs);
		System.out.println(editPath);
		
		// Form the combined result graph
		Graph graph = GraphConverter.combine(editPath);
		graph.printGraph(System.out);
		// Display result
		//return new OutputContainer(graph, editPath);
	}
}
