//package vnreal.algorithms.myrcrgf.util;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Properties;
//
//import vnreal.constraints.demands.BandwidthDemand;
//import vnreal.constraints.demands.CpuDemand;
//import vnreal.constraints.resources.BandwidthResource;
//import vnreal.constraints.resources.CpuResource;
//import vnreal.io.XMLExporter;
//import vnreal.network.NetworkStack;
//import vnreal.network.virtual.VirtualNetwork;
//import vnreal.network.virtual.VirtualNode;
//import vnreal.ui.dialog.ConstraintsGeneratorDialog;
//import vnreal.ui.dialog.ScenarioWizard;
// 
// 
//public class GenerateTopology {
//    public void write() throws FileNotFoundException, IOException {
//        String filename = "topology_" + snodes + "_" + vnodes + ".xml";
//        generateTopology(filename);
//    }
//    
//    private int[] virtualNodesArray() {
//        int[] array = new int[virtualNetworks];
//        for (int i = 0; i < virtualNetworks; i++) {
//            array[i] = vnodes;
//        }
//        return array;
//    }
//    
//    private double[] virtualAlphaArray() {
//        double[] arrray = new double[virtualNetworks];
//        for (int i = 0; i < virtualNetworks; i++) {
//            arrray[i] = valpha;
//        }
//        return arrray;
//    }
//    
//    private double[] virtualBetaArray() {
//        double[] arrray = new double[virtualNetworks];
//        for (int i = 0; i < virtualNetworks; i++) {
//            arrray[i] = 0.5;
//        }
//        return arrray;
//    }
//    
//    public void generateTopology(Properties properties) throws FileNotFoundException, IOException {
//        NetworkStack networkStack = null;
//        while (true) {
//            networkStack = ScenarioWizard.generateTopology(snodes, salpha, 0.5, virtualNetworks, virtualNodesArray(),
//                    virtualAlphaArray(), virtualBetaArray());
//            if (Utils.complete((VirtualNode)networkStack.getLayer(1).getVertices().iterator().next(), 
//                    (VirtualNetwork)networkStack.getLayer(1))) {
//                break;
//            }
//        }
//        List<Class<?>> resClassesToGenerate = new LinkedList<Class<?>>();
//        List<String[]> resParamNamesToGenerate = new LinkedList<String[]>();
//        List<String[]> resMaxValues = new ArrayList<String[]>();
//        resClassesToGenerate.add(CpuResource.class);
//        resClassesToGenerate.add(BandwidthResource.class);
//        resParamNamesToGenerate.add(new String[]{"cycles"});
//        resParamNamesToGenerate.add(new String[]{"bandwidth"});
//        resMaxValues.add(new String[]{"" + cpu_resource});
//        resMaxValues.add(new String[]{"" + bandwith_resource});
//        ConstraintsGeneratorDialog.generateConstraintsSubstrate(resClassesToGenerate, resParamNamesToGenerate, resMaxValues, networkStack);
//        List<List<Class<?>>> resClassesToGenerate_vn_all = new LinkedList<>();
//        List<List<String[]>> resParamNamesToGenerate_vn_all = new LinkedList<>();
//        List<List<String[]>> resMaxValues_vn_all = new ArrayList<>();
//        for (int i = 0; i < virtualNetworks; i++) {
//            List<Class<?>> resClassesToGenerate_vn = new LinkedList<>();
//            List<String[]> resParamNamesToGenerate_vn = new LinkedList<>();
//            List<String[]> resMaxValues_vn = new ArrayList<>();
//            resClassesToGenerate_vn.add(CpuDemand.class);
//            resClassesToGenerate_vn.add(BandwidthDemand.class);
//            resParamNamesToGenerate_vn.add(new String[]{"demandedCycles"});
//            resParamNamesToGenerate_vn.add(new String[]{"demandedBandwidth"});
//            resMaxValues_vn.add(new String[]{"" + (int)(cpu_resource * ration)});;
//            resMaxValues_vn.add(new String[]{"" + (int)(bandwith_resource * ration)});
//            resClassesToGenerate_vn_all.add(resClassesToGenerate_vn);
//            resParamNamesToGenerate_vn_all.add(resParamNamesToGenerate_vn);
//            resMaxValues_vn_all.add(resMaxValues_vn);
//        }
//        ConstraintsGeneratorDialog.generateConstraintsVirtual(resClassesToGenerate_vn_all,
//                resParamNamesToGenerate_vn_all, resMaxValues_vn_all, networkStack);
//        XMLExporter.exportStack(Constants.WRITE_RESOURCE + filename, networkStack);
////        PrintWriter out = new PrintWriter(Constants.WRITE_RESOURCE + logname);
////        out.println(toString());
////        out.close();
//    }
// 
//    @Override
//    public String toString() {
//        return null;
//    }
//    
//    public static void main(String[] args) throws FileNotFoundException, IOException {
//        GenerateTopology generateTopology = new GenerateTopology();
//        generateTopology.write();
//    }
//}
