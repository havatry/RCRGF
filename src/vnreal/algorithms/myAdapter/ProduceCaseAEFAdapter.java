package vnreal.algorithms.myAdapter;

import vnreal.algorithms.myAEF.util.Constants;
import vnreal.algorithms.myAEF.util.FileHelper;
import vnreal.algorithms.myAEF.util.GenerateGraph;
import vnreal.algorithms.myAEF.util.Utils;
import vnreal.network.NetworkStack;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.virtual.VirtualNetwork;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class ProduceCaseAEFAdapter {
    public String generateFile(double arrive_lambda, double preserve_lambda, int total, double baseBandwith, double bandwith,
                               double snAlpha, double snBeta) {
        Properties properties = initProperty();
        properties.put("snAlpha", snAlpha);
        properties.put("snBeta", snBeta);
        properties.put("substrateBaseBandwithResource", baseBandwith);
        properties.put("bandwithResource", bandwith);
        List<Integer> startList = new ArrayList<>();
        List<Integer> endList = new ArrayList<>();
        startList.add(Utils.exponentialDistribution(arrive_lambda));
        endList.add(Utils.exponentialDistribution(preserve_lambda));

        while (startList.get(startList.size() - 1) <= total) {
            startList.add(startList.get(startList.size() - 1) + Utils.exponentialDistribution(arrive_lambda));
            endList.add(Utils.exponentialDistribution(preserve_lambda));
        }
        startList.remove(startList.size() - 1);
        endList.remove(endList.size() - 1);

        // ��������
        GenerateGraph generateGraph = new GenerateGraph(properties);
        SubstrateNetwork substrateNetwork = generateGraph.generateSNet();
        // ������������
        List<VirtualNetwork> vns = new LinkedList<VirtualNetwork>();
        for (int i = 0; i < startList.size(); i++) {
            VirtualNetwork virtualNetwork;
            do {
                virtualNetwork= generateGraph.generateVNet();
            } while (!Utils.isConnected(virtualNetwork));
            vns.add(virtualNetwork);
        }
        NetworkStack networkStack = new NetworkStack(substrateNetwork, vns);
        return FileHelper.saveContext(Constants.FILE_NAME, networkStack, startList, endList);
    }
	public static void main(String[] args) {
		List<Integer> startList;
		List<Integer> endList;
		// 3.0 / 100 & 1.0 / 500
		double arrive_lambda = 10.0 / 100;
		double preserve_lambda = 1.0 / 1000;
		int end = 2000;
		startList = new ArrayList<Integer>();
		endList = new ArrayList<>();
		startList.add(Utils.exponentialDistribution(arrive_lambda));
		endList.add(Utils.exponentialDistribution(preserve_lambda));
		
		while (startList.get(startList.size() - 1) <= end) {
			startList.add(startList.get(startList.size() - 1) + Utils.exponentialDistribution(arrive_lambda));
			endList.add(Utils.exponentialDistribution(preserve_lambda));
		}
		startList.remove(startList.size() - 1);
		endList.remove(endList.size() - 1);
		
		// ��������
		GenerateGraph generateGraph = new GenerateGraph(initProperty());
		SubstrateNetwork substrateNetwork = generateGraph.generateSNet();
		// ������������
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
	
	public static LinkedList<VirtualNetwork> getVirtuals(int n) {
		GenerateGraph generateGraph = new GenerateGraph(initProperty());
		// ������������
		LinkedList<VirtualNetwork> vns = new LinkedList<VirtualNetwork>();
		for (int i = 0; i < n; i++) {
			VirtualNetwork virtualNetwork;
			do {
				virtualNetwork= generateGraph.generateVNet();
			} while (!Utils.isConnected(virtualNetwork));
			vns.add(virtualNetwork);
		}
		return vns;
	}
	
	private static Properties initProperty() {
		Properties properties = new Properties();
		properties.put("snNodes", "100");
		properties.put("minVNodes", "5");
		properties.put("maxVNodes", "10");
//		properties.put("snAlpha", "0.5"); // 0.1 -> 0.5
		properties.put("vnAlpha", "0.5");
//		properties.put("snBeta", "0.1");
		properties.put("vnBeta", "0.25");
//        properties.put("bandwithResource", "20");
//        properties.put("substrateBaseBandwithResource", "50");
		//---------//
		return properties;
	}
}
