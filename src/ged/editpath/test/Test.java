package ged.editpath.test;

import java.math.BigDecimal;

import ged.editpath.EditPath;
import ged.editpath.EditPathFinder;
import ged.graph.DecoratedGraph;
import ged.graph.GraphConverter;
import ged.processor.CostContainer;

/**
 * Test class.
 *
 * @author Roman Tekhov
 */
public class Test {


	public static void main(String[] args) {
		test(5, 10);
	}


	private static void test(int fromNodeCount, int toNodeCount) {
		try {
			double duration = 0;
			EditPath editPath;
			int n = 2;

			for(int i = 0; i < n; i++) {
				CostContainer costContainer = new CostContainer();
				costContainer.setNodeSubstitutionCost(new BigDecimal("1000000.00").setScale(2));
				
				String fromDotExpr = DotGenerator.generate(fromNodeCount, fromNodeCount * 3, 1, "from", true);
				String toDotExpr = DotGenerator.generate(toNodeCount, toNodeCount * 3, 1, "to", true);
				System.out.println(fromDotExpr);
				System.out.println(toDotExpr);
				
				DecoratedGraph from = GraphConverter.parse(fromDotExpr);
				DecoratedGraph to = GraphConverter.parse(toDotExpr);

				long start = System.currentTimeMillis();

				editPath = EditPathFinder.find(from, to, costContainer);

				long end = System.currentTimeMillis();
				System.out.println(editPath);
				
				duration += end - start;
			}

			duration /= n;

			System.out.println(fromNodeCount + ",  " + toNodeCount + " - " + duration);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
