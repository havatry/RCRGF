package vnreal.algorithms.myrcrgf.util;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import mulavito.graph.generators.WaxmanGraphGenerator;
import vnreal.constraints.resources.BandwidthResource;
import vnreal.constraints.resources.CpuResource;
import vnreal.network.NetworkStack;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.substrate.SubstrateNode;
import vnreal.network.virtual.VirtualNetwork;
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
		double vnAlpha = Double.parseDouble((String) properties.getOrDefault("vnAlpha", "1.0"));
		double vnBeta = Double.parseDouble((String) properties.getOrDefault("vnBeta", "0.5"));
		return null;
	}
}
