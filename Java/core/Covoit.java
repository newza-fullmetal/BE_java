package core;

import java.awt.Color;
import java.io.PrintStream;
import java.util.ArrayList;

import base.Readarg;

public class Covoit extends Algo{

	protected int zoneOrigine;
	protected int origine_conduct;
	protected int origine_pied;
	
	
	protected int zoneDest;
	protected int dest;
	
	protected int rdv;
	protected Chemin trajet_final;
	protected Chemin trajet_conduct;
	
	protected int disttemps;
	protected float vistesse_pied;
	
	public Covoit(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg) ;
	
		
		this.zoneOrigine = gr.getZone () ;
		this.origine_conduct = readarg.lireInt ("Numero du sommet d'origine voiture ? ") ;
		this.origine_pied = readarg.lireInt ("Numero du sommet d'origine piéton ? ") ;
		this.dest = readarg.lireInt ("Numero du sommet de destinationn ? ") ;
		this.disttemps = 0;
		this.vistesse_pied = 23; //Le pieton est en fait à vélo ! Le cachotier !
		//this.carte = new HashMap<Noeud, Label>();
		//this.tas = new BinaryHeap<Label>();
	
	
	
    }
	
	public void run(Boolean star){
		System.out.println("Recherche de covoiturage en cours...");
		ArrayList<Noeud> noeuds = this.graphe.getNoeuds();
		//Graphe gr, int origine, int destination, int disttemps) {
		PccStar pcc_covoit = new PccStar(this.graphe, this.origine_conduct, this.origine_pied, this.disttemps);
		
		//On lance le pccstar de la voiture vers le pieton...
		if(this.disttemps == 0){
			pcc_covoit.dijkstra("Distance", true);
			System.out.println("PccStar over");
		}else{
			pcc_covoit.dijkstra("Temps", true);
		}
		this.trajet_conduct = pcc_covoit.getchemin();
		int n_voit = this.trajet_conduct.get_listnode().get(this.trajet_conduct.get_listnode().size() - 1).getId();
		int n_pied = this.trajet_conduct.get_listnode().get(0).getId();
		double d_voit = 0;
		double d_pied = 0;
		
		Arete a_voit = this.trajet_conduct.getAreteList().get(this.trajet_conduct.getAreteList().size() - 1);
		Arete a_pied = this.trajet_conduct.getAreteList().get(0);
		double temps = 0;
		double d_restante_pied = a_pied.getLongueur();
		int compteur_voit = 1;
		int compteur_pied = 1;
		
		while((n_voit != n_pied) && (d_voit < this.trajet_conduct.get_Longueur() - d_pied)){
			a_voit = this.trajet_conduct.getAreteList().get(this.trajet_conduct.getAreteList().size() - compteur_voit);
			temps = a_voit.getTemps();
			d_voit += a_voit.getLongueur();
			
			//On avance le pieton d'autant de temps que la voiture, mais à 4 km/h...
			double avance = 0;
			
			avance = (this.vistesse_pied * 1000 / 60) * temps;
			System.out.println("On avance de " + avance);
			d_pied += avance;
			while(avance != 0){
				avance -= d_restante_pied;
				if(avance < 0){
					d_restante_pied = 0 - avance;
					avance = 0;
					System.out.println("Le pieton est dans la même rue !");
				}else{
					compteur_pied++;
					a_pied = this.trajet_conduct.getAreteList().get(compteur_pied);
					n_pied = a_pied.getDepart().getId();
					d_restante_pied = a_pied.getLongueur() + avance;
					System.out.println("On change d'arete pour le pieton");
					
				}
			}
			d_pied -= d_restante_pied;
			compteur_voit++;
			n_voit = a_voit.getArrivee().getId();
			System.out.println(compteur_voit);
			
		}
		System.out.println("Almost done... ");
		if((n_voit != n_pied)){
			this.rdv = n_voit;
			System.out.println("J'ai de la chance !");
		}else{
			this.rdv = a_pied.getArrivee().getId();
			System.out.println("On prend le plus pres du pieton...");
		}
		this.graphe.getDessin().setColor(Color.RED);
		this.graphe.getDessin().drawPoint(noeuds.get(n_pied).getLon(), noeuds.get(n_pied).getLat(), 8);
		
		
		//On fait la recherche depuis le rdv jusqu'à l'arrivée...
		PccStar pcc_final = new PccStar(this.graphe, this.rdv, this.dest, this.disttemps);
		if(this.disttemps == 0){
			pcc_final.dijkstra("Distance", true);
			System.out.println("PccStar over");
		}else{
			pcc_final.dijkstra("Temps", true);
		}
		this.trajet_final = pcc_final.getchemin();
		graphe.dessiner_chemin(this.trajet_final);
		
	}
	
	
}
