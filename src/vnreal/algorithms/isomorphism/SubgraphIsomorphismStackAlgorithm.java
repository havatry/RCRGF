/* ***** BEGIN LICENSE BLOCK *****
 * Copyright (C) 2010-2011, The VNREAL Project Team.
 * 
 * This work has been funded by the European FP7
 * Network of Excellence "Euro-NF" (grant agreement no. 216366)
 * through the Specific Joint Developments and Experiments Project
 * "Virtual Network Resource Embedding Algorithms" (VNREAL). 
 *
 * The VNREAL Project Team consists of members from:
 * - University of Wuerzburg, Germany
 * - Universitat Politecnica de Catalunya, Spain
 * - University of Passau, Germany
 * See the file AUTHORS for details and contact information.
 * 
 * This file is part of ALEVIN (ALgorithms for Embedding VIrtual Networks).
 *
 * ALEVIN is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License Version 3 or later
 * (the "GPL"), or the GNU Lesser General Public License Version 3 or later
 * (the "LGPL") as published by the Free Software Foundation.
 *
 * ALEVIN is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * or the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License and
 * GNU Lesser General Public License along with ALEVIN; see the file
 * COPYING. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */
package vnreal.algorithms.isomorphism;

import mulavito.algorithms.AbstractAlgorithmStatus;
import vnreal.algorithms.AbstractAlgorithm;
import vnreal.algorithms.AlgorithmParameter;
import vnreal.algorithms.isomorphism.SubgraphIsomorphismAlgorithm.MappingCandidate;
import vnreal.algorithms.rcrgf.utils.Util;
import vnreal.algorithms.utils.SubgraphBasicVN.NodeLinkMapping;
import vnreal.network.Network;
import vnreal.network.NetworkStack;
import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNode;
import vnreal.network.virtual.VirtualLink;
import vnreal.network.virtual.VirtualNetwork;
import vnreal.network.virtual.VirtualNode;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * See
 * "A Virtual Network Mapping Algorithm based on Subgraph Isomorphism Detection"
 * .
 * 
 * epsilon ~ 10 omega ~ 4 * |VN|
 * 
 * @author Michael Till Beck
 */
public class SubgraphIsomorphismStackAlgorithm extends AbstractAlgorithm {
	public static final boolean debug = false;
	
	protected SubgraphIsomorphismAlgorithm algorithm;

	private Iterator<VirtualNetwork> curIt = null;
	private Iterator<? extends Network<?, ?, ?>> curNetIt = null;
	private long executionTime;
	private boolean succ;
//	private Statistics statistics = new Statistics();
	
	public SubgraphIsomorphismStackAlgorithm(AlgorithmParameter params) {
		if (params.getBoolean("Advanced", true)) {
			this.algorithm = new AdvancedSubgraphIsomorphismAlgorithm();
		} else {
			this.algorithm = new SubgraphIsomorphismAlgorithm();
		}
		
	}
	
	public SubgraphIsomorphismStackAlgorithm(NetworkStack stack,
			SubgraphIsomorphismAlgorithm algorithm) {

		this.ns = stack;
		this.algorithm = algorithm;
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
	protected void evaluate() {
		reset();
		long start = System.currentTimeMillis();
		// Mapping previousResult = new Mapping();
		while (hasNext()) {
			VirtualNetwork vNetwork = getNext();
			succ = algorithm.mapNetwork(ns.getSubstrate(), vNetwork);
		}
		executionTime = System.currentTimeMillis() - start;
	}

	@Override
	protected void postRun() {
	}

	@Override
	protected boolean preRun() {
		return true;
	}

	@Override
	public List<AbstractAlgorithmStatus> getStati() {
		LinkedList<AbstractAlgorithmStatus> stati = new LinkedList<AbstractAlgorithmStatus>();
		
		stati.add(new AbstractAlgorithmStatus("Mapped VN links") {
			@Override
			public Integer getValue() {
				return succ ? 1 : 0;
			}

			@Override
			public Integer getMaximum() {
				return 1;
			}
		});
		stati.add(new AbstractAlgorithmStatus("Execution Time") {
			
			@Override
			public Long getValue() {
				// TODO Auto-generated method stub
				return executionTime;
			}
			
			@Override
			public Long getMaximum() {
				// TODO Auto-generated method stub
				return 1L;
			}
		});
		stati.add(new AbstractAlgorithmStatus("Revenue") {
			
			@Override
			public Double getValue() {
				// TODO Auto-generated method stub
				double nodeRevenue = 0.0, linkRevenue = 0.0;
				for (VirtualNode vn : ns.getVirtuals().get(0).getVertices()) {
					nodeRevenue += Util.getCpu(vn);
				}
				for (VirtualLink vl : ns.getVirtuals().get(0).getEdges()) {
					linkRevenue += Util.getBandwidth(vl);
				}
				return nodeRevenue + linkRevenue;
			}
			
			@Override
			public Double getMaximum() {
				// TODO Auto-generated method stub
				return 1.0;
			}
		});

		return stati;
	}

	static class MappingState {
		NodeLinkMapping m;
		MappingCandidate<VirtualNode, SubstrateNode> c;

		public MappingState(NodeLinkMapping m,
				MappingCandidate<VirtualNode, SubstrateNode> c) {
			this.m = m;
			this.c = c;
		}
	}
	
	private void reset() {
		curNetIt = null;
	}

	public Map<VirtualNode, SubstrateNode> getNodeMapping() {
	    return algorithm.getNodeMapping();
    }

    public Map<VirtualLink, List<SubstrateLink>> getLinkMapping() {
	    return algorithm.getLinkMapping();
    }
}
