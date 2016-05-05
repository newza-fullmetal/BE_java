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
    	
    	//On remplit la HashMap
    	for(Noeud n : noeuds){
    		Label lab = new Label(n);
    		this.carte.put(n, lab);
    	}
    	System.out.println("La HashMap : " + this.carte.toString());
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
    	
    	System.out.println("Le tas trié : \n");
    	this.tas.printSorted();
    	
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
    	    			
    	    			this.graphe.getDessin().drawPoint(noeuds.get(lab_courant.getCourant()).getLon(), noeuds.get(lab_courant.getCourant()).getLat(), 13);
        				
    	    			
    	    		}else{
    	    			System.out.println("Des beugs ! "+this.graphe.get_arete(lab_courant.getCourant(),lab_suiv.getCourant()));
    	    			System.out.println("Pas de chemin du noeud "+lab_courant.getCourant()+ " vers le noeud "+lab_suiv.getCourant()+" en effet : "+this.graphe.get_arete(lab_suiv.getCourant(), lab_courant.getCourant(), type).getDescripteur().getNom());
    	    		}
    	    		
    	    		
    			}
    			//Les suivants ont été ajoutés
    			
    		}else{
    			System.out.println("Le noeud " + lab_courant.getCourant() + " a déjà été visité !");
    		}
    		//On sort le min du tas
    		this.tas.deleteMin();
    	}
    	/*
    	 * Fin du parcours du graphe
    	 */
    	

    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	/*On cherche tant que la destination n'est pas atteinte ou que le tas n'est pas vide
    	--
    	  On ajoute les suivants du noeud de cout le plus faible
    	  On met à jour les couts en cherchant pour chaque noeud l'arrete la plus courte (mise à jour dans le tas)
    	  On sort le sommet de cout faible (haut du tas), on met son label à jour dans la carte (HashMap)
    	  On update le tas.
    	--
    	*/
    	//On cherche tant que la destination n'est pas atteinte ou que le tas n'est pas vide
    	/*while(lab_courant.getCourant() != this.destination || !this.tas.isEmpty()){
    		this.tas.printSorted();
    		//On ajoute les suivants du noeud de cout le plus faible
    		for(Noeud suiv: noeuds.get(lab_courant.getCourant()).getSuiv()){
    			//Uniquement si le sommet n'a pas déjà été visité
    			if(!this.carte.get(suiv).is_fixed()){
    				Label lab_suiv = new Label(suiv);
    				lab_suiv.setPere(lab_courant.getCourant());
    				//On calcule le cout du suivant
    				double cout = 0;
    				switch(type){
    				case "Temps" : 
    					//cout = lab_courant.getCout() + this.graphe.get_arete(lab_courant.getCourant(), suiv.getId(), type).getTemps();
    					break;
    				case "Distance" :
    					if(this.graphe.get_arete(lab_courant.getCourant(), suiv.getId(), type) == null){
    						System.out.println("il n'y a pas d'arrete");
    					}else{    					
    						cout = lab_courant.getCout() + this.graphe.get_arete(lab_courant.getCourant(), suiv.getId(), type).getLongueur() ;
    					}
    					break;
    				
    				}
    				//On met à jour le cout
    				lab_suiv.updateCout(cout);
    				System.out.println("On ajoute le label dans le tas : id = "+lab_suiv.getCourant()+ ", cout = " + lab_suiv.getCout());
    				//On ajoute le label dans le tas
    				this.tas.insert(lab_suiv);
    				//Afficher le noeud TODO
    				this.graphe.getDessin().drawPoint(noeuds.get(lab_courant.getCourant()).getLon(), noeuds.get(lab_courant.getCourant()).getLat(), 3);
    				//
    				this.tas.update(lab_suiv);
    			
    			}else{
    				System.out.println("Le noeud "+suiv.getId()+" a déjà été visité");
    			}
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
    
    	
    	
    	*/
    	
    }
    public void run() {

	System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;

	// A vous d'implementer la recherche de plus court chemin.
	dijkstra("Distance");
    }

}
