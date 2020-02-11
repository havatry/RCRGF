package vnreal.algorithms.myAEF.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Test;

import vnreal.algorithms.myAEF.util.Constants;
import vnreal.algorithms.myAEF.util.FileHelper;
import vnreal.algorithms.myAEF.util.GenerateGraph;
import vnreal.algorithms.myAEF.util.SummaryResult;
import vnreal.algorithms.myAEF.util.Utils;
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
	
	@Test
	public void testSaveContext() {
		Properties properties = new Properties();
		properties.put("snNodes", "10");
		properties.put("minVNodes", "5");
		properties.put("maxVNodes", "6");
		properties.put("snAlpha", "0.5");
		properties.put("vnAlpha", "0.5");
		GenerateGraph generateGraph = new GenerateGraph(properties);
		SubstrateNetwork substrateNetwork = generateGraph.generateSNet();
		VirtualNetwork virtualNetwork = generateGraph.generateVNet();
		List<Integer> startList;
	    List<Integer> endList;
		startList = new ArrayList<Integer>();
		endList = new ArrayList<>();
		startList.add(Utils.exponentialDistribution(3.0 / 100));
		endList.add(Utils.exponentialDistribution(1.0 / 500));
		
		while (startList.get(startList.size() - 1) <= 2000) {
			startList.add(startList.get(startList.size() - 1) + Utils.exponentialDistribution(3.0 / 100));
			endList.add(Utils.exponentialDistribution(1.0 / 500));
		}
		startList.remove(startList.size() - 1);
		endList.remove(endList.size() - 1);
		FileHelper.saveContext(Constants.FILE_NAME, new NetworkStack(substrateNetwork, Arrays.asList(virtualNetwork)),
				startList, endList);
	}
	
	@Test
	public void testReadContext() {
		Object[] result = FileHelper.readContext("results/file/substratework_20191201124020.xml");
		System.out.println(result[1]);
		System.out.println(result[2]);
	}
}
