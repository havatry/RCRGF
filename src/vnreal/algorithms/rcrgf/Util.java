package vnreal.algorithms.rcrgf;

import vnreal.constraints.demands.BandwidthDemand;
import vnreal.constraints.resources.BandwidthResource;
import vnreal.network.Link;
import vnreal.network.virtual.VirtualLink;

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

    public static int compareLinkBandwidth(Link l1, Link l2) {
        return Double.compare(getBandwidth(l1), getBandwidth(l2));
    }
}
