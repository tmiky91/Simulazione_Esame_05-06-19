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

	public String analizzaCrimini(Year anno) {
		String risultato="";
		EventsDao dao = new EventsDao();
		List<Distretto> distretti = dao.getDistretti(anno);
		for(Distretto d: distretti) {
			if(!grafo.containsVertex(d)) {
				grafo.addVertex(d);
			}
		}
		for(Distretto d: grafo.vertexSet()) {
			for(Distretto d1: grafo.vertexSet()) {
				if(!d.equals(d1)) {
					DefaultWeightedEdge edge = grafo.getEdge(d, d1);
					if(edge==null) {
						Graphs.addEdgeWithVertices(grafo, d, d1, LatLngTool.distance(d.getCoord(), d1.getCoord(), LengthUnit.KILOMETER));
					}
				}
			}
		}
		for(Distretto d: grafo.vertexSet()) {
			List<Distretto> vicini = Graphs.neighborListOf(grafo, d);
			Collections.sort(vicini, new Comparator<Distretto>() {

				@Override
				public int compare(Distretto o1, Distretto o2) {
					DefaultWeightedEdge edge1 = grafo.getEdge(d, o1);
					double peso1=grafo.getEdgeWeight(edge1);
					DefaultWeightedEdge edge2 = grafo.getEdge(d, o2);
					double peso2=grafo.getEdgeWeight(edge2);
					return (int) (peso1-peso2);
				}
			});
			for(Distretto d1: vicini) {
				DefaultWeightedEdge edge = grafo.getEdge(d, d1);
				risultato+="ID1: "+d.getId()+" ID2: "+d1.getId()+" Distanza: "+grafo.getEdgeWeight(edge)+"\n";
			}
		}
		return risultato;
	}
	
}
