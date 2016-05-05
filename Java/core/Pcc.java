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
     * @param  param "Temps" ou "Distance"
     */
    public void dijkstra(String type){
    	
    	//On récupère les noeuds du graphe
    	ArrayList<Noeud> noeuds = new ArrayList<Noeud>();
    	
    	noeuds = this.graphe.getNoeuds();
    	Label lab_courant = new Label(noeuds.get(this.origine));
    	
    	//int noeud_courant = this.origine;
    	
    	//On associe un label à l'origine
    	
    	//On ajoute l'origine dans le tas
    	this.tas.insert(this.carte.get(noeuds.get(this.origine)));
    	
    	
    	
    	/*On cherche tant que la destination n'est pas atteinte ou que le tas n'est pas vide
    	--
    	  On ajoute les suivants du noeud de cout le plus faible
    	  On met à jour les couts en cherchant pour chaque noeud l'arrete la plus courte (mise à jour dans le tas)
    	  On sort le sommet de cout faible (haut du tas), on met son label à jour dans la carte (HashMap)
    	  On update le tas.
    	--
    	*/
    	//On cherche tant que la destination n'est pas atteinte ou que le tas n'est pas vide
    	while(lab_courant.getCourant() != this.destination || !this.tas.isEmpty()){
    		
    		//On ajoute les suivants du noeud de cout le plus faible
    		for(Noeud suiv: noeuds.get(lab_courant.getCourant()).getSuiv()){
    			Label lab_suiv = new Label(suiv);
    			lab_suiv.setPere(lab_courant.getCourant());
    			//On calcule le cout du suivant
    			double cout = 0;
    			switch(type){
    			case "Temps" : 
    				//cout = lab_courant.getCout() + this.graphe.get_arete(lab_courant.getCourant(), suiv.getId(), type).getTemps();
    				break;
    			case "Distance" :
    				cout = lab_courant.getCout() + this.graphe.get_arete(lab_courant.getCourant(), suiv.getId(), type).getLongueur() ;
    			}
    			//On met à jour le cout
    			lab_suiv.updateCout(cout);
    			//On ajoute le label dans le tas
    			this.tas.insert(lab_suiv);
    			//Afficher le noeud TODO
    			this.graphe.getDessin().drawPoint(noeuds.get(lab_courant.getCourant()).getLon(), noeuds.get(lab_courant.getCourant()).getLat(), 3);
    			//
    			this.tas.update(lab_suiv);
    			
    			
    		}
    		
    		
    		
    		
    		//On sort le sommet de cout faible (haut du tas), on met son label à jour dans la carte (HashMap)
    		lab_courant = this.tas.findMin();
    		lab_courant.setMarquage(true);
    		//Dessiner l'arrete TODO
    		//this.graphe.dessineArete(this.graphe.get_arete(lab_courant.getPere(), lab_courant .getCourant(), type));	
    		//quand on sort un label du tas, il passe à visité
    		this.tas.deleteMin();
    		//this.carte.put(noeuds.get(lab_courant.getCourant()), new Label(noeuds.get(lab_courant.getCourant())));
    		this.carte.put(noeuds.get(lab_courant.getCourant()), lab_courant);
    		lab_courant = this.tas.findMin();
    	}
    
    	
    	
    }
    public void run() {

	System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;

	// A vous d'implementer la recherche de plus court chemin.
	dijkstra("Distance");
    }

}
