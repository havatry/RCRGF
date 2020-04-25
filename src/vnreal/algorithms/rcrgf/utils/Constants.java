package vnreal.algorithms.rcrgf.utils;

import java.io.PrintWriter;

public class Constants {
	public static boolean DIRECTED = false; // 设置图形界面显示无向图还是有向图
	public static boolean ADAPTE_UNDIRECTEDGRAPH = true; // 定制无向图类, 实现起点和终点的获取
	public static boolean ASSURE_UNIQUE = true; // 确保simpleDijkstra的起点和终点不相同
	public static boolean HIDDEN_RIGHT_TAB = true; // 隐藏图形界面中的映射和选择的选项卡
	public static String WRITE_FILE = "./results/outputRcrgf/"; // 将映射结果写入到文件中
	public static String WRITE_RESOURCE = "./results/fileRcrgf/";
	public static PrintWriter out;
}
