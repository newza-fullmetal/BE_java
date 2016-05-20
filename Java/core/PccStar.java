package core ;

import java.io.* ;
import java.util.ArrayList;

import base.Readarg ;

public class PccStar extends Pcc {

    public PccStar(Graphe gr, PrintStream sortie, Readarg readarg) {
	super(gr, sortie, readarg) ;
	
    }
    
    public PccStar(Graphe gr, int origine, int destination, int disttemps) {
    	super(gr, origine, destination,disttemps) ;
    }
    
    
    
    /**
     * REDEFINITION
     * Remplir la carte des sommets avec des Labels_A_Star
     * @param noeuds Le tableau des noeuds du graphe
     */
    /*public void remplirCarte(ArrayList<Noeud> noeuds){
    	//On remplit la HashMap
    	for(Noeud n : noeuds){
    		Label_A_Star lab = new Label_A_Star(n);
    		this.carte.put(n, lab);
    	}
    }*/
    
    
    
    public void run(Boolean star) {

    	System.out.println("Run PCC-Star de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;

    	// A vous d'implementer la recherche de plus court chemin A*
    	super.run(star);
    }

}
