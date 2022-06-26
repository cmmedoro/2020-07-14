package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Team, DefaultWeightedEdge> grafo;
	private List<Team> teams;
	private List<Statistiche> classifica;
	List<Match> matches;
	//simulazione
	Simulatore sim;
	double media;
	int sottoSoglia;
	
	public double getMedia() {		
		return this.media;
	}
	public int sottoSoglia() {
		return this.sottoSoglia;
	}
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		this.teams = new ArrayList<>();
		this.teams = this.dao.listAllTeams();
	}
	
	public List<Team> getAllTeams(){
		return this.teams;
	}
	
	public List<Statistiche> getClassifica(){
		List<Statistiche> classifica = new ArrayList<>();
		for(Team t : this.teams) {
			int punteggio = this.dao.getPunteggio(t);
			classifica.add(new Statistiche(t, punteggio));
		}
		Collections.sort(classifica);
		for(Statistiche s : classifica) {
			System.out.println(s.toString());
		}
		return classifica;
	}
	
	public List<Match> getMatches(){
		 matches = new ArrayList<>(this.dao.listAllMatches());
		return matches;
	}
	
	
	public void creaGrafo() {
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		//Aggiungo i vertici
		Graphs.addAllVertices(this.grafo, this.teams);
		//Aggiungo gli archi
		this.classifica = new ArrayList<>(this.getClassifica());
		for(Arco a : this.dao.getArchi()) {
			Team t1 = a.getT1();
			Team t2 = a.getT2();
			int punteggio1 = 0;
			int punteggio2 = 0;
			for(Statistiche s : this.classifica) {
				if(s.getTeam().equals(t1)) {
					punteggio1 = s.getPunteggio();
				}
				if(s.getTeam().equals(t2)) {
					punteggio2= s.getPunteggio();
				}
			}
			if(punteggio1 > punteggio2) {
				Graphs.addEdgeWithVertices(this.grafo, t1, t2, punteggio1-punteggio2);
			}else if(punteggio2 > punteggio1) {
				Graphs.addEdgeWithVertices(this.grafo, t2 , t1, punteggio2-punteggio1);
			}else if(punteggio2 == punteggio1) {
				//non creo l'arco
			}
		}
	}
	public boolean isGraphCreated() {
		if(this.grafo == null) {
			return false;
		}
		return true;
	}
	public int nVertices() {
		return this.grafo.vertexSet().size();
	}
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public int getPunteggio(Team t) {
		int punteggio = 0;
		for(Statistiche s : this.classifica) {
			if(s.getTeam().equals(t)) {
				punteggio = s.getPunteggio();
				break;
			}
		}
		return punteggio;
	}
	
	public List<Statistiche> squadreBattute(Team t){
		int punteggio = 0;
		List<Statistiche> battute = new ArrayList<>();
		for(Statistiche s : this.classifica) {
			if(s.getTeam().equals(t)) {
				punteggio = s.getPunteggio();
				break;
			}
		}
		for(Statistiche s : this.classifica) {
			if(!s.getTeam().equals(t) && s.getPunteggio() < punteggio) {
				battute.add(new Statistiche(s.getTeam(), punteggio-s.getPunteggio()));
			}
		}
		Collections.sort(battute);
		return battute;
	}
	
	public List<Statistiche> squadreMigliori(Team t){
		int punteggio = 0;
		List<Statistiche> migliori = new ArrayList<>();
		for(Statistiche s : this.classifica) {
			if(s.getTeam().equals(t)) {
				punteggio = s.getPunteggio();
				break;
			}
		}
		for(Statistiche s : this.classifica) {
			if(!s.getTeam().equals(t) && s.getPunteggio() > punteggio) {
				migliori.add(new Statistiche(s.getTeam(), s.getPunteggio()-punteggio));
			}
		}
		Collections.sort(migliori);
		return migliori;
	}
	
	public void simula(int N, int X) {
		this.sim = new Simulatore(N, X, this);
		this.sim.init(this);
		this.sim.run();
		this.media = this.sim.mediaReporterPerPartita();
		this.sottoSoglia = this.sim.partiteSottoSoglia();
		
	}
}
