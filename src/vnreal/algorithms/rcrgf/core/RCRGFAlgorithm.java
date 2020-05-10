package vnreal.algorithms.rcrgf.core;

import vnreal.algorithms.utils.NodeLinkAssignation;
import vnreal.algorithms.utils.SubgraphBasicVN.NodeLinkMapping;
import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.substrate.SubstrateNode;
import vnreal.network.virtual.VirtualLink;
import vnreal.network.virtual.VirtualNetwork;
import vnreal.network.virtual.VirtualNode;

import java.util.*;

import static vnreal.algorithms.rcrgf.config.GlobalVariable.setVisited;
import static vnreal.algorithms.rcrgf.utils.Util.*;

/**
 * Created on 2020/4/19
 * RCRGF Version 1
 */
public class RCRGFAlgorithm {
    private NodeLinkMapping nodeLinkMapping = new NodeLinkMapping();
    /**
     * 主算法 对于给定的底层网络和虚拟网络完成映射
     * @param substrateNetwork 底层网络
     * @param virtualNetwork 虚拟网络
     */
    public boolean work(SubstrateNetwork substrateNetwork, VirtualNetwork virtualNetwork) {
        Objects.requireNonNull(substrateNetwork, "substrate network cannot be null");
        Objects.requireNonNull(virtualNetwork, "virtual network cannot be null");

        // 获取网络中的核心节点
        VirtualNode root = (VirtualNode) selectCore(virtualNetwork).get();
        SubstrateNode core = (SubstrateNode) selectCore(substrateNetwork).get();
        // 是否满足约束
        if (small(getReferenceValue(core, substrateNetwork), getReferenceValue(root, virtualNetwork))
                || small(getCpu(core), getCpu(root))) {
            return false;
        }
        nodeLinkMapping.add(root, core);
        // 初始化BFS候选集合
        Set<SubstrateNode> bfsSet = new HashSet<>(4);
        bfsSet.add(core);
        BFSTravel bfsTravel = new BFSTravel(substrateNetwork);
        // 初始化虚拟网络中待映射的边
        TreeSet<VirtualLink> virtualLinks = new TreeSet<>((l1, l2) -> Double.compare(getBandwidth(l2), getBandwidth(l1)));
        virtualLinks.addAll(virtualNetwork.getOutEdges(root));

        // 当还有虚拟节点没有完成映射 同时底层节点还存在没有遍历到的
        while (!virtualLinks.isEmpty() && bfsTravel.hasNext()) {
            // 将bfsSet传入作为当前的集合进行遍历
            Set<SubstrateNode> currentSet = bfsTravel.travel(bfsSet);
            // 更新访问状态为已访问
            for (SubstrateNode sn : currentSet) {
                setVisited(sn);
            }
            // 当前轮次映射的虚拟节点
            Set<VirtualLink> currentAdd = new HashSet<>();
            // 对于虚拟集合的节点进行匹配
            Iterator<VirtualLink> iterator = virtualLinks.iterator();
            while (iterator.hasNext()) {
                VirtualLink vl = iterator.next();
                // 从大到小
                // 找到两个端点
                VirtualNode first = virtualNetwork.getEndpoints(vl).getFirst();
                VirtualNode second = virtualNetwork.getEndpoints(vl).getSecond();
                VirtualNode process = nodeLinkMapping.isMapped(first) ? second : first;
                VirtualNode mapped = nodeLinkMapping.isMapped(first) ? first : second;
                SubstrateNode substrateNode = nodeLinkMapping.getSubstrateNode(mapped);
                Objects.requireNonNull(substrateNode, "this substrate node can not be null");
                // 找到这个的候选集
                List<SubstrateNode> candicate = bfsTravel.filter(substrateNode, getBandwidth(vl));
                // 依次匹配
                for (SubstrateNode s : candicate) {
                    if (greatEqual(getReferenceValue(s, substrateNetwork), getReferenceValue(process, virtualNetwork))) {
                        // 满足条件 映射节点
                        if (!NodeLinkAssignation.vnm(process, s)) {
                            nodeLinkMapping.freeAllResources();
                            return false;
                        }
                        nodeLinkMapping.add(process, s);
                        iterator.remove(); // 去掉已经映射的
                        // 更新上下文
                        List<SubstrateLink> path = bfsTravel.update(nodeLinkMapping.getSubstrateNode(mapped),
                                nodeLinkMapping.getSubstrateNode(process), getBandwidth(vl));
                        if (!NodeLinkAssignation.vlm(vl, path, substrateNetwork, nodeLinkMapping.getSubstrateNode(mapped))) {
                            nodeLinkMapping.freeAllResources();
                            return false;
                        }
                        nodeLinkMapping.add(vl, path);
                        for (VirtualLink vlink : virtualNetwork.getOutEdges(process)) {
                            currentAdd.add(vlink);
                        }
                        currentAdd.remove(vl); // 去掉已经完成映射的
                        break;
                    }
                }
            }
            virtualLinks.addAll(currentAdd);
        }
        return virtualLinks.isEmpty(); // 都完成了虚拟链路的映射
    }

    // 返回映射结果
    public NodeLinkMapping getNodeLinkMapping() {
        return nodeLinkMapping;
    }
}
