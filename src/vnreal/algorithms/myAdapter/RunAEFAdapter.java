package vnreal.algorithms.myAdapter;

import mulavito.algorithms.AbstractAlgorithmStatus;
import vnreal.algorithms.AbstractAlgorithm;
import vnreal.algorithms.AlgorithmParameter;
import vnreal.algorithms.CoordinatedMapping;
import vnreal.algorithms.isomorphism.SubgraphIsomorphismStackAlgorithm;
import vnreal.algorithms.myAEF.strategies.AEFAlgorithm;
import vnreal.algorithms.myAEF.strategies.NRMAlgorithm;
import vnreal.algorithms.myAEF.util.Constants;
import vnreal.algorithms.myAEF.util.FileHelper;
import vnreal.algorithms.myAEF.util.SummaryResult;
import vnreal.algorithms.myAEF.util.Utils;
import vnreal.constraints.resources.BandwidthResource;
import vnreal.constraints.resources.CpuResource;
import vnreal.network.NetworkStack;
import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.substrate.SubstrateNode;
import vnreal.network.virtual.VirtualNetwork;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 提供调用接口process方法
 */
public class RunAEFAdapter {
	private static final int interval = 50; // 实验处理间隔
	private static final int end = 2000; // 模拟实验时间
	private static final int processed = -1; // -1表示该虚拟请求已经释放了或者没有映射成功, 无需释放
	private List<Integer> startList;
	private List<Integer> endList;
	//-------------// 初始化
	private List<VirtualNetwork> virtualNetworks; // 处理过的虚拟请求
	//-------------// 统计变量
	private double hasGainRevenue; // 上次为止获取的收益
	private int hasMappedSuccRequest; // 上次已经完成映射个数
	private long hasExecuteTime; // 上次算法已经计算时间
	private SummaryResult summaryResult = new SummaryResult();
	
	@SuppressWarnings("unchecked")
	public void process(AbstractAlgorithm algorithm, String filename, int id, String type, int total) {
	    EventProcess eventProcess = new EventProcess(algorithm, id, type);
		Object[] result = FileHelper.readContext(filename);
		SubstrateNetwork substrateNetwork = ((NetworkStack)result[0]).getSubstrate();
		virtualNetworks = ((NetworkStack)result[0]).getVirtuals();
//		virtualNetworks = ProduceCase.getVirtuals(2);
        if (result.length == 3) {
            startList = (List<Integer>) result[1];
            endList = (List<Integer>) result[2];
        } else {
            startList = Arrays.asList(0);
            endList = Arrays.asList(2 * end);
        }
		// 每隔50 time unit进行处理一次
		int inter = 0; // 下次处理的开始位置, 指示器
		for (int time = interval; time <= total; time += interval) {
			// 处理endList
			processEndList(time, inter);
			// 处理新的strtList
			for (int i = inter; i < startList.size(); i++) {
				if (startList.get(i) <= time) {
					// 调用算法去处理
					algorithm.setStack(new NetworkStack(substrateNetwork, Arrays.asList(virtualNetworks.get(i))));
					algorithm.performEvaluation();
					// 获取信息
					List<AbstractAlgorithmStatus> status = algorithm.getStati();
					// 第一项是映射成功率 第二项是执行时间 第三项是收益
					if (status.get(0).getRatio() == 100) {
						hasMappedSuccRequest++;
						hasGainRevenue += (Double)status.get(2).getValue();
					} else {
						// 撤销
						startList.set(i, processed);
					}
					hasExecuteTime += (Long)status.get(1).getValue();
					// 插入事件处理
                    eventProcess.process(i+1, substrateNetwork, virtualNetworks.get(i), status.get(0).getRatio() == 100);
				} else {
					break; // next time
				}
				inter++;
			}
			//-----------------------// 统计
//			summaryResult.addRevenueToTime(hasGainRevenue / (inter * interval));
			summaryResult.addRevenueToTime(hasGainRevenue / time);
			summaryResult.addTotaTime(hasExecuteTime);
			summaryResult.addVnAcceptance((double)hasMappedSuccRequest / inter);
			// 获取底层网络代价
			double nodeOcc = 0.0, linkOcc = 0.0;
            List<Double> nums = new ArrayList<>();
			for (SubstrateNode sn : substrateNetwork.getVertices()) {
				// 占用的资源
				nodeOcc += ((CpuResource)sn.get().get(0)).getOccupiedCycles();
			}
			for (SubstrateLink sl : substrateNetwork.getEdges()) {
				// 占用的带宽
				linkOcc += ((BandwidthResource)sl.get().get(0)).getOccupiedBandwidth();
				// 剩余的带宽
                nums.add(Utils.getBandwith(sl));
			}
			double cost = nodeOcc  + linkOcc;
			summaryResult.addCostToRevenue(cost / hasGainRevenue); // 调整顺序
            // 计算底层网络带宽标准差
            summaryResult.addBandwidthStandardDiff(Utils.stanrdDiff(nums));
		}
		// 输出到文件
		String fix;
		if (algorithm instanceof AEFAlgorithm) {
		    if (((AEFAlgorithm) algorithm).isAdvanced()) {
                fix = "aefAdvance";
            } else {
		        fix = "aefBaseline";
            }
		} else if (algorithm instanceof SubgraphIsomorphismStackAlgorithm) {
			fix = "subgraph";
		} else if (algorithm instanceof CoordinatedMapping) {
		    fix = "ViNE";
        } else if (algorithm instanceof NRMAlgorithm) {
		    fix = "NRM";
        } else {
			fix = "null";
		}
//		filename = filename.replace("file", "output");
//		String writeFileName = filename.substring(0, filename.lastIndexOf(".")) + "_" + fix + ".txt";
		String writeFileName = Constants.resultDir + File.separator + "simulation_" + fix + ".txt";
		summaryResult.writeToFile(writeFileName);
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
	
	private static AlgorithmParameter initParam() {
		AlgorithmParameter algorithmParameter = new AlgorithmParameter();
		algorithmParameter.put("linkMapAlgorithm", "bfs");
		algorithmParameter.put("distanceConstraint", "70.0");
		algorithmParameter.put("advanced", "false");
		algorithmParameter.put("eppstein", "false");
		algorithmParameter.put("AEFAdvanced", "true");
		//-----------//
		return algorithmParameter;
	}
}
