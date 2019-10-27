package vnreal.algorithms.argf.test;

import org.junit.Before;
import org.junit.Test;

import vnreal.algorithms.argf.config.Constants;
import vnreal.algorithms.argf.core.ARGFStackAlgorithm;
import vnreal.core.Scenario;
import vnreal.io.XMLImporter;
import vnreal.network.NetworkStack;

public class TestARGF {
	private static NetworkStack networkStack;
	
	@Before
	public void start() {
		String filename = "topology_150_1_0.03_0.3.xml";
		Scenario scenario = XMLImporter.importScenario(Constants.WRITE_RESOURCE + filename);
		networkStack = scenario.getNetworkStack();
	}
	
	@Test // ≤‚ ‘À„∑®π¶ƒ‹
	public void test01() {
		ARGFStackAlgorithm argfStackAlgorithm = new ARGFStackAlgorithm(networkStack);
		argfStackAlgorithm.performEvaluation();
	}
}
