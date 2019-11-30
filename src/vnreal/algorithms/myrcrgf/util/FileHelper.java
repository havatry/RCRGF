package vnreal.algorithms.myrcrgf.util;

import vnreal.io.XMLExporter;
import vnreal.io.XMLImporter;
import vnreal.network.NetworkStack;
import vnreal.network.substrate.SubstrateNetwork;

/**
 * 生成的文件加以保存
 * @author hudedong
 *
 */
public class FileHelper {
	public static void writeToXml(String filename, NetworkStack networkStack) {
		XMLExporter.exportStack(filename, networkStack);
	}
	
	public static SubstrateNetwork readFromXml(String filename) {
		return XMLImporter.importScenario(filename).getSubstrate();
	}
}
