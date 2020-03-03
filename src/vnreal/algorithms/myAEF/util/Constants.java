package vnreal.algorithms.myAEF.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * �������ļ��ж�ȡ���� ����
 * 2019��11��23�� ����9:50:04
 */
public class Constants {
	public static final boolean DIRECTED = false; // ����ͼ
	public static final boolean ASSURE_UNIQUE = false; // sample dijkstra
	public static final boolean ADAPTE_UNDIRECTEDGRAPH = true; // ��������ͼ
	public static final boolean HIDDEN_RIGHT_TAB = true; // ��ui�й�
	public static double SUBSTRATE_BASE_CPU_RESOURCE = 20.0; // ��׼�ײ�cpu��Դ
	public static double SUBSTRATE_BASE_BANDWITH_RESOURCE = 20.0; // ��׼�ײ�bandwith��Դ
	public static double VIRTUAL_BASE_CPU_RESOURCE = 5.0; // ��׼����cpu����
	public static double VIRTUAL_BASE_BANDWITH_RESOURCE = 5.0; // ��׼����bandwith����
	public static boolean SWITCH_BASE_RES_DEM = true; // �Ƿ����ӻ�׼��Դȥ����Լ��
	public static final String FILE_NAME = "results/file/substratework_" + 
			new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xml";
	public static String resultDir = "results/output";
	public static final Double C = 700D; // ������������ϵ��
    public static final Double BIG_NUM = 1000_000_000D; // ������·������ʱ���ֵ
}
