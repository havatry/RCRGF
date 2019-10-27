package vnreal.algorithms.argf.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.substrate.SubstrateNode;

/**
 * 用来遍历底层网络
 * 2019年10月24日 下午10:50:57
 */
public class BFSTravel {
	private SubstrateNode root;
	private List<SubstrateNode> visited = new LinkedList<>();
	private List<SubstrateNode> outter = new LinkedList<>();
	private SubstrateNetwork substrateNetwork; // 底层网络
	private Map<SubstrateNode, List<SubstrateNode>> HMBTL = new HashMap<SubstrateNode, List<SubstrateNode>>(); // 每层可以映射的节点集合
	
	public BFSTravel(SubstrateNode root, SubstrateNetwork substrateNetwork) {
		//Created constructor stubs
		this.root = root;
		this.substrateNetwork = substrateNetwork;
		outter.add(root);
		visited.add(root);
	}
	
	public boolean hasNext() {
		return true;
	}
	
	public Set<SubstrateNode> next(Set<SubstrateNode> visitedNodes, Set<SubstrateNode> outter) {
		Set<SubstrateNode> temp = new HashSet<SubstrateNode>();
		// 通过outter来产生
		HMBTL.clear();
		for (SubstrateNode sn : outter) {
			for (SubstrateLink sl : substrateNetwork.getOutEdges(sn)) {
				SubstrateNode neighbor = (SubstrateNode) Utils.opposite(sl, sn, substrateNetwork);
				if (visitedNodes.contains(neighbor)) {
					continue; // 已经访问过了
				} else {
					construct(sn, neighbor, sl);
					temp.add(neighbor); // 更正
				}
			}
		}
		return temp;
	}
	
	public void construct(SubstrateNode sn, SubstrateNode neighbor, SubstrateLink sl) {
		// 初始化BTL
		Utils.processBTL(neighbor, substrateNetwork.getVertexCount());
		// 继承上游的EBTL
		Map<SubstrateNode, PriorityQueue<SubstrateNode>> upEBRL = sn.getDtoSubstrate().getEBTL();
		neighbor.getDtoSubstrate().getEBTL().clear();
		Map<SubstrateNode, PriorityQueue<SubstrateNode>> currentEBTL = neighbor.getDtoSubstrate().getEBTL();
		// upEBTL是其他节点到sn的路径集合, 因此可以顺势延伸到当前EBTL中
		for (SubstrateNode other : upEBRL.keySet()) {
			addElement(other, sn, neighbor, currentEBTL);
		}
		// 增加新加的这个
		addElement(sn, sn, neighbor, currentEBTL);
		// 构建BTL
		for (SubstrateNode other2 : currentEBTL.keySet()) {
			SubstrateNode bstUpStreamToOther = neighbor.getDtoSubstrate().getEBTL().get(other2).peek();
			neighbor.getDtoSubstrate().getBTL()[Utils.getIndexForSubstrateNode(other2)]
					= Utils.computeSE(neighbor.getDtoSubstrate().getEBTL().get(other2).peek().getDtoSubstrate()
							.getBTL()[Utils.getIndexForSubstrateNode(other2)],
							substrateNetwork.findEdge(bstUpStreamToOther, neighbor));
			if (HMBTL.get(other2) == null) {
				HMBTL.put(other2, new LinkedList<>());
			}
			HMBTL.get(other2).add(neighbor);
			// 设置最佳上下游 
			// 去other2时候需要走过的最佳上游路径
			SubstrateNode uvn = neighbor.getDtoSubstrate().getEBTL().get(other2).peek();
			SubstrateLink usl = substrateNetwork.findEdge(uvn, neighbor);
			neighbor.getDtoSubstrate().getBestUpStream().put(other2, uvn);
			neighbor.getDtoSubstrate().getBestUpLink().put(other2, usl);
		}
	}
	
	private void addElement(SubstrateNode other, SubstrateNode sn, SubstrateNode neighbor, 
			Map<SubstrateNode, PriorityQueue<SubstrateNode>> currentEBTL) {
		if (currentEBTL.get(other) == null) {
			// 说明当前节点对应其他位置上 还没有设置
			currentEBTL.put(other, new PriorityQueue<>(new Comparator<SubstrateNode>() {

				@Override
				public int compare(SubstrateNode o1, SubstrateNode o2) {
					//Created method stubs
					SubstrateLink sl1 = substrateNetwork.findEdge(o1, neighbor);
					SubstrateLink sl2 = substrateNetwork.findEdge(o2, neighbor);
					double bandwith_o1 = Utils.computeSE(o1.getDtoSubstrate().getEBTL().get(other) == null ? 0.0 
							: o1.getDtoSubstrate().getEBTL().get(other).peek().getDtoSubstrate()
							.getBTL()[Utils.getIndexForSubstrateNode(other)], sl1);
					double bandwith_o2 = Utils.computeSE(o2.getDtoSubstrate().getEBTL().get(other) == null ? 0.0 
							: o2.getDtoSubstrate().getEBTL().get(other).peek().getDtoSubstrate()
							.getBTL()[Utils.getIndexForSubstrateNode(other)], sl2);
					if (Utils.equal(bandwith_o1, bandwith_o2)) {
						return 0;
					} else if (Utils.great(bandwith_o1, bandwith_o2)) {
						return -1;
					} else {
						return 1;
					}
				}
			}));
		}
		// 添加元素
		currentEBTL.get(other).offer(sn);
	}
	
	public Map<SubstrateNode, List<SubstrateNode>> getHMBTL() {
		return HMBTL;
	}
	
	public SubstrateNode getRoot() {
		return root;
	}
}
