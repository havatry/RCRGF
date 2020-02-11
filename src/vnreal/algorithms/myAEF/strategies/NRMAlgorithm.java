package vnreal.algorithms.myAEF.strategies;

import vnreal.algorithms.AlgorithmParameter;
import vnreal.algorithms.GenericMappingAlgorithm;
import vnreal.algorithms.linkmapping.kShortestPathLinkMapping;
import vnreal.algorithms.myAEF.strategies.aef.LinkMapping;
import vnreal.algorithms.myAEF.strategies.aef.NodeMapping;
import vnreal.algorithms.myAEF.strategies.nrm.NodeMappingNRM;

public class NRMAlgorithm extends GenericMappingAlgorithm {
    // Default values
    private static final boolean DEFAULT_OVERLOAD = false;
    private static final int DEFAULT_KSP = 1;
    private static final boolean DEFAULT_EPPSTEIN = true;
    private static final String DEFAULT_LINKMAP_ALGORITHM = "ksp";
    private static final int DEFAULT_SPL = 2;

    public NRMAlgorithm(AlgorithmParameter param) {
        boolean nodeOverload = param.getBoolean("overload", DEFAULT_OVERLOAD);
        nodeMappingAlgorithm = new NodeMappingNRM(nodeOverload);

        if (param.getString("linkMapAlgorithm", DEFAULT_LINKMAP_ALGORITHM).equals("ksp")) {
            int k = param.getInteger("ksp", DEFAULT_KSP);
            boolean eppstein = param.getBoolean("eppstein", DEFAULT_EPPSTEIN);
            linkMappingAlgorithm = new kShortestPathLinkMapping(k, eppstein);
        } else {
            linkMappingAlgorithm = new LinkMapping();
        }
    }
}

