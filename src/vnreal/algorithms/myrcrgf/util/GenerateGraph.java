package vnreal.algorithms.myrcrgf.util;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import mulavito.graph.generators.WaxmanGraphGenerator;
import vnreal.constraints.demands.BandwidthDemand;
import vnreal.constraints.demands.CpuDemand;
import vnreal.constraints.resources.BandwidthResource;
import vnreal.constraints.resources.CpuResource;
import vnreal.network.NetworkStack;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.substrate.SubstrateNode;
import vnreal.network.virtual.VirtualLink;
import vnreal.network.virtual.VirtualNetwork;
import vnreal.network.virtual.VirtualNode;
import vnreal.ui.dialog.ConstraintsGeneratorDialog;

public class GenerateGraph {
	private Properties properties;
	
	public GenerateGraph(Properties properties) {
		//Created constructor stubs
		this.properties = properties;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public SubstrateNetwork generateSNet() {
		SubstrateNetwork substrateNetwork = new SubstrateNetwork(false, Constants.DIRECTED);
		int snNodes = Integer.parseInt((String) properties.getOrDefault("snNodes", "100"));
		while (snNodes-- > 0)
			substrateNetwork.addVertex(new SubstrateNode());
		
		double snAlpha = Double.parseDouble((String)properties.getOrDefault("snAlpha", "1.0"));
		double snBeta = Double.parseDouble((String)properties.getOrDefault("snBeta", "0.5"));
		WaxmanGraphGenerator sgg = new WaxmanGraphGenerator<>(snAlpha, snBeta, false);
		sgg.generate(substrateNetwork);
		
		// 100 * 100
		HashMap<SubstrateNode, Point2D> spos = sgg.getPositions();
		for (SubstrateNode v : substrateNetwork.getVertices()) {
			v.setCoordinateX(100.0 * spos.get(v).getX());
			v.setCoordinateY(100.0 * spos.get(v).getY());
		}
		
		// 生成约束
		List<Class<?>> resClassesToGenerate = new LinkedList<Class<?>>();
        List<String[]> resParamNamesToGenerate = new LinkedList<String[]>();
        List<String[]> resMaxValues = new ArrayList<String[]>();
        resClassesToGenerate.add(CpuResource.class);
        resClassesToGenerate.add(BandwidthResource.class);
        resParamNamesToGenerate.add(new String[]{"cycles"});
        resParamNamesToGenerate.add(new String[]{"bandwidth"});
        String cpu_resource = (String) properties.getOrDefault("cpuResource", "51");
        String bandwith_resource = (String) properties.getOrDefault("bandwithResource", "51");
        resMaxValues.add(new String[]{cpu_resource});
        resMaxValues.add(new String[]{bandwith_resource});
        NetworkStack networkStack = new NetworkStack(substrateNetwork, null); // 临时构造
        // 设置基准资源
        Constants.SWITCH_BASE_RES_DEM = Boolean.parseBoolean((String) properties.getOrDefault("switch", "true"));
        Constants.SUBSTRATE_BASE_CPU_RESOURCE = Double.parseDouble((String) properties.getOrDefault("substrateBaseCpuResource", "50"));
        Constants.SUBSTRATE_BASE_BANDWITH_RESOURCE = Double.parseDouble((String) properties.getOrDefault("substrateBaseBandwithResource", "50"));
        ConstraintsGeneratorDialog.generateConstraintsSubstrate(resClassesToGenerate, resParamNamesToGenerate, resMaxValues, networkStack);
		
        return substrateNetwork;
	}
	
	public VirtualNetwork generateVNet() {
		VirtualNetwork virtualNetwork = new VirtualNetwork(1, true, Constants.DIRECTED);
		int minVNodes = Integer.parseInt((String) properties.getOrDefault("minVNodes", "5"));
		int maxVNodes = Integer.parseInt((String) properties.getOrDefault("maxVNodes", "11"));
		int vnNodes = minVNodes + (int)(Math.random() * (maxVNodes - minVNodes));
		while (vnNodes-- > 0) {
			virtualNetwork.addVertex(new VirtualNode(1));
		}
		double vnAlpha = Double.parseDouble((String) properties.getOrDefault("vnAlpha", "1.0"));
		double vnBeta = Double.parseDouble((String) properties.getOrDefault("vnBeta", "0.5"));
		WaxmanGraphGenerator<VirtualNode, VirtualLink> vgg =
				new WaxmanGraphGenerator<VirtualNode, VirtualLink>(vnAlpha, vnBeta, false);
		vgg.generate(virtualNetwork);
		// 不需要调整坐标
		// 接下来生成约束
		List<Class<?>> resClassesToGenerateVN = new LinkedList<Class<?>>();
		List<String[]> resParamNamesToGenerateVN = new LinkedList<String[]>();
		List<String[]> resMaxValuesVN = new LinkedList<String[]>();
		resClassesToGenerateVN.add(CpuDemand.class);
		resClassesToGenerateVN.add(BandwidthDemand.class);
		resParamNamesToGenerateVN.add(new String[] {"demandedCycles"});
		resParamNamesToGenerateVN.add(new String[] {"demandedBandwith"});
		String cpu_demand = (String)properties.getOrDefault("cpuDemand", "16");
		String bandwith_demand = (String) properties.getOrDefault("bandwithDemand", "16");
		resMaxValuesVN.add(new String[] {cpu_demand}); // 5 - 20
		resMaxValuesVN.add(new String[] {bandwith_demand}); // 5 - 20
		NetworkStack networkStack =  new NetworkStack(null, Arrays.asList(virtualNetwork)); // 临时构造
		// 设置基准资源
		Constants.SWITCH_BASE_RES_DEM = Boolean.parseBoolean((String) properties.getOrDefault("switch", "true"));
		Constants.VIRTUAL_BASE_CPU_RESOURCE = Double.parseDouble((String) properties.getOrDefault("substrateBaseCpuResource", "5"));
        Constants.VIRTUAL_BASE_BANDWITH_RESOURCE = Double.parseDouble((String) properties.getOrDefault("substrateBaseBandwithResource", "5"));
        ConstraintsGeneratorDialog.generateConstraintsVirtual(Arrays.asList(resClassesToGenerateVN), 
        		Arrays.asList(resParamNamesToGenerateVN), Arrays.asList(resMaxValuesVN), networkStack);
        
		return virtualNetwork;
	}
}
