package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;


public class Model {
	
	
	private PremierLeagueDAO dao;
	
	private List<Match> matches;
	
	private Graph<Player, DefaultWeightedEdge> grafo;
	private List<Player> vertici;
	private Map<Integer, Player> idPlayerMap;
	
	
	private List<Player> teamPlayer1;
	private List<Player> teamPlayer2;
	
	public Model() {
		
		this.dao = new PremierLeagueDAO();
		
		this.matches = new ArrayList<>(this.dao.listAllMatches());
		
		
	}
	
	
	
	
	
	public void creaGrafo(Match match) {
		
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.vertici = new ArrayList<>(this.dao.getVertici(match));
		
		this.idPlayerMap = new HashMap<>();
		for(Player p : this.vertici) {
			this.idPlayerMap.put(p.getPlayerID(), p);
		}
		
		// Vertici:
		Graphs.addAllVertices(this.grafo, this.vertici);

		
		
		// Archi
		for(Arco a : this.dao.getArchi(idPlayerMap, match)) {
			
			if(a.getPeso() >= 0) { // p1 > p2
				
				if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(), a.getPeso());
				}
				
			} else { // p2 > p1
				
				if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP2(), a.getP1(), (-1)*a.getPeso());
				}
				
			}
			
			
		}
		
		this.teamPlayer1 = new ArrayList<>(this.dao.getPlayerByMatchAndTeam(match, match.getTeamHomeID(), idPlayerMap));
		this.teamPlayer2 = new ArrayList<>(this.dao.getPlayerByMatchAndTeam(match, match.getTeamAwayID(), idPlayerMap));
		
		
		
		
	}
	
	public boolean isGrafoLoaded() {
		
		if(this.grafo == null)
			return false;
		
		return true;
		
	}
	
	
	public int getNNodes() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNArchi() {
		return this.grafo.edgeSet().size();
	}


	public GiocatoreMigliore getGiocatoreMigliore() {
		
		Player best = null;
		Double maxDelta = (double) Integer.MIN_VALUE;
		
		for(Player p : this.grafo.vertexSet()) {
			
			double pesoUscente = sommaArchi(this.grafo.outgoingEdgesOf(p));
			double pesoEntrante = sommaArchi(this.grafo.incomingEdgesOf(p));
			
			double delta = pesoUscente - pesoEntrante;
			
			System.out.println(p + " " + delta);
			
			if(delta > maxDelta) {
				
				best = p;
				maxDelta = delta;
				
			}
			
			
			
			
		}
		
		return new GiocatoreMigliore(best, maxDelta);
	}



	private double sommaArchi(Set<DefaultWeightedEdge> outgoingEdgesOf) {
		// TODO Auto-generated method stub
		
		double somma = 0.0;
		
		for(DefaultWeightedEdge e : outgoingEdgesOf) {
			
			somma += this.grafo.getEdgeWeight(e);
			
		}
		
		return somma;
	}





	public List<Match> getMatches() {
		
		Collections.sort(matches);
		
		return matches;
	}
	
	
	public SimulatorResult simula(int N) {
		
		Simulator sim = new Simulator(this.grafo, N, this.teamPlayer1, this.teamPlayer2);
		
		sim.init();
		
		sim.run();
		
		sim.messaggioProva();
		
		return sim.getResult();
		
	}
	
	
}
