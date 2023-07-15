package it.polito.tdp.PremierLeague.model;

public class SimulatorResult {

	private int numeroAzioni;
	private int goalCasa;
	private int goalOspite;
	private int espulsioniCasa;
	private int espulsioniOspite;
	
	
	public SimulatorResult(int numeroAzioni, int goalCasa, int goalOspite, int espulsioniCasa, int espulsioniOspite) {
		super();
		this.numeroAzioni = numeroAzioni;
		this.goalCasa = goalCasa;
		this.goalOspite = goalOspite;
		this.espulsioniCasa = espulsioniCasa;
		this.espulsioniOspite = espulsioniOspite;
	}


	public int getNumeroAzioni() {
		return numeroAzioni;
	}


	public void setNumeroAzioni(int numeroAzioni) {
		this.numeroAzioni = numeroAzioni;
	}


	public int getGoalCasa() {
		return goalCasa;
	}


	public void setGoalCasa(int goalCasa) {
		this.goalCasa = goalCasa;
	}


	public int getGoalOspite() {
		return goalOspite;
	}


	public void setGoalOspite(int goalOspite) {
		this.goalOspite = goalOspite;
	}


	public int getEspulsioniCasa() {
		return espulsioniCasa;
	}


	public void setEspulsioniCasa(int espulsioniCasa) {
		this.espulsioniCasa = espulsioniCasa;
	}


	public int getEspulsioniOspite() {
		return espulsioniOspite;
	}


	public void setEspulsioniOspite(int espulsioniOspite) {
		this.espulsioniOspite = espulsioniOspite;
	}


	@Override
	public String toString() {
		
		String s = "Simulazione della partita completata \n" 
				+ "Numero di azioni: " + numeroAzioni + "\n"
				+ "Goal Casa = " + goalCasa + "\n"
				+ "Goal Ospite = " + goalOspite + "\n"
				+ "Espulsioni Casa = " + espulsioniCasa + "\n"
				+ "Espulsioni Ospite = " + espulsioniOspite + "\n";
		
		
		return s;
	}
	
	
	
	
}
