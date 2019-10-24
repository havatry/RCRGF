package vnreal.algorithms.argf.util;

import vnreal.constraints.demands.BandwidthDemand;
import vnreal.constraints.demands.CpuDemand;
import vnreal.constraints.resources.BandwidthResource;
import vnreal.constraints.resources.CpuResource;
import vnreal.network.Link;
import vnreal.network.Network;
import vnreal.network.Node;
import vnreal.network.substrate.SubstrateNode;
import vnreal.network.virtual.VirtualLink;

public class Utils {
	private static final double esp = 1e-3;
	
	public static double getBandwith(Link<?> l) {
		if (l instanceof VirtualLink) {
			return ((BandwidthDemand)l.get().get(0)).getDemandedBandwidth();
		} else {
			return ((BandwidthResource)l.get().get(0)).getBandwidth();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void setBandwith(Link l, double bandwith) {
		if (l instanceof VirtualLink) {
			((BandwidthDemand)l.get().get(0)).setDemandedBandwidth(bandwith);
		} else {
			((BandwidthResource)l.get().get(0)).setBandwidth(bandwith);
		}
	}
	
	public static double getCpu(Node<?> n) {
		if (n instanceof SubstrateNode) {
			return ((CpuResource)n.get().get(0)).getCycles();
		} else {
			return ((CpuDemand)n.get().get(0)).getDemandedCycles();
		}
	}
	
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
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static double getReferencedResource(Node node, Network network, double alpha) {
		double bandwith_total = 0.0;
		for (Object l : network.getOutEdges(node)) {
			Link<?> link = (Link<?>)l;
			bandwith_total += alpha * getBandwith(link);
		}
		double v1 = getCpu(node) + bandwith_total;
		return v1;
	}
}
