package vnreal.algorithms.argf.util;

import java.util.Map;
import java.util.PriorityQueue;

import vnreal.network.substrate.SubstrateNode;

/**
 * 为节点类扩展的属性
 * 2019年10月24日 下午9:26:54
 */
public class DTOSubstrate {
	private int[][] BTL;
	private Map<SubstrateNode, PriorityQueue<SubstrateNode>> map;
	private double SE;
	private SubstrateNode upstream;
	private SubstrateNode downstream;
	public int[][] getBTL() {
		return BTL;
	}
	public void setBTL(int[][] bTL) {
		BTL = bTL;
	}
	public Map<SubstrateNode, PriorityQueue<SubstrateNode>> getMap() {
		return map;
	}
	public void setMap(Map<SubstrateNode, PriorityQueue<SubstrateNode>> map) {
		this.map = map;
	}
	public double getSE() {
		return SE;
	}
	public void setSE(double sE) {
		SE = sE;
	}
	public SubstrateNode getUpstream() {
		return upstream;
	}
	public void setUpstream(SubstrateNode upstream) {
		this.upstream = upstream;
	}
	public SubstrateNode getDownstream() {
		return downstream;
	}
	public void setDownstream(SubstrateNode downstream) {
		this.downstream = downstream;
	}
}
