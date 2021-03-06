package vnreal.algorithms.myRCRGF.test;

import org.junit.Before;
import org.junit.Test;

import vnreal.algorithms.myRCRGF.util.Constants;
import vnreal.algorithms.myRCRGF.core.RCRGFStackAlgorithm;
import vnreal.core.Scenario;
import vnreal.io.XMLImporter;
import vnreal.network.NetworkStack;

public class TestARGF {
	private static NetworkStack networkStack;
	
	@Before
	public void start() {
		String filename = "topology_100_1_0.01_1.1.xml";
		Scenario scenario = XMLImporter.importScenario(Constants.WRITE_RESOURCE + filename);
		networkStack = scenario.getNetworkStack();
	}
	
	@Test // 测试算法功能
	public void test01() {
		RCRGFStackAlgorithm argfStackAlgorithm = new RCRGFStackAlgorithm(networkStack);
		argfStackAlgorithm.performEvaluation();
	}
}
