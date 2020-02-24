package vnreal.algorithms.myRCRGF.test;

import org.junit.Before;
import org.junit.Test;

import vnreal.algorithms.isomorphism.SubgraphIsomorphismAlgorithm;
import vnreal.algorithms.isomorphism.SubgraphIsomorphismStackAlgorithm;
import vnreal.algorithms.myRCRGF.util.Constants;
import vnreal.core.Scenario;
import vnreal.io.XMLImporter;
import vnreal.network.NetworkStack;

public class TestIsomorphism {
private NetworkStack networkStack;
	
	@Before
	public void start() {
		// 从文件中读取网络栈
		String filename = "topology_140_1_0.02_0.6.xml";
		Scenario scenario = XMLImporter.importScenario(Constants.WRITE_RESOURCE + filename);
		networkStack = scenario.getNetworkStack();
	}
	
	@Test // 测试算法功能
	public void test01() {
		SubgraphIsomorphismStackAlgorithm stackAlgorithm = new SubgraphIsomorphismStackAlgorithm(networkStack,
				new SubgraphIsomorphismAlgorithm());
		stackAlgorithm.performEvaluation();
	}
}
