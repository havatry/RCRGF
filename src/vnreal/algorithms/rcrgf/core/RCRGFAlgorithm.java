package vnreal.algorithms.rcrgf.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vnreal.algorithms.rcrgf.util.Utils;
import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.substrate.SubstrateNode;
import vnreal.network.virtual.VirtualLink;
import vnreal.network.virtual.VirtualNetwork;
import vnreal.network.virtual.VirtualNode;

public class RCRGFAlgorithm {
	private Map<VirtualNode, SubstrateNode> nodeMapping = new HashMap<VirtualNode, SubstrateNode>(); // �ڵ�ӳ��
	private Map<VirtualLink, List<SubstrateLink>> linkMapping = new HashMap<>(); // ��·ӳ��
	
	public boolean compute(SubstrateNetwork substrateNetwork, VirtualNetwork virtualNetwork) {
		Utils.initMapSubstrateNode(substrateNetwork.getVertices());
		Utils.initMapVirtualNode(virtualNetwork.getVertices());
		MainAlgorithm mainAlgorithm = new MainAlgorithm(substrateNetwork, virtualNetwork);
		boolean result = mainAlgorithm.work();
		nodeMapping = mainAlgorithm.getNodeMapping();
		linkMapping = mainAlgorithm.getLinkMapping();
		return result;
	}
	
	public Map<VirtualNode, SubstrateNode> getNodeMapping() {
		return nodeMapping;
	}
	
	public Map<VirtualLink, List<SubstrateLink>> getLinkMapping() {
		return linkMapping;
	}
}