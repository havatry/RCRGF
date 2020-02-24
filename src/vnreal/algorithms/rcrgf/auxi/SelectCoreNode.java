package vnreal.algorithms.rcrgf.auxi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import vnreal.algorithms.rcrgf.util.Utils;
import vnreal.network.Network;
import vnreal.network.Node;

/**
 * ��ȷ�����������˺�Ϊ����ѡ��һ�����Ľڵ�,Ҳ��root�ڵ�
 * 2019��10��24�� ����8:46:57
 */
public class SelectCoreNode {
	private static double alpha = 1.0;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Deprecated
	public static Node selectForRootOld(Network network) {
		List<Node<?>> list = new ArrayList<>(network.getVertices());
		Collections.sort(list, (o1, o2) -> {
            //Created method stubs
            double v1 = Utils.getReferencedResource(o1, network, alpha);
            double v2 = Utils.getReferencedResource(o2, network, alpha);
            if (v1 == v2) {
                return 0;
            } else if (v1 < v2) {
                return 1;
            } else {
                return -1;
            }
        });
		return list.get(0); // ѡȡ��Դ����
	}
	
	@SuppressWarnings("rawtypes")
	public static Node selectForRoot(Network network) {
		double max = Double.MIN_VALUE;
		Node ret = null;
		for (Object n : network.getVertices()) {
			Node<?> node = (Node<?>)n;
			double current = Utils.getReferencedResource(node, network, alpha);
			if (current > max) {
				ret = node;
			}
		}
		return ret;
	}
	
	public static void setAlpha(double alpha) {
		SelectCoreNode.alpha = alpha;
	}
	
	public static double getAlpha() {
		return alpha;
	}
}
