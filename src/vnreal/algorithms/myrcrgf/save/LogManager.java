package vnreal.algorithms.myrcrgf.save;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对日志输出做一层封装
 * 2019年11月17日 下午8:32:05
 */
public class LogManager {
	public static Logger logger = LoggerFactory.getLogger(Logger.class);
	
	public static void info(String content, Object... objects) {
		logger.info(content, objects);
	}
	
	public static void debug(String content, Object... objects) {
		logger.debug(content, objects);
	}
}
