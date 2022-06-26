package it.polito.tdp.PremierLeague.model;

public class Evento implements Comparable<Evento>{
	
	//l'evento è il match ---> poi dovrò andare a vedere quale è stato il risultato
	private Match m;

	public Evento(Match m) {
		super();
		this.m = m;
	}

	public Match getM() {
		return m;
	}

	public void setM(Match m) {
		this.m = m;
	}

	@Override
	public int compareTo(Evento o) {
		// TODO Auto-generated method stub
		return m.getDate().compareTo(o.m.getDate());
	}
	
}
