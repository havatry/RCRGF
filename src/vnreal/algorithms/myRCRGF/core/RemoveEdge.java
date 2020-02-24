package vnreal.algorithms.myRCRGF.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import vnreal.algorithms.myRCRGF.util.DSU;
import vnreal.algorithms.myRCRGF.util.Utils;
import vnreal.constraints.demands.BandwidthDemand;
import vnreal.network.virtual.VirtualLink;
import vnreal.network.virtual.VirtualNetwork;
import vnreal.network.virtual.VirtualNode;

/**
 * ���߲���
 * 2019��10��24�� ����4:19:24
 */
public class RemoveEdge {
	private VirtualNetwork virtualNetwork;
	private Map<Long, Integer> map; // ���ڽڵ�ı�ź�id��ӳ���ϵ
	
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
	
	// ����������kurskal�㷨���������������еı߱���£���Ϊ��ѡɾ������
	// Ȼ���ҳ���·��ɾ����·����С���������ñ߼����ȥ
	private boolean doRemove() {
		// 1. �����б� ���մ���Ӵ�С����
		List<VirtualLink> links = new ArrayList<VirtualLink>(virtualNetwork.getEdges());
		Collections.sort(links, (o1, o2) -> {
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
        });
		// ÿ��ѡ�����Ĵ����
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
				// ����һ��������
				added.add(l);
				dsu.union(start, end);
			} else {
				remaining.add(l); // �޷�����ı�
			}
		}
		
		// ѡ����ڵ�
		VirtualNode root = select();
		
		// ����һ����
		build(root, added, new LinkedList<>());
		
		// ɾ��remaining�еı�
		for (VirtualLink r : remaining) {
			VirtualNode s = virtualNetwork.getEndpoints(r).getFirst();
			VirtualNode t = virtualNetwork.getEndpoints(r).getSecond();
			if (s.getDtoVirtual().getUpnode() == null && s != root) {
				// ˵����������֧��
				return false;
			}
			if (t.getDtoVirtual().getUpnode() == null && t != root) { // һ��С�Ĵ��� ����һ����Сʱ s != root �ĳ� t != root
				return false;
			}
			// �ҵ������ڵ�
			VirtualNode common = Utils.common(s, t, root);
			update(s, common, Utils.getBandwith(r));
			update(t, common, Utils.getBandwith(r));
			// ����ɾ��
			virtualNetwork.removeEdge(r);
		}
		
		// �ж��Ƿ���һ����ͨ����
		return true;
	}	
	
	private int mapRelation(long id) {
		return map.get(id);
	}
	
	private VirtualNode select() {
		return new ArrayList<>(virtualNetwork.getVertices()).get(0);
	}
	
	private void update(VirtualNode current, VirtualNode root, double addedBandwith) {
		// ���ݸ��´���
		while (current != root) {
			// �����ı�
			VirtualLink uplink = current.getDtoVirtual().getUplink();
			Utils.setBandwith(uplink, Utils.getBandwith(uplink) + addedBandwith);
			current = current.getDtoVirtual().getUpnode();
		}
	}
	
	private void build(VirtualNode root, List<VirtualLink> added, LinkedList<VirtualLink> visited) {
		// �������г��� 
		for (VirtualLink l : virtualNetwork.getOutEdges(root)) {
			if (visited.contains(l) || !added.contains(l)) {
				// �Ѿ����ʹ��ı� ���߲��ǹؼ���
				continue;
			}
			VirtualNode s = virtualNetwork.getEndpoints(l).getFirst();
			VirtualNode t = virtualNetwork.getEndpoints(l).getSecond();
			visited.add(l);
			if (s == root) {
				// ��ôt������
//				s.getDtoVirtual().setDownnode(t);
				t.getDtoVirtual().setUpnode(s);
//				s.getDtoVirtual().setDownlink(l);
				t.getDtoVirtual().setUplink(l);
				build(t, added, visited);
			} else {
				// ��ô������s
//				t.getDtoVirtual().setDownnode(s);
				s.getDtoVirtual().setUpnode(t);
//				t.getDtoVirtual().setDownlink(l);
				s.getDtoVirtual().setUplink(l);
				build(s, added, visited);
			}
		}
	}
}
