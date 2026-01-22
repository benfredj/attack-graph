package ged.gui;

import ged.editpath.EditPath;
import ged.graph.DotParseException;
import ged.graph.GraphConverter;
import java.io.StringWriter;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import att.grappa.Graph;

/**
 * Output displaying window.
 * 
 * @author Roman Tekhov
 */
class ResultFrame extends JFrame {

	private static final long serialVersionUID = 1L;
		
	ResultFrame(Graph grappaGraph, EditPath editPath)  {
		super("Result");
				
		GraphPanel graphPanel = new GraphPanel(grappaGraph);
		EditOperationsPanel editOperationsPanel = new EditOperationsPanel(editPath);
		DotOutputPanel dotOutputPanel;
		String content = null;
		//dotOutputPanel = new DotOutputPanel(GraphConverter.toDot(grappaGraph));
	 try {
		              StringWriter theGraph = new StringWriter();
		              grappaGraph.printGraph(theGraph);
		              theGraph.flush();
		              content = theGraph.toString();
		              theGraph.close();
		          } catch(Exception ex) {
		             ex.printStackTrace();
		              
		         }

			 
		
		dotOutputPanel = new DotOutputPanel(content);
		
		Dimension preferedSize = new Dimension(400, 700);
		graphPanel.setPreferredSize(preferedSize);
		editOperationsPanel.setPreferredSize(preferedSize);
		dotOutputPanel.setPreferredSize(preferedSize);
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
				graphPanel, editOperationsPanel);
		JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
				splitPane, dotOutputPanel);
		
		splitPane.setResizeWeight(0.5);
		splitPane2.setResizeWeight(0.5);
		
		//add(splitPane);
		add(splitPane2);
		
		pack();
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}
