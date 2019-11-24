package vnreal.algorithms.myrcrgf.save;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import vnreal.algorithms.myrcrgf.util.Utils;
import vnreal.constraints.resources.BandwidthResource;
import vnreal.constraints.resources.CpuResource;
import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNode;

/**
 * 管理虚拟网络请求占用资源和撤销资源
 * 2019年11月17日 下午6:42:54
 */
public class ResourceManager {
	private HashMap<Integer, List<String>> use;
	
	private static ResourceManager rm;
	
	private ResourceManager() {}
	
	// 单例模式
	public static ResourceManager getInstance() {
		if (rm == null) {
			// 单线程环境下 不进行双检锁
			rm = new ResourceManager();
		}
		return rm;
	}
	
	/**
	 * 记录每个虚拟请求在底层网络中使用的资源数量
	 * @param id 虚拟请求id
	 * @param req 请求资源的字符串表示。格式虚拟节点 = cpu占用量 & 虚拟链路 = 带宽占用量
	 * @return 请求是否可以被底层网络满足
	 */
	public boolean request(Integer id, String req) {
		if (use.get(id) == null) {
			use.put(id, new ArrayList<String>());
		}
		use.get(id).add(req);
		return operateResource(req, true);
	}
	
	/**
	 * 释放一个虚拟请求所有占用的资源
	 * @param id 虚拟请求id
	 */
	public void free(Integer id) {
		// 获取对应虚拟请求占用的所有资源
		List<String> usage = use.get(id);
		for (String info : usage) {
			operateResource(info, false);
		}
	}
	
	// 辅助方法
	private boolean operateResource(String req, boolean add) {
		String[] parts = req.split("&");
		String[] nodeInfo = parts[0].split("=");
		String[] linkInfo = parts[1].split("=");
		// find node
		SubstrateNode selectedNode = IdManager.getNodeForIndex(Integer.parseInt(nodeInfo[0]));
		CpuResource cpuResource = (CpuResource)selectedNode.get().get(0);
		cpuResource.setOccupiedCycles(cpuResource.getOccupiedCycles() +
				(add ? Double.parseDouble(nodeInfo[1]) : -Double.parseDouble(nodeInfo[1])));
		// find link
		SubstrateLink selectedLink = IdManager.getLinkForIndex(Integer.parseInt(linkInfo[0]));
		BandwidthResource bandwidthResource = (BandwidthResource) selectedLink.get().get(0);
		bandwidthResource.setOccupiedBandwidth(cpuResource.getOccupiedCycles() +
				(add ? Double.parseDouble(linkInfo[1]) : -Double.parseDouble(linkInfo[1])));
		return !add || Utils.greatEqual(cpuResource.getCycles(), cpuResource.getOccupiedCycles()) && 
				Utils.greatEqual(bandwidthResource.getBandwidth(), bandwidthResource.getOccupiedBandwidth());
	}
	
	/**
	 * 计算一个虚拟请求占用的底层资源数量
	 * @param id 虚拟请求id
	 * @return 占用资源量
	 */
	public Integer fulfill(Integer id) {
		Integer result = 0;
		for (String req : use.get(id)) {
			String[] parts = req.split("&");
			String[] nodeInfo = parts[0].split("=");
			String[] linkInfo = parts[1].split("=");
			result += Integer.parseInt(nodeInfo[1]) + Integer.parseInt(linkInfo[1]);
		}
		return result;
	}
}
