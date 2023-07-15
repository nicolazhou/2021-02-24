package it.polito.tdp.PremierLeague.model;

public class GiocatoreMigliore {
	
	private Player player;
	private Double delta;
	
	
	public GiocatoreMigliore(Player player, Double delta) {
		super();
		this.player = player;
		this.delta = delta;
	}


	@Override
	public String toString() {
		return player + ", delta efficienza = " + delta;
	}
	
	
	

}
