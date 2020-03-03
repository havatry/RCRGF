package vnreal.algorithms.myAEF.simulation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mulavito.algorithms.AbstractAlgorithmStatus;
import vnreal.algorithms.AbstractAlgorithm;
import vnreal.algorithms.AlgorithmParameter;
import vnreal.algorithms.AvailableResources;
import vnreal.algorithms.CoordinatedMapping;
import vnreal.algorithms.isomorphism.SubgraphIsomorphismStackAlgorithm;
import vnreal.algorithms.myAEF.strategies.AEFAlgorithm;
import vnreal.algorithms.myAEF.strategies.NRMAlgorithm;
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

/**
 * ���ղ��ɷֲ�������������Ŀ�ʼʱ�䣬ָ���ֲ��������������ͣ��ʱ�䣬1.ÿ��һ��ʱ��ͳ����������ӳ�����ʱ��
 * 2.ÿ��һ��ʱ��ͳ�ƴ���/����� 3.ÿ��һ��ʱ��ͳ�ƽ����ʡ� �Ա������㷨
 * 2019��11��24�� ����7:46:27
 */
public class Run {
	private static final int interval = 50; // ʵ�鴦����
	private static final int end = 2000; // ģ��ʵ��ʱ��
	private static final int processed = -1; // -1��ʾ�����������Ѿ��ͷ��˻���û��ӳ��ɹ�, �����ͷ�
	private List<Integer> startList;
	private List<Integer> endList;
	//-------------// ��ʼ��
	private List<VirtualNetwork> virtualNetworks; // ���������������
	//-------------// ͳ�Ʊ���
	private double hasGainRevenue; // �ϴ�Ϊֹ��ȡ������
	private int hasMappedSuccRequest; // �ϴ��Ѿ����ӳ�����
	private long hasExecuteTime; // �ϴ��㷨�Ѿ�����ʱ��
	private SummaryResult summaryResult = new SummaryResult();
	
	public static void main(String[] args) {
		String base = "results/file/";
		String filename = base + "substratework_20200213182601.xml";
		AlgorithmParameter parameter = initParam();
		new Run().process(new AEFAlgorithm(parameter, false), filename); // baseline
        new Run().process(new AEFAlgorithm(parameter, true), filename); // advanced
		new Run().process(new SubgraphIsomorphismStackAlgorithm(parameter), filename);
		new Run().process(new CoordinatedMapping(parameter), filename);
		new Run().process(new NRMAlgorithm(parameter), filename);
		System.out.println("Done");
	}
	
	@SuppressWarnings("unchecked")
	public void process(AbstractAlgorithm algorithm, String filename) {
		Object[] result = FileHelper.readContext(filename);
		SubstrateNetwork substrateNetwork = ((NetworkStack)result[0]).getSubstrate();
		virtualNetworks = ((NetworkStack)result[0]).getVirtuals();
        if (result.length == 3) {
            startList = (List<Integer>) result[1];
            endList = (List<Integer>) result[2];
        } else { // ����û��_aux���ļ�
            startList = Arrays.asList(0);
            endList = Arrays.asList(2 * end); // ���⵱ǰ�����ͷ�
        }
		// ÿ��50 time unit���д���һ��
		int inter = 0; // �´δ���Ŀ�ʼλ��, ָʾ��
		for (int time = interval; time <= end; time += interval) {
			// ����endList
			processEndList(time, inter);
			// �����µ�strtList
			for (int i = inter; i < startList.size(); i++) {
				if (startList.get(i) <= time) {
					// �����㷨ȥ����
					algorithm.setStack(new NetworkStack(substrateNetwork, Arrays.asList(virtualNetworks.get(i))));
					algorithm.performEvaluation();
					// ��ȡ��Ϣ
					List<AbstractAlgorithmStatus> status = algorithm.getStati();
					// ��һ����ӳ��ɹ��� �ڶ�����ִ��ʱ�� ������������
					if (status.get(0).getRatio() == 100) {
						hasMappedSuccRequest++;
						hasGainRevenue += (Double)status.get(2).getValue();
					} else {
						// ����
						startList.set(i, processed);
					}
					hasExecuteTime += (Long)status.get(1).getValue();
					// �����¼�����

				} else {
					break; // next time
				}
				inter++;
			}
			//-----------------------// ͳ��
//			summaryResult.addRevenueToTime(hasGainRevenue / (inter * interval));
			summaryResult.addRevenueToTime(hasGainRevenue / time);
			summaryResult.addTotaTime(hasExecuteTime);
			summaryResult.addVnAcceptance((double)hasMappedSuccRequest / inter);
			// ��ȡ�ײ��������
			double nodeOcc = 0.0, linkOcc = 0.0;
            List<Double> nums = new ArrayList<>();
			for (SubstrateNode sn : substrateNetwork.getVertices()) {
				// ռ�õ���Դ
				nodeOcc += ((CpuResource)sn.get().get(0)).getOccupiedCycles();
			}
			for (SubstrateLink sl : substrateNetwork.getEdges()) {
				// ռ�õĴ���
				linkOcc += ((BandwidthResource)sl.get().get(0)).getOccupiedBandwidth();
				// ʣ��Ĵ���
                nums.add(Utils.getBandwith(sl));
			}
			double cost = nodeOcc  + linkOcc;
			summaryResult.addCostToRevenue(cost / hasGainRevenue); // ����˳��
            // ����ײ���������׼��
            summaryResult.addBandwidthStandardDiff(Utils.stanrdDiff(nums));
		}
		// ������ļ�
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
		filename = filename.replace("file", "output");
		String writeFileName = filename.substring(0, filename.lastIndexOf(".")) + "_" + fix + ".txt";
		summaryResult.writeToFile(writeFileName);
	}
	
	private void processEndList(int time, int inter) {
		for (int i = 0; i < inter; i++) {
			if (startList.get(i) == processed) {
                // ���ô���
                continue;
            }
			if (startList.get(i) >= time) {
				// ���账��, ��Ϊ���ʱ��������1 time unit
				break;
			}
			if (startList.get(i) + endList.get(i) <= time) {
				// �ڵ�ǰʱ��֮ǰ��Ҫ�ͷ�
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
