package vnreal.algorithms.myrcrgf.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 从配置文件中读取变量 设置
 * 2019年11月23日 下午9:50:04
 */
public class Constants {
	// 距离约束D
	public static final Properties PROPERTIES;
	static {
		// 从配置文件中读取
		PROPERTIES = new Properties();
		try {
			PROPERTIES.load(new FileInputStream("results/setting/config.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static final boolean DIRECTED = false; // 无向图
	public static final boolean ASSURE_UNIQUE = false; // sample dijkstra
	public static final boolean ADAPTE_UNDIRECTEDGRAPH = true; // 定制无向图
	public static final boolean HIDDEN_RIGHT_TAB = true; // 和ui有关
	
}
