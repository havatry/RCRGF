package vnreal.algorithms.myRCRGF.util;

import java.io.PrintWriter;

public class Constants {
	public static boolean DIRECTED = false; // ����ͼ�ν�����ʾ����ͼ��������ͼ
	public static boolean ADAPTE_UNDIRECTEDGRAPH = true; // ��������ͼ��, ʵ�������յ�Ļ�ȡ
	public static boolean ASSURE_UNIQUE = true; // ȷ��simpleDijkstra�������յ㲻��ͬ
	public static boolean HIDDEN_RIGHT_TAB = true; // ����ͼ�ν����е�ӳ���ѡ���ѡ�
	public static String WRITE_FILE = "./results/outputRcrgf/"; // ��ӳ����д�뵽�ļ���
	public static boolean PRINT_LINK_MAPPING_RESULT = false; // ��ӡ��·ӳ��Ľ��
	public static boolean SUBGRAPHISOMORPHISM_DEBUG = false; // ��ӡ��ͼͬ��ʱ��DEBUG�����
	public static boolean SUBGRAPHISOMORPHISM_NORMAL = true; // ��ӡ��ͼͬ��ʱ�������������
	public static boolean SKIP_XML_VALIDATE = true; // ����xml�ļ���У��
	public static boolean REACH_ASSURE = true; // ��֤ͼ����ͨ��
	public static String WRITE_RESOURCE = "./results/fileRcrgf/";
	public static PrintWriter out;
}
