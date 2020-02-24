package vnreal.algorithms.myRCRGF.core;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import vnreal.algorithms.myRCRGF.util.Utils;
import vnreal.network.Link;
import vnreal.network.Node;
import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.substrate.SubstrateNode;
import vnreal.network.virtual.VirtualLink;
import vnreal.network.virtual.VirtualNetwork;
import vnreal.network.virtual.VirtualNode;

/**
 * ƥ�������
 * ��ƥ�����
 * 2019��10��24�� ����9:23:21
 */
public class MappingRules {
	// ���ķ���, �������ڵ㵽�ײ�ڵ��ӳ��
	public SubstrateNode mapTo(VirtualNode virtualNode, Collection<SubstrateNode> substrateNodeLists, VirtualNetwork virtualNetwork,
			SubstrateNetwork substrateNetwork, Set<SubstrateNode> hasMapped) {
		List<SubstrateNode> c = support(virtualNode, substrateNodeLists, virtualNetwork, substrateNetwork);
		List<SubstrateNode> candicates = new LinkedList<>();
		for (SubstrateNode sn : c) {
			if (hasMapped.contains(sn)) {
				continue;
			}
			candicates.add(sn);
		}
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
	
	// һ������ڵ�ӳ�䵽�����ѡ�ڵ��ϣ�����һЩ�����ϴ�������
	private List<SubstrateNode> support(VirtualNode virtualNode, Collection<SubstrateNode> substrateNodeLists, VirtualNetwork virtualNetwork,
			SubstrateNetwork substrateNetwork) {
		List<SubstrateNode> result = new LinkedList<SubstrateNode>();
		for (SubstrateNode sn : substrateNodeLists) {
			if (rulesForNodes(virtualNode, sn, virtualNetwork, substrateNetwork) && 
					rulesForLinksSimple(virtualNode, sn, virtualNetwork, substrateNetwork)) {
				result.add(sn);
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	@Deprecated
	private boolean rulesForNodesOld(VirtualNode virtualNode, SubstrateNode substrateNode, VirtualNetwork virtualNetwork,
			SubstrateNetwork substrateNetwork) {
		// �ж�����ڵ����Χ�ھӽڵ��Ƿ�����ײ�ڵ����Χ�ھӽڵ�
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
		// ע�ⲻ��ӳ�䵽ͬһ������
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
	
	private boolean rulesForNodes(VirtualNode virtualNode, SubstrateNode substrateNode, VirtualNetwork virtualNetwork,
			SubstrateNetwork substrateNetwork) {
		return Utils.smallEqual(Utils.getCpu(virtualNode), Utils.getCpu(substrateNode));
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private boolean rulesForLinks(VirtualNode virtualNode, SubstrateNode substrateNode, VirtualNetwork virtualNetwork,
			SubstrateNetwork substrateNetwork) {
		// ���մ���Ӵ�С����
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
		// copy�ײ�����
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
				// ����Ҫ��
				Utils.setBandwith(sLink, bandwith_s - bandwith_v);
				PQE_S.offer(sLink); // ��������
			} else {
				return false; // ������������
			}
		}
		return true;
	}
	
	private boolean rulesForLinksSimple(VirtualNode virtualNode, SubstrateNode substrateNode, VirtualNetwork virtualNetwork,
			SubstrateNetwork substrateNetwork) {
		return true;
	}
}
