package vnreal.algorithms.myRCRGF.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import vnreal.algorithms.myRCRGF.util.BFSTravel;
import vnreal.algorithms.myRCRGF.util.Utils;
import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.substrate.SubstrateNode;
import vnreal.network.virtual.VirtualLink;
import vnreal.network.virtual.VirtualNetwork;
import vnreal.network.virtual.VirtualNode;

public class MainAlgorithm {
	private SubstrateNetwork substrateNetwork;
	private VirtualNetwork virtualNetwork;
	private Map<VirtualNode, SubstrateNode> nodeMapping = new HashMap<VirtualNode, SubstrateNode>(); // 节点映射
	private Map<VirtualLink, List<SubstrateLink>> linkMapping = new HashMap<>(); // 链路映射
	private Set<SubstrateNode> hasMappedSubstrateNode = new HashSet<>(); // 已经映射的底层节点
	
	public MainAlgorithm(SubstrateNetwork substrateNetwork, VirtualNetwork virtualNetwork) {
		//Created constructor stubs
		this.substrateNetwork = substrateNetwork;
		this.virtualNetwork = virtualNetwork;
	}
	
	public boolean work() {
		Set<VirtualLink> MSE = new HashSet<>(); // 待映射的点
		Set<VirtualNode> HMS = new HashSet<>(); // 已经映射的点
		Set<SubstrateNode> outter = new HashSet<>();
		while (HMS.size() < virtualNetwork.getVertexCount()) { 
//			System.out.println("Retry");
			int sz = HMS.size();
			// 1. 选择虚拟网络的根节点
			// 2. 找到底层网络中适合的匹配点 作为核心节点
			MappingRules mappingRules = new MappingRules();
			VirtualNode root;
			SubstrateNode core;
			if (MSE.isEmpty()) {
				root = (VirtualNode) SelectCoreNode.selectForRoot(virtualNetwork);
				core = mappingRules.mapTo(root, substrateNetwork.getVertices(), virtualNetwork, substrateNetwork, hasMappedSubstrateNode);
			} else {
				// MSE中选择一个已经映射的点
				VirtualLink vl = MSE.iterator().next();
				VirtualNode s = virtualNetwork.getEndpoints(vl).getFirst();
				VirtualNode t = virtualNetwork.getEndpoints(vl).getSecond();
				if (nodeMapping.get(s) != null) {
					// 已经映射到的点
					root = s;
				} else {
					root = t;
				}
				core = nodeMapping.get(root);
			}
			outter.add(core);
			// 初始化core的BTL
			Utils.processBTL(core, substrateNetwork.getVertexCount());
			nodeMapping.put(root, core);
			BFSTravel bfsTravel = new BFSTravel(core, substrateNetwork);
			// 3. 初始化数据结构
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
					// 查找是否一个被映射了
					SubstrateNode s_s = nodeMapping.get(s); // 被成功映射了
					if (s_s == null) {
						s_s = nodeMapping.get(t);
						actual = t;
					}
					List<SubstrateNode> ls = HMBT.get(s_s); // t肯定被映射了
					// 这样就找到了可到达被映射的点其余点集合
					if (ls == null) {
						// 找不到可达到的点
//						System.out.println("To Think");
						return false;
					}
					// 过滤集合
					List<SubstrateNode> filterList = Utils.filter(ls, s_s, Utils.getBandwith(vl));
					SubstrateNode mappedSubstrateNode = mappingRules.mapTo((VirtualNode)Utils.opposite(vl, actual, 
							virtualNetwork), filterList, virtualNetwork, substrateNetwork, hasMappedSubstrateNode);
					if (mappedSubstrateNode == null) {
						// 映射没有成功
						continue;
					} else {
						// 找到路径
						List<SubstrateLink> links = Utils.findPath(mappedSubstrateNode, s_s);
						// 链路映射, 链路信息不准确
						if (!Utils.vlm(vl, links)) {
							next_round = true;
						} else {
							VirtualNode mappedVirtualNode = (VirtualNode)Utils.opposite(vl, actual, virtualNetwork);
							HMS.add(mappedVirtualNode);
							additional.add(mappedVirtualNode);
							attachLink.add(vl);
							outter.add(mappedSubstrateNode);
							// 节点映射
							Utils.vnm(mappedVirtualNode, mappedSubstrateNode); // 不能重复映射到一个节点上
							hasMappedSubstrateNode.add(mappedSubstrateNode);
							nodeMapping.put(mappedVirtualNode, mappedSubstrateNode); // 完成节点映射
							linkMapping.put(vl, links); // 完成链路映射
							update(links, bfsTravel, s_s); // 切换上游
						}
					}
				}
				// 更新MSE
				for (VirtualNode vn : additional) {
					MSE.addAll(virtualNetwork.getOutEdges(vn));
				}
				for (VirtualLink remove : attachLink) {
					MSE.remove(remove);
				}
				if (MSE.isEmpty()) {
					print();
					// 完成映射
					return true;
				}
				if (HMS.size() == sz) {
					// 没有发生变化, 认为不可行
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
			// 更新
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
