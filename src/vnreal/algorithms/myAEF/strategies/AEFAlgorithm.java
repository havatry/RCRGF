package vnreal.algorithms.myAEF.strategies;

import vnreal.algorithms.AlgorithmParameter;
import vnreal.algorithms.GenericMappingAlgorithm;
import vnreal.algorithms.linkmapping.kShortestPathLinkMapping;
import vnreal.algorithms.myAEF.strategies.aef.LinkMapping;
import vnreal.algorithms.myAEF.strategies.aef.NodeMapping;

public class AEFAlgorithm extends GenericMappingAlgorithm{
	// Default values
	private static final double DEFAULT_DISTANCE_CONSTRAINT = 70.0;
	private static final boolean DEFAULT_OVERLOAD = false;
	private static final int DEFAULT_KSP = 1;
	private static final boolean DEFAULT_EPPSTEIN = false;
	private static final String DEFAULT_LINKMAP_ALGORITHM = "ksp";
	
	public AEFAlgorithm(AlgorithmParameter param) {
		double distanceConstraint = param.getDouble("distanceConstraint", DEFAULT_DISTANCE_CONSTRAINT);
		boolean nodeOverload = param.getBoolean("overload", DEFAULT_OVERLOAD);
		nodeMappingAlgorithm = new NodeMapping(distanceConstraint, nodeOverload);
		
		if (param.getString("linkMapAlgorithm", DEFAULT_LINKMAP_ALGORITHM).equals("ksp")) {
			int k = param.getInteger("ksp", DEFAULT_KSP);
			boolean eppstein = param.getBoolean("eppstein", DEFAULT_EPPSTEIN);
			linkMappingAlgorithm = new kShortestPathLinkMapping(k, eppstein);
		} else {
			linkMappingAlgorithm = new LinkMapping(); // ¸üÐÂËã·¨
		}
		
	}
}
