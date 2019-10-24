package vnreal.algorithms.argf.core;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import vnreal.algorithms.argf.auxi.SelectCoreNode;
import vnreal.algorithms.argf.util.Utils;
import vnreal.network.Link;
import vnreal.network.Node;
import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.substrate.SubstrateNode;
import vnreal.network.virtual.VirtualLink;
import vnreal.network.virtual.VirtualNetwork;
import vnreal.network.virtual.VirtualNode;

/**
 * 匹配规则类
 * 边匹配规则
 * 2019年10月24日 下午9:23:21
 */
public class MappingRules {
	// 核心方法, 完成虚拟节点到底层节点的映射
	public SubstrateNode mapTo(VirtualNode virtualNode, List<SubstrateNode> substrateNodeLists, VirtualNetwork virtualNetwork,
			SubstrateNetwork substrateNetwork) {
		List<SubstrateNode> candicates = support(virtualNode, substrateNodeLists, virtualNetwork, substrateNetwork);
		SubstrateNode ret = null;
		double max = Double.MIN_VALUE;
		for (SubstrateNode sn : candicates) {
			double current = Utils.getReferencedResource(sn, substrateNetwork, SelectCoreNode.getAlpha());
			if (current > max) {
				max = current;
				ret = sn;
			}
		}
		return ret;
	}
	
	// 一个虚拟节点映射到多个候选节点上，过滤一些不符合带宽规则的
	private List<SubstrateNode> support(VirtualNode virtualNode, List<SubstrateNode> substrateNodeLists, VirtualNetwork virtualNetwork,
			SubstrateNetwork substrateNetwork) {
		List<SubstrateNode> result = new LinkedList<SubstrateNode>();
		for (SubstrateNode sn : substrateNodeLists) {
			if (rulesForLinks(virtualNode, sn, virtualNetwork, substrateNetwork)) {
				result.add(sn);
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	@Deprecated
	private boolean rulesForNodes(VirtualNode virtualNode, SubstrateNode substrateNode, VirtualNetwork virtualNetwork,
			SubstrateNetwork substrateNetwork) {
		// 判断虚拟节点的周围邻居节点是否都满足底层节点的周围邻居节点
		Comparator<Node> c = new Comparator<Node>() {
			@Override
			public int compare(Node o1, Node o2) {
				//Created method stubs
				double cpu_o1 = Utils.getCpu(o1);
				double cpu_o2 = Utils.getCpu(o2);
				if (cpu_o1 == cpu_o2) {
					return 0;
				} else if (cpu_o1 > cpu_o2) {
					return -1;
				} else {
					return 1;
				}
			}
		};
		PriorityQueue<VirtualNode> PQN_V = new PriorityQueue<VirtualNode>(c);
		PQN_V.addAll(virtualNetwork.getNeighbors(virtualNode));
		PriorityQueue<SubstrateNode> PQN_S = new PriorityQueue<SubstrateNode>(c);
		PQN_S.addAll(substrateNetwork.getNeighbors(substrateNode));
		// 注意不能映射到同一个点上
		while (!PQN_V.isEmpty()) {
			VirtualNode vNode = PQN_V.poll();
			SubstrateNode sNode = PQN_S.poll();
			double cpu_v = Utils.getCpu(vNode);
			double cpu_s = Utils.getCpu(sNode);
			if (Utils.greatEqual(cpu_v, cpu_s)) {
				return false;
			}
		}
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	private boolean rulesForLinks(VirtualNode virtualNode, SubstrateNode substrateNode, VirtualNetwork virtualNetwork,
			SubstrateNetwork substrateNetwork) {
		// 按照带宽从大到小排序
		Comparator<Link> c = new Comparator<Link>() {
			@Override
			public int compare(Link o1, Link o2) {
				//Created method stubs
				double bandwith_o1 = Utils.getBandwith(o1);
				double bandwith_o2 = Utils.getBandwith(o2);
				if (Utils.equal(bandwith_o1, bandwith_o2)) {
					return 0;
				} else if (Utils.great(bandwith_o1, bandwith_o2)) {
					return -1;
				} else {
					return 1;
				}
			}
		};
		// copy底层网络
		SubstrateNetwork substrateNetwork_backup = substrateNetwork.getCopy(false, true);
		PriorityQueue<VirtualLink> PQE_V = new PriorityQueue<>(c);
		PriorityQueue<SubstrateLink> PQE_S = new PriorityQueue<>(c);
		PQE_V.addAll(virtualNetwork.getOutEdges(virtualNode));
		PQE_S.addAll(substrateNetwork_backup.getOutEdges(substrateNode));
		while (!PQE_V.isEmpty()) {
			VirtualLink vLink = PQE_V.poll();
			SubstrateLink sLink = PQE_S.poll();
			double bandwith_v = Utils.getBandwith(vLink);
			double bandwith_s = Utils.getBandwith(sLink);
			if (Utils.smallEqual(bandwith_v, bandwith_s)) {
				// 满足要求
				Utils.setBandwith(sLink, bandwith_s - bandwith_v);
				PQE_S.offer(sLink); // 保持有序
			} else {
				return false; // 不满足带宽规则
			}
		}
		return true;
	}
}
