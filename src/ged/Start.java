package ged;

import java.math.BigDecimal;
import java.math.MathContext;

import ged.editpath.CostLimitExceededException;
import ged.graph.DotParseException;
import ged.gui.GUICreator;
import ged.processor.CostContainer;
import ged.processor.Processor;

/**
 * Application starter.
 * 
 * @author Roman Tekhov
 */
public class Start {
	
	public static void main(String[] args) throws DotParseException, CostLimitExceededException {		
		//GUICreator.createAndShow();
		String fromDot="digraph '10.31.6.2' {"+
"  subgraph 'clusterhost10.31.6.2' {"+
"    graph ["+
"      label = 'IP: 10.31.6.2'"+
"    ];"+

"    subgraph clusterport80 {"+
"      graph ["+
"        label = 'port: 80'"+
"      ];"+

"      '10.31.6.2:80-2013031' ["+
"        color = red,"+
"        label = <<FONT COLOR='blue'>2013031</FONT>>,"+
"        shape = Msquare"+
"      ];"+
"    }"+
"  }"+
"  '10.31.6.2:80-2013031' ["+
"    color = red,"+
"    label = <<FONT COLOR='blue'>2013031</FONT>>,"+
"    shape = Msquare"+
"  ];"+
"  '10.31.6.2:80-2013031' -> '10.31.6.2:80-2013031' ["+
"    label = 84"+
"  ];"+
"}";
		String toDot="digraph '10.31.8.2' {"+
"  subgraph 'clusterhost10.31.8.2' {"+
"    graph ["+
"      label = 'IP: 10.31.8.2'"+
"    ];"+

"    subgraph clusterport80 {"+
"      graph ["+
"        label = 'port: 80'"+
"      ];"+

"      '10.31.8.2:80-2013031' ["+
"        color = red,"+
"        label = <<FONT COLOR='blue'>2013031</FONT>>,"+
"        shape = Msquare"+
"      ];"+
"    }"+
"  }"+
"  '10.31.8.2:80-2013031' ["+
"    color = red,"+
"    label = <<FONT COLOR='blue'>2013031</FONT>>,"+
"    shape = Msquare"+
"  ];"+
"  '10.31.8.2:80-2013031' -> '10.31.8.2:80-2013031' ["+
"    label = 90"+
"  ];"+
"}";
		//CostContainer costs = new CostContainer();
		//System.out.println(costs.getEdgeDeletionCost());
		//Processor.process(fromDot, toDot, costs);
		
		double rho = 0.95;
		CostContainer costs = new CostContainer(BigDecimal.valueOf(rho).round(MathContext.DECIMAL32),
    			BigDecimal.valueOf(1-rho).round(MathContext.DECIMAL32),
    			BigDecimal.valueOf(Math.sqrt(rho)).round(MathContext.DECIMAL32),
    			BigDecimal.valueOf(Math.sqrt(1-rho)).round(MathContext.DECIMAL32),
    			BigDecimal.valueOf(1000000.00));
		
		
		
		System.out.println(costs.getEdgeDeletionCost());
		System.out.println(6.1/34);
	}

}
