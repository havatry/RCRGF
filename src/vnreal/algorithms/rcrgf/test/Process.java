package vnreal.algorithms.rcrgf.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import vnreal.algorithms.AlgorithmParameter;
import vnreal.algorithms.AvailableResources;
import vnreal.algorithms.isomorphism.SubgraphIsomorphismAlgorithm;
import vnreal.algorithms.isomorphism.SubgraphIsomorphismStackAlgorithm;
import vnreal.algorithms.rcrgf.config.Constants;
import vnreal.algorithms.rcrgf.core.RCRGFStackAlgorithm;
import vnreal.core.Scenario;
import vnreal.io.XMLImporter;

public class Process {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// 获取Map
		Properties properties = new Properties();
		properties.load(new FileInputStream("results/setting/config.properties"));
		if (properties.getProperty("resource") != null) {
			Constants.WRITE_RESOURCE = properties.getProperty("resource");
		}
		// 执行生成拓扑
		String[] snodesInfo = properties.getProperty("snodes").split("-");
		int snodes_min = Integer.parseInt(snodesInfo[0]);
		int snodes_step = Integer.parseInt(snodesInfo[1]);
		int snodes_max = Integer.parseInt(snodesInfo[2]);
//		String[] ratioInfo = properties.getProperty("ratio").split("-");
//		double ratio_min = Double.parseDouble(ratioInfo[0]);
//		double ratio_step = Double.parseDouble(ratioInfo[1]);
//		double ratio_max = Double.parseDouble(ratioInfo[2]);
		String[] vnodesInfo = properties.getProperty("vnodes").split("-");
		int vnodes_min = Integer.parseInt(vnodesInfo[0]);
		int vnodes_step = Integer.parseInt(vnodesInfo[1]);
		int vnodes_max = Integer.parseInt(vnodesInfo[2]);
		String ksp = properties.getProperty("ksp", "1");
		
		if (properties.getProperty("action").contains("produce")) {
			GenerateTopology generateTopology = new GenerateTopology();
			for (int snodes = snodes_min; snodes <= snodes_max; snodes += snodes_step) {
				for (int vnodes = vnodes_min; vnodes <= vnodes_max; vnodes += vnodes_step) {
					generateTopology.setSnodes(snodes);
					generateTopology.setVnodes(vnodes);
					generateTopology.write();
					System.out.println("write: " + generateTopology);
				}
			}
		}
		if (properties.getProperty("action").contains("compute")) {
			Process process = new Process();
			for (int snodes = snodes_min; snodes <= snodes_max; snodes += snodes_step) {
				for (int vnodes = vnodes_min; vnodes <= vnodes_max; vnodes += vnodes_step) {
					String filename = Constants.WRITE_RESOURCE + "topology_" + snodes + "_" + vnodes + ".xml";
					System.out.println("Process: " + filename);
					process.doRCRGF(filename);
					process.doSubgraph(filename);
					process.doGreedy(filename, ksp);
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
	
	private void doGreedy(String filename, String ksp) {
		Scenario scenario = XMLImporter.importScenario(filename);
		AlgorithmParameter param = new AlgorithmParameter();
		param.put("distance", "20"); // 论文中
		param.put("kShortestPaths", ksp); //
		param.put("overload", "False");
		param.put("PathSplitting", "False");
		AvailableResources availableResources
			= new AvailableResources(param);
		availableResources.setStack(scenario.getNetworkStack());
		availableResources.performEvaluation();
	}
}
