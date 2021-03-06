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
package vnreal.evaluations.metrics;

import vnreal.algorithms.utils.SubgraphBasicVN.Utils;
import vnreal.network.NetworkStack;
import vnreal.network.substrate.SubstrateLink;
import vnreal.network.substrate.SubstrateNetwork;

/**
 * This metrics calculates the maximum link stress after the mapping. The link
 * stress, of a substrate link, is the number of virtual links that are mapped
 * on top of this substrate link.
 * 
 * 
 */

public final class MaxLinkStress implements EvaluationMetric<NetworkStack> {
	public double calculate(NetworkStack stack) {
		SubstrateNetwork sNetwork = stack.getSubstrate();

		int max = 0;
		for (SubstrateLink sl : sNetwork.getEdges()) {
			int stress = Utils.getStressLevel(sl);
			if (stress > max) {
				max = stress;
			}
		}

		return max;
	}

	public String toString() {
		return "MaxLinkStress";
	}

}
