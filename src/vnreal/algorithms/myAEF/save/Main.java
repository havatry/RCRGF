package vnreal.algorithms.myAEF.save;

import java.util.Arrays;

public class Main {
	public static int time; // 仿真实验的时间点
	public static int id; // 处理的虚拟请求id
	
	public void process() {
		TimeSequence timeSequence = new TimeSequence();
		int[] startTime = timeSequence.produceStatrTime();
		int[] endTime = timeSequence.produceEndTime();
		int[][] merge = new int[startTime.length][2];
		for (int i = 0; i < startTime.length; i++) {
			merge[i][0] = startTime[i];
			merge[i][1] = endTime[1];
		}
		Arrays.sort(merge, (a, b) -> {
			if (a[0] > b[0]) {
				return 1;
			} else if (a[0] < b[0]) {
				return -1;
			} else {
				return 0;
			}
		});
		int index = 0; // 指向排序数组的第一个
		for (time = 0; time <= 10_000; time++) {
			// 撤销资源
			TimeManager.filter(time);
			if (time == merge[index][0]) {
				// 注册
				TimeManager.register(id++, merge[index++][1]);
			}
		}
	}
}
