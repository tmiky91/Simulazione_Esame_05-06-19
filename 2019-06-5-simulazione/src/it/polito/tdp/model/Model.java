package it.polito.tdp.model;

import java.time.Year;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.db.EventsDao;

public class Model {
	
	private SimpleWeightedGraph<Distretto, DefaultWeightedEdge> grafo;
	
	public Model() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	}

	public List<Year> getAnni() {
		EventsDao dao = new EventsDao();
		return dao.getAnni();
	}

	public String getVicini(Year anno) {
		String risultato="";
		EventsDao dao = new EventsDao();
		List<Distretto> distretti = dao.getDistretti(anno);
		for(Distretto d: distretti) {
			if(!grafo.containsVertex(d)) {
				grafo.addVertex(d);
			}
		}
		for(Distretto d1: grafo.vertexSet()) {
			for(Distretto d2: grafo.vertexSet()) {
				if(!d1.equals(d2)) {
					DefaultWeightedEdge edge = grafo.getEdge(d1, d2);
					if(edge==null) {
						Graphs.addEdgeWithVertices(grafo, d1, d2, LatLngTool.distance(d1.getLatLon(), d2.getLatLon(), LengthUnit.KILOMETER));
					}
				}
			}
		}
		for(Distretto d: grafo.vertexSet()) {
			List<Distretto> vicini = Graphs.neighborListOf(grafo, d);
			Collections.sort(vicini, new Comparator<Distretto>() {

				@Override
				public int compare(Distretto o1, Distretto o2) {
					DefaultWeightedEdge edge1 = grafo.getEdge(o1, d);
					double peso1 = grafo.getEdgeWeight(edge1);
					DefaultWeightedEdge edge2 = grafo.getEdge(o2, d);
					double peso2 = grafo.getEdgeWeight(edge2);
					return (int) -(peso2-peso1);
				}
			});
			for(Distretto d1: vicini) {
				DefaultWeightedEdge edge = grafo.getEdge(d, d1);
				risultato+=d1.getId()+" "+grafo.getEdgeWeight(edge)+"\n";
			}
		}
		return risultato;
	}
	
}
