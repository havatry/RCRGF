package vnreal.algorithms.rcrgf;

import vnreal.constraints.demands.BandwidthDemand;
import vnreal.constraints.demands.CpuDemand;
import vnreal.constraints.resources.BandwidthResource;
import vnreal.constraints.resources.CpuResource;
import vnreal.network.Link;
import vnreal.network.Network;
import vnreal.network.Node;
import vnreal.network.substrate.SubstrateNode;
import vnreal.network.virtual.VirtualLink;

import java.util.Optional;

/**
 * Created at 2020/4/10
 * 这是一个工具类
 */
public class Util {
    /**
     * 获取链路上的带宽属性值 链路不区分虚拟链路还是底层链路
     * @param l 链路
     * @return 带宽资源
     */
    public static double getBandwidth(Link l) {
        if (l instanceof VirtualLink) {
            return ((BandwidthDemand)l.get().get(0)).getDemandedBandwidth();
        } else {
            return ((BandwidthResource)l.get().get(0)).getBandwidth() - ((BandwidthResource)l.get().get(0)).getOccupiedBandwidth();
        }
    }

    /**
     * 设置链路上的贷带宽属性值 不区分虚拟链路或者底层链路
     * @param l 链路
     * @param bandwith 带宽值
     */
    public static void setBandwidth(Link l, double bandwith) {
        if (l instanceof VirtualLink) {
            ((BandwidthDemand)l.get().get(0)).setDemandedBandwidth(bandwith);
        } else {
            ((BandwidthResource)l.get().get(0)).setBandwidth(bandwith);
        }
    }

    /**
     * 获取节点的cpu资源
     * @param n 节点
     * @return cpu资源值
     */
    public static double getCpu(Node<?> n) {
        if (n instanceof SubstrateNode) {
            return ((CpuResource)n.get().get(0)).getCycles() - ((CpuResource)n.get().get(0)).getOccupiedCycles();
        } else {
            return ((CpuDemand)n.get().get(0)).getDemandedCycles();
        }
    }


    public static int compareLinkBandwidth(Link l1, Link l2) {
        return Double.compare(getBandwidth(l1), getBandwidth(l2));
    }

    /**
     * 选择网络中的核心节点，这类节点的特点是拥有的资源是最多的
     * @param network 给定的网络拓扑
     * @return 该网络这reference值最大的那个， 可能为null
     */
    public Optional<Node> selectCore(Network network) {
        double referenceValue = -1.0;
        Node select = null;
        for (Object o : network.getVertices()) {
            Node n = (Node) o;
            double current = getReferenceValue(n, network);
            if (current > referenceValue) {
                referenceValue = current;
                select = n;
            }
        }
        return Optional.ofNullable(select);
    }

    // 计算给定节点的reference值
    public static double getReferenceValue(Node node, Network network) {
        double bandwidth = 0.0;
        for (Object o : network.getOutEdges(node)) {
            // 强制转换为Link
            Link l = (Link) o;
            bandwidth += getBandwidth(l);
        }
        return getCpu(node) * bandwidth;
    }

    // 基本double判断条件
    private static final double esp = 1e-3;
    public static boolean equal(double a, double b) {
        return a <= b + esp && a >= b - esp;
    }
    public static boolean great(double a, double b) {
        return a > b + esp;
    }
    public static boolean small(double a, double b) {
        return a < b - esp;
    }
    public static boolean greatEqual(double a, double b) {
        return !small(a, b);
    }
    public static boolean smallEqual(double a, double b) {
        return !great(a, b);
    }
}
