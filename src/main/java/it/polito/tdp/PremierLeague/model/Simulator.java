package it.polito.tdp.PremierLeague.model;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.PremierLeague.model.Event.EventType;

public class Simulator {

	/*
	 * AZIONI SALIENTI n
	 * 
	 * 
	 * ------------------------
	 * 50% GOAL
	 * n giocatori = --> squadra con giocatore migliore segna
	 * 
	 * 
	 * 30% ESPULSIONE -> giocatore a caso
	 * -- 60% squadra con giocatore migliore
	 * -- 40% altra squadra
	 * 
	 * 
	 * 20% INFORTUNIO
	 * --> Aumenta numero di azioni salienti di 2/3 (50%)
	 * 
	 */
	
	
	// Parametri del sistema
	private Graph<Player, DefaultWeightedEdge> grafo;
	private int numGiocatoriHome;
	private int numGiocatoriAway;
	
	
	// Dati in input
	private Integer N; // numero azioni
	private List<Player> giocatoriT1;
	private List<Player> giocatoriT2;
	
	
	
	// Dati in output
	private int goalCasa;
	private int goalOspite;
	private int numEspulsiHome;
	private int numEspulsiOspite;
	
	
	// Coda degli eventi
	PriorityQueue<Event> queue;



	public Simulator(Graph<Player, DefaultWeightedEdge> grafo, Integer n, List<Player> giocatoriT1,
			List<Player> giocatoriT2) {
		super();
		this.grafo = grafo;
		N = n;
		this.giocatoriT1 = giocatoriT1;
		this.giocatoriT2 = giocatoriT2;
	}
	
	
	public void init() {
		
		this.numGiocatoriHome = 11;
		this.numGiocatoriAway = 11;
		
		
		this.goalCasa = 0;
		this.goalOspite = 0;
		
		this.numEspulsiHome = 0;
		this.numEspulsiOspite = 0;
		
		this.queue = new PriorityQueue<>();
		
		
		// Creo eventi iniziali
		for(int i = 0; i < this.N; i++) {
			
			this.queue.add(new Event(EventType.AZIONE, i));
			
			
		}
		
		
	}
	
	public void run() {
		
		
		while(!this.queue.isEmpty()) {
			
			Event e = this.queue.poll();
			
			System.out.println(e);
			
			int time = e.getTime();
			
			
			switch(e.getType()) {
			case AZIONE:
				
				double prob = Math.random();
				
				if(prob < 0.5) { // 50% GOAL
					
					this.queue.add(new Event(EventType.GOAL, time));
					
				} else if(prob < 0.8) { // 30% ESPULSIONE
					
					this.queue.add(new Event(EventType.ESPULSIONE, time));
					
				} else {
					
					this.queue.add(new Event(EventType.INFORTUNIO, time));
					
				}
				
				
				break;
				
			case GOAL:
				
				if(this.numGiocatoriHome == this.numGiocatoriAway) { // Stesso numero di giocatori
					
					Player best = this.getGiocatoreMigliore();
					
					if(this.giocatoriT1.contains(best)) { // Giocatore squadra home migliore
						
						this.goalCasa ++;
						
					} else {
						
						this.goalOspite ++;
						
					}
					
					
				} else if(this.numGiocatoriHome > this.numGiocatoriAway) { // Squadra casa ha più giocatori
					
					this.goalCasa ++;
					
				} else {
					
					this.goalOspite ++;
					
				}
				
				break;
				
			case ESPULSIONE:
				
				double probEspulsione = Math.random();
				
				Player best = this.getGiocatoreMigliore();
				
				boolean casa = false;
				
				if(this.giocatoriT1.contains(best)) { // Giocatore squadra home migliore
					
					casa = true;
					
				} else {
					
					casa = false;
					
				}
				
				
				if(probEspulsione < 0.6) { // Giocatore best
					
					// Squadra migliore
					if(casa) {
						
						this.numEspulsiHome ++;
						
						this.numGiocatoriHome --;
						
					} else {
						
						this.numEspulsiOspite ++;
						
						this.numGiocatoriAway --;
						
					}
					
					
				} else {
					
					// Altra
					if(casa) {
						
						this.numEspulsiOspite ++;
						
						this.numGiocatoriAway --;
						
					} else {
						
						this.numEspulsiHome ++;
						
						this.numGiocatoriHome --;
						
					}
					
					
				}
				
		
				break;
				
			case INFORTUNIO:
				
				double probInfortunio = Math.random();
				
				if(probInfortunio < 0.5) { // 2 nuove azioni
					
					this.queue.add(new Event(EventType.AZIONE, time));
					this.queue.add(new Event(EventType.AZIONE, time));
					
					this.N += 2;
					
					
				} else { // 3 nuove azioni
					
					this.queue.add(new Event(EventType.AZIONE, time));
					this.queue.add(new Event(EventType.AZIONE, time));
					this.queue.add(new Event(EventType.AZIONE, time));
				
					
					this.N += 3;
				}
				
				
				break;
			
			
			
			}
			
			
		}
		
		
	}
	
	
	public Player getGiocatoreMigliore() {
		
		Player best = null;
		Double maxDelta = (double) Integer.MIN_VALUE;
		
		for(Player p : this.grafo.vertexSet()) {
			
			double pesoUscente = sommaArchi(this.grafo.outgoingEdgesOf(p));
			double pesoEntrante = sommaArchi(this.grafo.incomingEdgesOf(p));
			
			double delta = pesoUscente - pesoEntrante;
			
			if(delta > maxDelta) {
				
				best = p;
				maxDelta = delta;
				
			}
			
			
			
			
		}
		
		return best;
	}
	
	private double sommaArchi(Set<DefaultWeightedEdge> outgoingEdgesOf) {
		// TODO Auto-generated method stub
		
		double somma = 0.0;
		
		for(DefaultWeightedEdge e : outgoingEdgesOf) {
			
			somma += this.grafo.getEdgeWeight(e);
			
		}
		
		return somma;
	}
	
	public void messaggioProva() {
		
		System.out.println("Numero di azioni: " + this.N);
		
		System.out.println("Goal casa: " + this.goalCasa);
		System.out.println("Goal ospiti: " + this.goalOspite);
		
		System.out.println("Espulsioni casa: " + this.numEspulsiHome);
		System.out.println("Espulsioni ospite: " + this.numEspulsiOspite);
		
	}
	
	
	public SimulatorResult getResult() {
		
		
		return new SimulatorResult(this.N, this.goalCasa, this.goalOspite, this.numEspulsiHome, this.numEspulsiOspite);
		
	}
	
	
	
}
