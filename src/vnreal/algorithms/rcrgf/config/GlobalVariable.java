package vnreal.algorithms.rcrgf.config;

import vnreal.network.substrate.SubstrateNode;
import vnreal.network.virtual.VirtualNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Created at 2020/4/10
 * 对于每个节点都有一个唯一的编号，对于一个底层网络和多个虚拟网络同样适用
 * 这里将使用两个map 分别代表底层网络和虚拟网络
 * 如map.put(1,2)表示标签为1的底层节点上游是2
 */
public class GlobalVariable {
    private static Map<VirtualNode, VirtualNode> virtualMap = new HashMap<>();
    private static Map<SubstrateNode, SubstrateNode> substrateMap = new HashMap<>();
    private static Map<SubstrateNode, Boolean> visited = new HashMap<>();
    // 设置值
    public static void setVirtualUpStream(VirtualNode cur, VirtualNode up) {
        virtualMap.put(cur, up);
    }
    public static void setSubstrateUpStream(SubstrateNode cur, SubstrateNode up) {
        substrateMap.put(cur, up);
    }
    public static void setVisited(SubstrateNode cur) {
        visited.put(cur, true);
    }
    public static void cancelVisited(SubstrateNode cur) {
        visited.put(cur, false);
    }
    // 获取值
    public static VirtualNode getVirtualUpStream(VirtualNode cur) {
        return virtualMap.get(cur);
    }
    public static SubstrateNode getSubstrateUpStream(SubstrateNode cur) {
        return substrateMap.get(cur);
    }
    public static Boolean isVisited(SubstrateNode cur) {
        return visited.getOrDefault(cur, false);
    }
}
