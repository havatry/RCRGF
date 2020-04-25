package vnreal.algorithms.rcrgf.main;

import vnreal.algorithms.AlgorithmParameter;
import vnreal.algorithms.rcrgf.utils.Constants;
import vnreal.core.Scenario;
import vnreal.io.XMLImporter;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Simulation {
	public static void main(String[] args) throws IOException {
		Simulation simulation = new Simulation();
        Properties properties = new Properties();
        properties.load(new FileInputStream("results/conf/rcrgf.properties"));
		for (int snodes = Integer.parseInt(properties.getProperty("rcrgf.snode.min"));
             snodes < Integer.parseInt(properties.getProperty("rcrgf.snode.max"));
             snodes += Integer.parseInt(properties.getProperty("rcrgf.snode.step"))) {
//			for (double ration = 0.01; ration < 0.105; ration += 0.01) {
            double ration = Double.parseDouble(properties.getProperty("rcrgf.ratio")),
                    alpha = Double.parseDouble(properties.getProperty("rcrgf.alpha"));
//				for (double alpha = 0.3; alpha < 1.25; alpha += 0.1) {
            String filename = Constants.WRITE_RESOURCE + "topology_" + snodes + "_1_" + ration + "_" + alpha + ".xml";
            System.out.println("Process: " + filename);
            // 读取文件
            simulation.doRCRGF(filename);
            simulation.doSubgraph(filename);
            simulation.doGreedy(filename);
//				}
//			}
		}
	}
	
	private void doSubgraph(String filename) {

	}
	
	private void doRCRGF(String filename) {
		Scenario scenario = XMLImporter.importScenario(filename);
		RCRGFAlgorithmWrap rcrgfStackAlgorithm =
				new RCRGFAlgorithmWrap(scenario.getNetworkStack());
		rcrgfStackAlgorithm.performEvaluation();
	}
	
	private void doGreedy(String filename) {
		Scenario scenario = XMLImporter.importScenario(filename);
		AlgorithmParameter param = new AlgorithmParameter();
		param.put("distance", "20"); // 论文中
		param.put("kShortestPaths", "1"); //
		param.put("overload", "False");
		param.put("PathSplitting", "False");

	}
}
