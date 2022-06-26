package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Simulatore {
	
	//dati in ingresso
	private int N;
	private int X;
	
	//dati in uscita
	private double mediaReporterPerPartita;
	private int partiteSottoSoglia;
	
	//stato del mondo
	private List<Match> allMatches;
	private Map<Team, Integer> reporterPerTeam;
	private Map<Integer, Team> team;
	Model m;
	
	//coda prioritaria
	private PriorityQueue<Evento> queue;
	
	//costruttore
	public Simulatore(int N, int X, Model m) {
		this.N = N;
		this.X = X;
		this.m = m;
	}
	
	//inizializzo
	public void init(Model m) {
		this.allMatches = new ArrayList<>(m.getMatches());
		this.team = new HashMap<Integer, Team>();
		for(Team t : m.getAllTeams()) {
			this.team.put(t.getTeamID(), t);
		}
		this.reporterPerTeam = new HashMap<>();
		//inizializzo i reporter per team: all'inizio ve ne sono N in tutti i team
		for(Team t : this.team.values()) {
			this.reporterPerTeam.put(t, this.N);
		}
		//inizializzo la coda degli eventi
		this.queue = new PriorityQueue<>();
		for(Match mm : this.allMatches) {
			this.queue.add(new Evento(mm));
		}
		this.mediaReporterPerPartita = 0.0;
		this.partiteSottoSoglia = 0;
	}
	
	//eseguo
	public void run() {
		while(!this.queue.isEmpty()) {
			Evento e = this.queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Evento e) {
		Team home = this.team.get(e.getM().getTeamHomeID());
		Team away = this.team.get(e.getM().getTeamAwayID());
		int risultatoHomeTeam = e.getM().getResultOfTeamHome();
		
		this.mediaReporterPerPartita += this.reporterPerTeam.get(home);
		this.mediaReporterPerPartita += this.reporterPerTeam.get(away);
		if(this.reporterPerTeam.get(home)+this.reporterPerTeam.get(away) < this.X) {
			this.partiteSottoSoglia++;
		}
		
		Team vincente = null;
		Team perdente = null;
		if(risultatoHomeTeam == 1) {
			vincente = home;
			perdente = away;
		}else if(risultatoHomeTeam == -1) {
			vincente = away;
			perdente = home;
		}else if(risultatoHomeTeam == 0) {
			//pareggio
		}
		
		if(vincente != null && perdente != null) {
			if(Math.random() < 0.5) {
				Team piuBlasonato = this.teamMigliore(vincente);
				if(piuBlasonato == null) {
					//nothing happens
				}else {
					int num = this.reporterPerTeam.get(vincente);
					this.reporterPerTeam.replace(vincente, num-1);
					this.reporterPerTeam.replace(piuBlasonato, this.reporterPerTeam.get(piuBlasonato)+1);
				}
			}else if(Math.random() > 0.8) {
				Team menoBlasonato = this.teamPeggiore(perdente);
				if(menoBlasonato != null) {
					int bocciati = (int)(Math.random()*this.reporterPerTeam.get(perdente));
					int num = this.reporterPerTeam.get(perdente);
					this.reporterPerTeam.replace(perdente, num-bocciati);
					this.reporterPerTeam.replace(menoBlasonato, this.reporterPerTeam.get(menoBlasonato)+bocciati);
				}
			}
		}
	}
	
	private Team teamMigliore(Team vincente) {
		//int punteggio = this.m.getPunteggio(vincente);
		List<Statistiche> best = this.m.squadreMigliori(vincente);
		List<Team> migliori = new ArrayList<>();
		for(Statistiche s : best) {
			migliori.add(s.getTeam());
		}
		int scelto = (int)(Math.random()*migliori.size());
		return migliori.get(scelto);
	}
	
	private Team teamPeggiore(Team perdente) {
		//int punteggio = this.m.getPunteggio(vincente);
		List<Statistiche> worst = this.m.squadreBattute(perdente);
		List<Team> peggiori = new ArrayList<>();
		for(Statistiche s : worst) {
			peggiori.add(s.getTeam());
		}
		int scelto = (int)(Math.random()*peggiori.size());
		return peggiori.get(scelto);
	}
	
	public int partiteSottoSoglia() {
		return this.partiteSottoSoglia;
	}
	public double mediaReporterPerPartita() {
		return (this.mediaReporterPerPartita/ this.allMatches.size());
	}

}
