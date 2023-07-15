package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Arco;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;
import it.polito.tdp.PremierLeague.model.Team;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"), 0.0);
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Team> listAllTeams(){
		String sql = "SELECT * FROM Teams";
		List<Team> result = new ArrayList<Team>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Team team = new Team(res.getInt("TeamID"), res.getString("Name"));
				result.add(team);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Match> listAllMatches(){
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID";
		List<Match> result = new ArrayList<Match>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				
				Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
				
				
				result.add(match);

			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Player> getVertici(Match match){
		String sql = "SELECT p.PlayerID, p.Name, ac.Assists, ac.totalSuccessfulPassesAll, ac.TimePlayed "
				+ "FROM Actions ac, Players p "
				+ "WHERE MatchID = ? AND ac.PlayerID = p.PlayerID";
		
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, match.getMatchID());
			
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				double efficienza = (res.getInt("totalSuccessfulPassesAll") + res.getInt("Assists"))/res.getInt("TimePlayed");

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"), efficienza);
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Arco> getArchi(Map<Integer, Player> idMap, Match match){
		String sql = "SELECT a1.PlayerID as p1, a2.PlayerID as p2, ((a1.totalSuccessfulPassesAll + a1.assists)/a1.timePlayed - (a2.totalSuccessfulPassesAll + a2.assists)/a2.timePlayed) as peso "
				+ "FROM Actions a1, Actions a2 "
				+ "WHERE a1.MatchID = a2.MatchID "
				+ "AND a1.MatchID = ? "
				+ "AND a1.PlayerID > a2.PlayerID "
				+ "AND a1.TeamID <> a2.TeamID ";
		
		List<Arco> result = new ArrayList<Arco>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, match.getMatchID());
			
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Player p1 = idMap.get(res.getInt("p1"));
				Player p2 = idMap.get(res.getInt("p2"));
				Arco arco = new Arco(p1, p2, res.getDouble("peso"));
				
				result.add(arco);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public List<Player> getPlayerByMatchAndTeam(Match match, Integer team, Map<Integer, Player> idMap){
		String sql = "SELECT PlayerID "
				+ "FROM Actions "
				+ "WHERE MatchID = ? AND TeamID = ?";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			
			st.setInt(1, match.getMatchID());
			st.setInt(2, team);
			
			ResultSet res = st.executeQuery();
			while (res.next()) {
				
				Player player = idMap.get(res.getInt("PlayerID"));
				
				if(player != null) {
					
					result.add(player);
					
				}

			
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
