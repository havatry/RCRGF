package vnreal.algorithms.myrcrgf.simulation;

import java.util.ArrayList;
import java.util.List;

import mulavito.algorithms.IAlgorithm;
import vnreal.algorithms.myrcrgf.util.Utils;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.virtual.VirtualNetwork;

/**
 * 按照泊松分布生成虚拟请求的开始时间，指数分布生成虚拟请求的停留时间，1.每隔一段时间统计所有请求映射的总时间
 * 2.每隔一段时间统计代价/收益比 3.每隔一段时间统计接收率。 对比三种算法
 * 2019年11月24日 下午7:46:27
 */
public class Main {
	private static final int interval = 50; // 实验处理间隔
	private static final int end = 2000; // 模拟实验时间
	private static final double arive_lambda = 3.0 / 100; // lambda1
	private static final double preserve_lambda = 1.0 / 500; // lambda2
	private static final int processed = -1; // -1表示该虚拟请求已经释放了或者没有映射成功, 无需释放
	private List<Integer> startList;
	private List<Integer> endList;
	private List<VirtualNetwork> virtualNetworks; // 处理过的虚拟请求
	
	public static void main(String[] args) {
		Main main = new Main();
		main.init();
		
	}
	
	// 生成虚拟请求的到达时间和停留时间
	private void init() {
		startList = new ArrayList<Integer>();
		endList = new ArrayList<>();
		startList.add(Utils.exponentialDistribution(arive_lambda));
		endList.add(Utils.exponentialDistribution(preserve_lambda));
		
		while (startList.get(startList.size() - 1) <= end) {
			startList.add(startList.get(startList.size() - 1) + Utils.exponentialDistribution(arive_lambda));
			endList.add(Utils.exponentialDistribution(preserve_lambda));
		}
		startList.remove(startList.size() - 1);
		endList.remove(endList.size() - 1);
	}
	
	private void process(IAlgorithm algorithm) {
		// 每隔50 time unit进行处理一次
		int inter = 0; // 下次处理的开始位置, 指示器
		for (int time = interval; time <= end; time += interval) {
			// 处理endList
			processEndList(time, inter);
			// 处理新的strtList
			for (int i = inter; i < startList.size(); i++) {
				if (startList.get(i) <= time) {
					// 需要处理
					// 调用Main函数
				}
			}
		}
	}
	
	private void processEndList(int time, int inter) {
		for (int i = 0; i < inter; i++) {
			if (startList.get(i) == processed) {
				// 不用处理
				continue;
			}
			if (startList.get(i) >= time) {
				// 无需处理, 因为间隔时间至少是1 time unit
				break;
			}
			if (startList.get(i) + endList.get(i) <= time) {
				// 在当前时刻之前需要释放
				virtualNetworks.get(i).clearVnrMappings();
				startList.set(i, processed);
			}
		}
	}
	
//	private SubstrateNetwork produceSNet() {
//		
//	}
}
