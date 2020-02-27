package vnreal.algorithms.myRCRGF.util;

import vnreal.algorithms.AlgorithmParameter;
import vnreal.algorithms.GenericMappingAlgorithmCompare;
import vnreal.algorithms.linkmapping.PathSplittingVirtualLinkMapping;
import vnreal.algorithms.linkmapping.kShortestPathLinkMapping;
import vnreal.algorithms.nodemapping.AvailableResourcesNodeMapping;
import vnreal.algorithms.utils.MiscelFunctions;
import vnreal.network.NetworkStack;

public class AvailableResourcesCompare extends GenericMappingAlgorithmCompare {
    // Default values
    private static int DEFAULT_DIST = -1; // No distance calculation
    private static boolean DEFAULT_OVERLOAD = false; // No node overload
    private static boolean DEFAULT_PS = false; // No path splitting
    private static double DEFAULT_WCPU = 1;
    private static double DEFAULT_WBW = 1;
    private static int DEFAULT_KSP = 1;
    private static boolean DEFAULT_EPPSTEIN = true;

    /**
     * Constructor of the algorithm
     *
     * "distance" (int) - The maximum distance within which to search for substrate nodes
     * (set to a negative value to disable) <br />
     * "overload" (boolean) - Indicate, whether a substrate node may host more than one
     * node of each VirtualNetwork <br />
     * "PathSplitting" (boolean) - Indicate, whether link mapping should allow virtual
     * links to be split over several paths <br />
     * "weightCpu" (double) - The weight of CPU resources <br />
     * "weightBw" (double) - The weight of BW resources <br />
     * "kShortestPath" (int) - The number k of shortest paths to compute
     *
     */
    public AvailableResourcesCompare(AlgorithmParameter param) {
        int distance = param.getInteger("distance", DEFAULT_DIST);
        boolean nodeOverload = param.getBoolean("overload", DEFAULT_OVERLOAD);
        this.nodeMappingAlgorithm = new AvailableResourcesNodeMapping(distance, nodeOverload);

        if (param.getBoolean("PathSplitting", DEFAULT_PS)) {
            double weightCpu = param.getDouble("weightCpu", DEFAULT_WCPU);
            double weightBw = param.getDouble("weightBw", DEFAULT_WBW);
            this.linkMappingAlgorithm = new PathSplittingVirtualLinkMapping(weightCpu, weightBw);
        } else {
            int k = param.getInteger("kShortestPaths", DEFAULT_KSP);
            boolean eppstein = param.getBoolean("eppstein", DEFAULT_EPPSTEIN);
            this.linkMappingAlgorithm = new kShortestPathLinkMapping(k, eppstein);
        }
    }

    @Override
    public void setStack(NetworkStack stack) {
        this.ns = MiscelFunctions.sortByRevenues(stack);
    }
}
