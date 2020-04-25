package vnreal.algorithms.rcrgf.core;

import vnreal.algorithms.rcrgf.config.GlobalVariable;
import vnreal.algorithms.rcrgf.utils.DSU;
import vnreal.algorithms.rcrgf.utils.Util;
import vnreal.network.virtual.VirtualLink;
import vnreal.network.virtual.VirtualNetwork;
import vnreal.network.virtual.VirtualNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 减边操作
 * 2019年10月24日 下午4:19:24
 */
public class RemoveEdge {
	// 采用生成树kurskal算法，将不在生成树中的边标记下，作为候选删除集合
	// 然后找出环路，删除环路中最小带宽，并将该边加入进去
	public static boolean work(VirtualNetwork virtualNetwork) {
		// 1. 对所有边 按照带宽从大到小排序
		List<VirtualLink> links = new ArrayList<>(virtualNetwork.getEdges());
		Collections.sort(links, Util::compareLinkBandwidth);
		// 每次选择最大的带宽边
		DSU dsu = new DSU(virtualNetwork.getVertexCount());
		LinkedList<VirtualLink> other = new LinkedList<>(); // 待删除的边
		LinkedList<VirtualLink> remaining = new LinkedList<>(); // 保留的边
		for (VirtualLink l : links) {
			VirtualNode s = virtualNetwork.getEndpoints(l).getFirst();
			VirtualNode t = virtualNetwork.getEndpoints(l).getSecond();
			int start = (int) s.getId(), end = (int) t.getId();
			if (dsu.find(start) != dsu.find(end)) {
				// 不在一个集合中
				remaining.add(l);
				dsu.union(start, end);
			} else {
				other.add(l); // 无法加入的边
			}
		}
		// 选择根节点
		VirtualNode root = virtualNetwork.getVertices().iterator().next(); // 获取第一个作为根节点
		// 构造一棵树
		build(virtualNetwork, root, other, new LinkedList<>());
		// 删除remaining中的边
		for (VirtualLink r : other) {
			VirtualNode s = virtualNetwork.getEndpoints(r).getFirst();
			VirtualNode t = virtualNetwork.getEndpoints(r).getSecond();
			if ((GlobalVariable.getVirtualUpStream(s) == null && s != root)
					|| GlobalVariable.getVirtualUpStream(t) == null && t != root) {
				// 说明在其他分支上 存在多个连通分量
				return false;
			}
            // 由于加当前的链路r才引起的一个环，那么找到这个环就行
            // 首先 找出两个节点到根节点之间的路径，去掉重合的链路，剩余链路就是重合的部分
            List<VirtualLink> links1 = findLinksToRoot(virtualNetwork, s, root);
			List<VirtualLink> links2 = findLinksToRoot(virtualNetwork, t, root);
            List<VirtualLink> f = merge(links1, links2);
            for (VirtualLink l : f) {
                // 处理这些边
                Util.setBandwidth(l, Util.getBandwidth(l) + Util.getBandwidth(r));
            }
			// 将边删除
			virtualNetwork.removeEdge(r);
		}
		// 判断是否是一个连通分量
		return true;
	}

    /**
     * 依据上游节点回溯找到从当前节点到根节点的一个路径
     * @param virtualNetwork 虚拟网络
     * @param cur 当前节点
     * @param root 根节点
     * @return 当前节点到根节点沿途的链路
     */
	private static List<VirtualLink> findLinksToRoot(final VirtualNetwork virtualNetwork, VirtualNode cur, VirtualNode root) {
        List<VirtualLink> links = new ArrayList<>();
	    while (cur != root) {
            // 一直往上游搜索
            VirtualNode up = GlobalVariable.getVirtualUpStream(cur);
            // 获取链路
            VirtualLink vl = virtualNetwork.findEdge(cur, up);
            links.add(vl);
        }
        return links;
    }

    /**
     * 将两个路径进行merge
     * 规则是去除结尾一致的路径
     * @param links1 路径1
     * @param links2 路径2
     * @return 对路径1和路径2进行合并处理
     */
    private static List<VirtualLink> merge(List<VirtualLink> links1, List<VirtualLink> links2) {
        int i = links1.size() - 1, j = links2.size() - 1;
        for (;i >=0 && j >= 0 && links1.get(i) == links2.get(j); i--, j--);
        // 到此i和j不一样
        List<VirtualLink> left = links1.subList(0, i + 1);
        left.addAll(links2.subList(0, j + 1));
        return left;
    }

    /**
     * 递归设置树形结构的上游节点
     * @param virtualNetwork 虚拟网络
     * @param root 根节点
     * @param other 待删除的边， 设置上游只针对保留边而言
     * @param visited 已经访问过的集合
     */
	private static void build(final VirtualNetwork virtualNetwork, VirtualNode root, final List<VirtualLink> other, LinkedList<VirtualLink> visited) {
		// 考虑所有出边 
		for (VirtualLink l : virtualNetwork.getOutEdges(root)) {
			if (visited.contains(l) || other.contains(l)) {
				// 已经访问过的边 或者不是保留边
				continue;
			}
			VirtualNode s = virtualNetwork.getEndpoints(l).getFirst();
			VirtualNode t = virtualNetwork.getEndpoints(l).getSecond();
			visited.add(l);
			if (s == root) {
				// 那么t是下游
				GlobalVariable.setVirtualUpStream(t, s);
				build(virtualNetwork, t, other, visited);
			} else {
				// 那么下游是s
				GlobalVariable.setVirtualUpStream(s, t);
				build(virtualNetwork, s, other, visited);
			}
		}
	}
}
