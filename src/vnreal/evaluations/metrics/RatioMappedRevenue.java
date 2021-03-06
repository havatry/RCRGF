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

import vnreal.network.NetworkStack;

/**
 * 
 * Class to obtain the ratio of mapped revenue.
 * 
 * This metric measures the percentage of mapped revenue. It is calculated by
 * dividing the mapped revenue over the total revenue.
 * 
 * @author Juan Felipe Botero
 * @since 2011-03-08
 * 
 */

public class RatioMappedRevenue implements EvaluationMetric<NetworkStack> {

	boolean isPathSplitting;

	public RatioMappedRevenue(boolean isPsAlgorithm) {
		this.isPathSplitting = isPsAlgorithm;
	}

	@Override
	public double calculate(NetworkStack stack) {
		MappedRevenue rev = new MappedRevenue(isPathSplitting);
		TotalRevenue totalRev = new TotalRevenue(isPathSplitting);
		double revRatio = (rev.calculate(stack) / totalRev.calculate(stack)) * 100;
		return (revRatio);
	}

	@Override
	public String toString() {
		return "RatioMappedRevenue";
	}

}
