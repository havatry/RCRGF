package vnreal.algorithms.myAEF.save;

import java.util.Iterator;
import java.util.TreeMap;

/**
 * 时间管理 用于管理每个时间节点的操作。由外部进行调用
 * 2019年11月17日 下午8:11:52
 */
public class TimeManager {
	private static TreeMap<Integer, Integer> freeTime; // 每个请求的撤销时间
	
	/**
	 * 处理当前请求，并将其注册到freeTime中，以便后面进行释放
	 * @param id 虚拟请求的id
	 * @param endTime 该虚拟请求释放时间
	 */
	public static void register(Integer id, Integer endTime) {
		freeTime.put(endTime, id);
		// 首先处理当前请求
		// 委托给实际算法处理
		
		// 返回数据 接收率和时间和收益
		// 触发DataManager机制
	}
	
	/**
	 * 释放当前timeUnit之前的虚拟请求
	 * @param timeUnit
	 */
	public static void filter(Integer timeUnit) {
		// 过滤当前时间之前的虚拟请求
		Iterator<Integer> iterator = freeTime.keySet().iterator();
		while (iterator.hasNext()) {
			Integer key = iterator.next();
			if (key <= timeUnit) {
				// free资源
				ResourceManager.getInstance().free(freeTime.get(key));
				iterator.remove();
			} else {
				break;
			}
		}
		// 更新cost
		// 触发DataManager机制
	}
}
