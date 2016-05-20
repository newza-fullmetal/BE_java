package core ;

import java.io.* ;
import base.* ;

/**
 * Classe abstraite representant un algorithme (connexite, plus court chemin, etc.)
 */
public abstract class Algo {

    protected PrintStream sortie ;
    protected Graphe graphe ;
    
    protected Algo(Graphe gr, PrintStream fichierSortie, Readarg readarg) {
		this.graphe = gr ;
		this.sortie = fichierSortie ;	
    }
    /**
     * Constructeur pour les tests en séries
     * @param gr graphe 
     * @param origine noeud de départ
     * @param destination noeud d'arrivé
     */
    
    protected Algo(Graphe gr) {
		this.graphe = gr ;
			
    }
    
    
    
    
    public abstract void run(Boolean star) ;
    //déclaration dégueux sorry 
	public Chemin getchemin() {return null;}
	public long gettemps_exec(){return 0;}
	public  int getmax_noeuds_tas(){return 0;} 

}
