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
package vnreal.ui.transformer;

import org.apache.commons.collections15.Transformer;

import vnreal.constraints.AbstractConstraint;
import vnreal.network.Link;
import vnreal.network.virtual.VirtualLink;

public final class LinkToolTipTransformer<E extends Link<? extends AbstractConstraint>>
		implements Transformer<E, String> {
	private boolean enabled = false;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String transform(E e) {
		if (enabled) {
			StringBuilder labelSb = new StringBuilder();
			labelSb.append("<html>" + e.toString() + "<ul>");
			for (AbstractConstraint c : e.get()) {
				labelSb.append("<li>");
				labelSb.append(c.toString());
				labelSb.append("</li>");
			}
			if (e.getClass().equals(VirtualLink.class))
				for (AbstractConstraint hh : ((VirtualLink) e)
						.getHiddenHopDemands()) {
					labelSb.append("<li>HH ");
					labelSb.append(hh.toString());
					labelSb.append("</li>");
				}
			labelSb.append("</ul></html>");
			return labelSb.toString();
		} else
			return null;
	}

}
