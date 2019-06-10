package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.AnnoPunteggio;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {
	
	Map<Integer,Season> seasonMap= new HashMap<>();

	public List<Season> listAllSeasons() {
		String sql = "SELECT season, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Season s= new Season(res.getInt("season"), res.getString("description"));
				result.add(s);
				seasonMap.put(s.getSeason(), s);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Team(res.getString("team")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public AnnoPunteggio getAnnoPunteggio( Team t, Season s) {
		String sql = "SELECT HomeTeam, AwayTeam, FTR " + 
				"FROM matches " + 
				"WHERE (HomeTeam=? OR AwayTeam=? ) " + 
				"AND Season=? ";
		
		AnnoPunteggio ap;
		Connection conn = DBConnect.getConnection();
		int p= 0;

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, t.getTeam());
			st.setString(2, t.getTeam());
			st.setInt(3, s.getSeason());
			
			
			ResultSet res = st.executeQuery();

			while (res.next()) {
				
				String h= res.getString("HomeTeam");
				String a= res.getString("AwayTeam");
				String risultato= res.getString("FTR");
				
				if (h.equals(t.getTeam()) && risultato.equals("H"))
					p+=3;
				
				if (a.equals(t.getTeam()) && risultato.equals("A"))
					p+=3;
				
				if (risultato.equals("D"))
					p+=1;
				
			}

			conn.close();
			ap= new AnnoPunteggio(s,p);
			return ap;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Season> listSeasons(Team t) {
		String sql = "SELECT DISTINCT Season " + 
				"FROM matches " + 
				"WHERE (HomeTeam=? OR AwayTeam=?) ";
		
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, t.getTeam());
			st.setString(2, t.getTeam());
			
			
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(seasonMap.get(res.getInt("Season")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
