package vnreal.algorithms.myrcrgf.test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Properties;

import org.junit.Test;

import vnreal.algorithms.myrcrgf.util.Constants;
import vnreal.algorithms.myrcrgf.util.FileHelper;
import vnreal.algorithms.myrcrgf.util.GenerateGraph;
import vnreal.algorithms.myrcrgf.util.SummaryResult;
import vnreal.algorithms.myrcrgf.util.Utils;
import vnreal.io.XMLImporter;
import vnreal.network.NetworkStack;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.virtual.VirtualNetwork;

public class TestCase {
	@Test
	public void testDistribution() {
		int[] v1 = new int[6];
		int[] v2 = new int[6];
		for (int i = 1; i <= 5; i++) {
			v1[i] = v1[i - 1] + Utils.exponentialDistribution(3.0 / 100);
			v2[i] = Utils.exponentialDistribution(1.0 / 500);
		}
		System.out.println(Arrays.toString(v1));
		System.out.println(Arrays.toString(v2));
	}
	
	@Test
	public void testPrintResult() {
		SummaryResult summaryResult = new SummaryResult();
		System.out.println(summaryResult);
	}
	
	@Test
	public void testWriteFile() {
		Properties properties = new Properties();
		properties.put("snNodes", "10");
		properties.put("minVNodes", "5");
		properties.put("maxVNodes", "6");
		properties.put("snAlpha", "0.5");
		properties.put("vnAlpha", "0.5");
		GenerateGraph generateGraph = new GenerateGraph(properties);
		SubstrateNetwork substrateNetwork = generateGraph.generateSNet();
		VirtualNetwork virtualNetwork = generateGraph.generateVNet();
		FileHelper.writeToXml(Constants.FILE_NAME, new NetworkStack(substrateNetwork,
				Arrays.asList(virtualNetwork)));
	}
	
	@Test
	public void testReadFile() {
		System.out.println(XMLImporter.importScenario("results/file/substratework_20191130135939.xml").getSubstrate());
	}
}
