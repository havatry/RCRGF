package vnreal.algorithms.argf.test;

import org.junit.Before;
import org.junit.Test;

import vnreal.algorithms.argf.config.Constants;
import vnreal.algorithms.argf.core.ARGFStackAlgorithm;
import vnreal.core.Scenario;
import vnreal.io.XMLImporter;
import vnreal.network.NetworkStack;

public class TestARGF {
	private NetworkStack networkStack;
	
	@Before
	public void start() {
		// 从文件中读取网络栈
		String filename = "topology_600_1_0.01_0.3.xml";
		Scenario scenario = XMLImporter.importScenario(Constants.WRITE_RESOURCE + filename);
		networkStack = scenario.getNetworkStack();
	}
	
	@Test // 测试算法功能
	public void test01() {
		ARGFStackAlgorithm argfStackAlgorithm = new ARGFStackAlgorithm(networkStack);
		argfStackAlgorithm.performEvaluation();
	}
}
