package vnreal.algorithms.rcrgf.core;

import vnreal.algorithms.rcrgf.util.Utils;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.virtual.VirtualNetwork;

public class RCRGFAlgorithm {
	public boolean compute(SubstrateNetwork substrateNetwork, VirtualNetwork virtualNetwork) {
		Utils.initMapSubstrateNode(substrateNetwork.getVertices());
		Utils.initMapVirtualNode(virtualNetwork.getVertices());
		return new MainAlgorithm(substrateNetwork, virtualNetwork).work();
	}
}
