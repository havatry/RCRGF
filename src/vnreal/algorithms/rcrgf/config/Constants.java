package vnreal.algorithms.rcrgf.config;

import java.io.PrintWriter;

public class Constants {
	public static boolean DIRECTED = false; // 设置图形界面显示无向图还是有向图
	public static boolean ADAPTE_UNDIRECTEDGRAPH = true; // 定制无向图类, 实现起点和终点的获取
	public static boolean ASSURE_UNIQUE = true; // 确保simpleDijkstra的起点和终点不相同
	public static boolean HIDDEN_RIGHT_TAB = true; // 隐藏图形界面中的映射和选择的选项卡
	public static String WRITE_FILE = "./results/file/"; // 将映射结果写入到文件中
	public static boolean PRINT_LINK_MAPPING_RESULT = false; // 打印链路映射的结果
	public static boolean SUBGRAPHISOMORPHISM_DEBUG = false; // 打印子图同构时候DEBUG的输出
	public static boolean SUBGRAPHISOMORPHISM_NORMAL = true; // 打印子图同构时候的正常输出结果
	public static boolean SKIP_XML_VALIDATE = true; // 跳过xml文件的校验
	public static boolean REACH_ASSURE = true; // 保证图的连通性
	public static String WRITE_RESOURCE = "./results/resources4/";
	public static PrintWriter out;
}
