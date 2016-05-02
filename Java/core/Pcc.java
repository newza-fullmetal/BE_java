package core ;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import base.Readarg ;

public class Pcc extends Algo {

    // Numero des sommets origine et destination
    protected int zoneOrigine ;
    protected int origine ;

    protected int zoneDestination ;
    protected int destination ;
    
    /**
     * La carte qui permet d'associer un label à un noeud
     */
    protected HashMap<Noeud, Label> carte;
    
    /**
     * Le tas qui contient les noeuds visités
     */
    protected BinaryHeap<Label> tas;

    public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
	super(gr, sortie, readarg) ;

	this.zoneOrigine = gr.getZone () ;
	this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;

	// Demander la zone et le sommet destination.
	this.zoneOrigine = gr.getZone () ;
	this.destination = readarg.lireInt ("Numero du sommet destination ? ");
	
	this.carte = new HashMap<Noeud, Label>();
	this.tas = new BinaryHeap<Label>();
	
	
    }
    /**
     * Première version de l'algorithme de Dijkstra
     */
    public void dijkstra(){
    	
    	//On récupère les noeuds du graphe
    	ArrayList<Noeud> noeuds = new ArrayList<Noeud>();
    	noeuds = this.graphe.getNoeuds();
    	
    	//On associe un label à l'origine
    	this.carte.put(noeuds.get(this.origine), new Label(noeuds.get(this.origine)));
    	//On ajoute l'origine dans le tas
    	this.tas.insert(this.carte.get(noeuds.get(this.origine)));
    	
    	//On cherche tant que la destination n'est pas atteinte ou que le tas n'est pas vide
    	
    	
    	
    }
    public void run() {

	System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;

	// A vous d'implementer la recherche de plus court chemin.
    }

}
