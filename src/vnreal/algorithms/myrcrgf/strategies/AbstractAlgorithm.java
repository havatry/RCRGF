package vnreal.algorithms.myrcrgf.strategies;

import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.virtual.VirtualNetwork;

/**
 * 为不同算法提供一个基本模板，统计算法计算时间和结果
 * 2019年11月19日 下午7:10:33
 */
public abstract class AbstractAlgorithm {
	private long executeTime; // 算法执行时间
	private boolean succ; // 是否成功映射
	
	public void wrap(SubstrateNetwork sn, VirtualNetwork vn) {
		long start = System.currentTimeMillis();
		succ = work(sn, vn);
		executeTime = System.currentTimeMillis() - start;
	}
	
	protected abstract boolean work(SubstrateNetwork sn, VirtualNetwork vn);
	
	public long getExecuteTime() {
		return executeTime;
	}
	
	public boolean isSucc() {
		return succ;
	}
}
