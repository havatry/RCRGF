package vnreal.algorithms.rcrgf;

import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.substrate.SubstrateNode;

import java.util.*;
import java.util.stream.Collectors;

import static vnreal.algorithms.rcrgf.GlobalVariable.isVisited;
import static vnreal.algorithms.rcrgf.GlobalVariable.setSubstrateUpStream;
import static vnreal.algorithms.rcrgf.Util.equal;
import static vnreal.algorithms.rcrgf.Util.getBandwidth;
import static vnreal.algorithms.rcrgf.Util.greatEqual;

/**
 * Created on 2020/4/19
 * 使用BFS策略遍历
 */
public class BFSTravel {
    private Map<SubstrateNode, Map<SubstrateNode, PriorityQueue<Node>>> EBTL;
    private SubstrateNetwork substrateNetwork;

    public BFSTravel(SubstrateNetwork substrateNetwork) {
        this.EBTL = new HashMap<>(16);
        this.substrateNetwork = substrateNetwork;
        for (SubstrateNode substrateNode : substrateNetwork.getVertices()) {
            // 初始化 每个节点初始上游节点是自身 值是0.0
            Map<SubstrateNode, PriorityQueue<Node>> cur = new HashMap<>(4);
            cur.put(substrateNode, new PriorityQueue());
            cur.get(substrateNode).add(new Node(substrateNode, 0.0));
            EBTL.put(substrateNode, cur);
        }
    }

    /**
     * EBTL的元素，被放置于优先队列中进行排序 排序的依据是value值
     */
    private class Node implements Comparable<Node>{
        private SubstrateNode substrateNode;
        private double value;

        public Node(SubstrateNode substrateNode, double value) {
            this.substrateNode = substrateNode;
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        public SubstrateNode getSubstrateNode() {
            return substrateNode;
        }

        @Override
        public int compareTo(Node o) {
            return Double.compare(value, o.value);
        }
    }

    /**
     * 目前实现比较简单版本 将当前轮遍历的底层节点全部设置已经访问
     * 之后看测试再调整这个方面 只将映射的节点设置已经访问*
     * @param sourceSet 给定的初始集
     */
    public Set<SubstrateNode> travel(Set<SubstrateNode> sourceSet) {
        Set<SubstrateNode> access = new HashSet<>(16);
        // 遍历给定的集合 计算出EBTL
        for (SubstrateNode up : sourceSet) {
            for (SubstrateNode cur: substrateNetwork.getNeighbors(up)) {
                // 判断是否已经遍历了
                if (isVisited(cur)) {
                    continue;
                }
                access.add(cur); // 访问的节点
                // 设置上游
                setSubstrateUpStream(cur, up);
                // 计算EBTL
                // 将上游节点的EBTL继承过来
                Map<SubstrateNode, PriorityQueue<Node>> _BTL = EBTL.get(up);
                for (SubstrateNode other : _BTL.keySet()) {
                    if (EBTL.get(cur).get(other) == null) {
                        // 新条目 初始化
                        EBTL.get(cur).put(other, new PriorityQueue<>());
                    }
                    // 上游节点的对应other的优先队列第一个元素
                    Node node = EBTL.get(up).get(other).peek();
                    SubstrateLink link = substrateNetwork.findEdge(cur, up);
                    double newBandwidth = getBandwidth(link);
                    EBTL.get(cur).get(other).add(new Node(up, equal(node.getValue(), 0.0) ?
                            newBandwidth : Math.min(node.getValue(), newBandwidth)));
                }
            }
        }
        return access;
    }

    /**
     * 按照给定带宽过滤指定节点中的一些其他可达节点
     * @param other 其他可到当前的节点
     * @param baseBandwidth 当前虚拟节点的带宽需求
     * @return 过滤当前节点的可达节点
     */
    public Set<SubstrateNode> filter(SubstrateNode other, double baseBandwidth) {
        return EBTL.get(other).keySet().stream().filter(
                o -> greatEqual(EBTL.get(other).get(o).peek().getValue(), baseBandwidth))
                .collect(Collectors.toSet());
    }
}
