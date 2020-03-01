package vnreal.algorithms.myRCRGF.util;

import vnreal.algorithms.isomorphism.SubgraphIsomorphismAlgorithm;
import vnreal.algorithms.isomorphism.SubgraphIsomorphismStackAlgorithm;
import vnreal.network.NetworkStack;
import vnreal.network.virtual.VirtualNetwork;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SubgraphIsomorphismCompare extends SubgraphIsomorphismStackAlgorithm{
    private Statistics statistics = new Statistics();

    public SubgraphIsomorphismCompare(NetworkStack stack, SubgraphIsomorphismAlgorithm algorithm) {
        super(stack, algorithm);
    }

    @Override
    protected void evaluate() {
        boolean result = false;
        // Mapping previousResult = new Mapping();
        statistics.setStartTime(System.currentTimeMillis());
        while (hasNext()) {
            VirtualNetwork vNetwork = getNext();
            result = algorithm.mapNetwork(ns.getSubstrate(), vNetwork);
            statistics.setRevenToCost(
                    Utils.revenueToCostRation(algorithm.getNodeMapping(),
                     algorithm.getLinkMapping()));
        }
        if (result) {
            statistics.setSuccVns(1);
        } else {
            statistics.setSuccVns(0);
        }
        statistics.setEndTime(System.currentTimeMillis());
    }

    @Override
    protected void postRun() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(Constants.WRITE_FILE + "simulation.txt", true));
            out.print(statistics);
            out.print(",");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Statistics getStatistics() {
        return statistics;
    }
}
