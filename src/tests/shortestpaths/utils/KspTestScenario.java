/* ***** BEGIN LICENSE BLOCK *****
 * Copyright (C) 2008-2011, The 100GET-E3-R3G Project Team.
 * 
 * This work has been funded by the Federal Ministry of Education
 * and Research of the Federal Republic of Germany
 * (BMBF F枚rderkennzeichen 01BP0775). It is part of the EUREKA project
 * "100 Gbit/s Carrier-Grade Ethernet Transport Technologies
 * (CELTIC CP4-001)". The authors alone are responsible for this work.
 *
 * See the file AUTHORS for details and contact information.
 * 
 * This file is part of MuLaViTo (Multi-Layer Visualization Tool).
 *
 * MuLaViTo is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License Version 3 or later
 * (the "GPL"), or the GNU Lesser General Public License Version 3 or later
 * (the "LGPL") as published by the Free Software Foundation.
 *
 * MuLaViTo is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * or the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License and
 * GNU Lesser General Public License along with MuLaViTo; see the file
 * COPYING. If not, see <http://www.gnu.org/licenses/>.
 *
 * ***** END LICENSE BLOCK ***** */
package tests.shortestpaths.utils;

import java.util.List;

import edu.uci.ics.jung.graph.Graph;

/**
 * @author Michael Duelli
 * @since 2010-11-29
 */
public final class KspTestScenario<V, E> {
	private final Graph<V, E> graph;
	private final List<List<E>> solutions;
	private final V source;
	private final V target;

	public KspTestScenario(Graph<V, E> graph, List<List<E>> solutions,
			V source, V target) {
		this.graph = graph;
		this.solutions = solutions;
		this.source = source;
		this.target = target;
	}

	public Graph<V, E> getGraph() {
		return graph;
	}

	public List<List<E>> getSolutions() {
		return solutions;
	}

	public V getSource() {
		return source;
	}

	public V getTarget() {
		return target;
	}
}
