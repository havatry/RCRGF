package vnreal.algorithms.myAEF.strategies.aef;

import java.util.Collections;
import java.util.PriorityQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vnreal.algorithms.myAEF.util.Utils;
import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.substrate.SubstrateNode;
import vnreal.network.virtual.VirtualLink;
import vnreal.network.virtual.VirtualNetwork;
import vnreal.network.virtual.VirtualNode;

/**
 * 映射规则
 * 2019年11月20日 下午9:42:05
 */
public class MappingRule {
	private SubstrateNetwork substrateNetwork;
	private VirtualNetwork virtualNetwork;
	private Logger log = LoggerFactory.getLogger(MappingRule.class);
	
	public MappingRule(SubstrateNetwork substrateNetwork, VirtualNetwork virtualNetwork) {
		//Created constructor stubs
		this.substrateNetwork = substrateNetwork;
		this.virtualNetwork = virtualNetwork;
	}
	
	/**
	 * 判断虚拟节点映射到底层节点是否合适
	 * @param substrateNode 
	 * @param virtualNode
	 * @return
	 */
	public boolean rule(SubstrateNode substrateNode, VirtualNode virtualNode) {
		return ruleForNode(substrateNode, virtualNode) && ruleForLink(substrateNode, virtualNode);
	}
	
	private boolean ruleForNode(SubstrateNode substrateNode, VirtualNode virtualNode) {
		// 只需要判断节点是否满足需求即可 底层资源 >= 虚拟请求
		return Utils.greatEqual(Utils.getCpu(substrateNode), Utils.getCpu(virtualNode));
	}
	
	private boolean ruleForLink(SubstrateNode substrateNode, VirtualNode virtualNode) {
		// 判断周边的链路资源是否满足请求的链路需求
		// 模拟测试
		PriorityQueue<Double> priorityQueueSubstrate = new PriorityQueue<>(Collections.reverseOrder()); // 逆序底层链路
		PriorityQueue<Double> priorityQueueVirtual = new PriorityQueue<>(Collections.reverseOrder()); // 逆序请求链路
		for (VirtualLink vl : virtualNetwork.getOutEdges(virtualNode)) {
			priorityQueueVirtual.offer(Utils.getBandwith(vl));
		}
		for (SubstrateLink sl : substrateNetwork.getOutEdges(substrateNode)) {
			priorityQueueSubstrate.offer(Utils.getBandwith(sl));
		}
		
		// 当处理完所有虚拟请求的带宽资源时候退出循环
		while (!priorityQueueVirtual.isEmpty()) {
			Double demand = priorityQueueVirtual.poll();
			Double resource = priorityQueueSubstrate.poll();
			if (Utils.greatEqual(resource, demand)) {
				// 更新
				priorityQueueSubstrate.offer(resource - demand);
			} else {
				// 不满足要求
				return false;
			}
		}
		// 符合要求
		return true;
	}
}
