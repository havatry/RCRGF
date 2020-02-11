package vnreal.algorithms.myAEF.save;

import java.util.HashMap;

import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNode;

/**
 * 底层节点和链路和id对应
 * 2019年11月17日 下午7:08:42
 */
public class IdManager {
	public static HashMap<Long, SubstrateNode> nodes; // 由其他地方传入
	public static HashMap<Long, SubstrateLink> links;
	
	public static Long getIndexForNode(SubstrateNode sn) {
		return sn.getId();
	}
	
	public static Long getIndexForLink(SubstrateLink sl) {
		return sl.getId();
	}
	
	public static SubstrateNode getNodeForIndex(Integer index) {
		return nodes.get(index);
	}
	
	public static SubstrateLink getLinkForIndex(Integer index) {
		return links.get(index);
	}
}
