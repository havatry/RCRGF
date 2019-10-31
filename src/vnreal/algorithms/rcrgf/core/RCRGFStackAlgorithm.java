package vnreal.algorithms.rcrgf.core;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import mulavito.algorithms.AbstractAlgorithmStatus;
import vnreal.algorithms.AbstractAlgorithm;
import vnreal.algorithms.rcrgf.config.Constants;
import vnreal.algorithms.rcrgf.util.Statistics;
import vnreal.algorithms.rcrgf.util.Utils;
import vnreal.network.Network;
import vnreal.network.NetworkStack;
import vnreal.network.virtual.VirtualNetwork;

public class RCRGFStackAlgorithm extends AbstractAlgorithm{
	private RCRGFAlgorithm algorithm;
	private Iterator<VirtualNetwork> curIt = null;
	private Iterator<? extends Network<?, ?, ?>> curNetIt = null;
	private Statistics statistics = new Statistics();
	
	public RCRGFStackAlgorithm(NetworkStack networkStack) {
		//Created constructor stubs
		this.algorithm = new RCRGFAlgorithm();
		this.ns = networkStack;
	}
	
	@SuppressWarnings("unchecked")
	protected boolean hasNext() {
		if (curNetIt == null) {
			curNetIt = ns.iterator();
		}
		if (curIt == null || !curIt.hasNext()) {
			if (curNetIt.hasNext()) {
				@SuppressWarnings("unused")
				Network<?, ?, ?> tmp = curNetIt.next();

				curIt = (Iterator<VirtualNetwork>) curNetIt;
				return hasNext();
			} else
				return false;
		} else
			return true;
	}

	protected VirtualNetwork getNext() {
		if (!hasNext())
			return null;
		else {
			return curIt.next();
		}
	}
	
	@Override
	public List<AbstractAlgorithmStatus> getStati() {
		//Created method stubs
		return null;
	}

	@Override
	protected boolean preRun() {
		long start = System.currentTimeMillis();
		//Created method stubs
		for (VirtualNetwork vn : ns.getVirtuals()) {
			RemoveEdge removeEdge = new RemoveEdge(vn);
			if (!removeEdge.work()) {
				return false;
			}
		}
//		System.out.println("additional time: " + (System.currentTimeMillis() - start) + "ms");
		return true;
	}

	@Override
	protected void evaluate() {
		//Created method stubs
		statistics.setStartTime(System.currentTimeMillis());
		while (hasNext()) {
			VirtualNetwork virtualNetwork = getNext();
			boolean result = algorithm.compute(ns.getSubstrate(), virtualNetwork);
			if (!result) {
				// 未映射成功
				statistics.setSuccVns(0);
			} else {
				// 映射成功
				statistics.setSuccVns(1);
			}
		}
		statistics.setRevenToCost(
				Utils.revenueToCostRation(algorithm.getNodeMapping(), 
						algorithm.getLinkMapping()));
		statistics.setEndTime(System.currentTimeMillis());
	}

	@Override
	protected void postRun() {
		//Created method stubs
		// 打印statics
		try {
			PrintWriter out = new PrintWriter(new FileWriter(Constants.WRITE_FILE + "simulation.txt", true));
			out.print(statistics);
			out.print(",");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
