package vnreal.algorithms.myRCRGF.util;

import vnreal.algorithms.GenericMappingAlgorithm;
import vnreal.network.virtual.VirtualNetwork;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GenericMappingAlgorithmCompare extends GenericMappingAlgorithm{
    private Statistics statistics = new Statistics();

    @Override
    protected void postRun() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(Constants.WRITE_FILE + "simulation.txt", true));
            out.print(statistics);
            out.println();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean process(VirtualNetwork p) {
        // Node mapping stage
        statistics.setStartTime(System.currentTimeMillis());
        if (nodeMappingAlgorithm.isPreNodeMappingFeasible(ns.getSubstrate(), p)) {
            if (!nodeMappingAlgorithm.isPreNodeMappingComplete()) {
                if (!nodeMappingAlgorithm.nodeMapping(ns.getSubstrate(), p)) {
                    ns.clearVnrMappings(p);
                    processedLinks += p.getEdges().size();
                    return true;
                }
            }
        } else {
            ns.clearVnrMappings(p);
            processedLinks += p.getEdges().size();
            return true;
        }
        // Link Mapping stage
        if (!linkMappingAlgorithm.linkMapping(ns.getSubstrate(), p, nodeMappingAlgorithm
                .getNodeMapping())) {
            ns.clearVnrMappings(p);
        } else {
                mappedLinks += linkMappingAlgorithm.getMappedLinks();
        }
        statistics.setRevenToCost(Utils.revenueToCostRation(nodeMappingAlgorithm.getNodeMapping(), linkMappingAlgorithm.getLinkMapping()));
        statistics.setSuccVns(1);
        processedLinks += linkMappingAlgorithm.getProcessedLinks();
        statistics.setEndTime(System.currentTimeMillis());
        return true;
    }
}
