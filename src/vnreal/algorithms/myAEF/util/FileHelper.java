package vnreal.algorithms.myAEF.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import vnreal.io.XMLExporter;
import vnreal.io.XMLImporter;
import vnreal.network.NetworkStack;

/**
 * 生成的文件加以保存
 * @author hudedong
 *
 */
public class FileHelper {
	public static void writeToXml(String filename, NetworkStack networkStack) {
		XMLExporter.exportStack(filename, networkStack);
	}
	
	public static NetworkStack readFromXml(String filename) {
		return XMLImporter.importScenario(filename).getNetworkStack();
	}
	
	public static String saveContext(String filename, NetworkStack networkStack, List<Integer> startList, List<Integer> endList) {
		// 将虚拟网络包装成networkStack
		// 将开始时间和结束时间列表保存下来
		writeToXml(filename, networkStack);
		String filename_aux = filename.substring(0,  filename.lastIndexOf(".")) + "_aux.txt";
		try {
			PrintWriter out = new PrintWriter(filename_aux);
			for (Integer inte : startList) {
				out.print(inte);
				out.print(" ");
			}
			out.println();
			for (Integer inte2 : endList) {
				out.print(inte2);
				out.print(" ");
			}
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filename_aux;
	}
	
	public static Object[] readContext(String filename) {
		NetworkStack networkStack = readFromXml(filename);
		File f = new File(filename.substring(0, filename.lastIndexOf(".")) + "_aux.txt");
		if (!f.exists()) {
            return new Object[]{networkStack};
        }
		List<Integer> startList = new ArrayList<Integer>();
		List<Integer> endList = new ArrayList<Integer>();
		try {
			Scanner in = new Scanner(f);
			String[] part = in.nextLine().split(" ");
			for (String p : part) {
				startList.add(Integer.parseInt(p));
			}
			part = in.nextLine().split(" ");
			for (String p2 : part) {
				endList.add(Integer.parseInt(p2));
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Object[] {networkStack, startList, endList};
	}
}
