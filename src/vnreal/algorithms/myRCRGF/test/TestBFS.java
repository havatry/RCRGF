package vnreal.algorithms.myRCRGF.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import vnreal.algorithms.myRCRGF.util.Constants;
import vnreal.algorithms.myRCRGF.core.RemoveEdge;
import vnreal.constraints.demands.BandwidthDemand;
import vnreal.constraints.demands.CpuDemand;
import vnreal.constraints.resources.BandwidthResource;
import vnreal.constraints.resources.CpuResource;
import vnreal.core.Scenario;
import vnreal.io.XMLExporter;
import vnreal.io.XMLImporter;
import vnreal.network.NetworkStack;
import vnreal.network.virtual.VirtualNetwork;
import vnreal.ui.dialog.ConstraintsGeneratorDialog;
import vnreal.ui.dialog.ScenarioWizard;

/**
 * 测试BFS的一些功能类
 * 2019年10月24日 下午1:21:28
 */
public class TestBFS {
	private NetworkStack networkStack;
	private String filename = "serial3.xml";
	private boolean SKIP_TEST = true;
	
	@Before
	public void generateTopology() throws FileNotFoundException, IOException {
		if (Files.exists(Paths.get(Constants.WRITE_FILE + filename))) {
			readFromFile();
			return;
		}
		// 生成底层网络
		NetworkStack networkStack = ScenarioWizard.generateTopology(150, 1.0, 0.5, 1, new int[]{50}, new double[]{1.0}, new double[]{0.5});
		// 为底层网络生成约束
		List<Class<?>> resClassesToGenerate = new LinkedList<Class<?>>();
		List<String[]> resParamNamesToGenerate = new LinkedList<String[]>();
		List<String[]> resMaxValues = new ArrayList<String[]>();
		// 添加cpu属性 带宽属性
		resClassesToGenerate.add(CpuResource.class);
		resClassesToGenerate.add(BandwidthResource.class);
		resParamNamesToGenerate.add(new String[]{"cycles"});
		resParamNamesToGenerate.add(new String[]{"bandwidth"});
		resMaxValues.add(new String[]{"2000"});
		resMaxValues.add(new String[]{"2000"});
		ConstraintsGeneratorDialog.generateConstraintsSubstrate(resClassesToGenerate, resParamNamesToGenerate, resMaxValues, networkStack);
		// 为虚拟网络生成约束
		List<List<Class<?>>> resClassesToGenerate_vn_all = new LinkedList<>();
		List<List<String[]>> resParamNamesToGenerate_vn_all = new LinkedList<>();
		List<List<String[]>> resMaxValues_vn_all = new ArrayList<>();
		List<Class<?>> resClassesToGenerate_vn = new LinkedList<>();
		List<String[]> resParamNamesToGenerate_vn = new LinkedList<>();
		List<String[]> resMaxValues_vn = new ArrayList<>();
		// 添加cpu需求 带宽需求
		resClassesToGenerate_vn.add(CpuDemand.class);
		resClassesToGenerate_vn.add(BandwidthDemand.class);
		resParamNamesToGenerate_vn.add(new String[]{"demandedCycles"});
		resParamNamesToGenerate_vn.add(new String[]{"demandedBandwidth"});
		resMaxValues_vn.add(new String[]{"2"});
		resMaxValues_vn.add(new String[]{"2"});
		resClassesToGenerate_vn_all.add(resClassesToGenerate_vn);
		resParamNamesToGenerate_vn_all.add(resParamNamesToGenerate_vn);
		resMaxValues_vn_all.add(resMaxValues_vn);
		ConstraintsGeneratorDialog.generateConstraintsVirtual(resClassesToGenerate_vn_all,
				resParamNamesToGenerate_vn_all, resMaxValues_vn_all, networkStack);
		writeToFile(networkStack);
		System.out.println("write finish");
	}
	
	// 测试BFS的搜索功能有效
	@Ignore
	public void test01() {
	}
	
	private void writeToFile(NetworkStack networkStack) throws FileNotFoundException, IOException {
		XMLExporter.exportStack(Constants.WRITE_FILE + filename, 
				networkStack);
		this.networkStack = networkStack;
	}
	
	private void readFromFile() {
		Scenario scenario = XMLImporter.importScenario(Constants.WRITE_FILE + filename);
		networkStack = scenario.getNetworkStack();
	}
	
	@Ignore
	public void test02() {
	}
	
	@Test
	public void test03() {
		if (SKIP_TEST) {
			return;
		}
		// 测试减边操作
		// 删边之前的网络
		System.out.println(networkStack.getLayer(1));
		// 开始删边
		RemoveEdge removeEdge = new RemoveEdge((VirtualNetwork) networkStack.getLayer(1));
		removeEdge.work();
		System.out.println(networkStack.getLayer(1));
	}
}
