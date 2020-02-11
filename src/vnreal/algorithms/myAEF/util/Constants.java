package vnreal.algorithms.myAEF.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 从配置文件中读取变量 设置
 * 2019年11月23日 下午9:50:04
 */
public class Constants {
	public static final boolean DIRECTED = false; // 无向图
	public static final boolean ASSURE_UNIQUE = false; // sample dijkstra
	public static final boolean ADAPTE_UNDIRECTEDGRAPH = true; // 定制无向图
	public static final boolean HIDDEN_RIGHT_TAB = true; // 和ui有关
	public static double SUBSTRATE_BASE_CPU_RESOURCE = 20.0; // 基准底层cpu资源
	public static double SUBSTRATE_BASE_BANDWITH_RESOURCE = 20.0; // 基准底层bandwith资源
	public static double VIRTUAL_BASE_CPU_RESOURCE = 5.0; // 基准虚拟cpu需求
	public static double VIRTUAL_BASE_BANDWITH_RESOURCE = 5.0; // 基准虚拟bandwith需求
	public static boolean SWITCH_BASE_RES_DEM = true; // 是否增加基准资源去生成约束
	public static final String FILE_NAME = "results/file/substratework_" + 
			new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xml";
}
