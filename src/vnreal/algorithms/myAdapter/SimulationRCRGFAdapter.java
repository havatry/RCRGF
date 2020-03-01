package vnreal.algorithms.myAdapter;

import vnreal.algorithms.AlgorithmParameter;
import vnreal.algorithms.isomorphism.SubgraphIsomorphismAlgorithm;
import vnreal.algorithms.isomorphism.SubgraphIsomorphismStackAlgorithm;
import vnreal.algorithms.myRCRGF.core.RCRGFStackAlgorithm;
import vnreal.algorithms.myRCRGF.util.AvailableResourcesCompare;
import vnreal.algorithms.myRCRGF.util.Constants;
import vnreal.algorithms.myRCRGF.util.SubgraphIsomorphismCompare;
import vnreal.core.Scenario;
import vnreal.io.XMLImporter;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Properties;

/**
 * 对外提供一阶段实验接口
 */
public class SimulationRCRGFAdapter {
	public void doSubgraph(String filename, int id, String type) {
		Scenario scenario = XMLImporter.importScenario(filename);
		SubgraphIsomorphismCompare algorithm =
				new SubgraphIsomorphismCompare(scenario.getNetworkStack(), new SubgraphIsomorphismAlgorithm());
		EventProcess eventProcess = new EventProcess(algorithm, id, type);
		algorithm.performEvaluation();
		eventProcess.process(1, scenario.getSubstrate(), scenario.getVirtuals().get(0),algorithm.getStatistics().getSuccVns() == 1);
	}
	
	public void doRCRGF(String filename, int id, String type) {
		Scenario scenario = XMLImporter.importScenario(filename);
		RCRGFStackAlgorithm rcrgfStackAlgorithm =
				new RCRGFStackAlgorithm(scenario.getNetworkStack());
		EventProcess eventProcess = new EventProcess(rcrgfStackAlgorithm, id, type);
		rcrgfStackAlgorithm.performEvaluation();
		eventProcess.process(1, scenario.getSubstrate(), scenario.getVirtuals().get(0), rcrgfStackAlgorithm.getStatistics().getSuccVns() == 1);
	}
	
	public void doGreedy(String filename, int id, String type) {
		Scenario scenario = XMLImporter.importScenario(filename);
		AlgorithmParameter param = new AlgorithmParameter();
		param.put("distance", "20"); // 论文中
		param.put("kShortestPaths", "1"); //
		param.put("overload", "False");
		param.put("PathSplitting", "False");
		AvailableResourcesCompare availableResources
			= new AvailableResourcesCompare(param);
		availableResources.setStack(scenario.getNetworkStack());
		EventProcess eventProcess = new EventProcess(availableResources, id, type);
		availableResources.performEvaluation();
		eventProcess.process(1, scenario.getSubstrate(), scenario.getVirtuals().get(0), availableResources.getStatistics().getSuccVns() == 1);
	}
}
