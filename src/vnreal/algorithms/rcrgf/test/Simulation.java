package vnreal.algorithms.rcrgf.test;

import java.text.DecimalFormat;

import vnreal.algorithms.AlgorithmParameter;
import vnreal.algorithms.AvailableResources;
import vnreal.algorithms.isomorphism.SubgraphIsomorphismAlgorithm;
import vnreal.algorithms.isomorphism.SubgraphIsomorphismStackAlgorithm;
import vnreal.algorithms.rcrgf.config.Constants;
import vnreal.algorithms.rcrgf.core.RCRGFStackAlgorithm;
import vnreal.algorithms.rcrgf.util.Utils;
import vnreal.core.Scenario;
import vnreal.io.XMLImporter;

public class Simulation {
	public static void main(String[] args) {
		Simulation simulation = new Simulation();
		DecimalFormat decimalFormat = new DecimalFormat("#0.00");
		DecimalFormat decimalFormat2 = new DecimalFormat("#0.0");
		for (int snodes = 100; snodes <= 500; snodes += 50) {
			for (double ration = 0.01; ration < 0.105; ration += 0.01) {
				for (double alpha = 0.3; alpha < 1.25; alpha += 0.1) {
					String filename = Constants.WRITE_RESOURCE + "topology_" + snodes + "_1_" + 
						(Utils.equal(ration, 0.1) ? "0.1" : decimalFormat.format(ration))
						+ "_" + decimalFormat2.format(alpha) + ".xml";
					System.out.println("Process: " + filename);
					// 读取文件
					simulation.doRCRGF(filename);
					simulation.doSubgraph(filename);
					simulation.doGreedy(filename);
				}
			}
		}
	}
	
	private void doSubgraph(String filename) {
		Scenario scenario = XMLImporter.importScenario(filename);
		SubgraphIsomorphismStackAlgorithm algorithm =
				new SubgraphIsomorphismStackAlgorithm(scenario.getNetworkStack(), new SubgraphIsomorphismAlgorithm());
		algorithm.performEvaluation();
	}
	
	private void doRCRGF(String filename) {
		Scenario scenario = XMLImporter.importScenario(filename);
		RCRGFStackAlgorithm rcrgfStackAlgorithm =
				new RCRGFStackAlgorithm(scenario.getNetworkStack());
		rcrgfStackAlgorithm.performEvaluation();
	}
	
	private void doGreedy(String filename) {
		Scenario scenario = XMLImporter.importScenario(filename);
		AlgorithmParameter param = new AlgorithmParameter();
		param.put("distance", "20"); // 论文中
		param.put("kShortestPaths", "5"); //
		param.put("overload", "False");
		param.put("PathSplitting", "False");
		AvailableResources availableResources
			= new AvailableResources(param);
		availableResources.setStack(scenario.getNetworkStack());
		availableResources.performEvaluation();
	}
}
