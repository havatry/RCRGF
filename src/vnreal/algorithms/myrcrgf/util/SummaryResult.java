package vnreal.algorithms.myrcrgf.util;

import java.util.List;

/**
 * 结果输出封装
 * @author hudedong
 *
 */
public class SummaryResult {
	private List<Long> totalTime; // 程序执行到每个时间点的总时间
	private List<Double> vnAcceptance; // 每个时间点的请求接受率
	private List<Double> costToRevenue; // 每个时间点的代价收益比
	private List<Double> revenueToTime; // 每个时间点收益时间比
	
	public void addTotaTime(long executionTime) {
		totalTime.add(executionTime);
	}
	
	// 直接计算后 添加进来
	public void addVnAcceptance(double vna) {
		vnAcceptance.add(vna);
	}
	
	public void addCostToRevenue(double ctr) {
		costToRevenue.add(ctr);
	}
	
	public void addRevenueToTime(double rtt) {
		revenueToTime.add(rtt);
	}
	
	public List<Double> getCostToRevenue() {
		return costToRevenue;
	}
	
	public List<Double> getRevenueToTime() {
		return revenueToTime;
	}
	
	public List<Long> getTotalTime() {
		return totalTime;
	}
	
	public List<Double> getVnAcceptance() {
		return vnAcceptance;
	}
}
