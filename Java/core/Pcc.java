package core ;

import java.awt.Color;
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
     * @return nombre max de noeuds dans le tas
     */
    public int dijkstra(String type){
    	
    	this.graphe.getDessin().setColor(Color.BLUE);
    	int max_noeuds_in_tas = 0;
    	//On récupère les noeuds du graphe
    	ArrayList<Noeud> noeuds = new ArrayList<Noeud>();
    	noeuds = this.graphe.getNoeuds();
    	
    	//On remplit la HashMap
    	for(Noeud n : noeuds){
    		Label lab = new Label(n);
    		this.carte.put(n, lab);
    	}
    	//System.out.println("La HashMap : " + this.carte.toString());
    	//On remplit le premier noeud
    	Label lab_courant = this.carte.get(noeuds.get(this.origine));
    	lab_courant.updateCout(0);
    	lab_courant.setMarquage(true);
    	
    	//System.out.println("La HashMap : " + this.carte.toString());
    	    	
    	//On ajoute l'origine dans le tas
    	
    	this.tas.insert(lab_courant);
    	this.tas.update(lab_courant);
    	//System.out.println(noeuds.get(lab_courant.getCourant()).getSuiv());
    	
    	//On ajoute les suivants de l'origine dans le tas
    	for(Noeud suiv : noeuds.get(lab_courant.getCourant()).getSuiv()){
    		Label lab_suiv = new Label(suiv);
    		double cout_suiv = 0;
    		if(this.graphe.get_arete(lab_courant.getCourant(),lab_suiv.getCourant()) != null){//Remettre le parametre sur get_arrete après l'avoir corrigé TODO
    			
    			switch(type){
				case "Temps" : 
					cout_suiv = this.graphe.get_arete(lab_courant.getCourant(),lab_suiv.getCourant()).getTemps();//Remettre le parametre sur get_arrete après l'avoir corrigé TODO
	    			break;
				case "Distance" :
					cout_suiv = this.graphe.get_arete(lab_courant.getCourant(),lab_suiv.getCourant()).getLongueur();//Remettre le parametre sur get_arrete après l'avoir corrigé TODO
					break;
				
				}
    			lab_suiv.updateCout(cout_suiv);
    			lab_suiv.setPere(lab_courant.getCourant());
    			
    			this.tas.insert(lab_suiv);
    			this.tas.update(lab_suiv);
    		}else{
    			System.out.println("Des beugs ! "+this.graphe.get_arete(lab_courant.getCourant(),lab_suiv.getCourant()));
    			System.out.println("Pas de chemin du noeud "+lab_courant.getCourant()+ " vers le noeud "+lab_suiv.getCourant()+" en effet : "+this.graphe.get_arete(lab_suiv.getCourant(), lab_courant.getCourant(), type).getDescripteur().getNom());
    		}
    	}
    	max_noeuds_in_tas = this.tas.size();
    	//System.out.println("Le tas trié : \n");
    	//this.tas.printSorted();
    	
    	//Penser à sortir le noeud de départ du tas TODO
    	
    	while((!this.tas.isEmpty()) && lab_courant.getCourant() != this.destination){ // ajouter la condition si on a trouvé, on arrete de chercher TODO
    		lab_courant = this.tas.findMin();
    		Noeud noeud_courant = noeuds.get(lab_courant.getCourant());
    		//à voir si on le vire là TODO
    		
    		//Si ce sommet n'a pas été fixé...
    		if(!this.carte.get(noeud_courant).is_fixed()){
    			
    			//On marque le noeud visité
    			lab_courant.setMarquage(true);
    			//On met à jour le noeud dans la HashMap (cout...)
    			this.carte.put(noeud_courant, lab_courant);
    			
    			//On ajoute les suivants du noeud dans le tas
    			for(Noeud suiv : noeuds.get(lab_courant.getCourant()).getSuiv()){
    				Label lab_suiv = new Label(suiv);
    	    		double cout_suiv = 0;
    	    		//On cherche le cout
    	    		if(this.graphe.get_arete(lab_courant.getCourant(),lab_suiv.getCourant()) != null){//Remettre le parametre sur get_arrete après l'avoir corrigé TODO
    	    			
    	    			switch(type){
    					case "Temps" : 
    						cout_suiv = lab_courant.getCout() + this.graphe.get_arete(lab_courant.getCourant(),lab_suiv.getCourant()).getTemps(); //Remettre le parametre sur get_arrete après l'avoir corrigé TODO
    		    			break;
    					case "Distance" :
    						cout_suiv = lab_courant.getCout() + this.graphe.get_arete(lab_courant.getCourant(),lab_suiv.getCourant()).getLongueur(); //Remettre le parametre sur get_arrete après l'avoir corrigé TODO
    						break;
    					
    					}
    	    			lab_suiv.updateCout(cout_suiv);
    	    			lab_suiv.setPere(lab_courant.getCourant());
    	    			
    	    			//On insère le suiv dans le tas (vérifier si le tas gère si le label est déjà dedans) TODO
    	    			this.tas.insert(lab_suiv);
    	    			this.tas.update(lab_suiv);
    	    			
    	    			this.graphe.getDessin().drawPoint(noeuds.get(lab_courant.getCourant()).getLon(), noeuds.get(lab_courant.getCourant()).getLat(), 5);
        				
    	    			
    	    		}else{
    	    			System.out.println("Des beugs ! "+this.graphe.get_arete(lab_courant.getCourant(),lab_suiv.getCourant()));
    	    			System.out.println("Pas de chemin du noeud "+lab_courant.getCourant()+ " vers le noeud "+lab_suiv.getCourant()+" en effet : "+this.graphe.get_arete(lab_suiv.getCourant(), lab_courant.getCourant(), type).getDescripteur().getNom());
    	    		}
    	    		
    	    		
    			}
    			//Les suivants ont été ajoutés
    			
    		}else{
    			//System.out.println("Le noeud " + lab_courant.getCourant() + " a déjà été visité !");
    		}
    		if(max_noeuds_in_tas < this.tas.size()){
    			max_noeuds_in_tas = this.tas.size();
    		}
    		//On sort le min du tas
    		this.tas.deleteMin();
    	}
    	/*
    	 * Fin du parcours du graphe
    	 */
    	return max_noeuds_in_tas;
    }
    public void run() {

	System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;

	// A vous d'implementer la recherche de plus court chemin.
	System.out.println("Max noeuds dans le tas : " + dijkstra("Distance"));
    }

}
