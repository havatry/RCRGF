package vnreal.algorithms.argf.adapter;

import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;

public class MyUndirectedGraph<V, E> extends UndirectedSparseMultigraph<V, E>{
	private static final long serialVersionUID = 1L;

	@Override
	public V getSource(E undirected_edge) {
		//Created method stubs
		return getEndpoints(undirected_edge).getFirst();
	}
	
	@Override
	public V getDest(E undirected_edge) {
		//Created method stubs
		return getEndpoints(undirected_edge).getSecond();
	}
}
