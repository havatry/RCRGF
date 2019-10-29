package vnreal.algorithms.rcrgf.core;

import java.util.Iterator;
import java.util.List;

import mulavito.algorithms.AbstractAlgorithmStatus;
import vnreal.algorithms.AbstractAlgorithm;
import vnreal.algorithms.rcrgf.util.Statistics;
import vnreal.network.Network;
import vnreal.network.NetworkStack;
import vnreal.network.substrate.SubstrateNetwork;
import vnreal.network.virtual.VirtualNetwork;

public class ARGFStackAlgorithm extends AbstractAlgorithm{
	private ARGFAlgorithm algorithm;
	private Iterator<VirtualNetwork> curIt = null;
	private Iterator<? extends Network<?, ?, ?>> curNetIt = null;
	private Statistics statistics = new Statistics();
	
	public ARGFStackAlgorithm(NetworkStack networkStack) {
		//Created constructor stubs
		this.algorithm = new ARGFAlgorithm();
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
		System.out.println("additional time: " + (System.currentTimeMillis() - start) + "ms");
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
				System.out.println("Mapped Not Success");
			} else {
				// 映射成功
				System.out.println("Mapped Success");
			}
		}
		statistics.setEndTime(System.currentTimeMillis());
	}

	@Override
	protected void postRun() {
		//Created method stubs
		// 打印statics
		System.out.println(statistics);
	}

}
