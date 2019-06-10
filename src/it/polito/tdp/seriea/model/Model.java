package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private SerieADAO dao= new SerieADAO();
	private List<Team> listaTeam;
	private List<Season> listaSeason;
	private List<AnnoPunteggio> punteggiPerStagioni;
	private SimpleDirectedWeightedGraph<Season,DefaultWeightedEdge> graph;

	public List<Team> getListTeam() {
		
		listaTeam= new ArrayList<Team>(dao.listTeams());
		
		
		return listaTeam;
	}

	public String getSeasonEPunteggi(Team t) {
		dao.listAllSeasons();
		listaSeason= new ArrayList<Season>(dao.listSeasons(t));
		punteggiPerStagioni= new ArrayList<AnnoPunteggio>();
		String risultato= "I punteggi per annata della squadra "+ t.getTeam()+ " sono: \n";
		for (Season s: listaSeason)
		{
			AnnoPunteggio ap= dao.getAnnoPunteggio(t, s);
			punteggiPerStagioni.add(ap);
			risultato+= s.getDescription() + " "+ ap.getPunteggio()+ "\n";
		}
		
		
		return risultato;
		
	}

	public AnnoPunteggio creaGrafo() {
		graph= new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		try {
		Graphs.addAllVertices(this.graph, listaSeason);
		}catch(NullPointerException nv) {}
		
		if (punteggiPerStagioni.size()==1)
		{
			
			AnnoPunteggio ap= new AnnoPunteggio(punteggiPerStagioni.get(0).getS(), 0);
			return ap;
		}
		
		
		for (AnnoPunteggio ap1: punteggiPerStagioni)
		{
			for (AnnoPunteggio ap2: punteggiPerStagioni)
			{
				if( ap1.getPunteggio() > ap2.getPunteggio())
				{
					double peso= ap1.getPunteggio() - ap2.getPunteggio();
					DefaultWeightedEdge e= graph.addEdge(ap2.getS(), ap1.getS());
					try {
					graph.setEdgeWeight(e, peso);
					}catch(NullPointerException n1) {}
					
				}
				
				if( ap2.getPunteggio() > ap1.getPunteggio())
				{
					double peso= ap2.getPunteggio() - ap1.getPunteggio();
					DefaultWeightedEdge e= graph.addEdge(ap1.getS(), ap2.getS());
					try {
					graph.setEdgeWeight(e, peso);
					} catch(NullPointerException n) {}
					
				}
			}
		}
		
		int max= 0;
		Season migliore = null;
		
		
		for(Season s: graph.vertexSet())
		{
			int i= calcolaPesoTotale(s);
			
			if(i>max)
			{
				max=i;
				migliore=s;
			}
			
		}
		
		
		
		AnnoPunteggio annatadoro= new AnnoPunteggio(migliore,max);
		return annatadoro;	
		
	}

	private int calcolaPesoTotale(Season s) {
		
		Set<DefaultWeightedEdge> inc= graph.incomingEdgesOf(s);
		Set<DefaultWeightedEdge> out= graph.outgoingEdgesOf(s);
		int incTot=0;
		int outTot=0;
		
		for (DefaultWeightedEdge e: inc)
		{
			incTot+= graph.getEdgeWeight(e);
		}
		for (DefaultWeightedEdge e: out)
		{
			outTot+= graph.getEdgeWeight(e);
		}
		
		return (incTot-outTot);
	}
	
	
	
	

}
