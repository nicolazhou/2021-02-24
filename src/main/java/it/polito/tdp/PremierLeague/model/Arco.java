package it.polito.tdp.PremierLeague.model;

import java.util.Objects;

public class Arco {

	Player p1;
	Player p2;
	double peso;
	
	
	public Arco(Player p1, Player p2, double peso) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		this.peso = peso;
	}
	
	
	public Player getP1() {
		return p1;
	}
	
	
	public void setP1(Player p1) {
		this.p1 = p1;
	}
	
	
	public Player getP2() {
		return p2;
	}
	
	
	public void setP2(Player p2) {
		this.p2 = p2;
	}
	
	
	public double getPeso() {
		return peso;
	}
	
	
	public void setPeso(double peso) {
		this.peso = peso;
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(p1, p2, peso);
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arco other = (Arco) obj;
		return Objects.equals(p1, other.p1) && Objects.equals(p2, other.p2)
				&& Double.doubleToLongBits(peso) == Double.doubleToLongBits(other.peso);
	}
	
	
	
}
