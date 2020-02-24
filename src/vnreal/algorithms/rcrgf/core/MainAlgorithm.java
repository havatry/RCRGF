package vnreal.algorithms.rcrgf.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import vnreal.algorithms.rcrgf.auxi.SelectCoreNode;
import vnreal.algorithms.rcrgf.config.Constants;
import vnreal.algorithms.rcrgf.util.BFSTravel;
import vnreal.algorithms.rcrgf.util.Utils;
import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.substrate.SubstrateNode;
import vnreal.network.virtual.VirtualLink;
import vnreal.network.virtual.VirtualNetwork;
import vnreal.network.virtual.VirtualNode;

public class MainAlgorithm {
	private SubstrateNetwork substrateNetwork;
	private VirtualNetwork virtualNetwork;
	private Map<VirtualNode, SubstrateNode> nodeMapping = new HashMap<VirtualNode, SubstrateNode>(); // �ڵ�ӳ��
	private Map<VirtualLink, List<SubstrateLink>> linkMapping = new HashMap<>(); // ��·ӳ��
	private Set<SubstrateNode> hasMappedSubstrateNode = new HashSet<>(); // �Ѿ�ӳ��ĵײ�ڵ�
	
	public MainAlgorithm(SubstrateNetwork substrateNetwork, VirtualNetwork virtualNetwork) {
		//Created constructor stubs
		this.substrateNetwork = substrateNetwork;
		this.virtualNetwork = virtualNetwork;
	}
	
	public boolean work() {
		Set<VirtualLink> MSE = new HashSet<>(); // ��ӳ��ĵ�
		Set<VirtualNode> HMS = new HashSet<>(); // �Ѿ�ӳ��ĵ�
		Set<SubstrateNode> outter = new HashSet<>();
		while (HMS.size() < virtualNetwork.getVertexCount()) { 
//			System.out.println("Retry");
			int sz = HMS.size();
			// 1. ѡ����������ĸ��ڵ�
			// 2. �ҵ��ײ��������ʺϵ�ƥ��� ��Ϊ���Ľڵ�
			MappingRules mappingRules = new MappingRules();
			VirtualNode root;
			SubstrateNode core;
			if (MSE.isEmpty()) {
				root = (VirtualNode) SelectCoreNode.selectForRoot(virtualNetwork);
				core = mappingRules.mapTo(root, substrateNetwork.getVertices(), virtualNetwork, substrateNetwork, hasMappedSubstrateNode);
			} else {
				// MSE��ѡ��һ���Ѿ�ӳ��ĵ�
				VirtualLink vl = MSE.iterator().next();
				VirtualNode s = virtualNetwork.getEndpoints(vl).getFirst();
				VirtualNode t = virtualNetwork.getEndpoints(vl).getSecond();
				if (nodeMapping.get(s) != null) {
					// �Ѿ�ӳ�䵽�ĵ�
					root = s;
				} else {
					root = t;
				}
				core = nodeMapping.get(root);
			}
			outter.add(core);
			// ��ʼ��core��BTL
			Utils.processBTL(core, substrateNetwork.getVertexCount());
			nodeMapping.put(root, core);
			BFSTravel bfsTravel = new BFSTravel(core, substrateNetwork);
			// 3. ��ʼ�����ݽṹ
			MSE.addAll(virtualNetwork.getOutEdges(root));
			HMS.add(root);
			while (bfsTravel.hasNext()) {
//				System.out.println("while loop");
				Set<VirtualNode> additional = new HashSet<>();
				Set<VirtualLink> attachLink = new HashSet<>();
				bfsTravel.next(hasMappedSubstrateNode, outter); // layer data,
				outter.clear();
				Map<SubstrateNode, List<SubstrateNode>> HMBT = bfsTravel.getHMBTL();
				boolean next_round = false;
				for (VirtualLink vl : MSE) {
//					System.out.println("mse loop");
					VirtualNode s = virtualNetwork.getEndpoints(vl).getFirst();
					VirtualNode t = virtualNetwork.getEndpoints(vl).getSecond();
					VirtualNode actual = s;
					// �����Ƿ�һ����ӳ����
					SubstrateNode s_s = nodeMapping.get(s); // ���ɹ�ӳ����
					if (s_s == null) {
						s_s = nodeMapping.get(t);
						actual = t;
					}
					List<SubstrateNode> ls = HMBT.get(s_s); // t�϶���ӳ����
					// �������ҵ��˿ɵ��ﱻӳ��ĵ�����㼯��
					if (ls == null) {
						// �Ҳ����ɴﵽ�ĵ�
//						System.out.println("To Think");
						return false;
					}
					// ���˼���
					List<SubstrateNode> filterList = Utils.filter(ls, s_s, Utils.getBandwith(vl));
					SubstrateNode mappedSubstrateNode = mappingRules.mapTo((VirtualNode)Utils.opposite(vl, actual, 
							virtualNetwork), filterList, virtualNetwork, substrateNetwork, hasMappedSubstrateNode);
					if (mappedSubstrateNode == null) {
						// ӳ��û�гɹ�
						continue;
					} else {
						// �ҵ�·��
						List<SubstrateLink> links = Utils.findPath(mappedSubstrateNode, s_s);
						// ��·ӳ��, ��·��Ϣ��׼ȷ
						if (!Utils.vlm(vl, links)) {
							next_round = true;
						} else {
							VirtualNode mappedVirtualNode = (VirtualNode)Utils.opposite(vl, actual, virtualNetwork);
							HMS.add(mappedVirtualNode);
							additional.add(mappedVirtualNode);
							attachLink.add(vl);
							outter.add(mappedSubstrateNode);
							// �ڵ�ӳ��
							Utils.vnm(mappedVirtualNode, mappedSubstrateNode); // �����ظ�ӳ�䵽һ���ڵ���
							hasMappedSubstrateNode.add(mappedSubstrateNode);
							nodeMapping.put(mappedVirtualNode, mappedSubstrateNode); // ��ɽڵ�ӳ��
							linkMapping.put(vl, links); // �����·ӳ��
							update(links, bfsTravel, s_s); // �л�����
						}
					}
				}
				// ����MSE
				for (VirtualNode vn : additional) {
					MSE.addAll(virtualNetwork.getOutEdges(vn));
				}
				for (VirtualLink remove : attachLink) {
					MSE.remove(remove);
				}
				if (MSE.isEmpty()) {
					print();
					// ���ӳ��
					return true;
				}
				if (HMS.size() == sz) {
					// û�з����仯, ��Ϊ������
//					System.out.println("not add node to exit");
					return false;
				}
				if (next_round) {
					break;
				}
			}
		}
		print();
		return true;
	}
	
	private void print() {
//		System.out.println("revenue / cost = " + Utils.revenueToCostRation(nodeMapping, linkMapping));
	}
	
	private void update(List<SubstrateLink> path, BFSTravel bfsTravel, SubstrateNode target) {
		for (int i = path.size() - 1; i >= 0; i--) {
			SubstrateLink sl = path.get(i);
			// ����
			SubstrateNode s = substrateNetwork.getEndpoints(sl).getFirst();
			SubstrateNode t = substrateNetwork.getEndpoints(sl).getSecond();
			if (s.getDtoSubstrate().getBestUpLink().get(target) == sl) {
				bfsTravel.construct(t, s, sl);
			} else {
				bfsTravel.construct(s, t, sl);
			}
		}
	}
	
	public Map<VirtualNode, SubstrateNode> getNodeMapping() {
		return nodeMapping;
	}
	
	public Map<VirtualLink, List<SubstrateLink>> getLinkMapping() {
		return linkMapping;
	}
}
