package vnreal.algorithms.myrcrgf.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import vnreal.network.NetworkStack;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.virtual.VirtualNetwork;

public class ProduceCase {
	public static void main(String[] args) {
		List<Integer> startList;
		List<Integer> endList;
		double arive_lambda = 3.0 / 100;
		double preserve_lambda = 1.0 / 500;
		int end = 2000;
		startList = new ArrayList<Integer>();
		endList = new ArrayList<>();
		startList.add(Utils.exponentialDistribution(arive_lambda));
		endList.add(Utils.exponentialDistribution(preserve_lambda));
		
		while (startList.get(startList.size() - 1) <= end) {
			startList.add(startList.get(startList.size() - 1) + Utils.exponentialDistribution(arive_lambda));
			endList.add(Utils.exponentialDistribution(preserve_lambda));
		}
		startList.remove(startList.size() - 1);
		endList.remove(endList.size() - 1);
		// 生成拓扑
		GenerateGraph generateGraph = new GenerateGraph(initProperty());
		SubstrateNetwork substrateNetwork = generateGraph.generateSNet();
		// 生产虚拟网络
		List<VirtualNetwork> vns = new LinkedList<VirtualNetwork>();
		for (int i = 0; i < startList.size(); i++) {
			VirtualNetwork virtualNetwork;
			do {
				virtualNetwork= generateGraph.generateVNet();
			} while (!Utils.isConnected(virtualNetwork));
			vns.add(virtualNetwork);
		}
		NetworkStack networkStack = new NetworkStack(substrateNetwork, vns);
		FileHelper.saveContext(Constants.FILE_NAME, networkStack, startList, endList);
		System.out.println("Produce Successfully");
	}
	
	private static Properties initProperty() {
		Properties properties = new Properties();
		properties.put("snNodes", "100");
		properties.put("minVNodes", "5");
		properties.put("maxVNodes", "11");
		properties.put("snAlpha", "0.5");
		properties.put("vnAlpha", "0.5");
		//---------//
		return properties;
	}
}
