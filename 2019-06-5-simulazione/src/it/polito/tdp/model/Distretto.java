package it.polito.tdp.model;

import com.javadocmd.simplelatlng.LatLng;

public class Distretto {
	
	private int id;
	private LatLng latLon;
	
	public Distretto(int id, LatLng latLon) {
		super();
		this.id = id;
		this.latLon = latLon;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LatLng getLatLon() {
		return latLon;
	}
	public void setLatLon(LatLng latLon) {
		this.latLon = latLon;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Distretto other = (Distretto) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
