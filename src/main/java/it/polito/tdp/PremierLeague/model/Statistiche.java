package it.polito.tdp.PremierLeague.model;

public class Statistiche implements Comparable<Statistiche> {
	
	private Team team;
	private int punteggio;
	public Statistiche(Team team, int punteggio) {
		super();
		this.team = team;
		this.punteggio = punteggio;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public int getPunteggio() {
		return punteggio;
	}
	public void setPunteggio(int punteggio) {
		this.punteggio = punteggio;
	}
	@Override
	public String toString() {
		return team + " - " + punteggio ;
	}
	@Override
	public int compareTo(Statistiche o) {
		return (this.punteggio-o.punteggio);
	}
	
	
}
