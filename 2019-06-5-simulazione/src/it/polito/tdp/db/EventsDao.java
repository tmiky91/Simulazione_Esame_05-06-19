package it.polito.tdp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.model.Distretto;
import it.polito.tdp.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<Year> getAnni() {
		final String sql=	"select distinct year(e.reported_date) as year " + 
							"from `events` as e " +
							"order by year";
		List<Year> anni = new LinkedList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql) ;
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				Year anno = Year.of(res.getInt("year"));
				anni.add(anno);
			}
			
			conn.close();
			return anni ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<Distretto> getDistretti(Map<Integer, Distretto> idMap, Year anno) {
		final String sql=	"select e.district_id as id, avg(e.geo_lat) as lat, avg(e.geo_lon) as lon " + 
							"from `events` as e " + 
							"where year(e.reported_date)=? " + 
							"group by id";
		List<Distretto> distretti = new LinkedList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, anno.getValue());
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				if(!idMap.containsKey(res.getInt("id"))) {
					Distretto distretto = new Distretto(res.getInt("id"), new LatLng(res.getDouble("lat"), res.getDouble("lon")));
					distretti.add(distretto);
					idMap.put(res.getInt("id"), distretto);
				}else {
					distretti.add(idMap.get(res.getInt("id")));
				}
			}
			
			conn.close();
			return distretti;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

}
