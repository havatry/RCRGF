package vnreal.algorithms.myrcrgf.control;

import java.util.TreeMap;

/**
 * 管理数据服务
 * 2019年11月17日 下午8:32:01
 */
public class DataManager {
	private TreeMap<Integer, Double> timeAve; // value 单位ms
	private TreeMap<Integer, Integer> revenueUse; // value
	private TreeMap<Integer, Integer> costUse; // 重新计算
	private TreeMap<Integer, Double> acceptanceAve; // 0或者1
	
	public String report(Integer timeUnit) {
		// 报告当前时刻的数据
		StringBuilder stringBuilder = new StringBuilder("timeAve=");
		stringBuilder.append(timeAve.floorEntry(timeUnit).getValue());
		stringBuilder.append("&");
		stringBuilder.append("revenueUse=");
		stringBuilder.append(revenueUse.floorEntry(timeUnit).getValue());
		stringBuilder.append("&");
		stringBuilder.append("costUse=");
		stringBuilder.append(costUse.floorEntry(timeUnit).getValue());
		stringBuilder.append("&");
		stringBuilder.append("acceptanceAve=");
		stringBuilder.append(acceptanceAve.floorEntry(timeUnit).getValue());
		return stringBuilder.toString();
	}
	
	public void update(String info) {
		// 信号 时间 value
		String[] part = info.split(" ");
		Integer timeUnit = Integer.parseInt(part[1]);
		switch (part[0]) {
			case "timeAve":
				Double newValue = (timeAve.floorEntry(timeUnit).getValue() * timeAve.size() + Integer.parseInt(part[2])) / (timeAve.size() + 1);
				timeAve.put(timeUnit, newValue);
				break;
			case "revenueUse":
				Integer revenueValue = revenueUse.floorEntry(timeUnit).getValue() + Integer.parseInt(part[2]);
				revenueUse.put(timeUnit, revenueValue);
				break;
			case "costUse":
				// 由其他地方传入
				Integer costValue = costUse.floorEntry(timeUnit).getValue() + Integer.parseInt(part[2]); // 存在正负号
				costUse.put(timeUnit, costValue);
				break;
			default:
				Double acceptanceValue = (acceptanceAve.floorEntry(timeUnit).getValue() 
						* acceptanceAve.size() + Integer.parseInt(part[2])) / (acceptanceAve.size() + 1);
				acceptanceAve.put(timeUnit, acceptanceValue);
				break;
		}
	}
}
