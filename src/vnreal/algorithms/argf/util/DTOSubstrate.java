package vnreal.algorithms.argf.util;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNode;

/**
 * 为节点类扩展的属性
 * 2019年10月24日 下午9:26:54
 */
public class DTOSubstrate {
	private double[] BTL;
	private Map<SubstrateNode, PriorityQueue<SubstrateNode>> EBTL = new HashMap<>();
	private Map<SubstrateNode, SubstrateNode> bestUpStream = new HashMap<>();
	private Map<SubstrateNode, SubstrateLink> bestUpLink = new HashMap<>();
	
	public double[] getBTL() {
		return BTL;
	}
	public void setBTL(double[] bTL) {
		BTL = bTL;
	}
	public Map<SubstrateNode, PriorityQueue<SubstrateNode>> getEBTL() {
		return EBTL;
	}
	public void setEBTL(Map<SubstrateNode, PriorityQueue<SubstrateNode>> eBTL) {
		EBTL = eBTL;
	}
	public Map<SubstrateNode, SubstrateNode> getBestUpStream() {
		return bestUpStream;
	}
	public void setBestUpStream(Map<SubstrateNode, SubstrateNode> bestUpStream) {
		this.bestUpStream = bestUpStream;
	}
	public Map<SubstrateNode, SubstrateLink> getBestUpLink() {
		return bestUpLink;
	}
	public void setBestUpLink(Map<SubstrateNode, SubstrateLink> bestUpLink) {
		this.bestUpLink = bestUpLink;
	}
}
