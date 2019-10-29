package vnreal.algorithms.rcrgf.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import vnreal.algorithms.rcrgf.util.DSU;
import vnreal.algorithms.rcrgf.util.Utils;
import vnreal.constraints.demands.BandwidthDemand;
import vnreal.network.virtual.VirtualLink;
import vnreal.network.virtual.VirtualNetwork;
import vnreal.network.virtual.VirtualNode;

/**
 * 减边操作
 * 2019年10月24日 下午4:19:24
 */
public class RemoveEdge {
	private VirtualNetwork virtualNetwork;
	private Map<Long, Integer> map; // 关于节点的编号和id的映射关系
	
	public RemoveEdge(VirtualNetwork virtualNetwork) {
		//Created constructor stubs
		this.virtualNetwork = virtualNetwork;
		int index = 0;
		map = new HashMap<Long, Integer>();
		for (VirtualNode n : virtualNetwork.getVertices()) {
			map.put(n.getId(), index++);
		}
	}
	
	public boolean work() {
		return doRemove();
	}
	
	// 采用生成树kurskal算法，将不在生成树中的边标记下，作为候选删除集合
	// 然后找出环路，删除环路中最小带宽，并将该边加入进去
	private boolean doRemove() {
		// 1. 对所有边 按照带宽从大到小排序
		List<VirtualLink> links = new ArrayList<VirtualLink>(virtualNetwork.getEdges());
		Collections.sort(links, new Comparator<VirtualLink>() {

			@Override
			public int compare(VirtualLink o1, VirtualLink o2) {
				//Created method stubs
				double bandwith_o1 = ((BandwidthDemand)o1.get().get(0)).getDemandedBandwidth();
				double bandwith_o2 = ((BandwidthDemand)o2.get().get(0)).getDemandedBandwidth();
				if (bandwith_o1 == bandwith_o2) {
					return 0;
				} else if (bandwith_o1 > bandwith_o2) {
					return -1;
				} else {
					return 0;
				}
			}
		});
		// 每次选择最大的带宽边
		int nodes = virtualNetwork.getVertexCount();
		DSU dsu = new DSU(nodes);
		LinkedList<VirtualLink> remaining = new LinkedList<>();
		LinkedList<VirtualLink> added = new LinkedList<>();
		for (VirtualLink l : links) {
			VirtualNode s = virtualNetwork.getEndpoints(l).getFirst();
			VirtualNode t = virtualNetwork.getEndpoints(l).getSecond();
			int start = mapRelation(s.getId());
			int end = mapRelation(t.getId());
			if (dsu.find(start) != dsu.find(end)) {
				// 不在一个集合中
				added.add(l);
				dsu.union(start, end);
			} else {
				remaining.add(l); // 无法加入的边
			}
		}
		
		// 选择根节点
		VirtualNode root = select();
		
		// 构造一棵树
		build(root, added, new LinkedList<>());
		
		// 删除remaining中的边
		for (VirtualLink r : remaining) {
			VirtualNode s = virtualNetwork.getEndpoints(r).getFirst();
			VirtualNode t = virtualNetwork.getEndpoints(r).getSecond();
			if (s.getDtoVirtual().getUpnode() == null && s != root) {
				// 说明在其他分支上
				return false;
			}
			if (t.getDtoVirtual().getUpnode() == null && t != root) { // 一个小的错误 纠正一个多小时 s != root 改成 t != root
				return false;
			}
			// 找到公共节点
			VirtualNode common = Utils.common(s, t, root);
			update(s, common, Utils.getBandwith(r));
			update(t, common, Utils.getBandwith(r));
			// 将边删除
			virtualNetwork.removeEdge(r);
		}
		
		// 判断是否是一个连通分量
		return true;
	}	
	
	private int mapRelation(long id) {
		return map.get(id);
	}
	
	private VirtualNode select() {
		return new ArrayList<>(virtualNetwork.getVertices()).get(0);
	}
	
	private void update(VirtualNode current, VirtualNode root, double addedBandwith) {
		// 上溯更新带宽
		while (current != root) {
			// 关联的边
			VirtualLink uplink = current.getDtoVirtual().getUplink();
			Utils.setBandwith(uplink, Utils.getBandwith(uplink) + addedBandwith);
			current = current.getDtoVirtual().getUpnode();
		}
	}
	
	private void build(VirtualNode root, List<VirtualLink> added, LinkedList<VirtualLink> visited) {
		// 考虑所有出边 
		for (VirtualLink l : virtualNetwork.getOutEdges(root)) {
			if (visited.contains(l) || !added.contains(l)) {
				// 已经访问过的边 或者不是关键边
				continue;
			}
			VirtualNode s = virtualNetwork.getEndpoints(l).getFirst();
			VirtualNode t = virtualNetwork.getEndpoints(l).getSecond();
			visited.add(l);
			if (s == root) {
				// 那么t是下游
//				s.getDtoVirtual().setDownnode(t);
				t.getDtoVirtual().setUpnode(s);
//				s.getDtoVirtual().setDownlink(l);
				t.getDtoVirtual().setUplink(l);
				build(t, added, visited);
			} else {
				// 那么下游是s
//				t.getDtoVirtual().setDownnode(s);
				s.getDtoVirtual().setUpnode(t);
//				t.getDtoVirtual().setDownlink(l);
				s.getDtoVirtual().setUplink(l);
				build(s, added, visited);
			}
		}
	}
}
