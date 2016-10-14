package core ;

import java.awt.Color;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import base.Readarg ;

public class Pcc extends Algo {

    // Numero des sommets origine et destination
    protected int zoneOrigine ;
    protected int origine ;

    protected int zoneDestination ;
    protected int destination ;
    
    protected long temps_exec ;
    
    protected Chemin itineraire;
    
    protected int max_noeud_tas;
    
    protected int noeuds_explores;
    
    protected int disttemps = -1; // détermine calcul en temps ou distance
    
    /**
     * La carte qui permet d'associer un label à un noeud
     */
    protected HashMap<Noeud, Label> carte;
    protected HashMap<Noeud, Label> carte2;
    
    /**
     * Le tas qui contient les noeuds visités
     */
    protected BinaryHeap<Label> tas;

    public Pcc(Graphe gr, PrintStream sortie, Readarg readarg) {
		super(gr, sortie, readarg) ;
	
		this.max_noeud_tas = 0;
		this.noeuds_explores = 0;
		this.zoneOrigine = gr.getZone () ;
		this.origine = readarg.lireInt ("Numero du sommet d'origine ? ") ;
	
		// Demander la zone et le sommet destination.
		this.zoneOrigine = gr.getZone () ;
		this.destination = readarg.lireInt ("Numero du sommet destination ? ");
		
		this.carte = new HashMap<Noeud, Label>();
		this.tas = new BinaryHeap<Label>();
	
	
	
    }
    
    public Pcc(Graphe gr, int origine, int destination, int disttemps) {
    	super(gr);
    	this.disttemps=disttemps;
		this.zoneOrigine = gr.getZone () ;
		this.origine = origine;
		
		// Demander la zone et le sommet destination.
		this.zoneOrigine = gr.getZone () ;
		this.destination = destination;
		
		this.carte = new HashMap<Noeud, Label>();
		this.tas = new BinaryHeap<Label>();
    	
    }
       
    
    /**
     * Première version de l'algorithme de Dijkstra
     * @param  param "Temps" ou "Distance"
     * @param star false par défaut, true si on veut l'algo PccStar @see {@link Pcc#dijkstra(String)}
     * @return nombre max de noeuds dans le tas
     */
    public int dijkstra(String type, Boolean star){
    	
    	
    	//this.graphe.getDessin().setColor(Color.BLUE);
    	//Chemin(int version, int magicnumber, int ID_map, int NB_noeud )
    	this.itineraire = new Chemin(1, 1, 1, 0);
    	int max_noeuds_in_tas = 0;
    	//On récupère les noeuds du graphe TODO optionnel mais pratique
    	ArrayList<Noeud> noeuds = this.graphe.getNoeuds();
    	
    	Noeud dest = noeuds.get(this.destination);
    	// - >   this.remplirCarte(noeuds);
    	//System.out.println("La HASHMAP est OK !!! ");
    	
    	/////// On remplit le premier noeud ///////////////////
    	Noeud N1 = noeuds.get(this.origine);
    	Label lab = (star) ? new Label_A_Star(N1) : new Label (N1);
    	
    	this.carte.put(N1, lab);
    	//System.out.println("type : " + lab.getClass());
    		
    	Label lab_courant = this.carte.get(noeuds.get(this.origine));    	
    	lab_courant.updateCout(0);
    	lab_courant.setMarquage(true);
    	
    	//System.out.println("La HashMap : " + this.carte.toString());
    	    	
    	//On ajoute l'origine dans le tas
    	
    	this.tas.insert(lab_courant);
    	this.tas.update(lab_courant);
    	//System.out.println(noeuds.get(lab_courant.getCourant()).getSuiv());
    	this.noeuds_explores ++;
    	
    	//On ajoute les suivants de l'origine dans le tas
    	for(Noeud suiv : noeuds.get(lab_courant.getCourant()).getSuiv()){
    		//Label lab_suiv = new Label(suiv);
    		Label lab_suiv = (star) ? new Label_A_Star(suiv) : new Label (suiv);
    		if (!this.carte.containsKey(suiv)){
    			this.carte.put(suiv, lab_suiv);
    		}else{
    			lab_suiv = this.carte.get(suiv);
    		}
    		double cout_suiv = 0;
			Arete a = null;
			
			//a= this.graphe.New_get_arete(lab_courant.getCourant(),lab_suiv.getCourant(),type);
			
			a = noeuds.get(lab_courant.getCourant()).trouveArete(suiv);
			
			if  (a != null) {
				switch(type){
				case "Temps" : 		
					lab_suiv.updateEstimation((Graphe.distance(suiv.getLon(), suiv.getLat(), dest.getLon(), dest.getLat())) /(130 * 1000/60));
					//System.out.println("Distance 1 : " + Graphe.distance(suiv.getLon(), suiv.getLat(), dest.getLon(), dest.getLat()));
					//System.out.println("Distance estimée : "+lab_suiv.getEstimation());
					//System.out.println("Le cout_cour : " + lab_suiv.getEstimation() +" Le cout de a : " + a.getTemps());

					cout_suiv = a.getTemps();
					break;
				case "Distance" :
					cout_suiv = a.getLongueur();
					lab_suiv.updateEstimation(Graphe.distance(suiv.getLon(), suiv.getLat(), dest.getLon(), dest.getLat()));
	    			
					break;
				
				}
				
				lab_suiv.updateCout(cout_suiv);
				lab_suiv.setPere(lab_courant.getCourant());
				lab_suiv.setArete(a);			
    			this.tas.insert(lab_suiv);
    			this.tas.update(lab_suiv);
    			this.carte.put(suiv, lab_suiv);
    			//System.out.println("Le cout du noeud " + lab_suiv.getCourant() + " : " + cout_suiv);

    			
    		}else{
    			System.out.println("Des beugs ! "+this.graphe.New_get_arete(lab_courant.getCourant(),lab_suiv.getCourant(),type));
    			System.out.println("Pas de chemin du noeud "+lab_courant.getCourant()+ " vers le noeud "+lab_suiv.getCourant()+" en effet : "+this.graphe.New_get_arete(lab_suiv.getCourant(), lab_courant.getCourant(), type).getDescripteur().getNom());
    		}
    	}
    	
    	//System.out.println("Les suivants de l'origine OK");
    	max_noeuds_in_tas = this.tas.size();
    	//System.out.println("Le tas trié : \n");
    	//this.tas.printSorted();
    	
    	//Penser à sortir le noeud de départ du tas TODO
    	boolean stop = false;
    	while((!this.tas.isEmpty()) && (lab_courant.getCourant() != this.destination) && !stop){ // ajouter la condition si on a trouvé, on arrete de chercher TODO
    		lab_courant = this.tas.findMin();
    		//if(lab_courant.getCourant() == this.destination)
    			//System.out.println("La destination est atteinte !");
    		//System.out.println(".");
    		this.tas.deleteMin();
    		Noeud noeud_courant = noeuds.get(lab_courant.getCourant());
    		//System.out.println("Le min : " + lab_courant.getCourant());
    		
    		//Si ce sommet n'a pas été fixé...
    		if(!this.carte.get(noeud_courant).is_fixed()){// TODO verifier que le label est dans la carte #try/catch ?
    			//System.out.println("noeud non visité");
    			   			
    			//On marque le noeud visité
    			lab_courant.setMarquage(true);
    			//On met à jour le noeud dans la HashMap (cout...)
    			
    			this.carte.put(noeud_courant, lab_courant);
    			this.noeuds_explores ++;
    			//On ajoute les suivants du noeud dans le tas
    			for(Noeud suiv : noeuds.get(lab_courant.getCourant()).getSuiv()){
    				
    				//Label lab_suiv = new Label(suiv);
    				Label lab_suiv = (star) ? new Label_A_Star(suiv) : new Label (suiv);
    				//on vérifie si le noeud est déjà dans la carte
    				if (!this.carte.containsKey(suiv)){
    					//Mettre à jour le cout ?
    					this.carte.put(suiv, lab_suiv);
    				}else{
    					lab_suiv = this.carte.get(suiv);
    				}
    				if(!lab_suiv.is_fixed()){
	    	    		double cout_suiv = 0;
	    	    		//On cherche le cout
	    	    		double cout_cour = lab_courant.getCout(); 	    		
	
	    	    		//Arete a = this.graphe.New_get_arete(lab_courant.getCourant(),suiv.getId(),type);
	    	    		Arete a = noeuds.get(lab_courant.getCourant()).trouveArete(suiv);
	    	    		//System.out.println("Le New_get_arete dure : " + d + "ms.");
	    	    		//Thread.sleep(2000);
	    	    		if(a != null){
	    	    			//System.out.println("\n" + this.graphe.New_get_arete(lab_courant.getCourant(),lab_suiv.getCourant()).toString() + "\n");
	    	    			switch(type){
	    					case "Temps" : 
	    						if(lab_suiv.getCourant() == this.destination){
	    							lab_suiv.updateEstimation(0);
	    							cout_suiv = 0;
	    							stop = true;
	    						}else{
	    							lab_suiv.updateEstimation((Graphe.distance(suiv.getLon(), suiv.getLat(), dest.getLon(), dest.getLat())) /(130 * 1000 / 60));
	    							//System.out.println("Le cout_cour : " + cout_cour +" Le cout de a : " + a.getTemps());
	    							cout_suiv = cout_cour + a.getTemps();
	    						}
	    		    			break;
	    					case "Distance" :
	    						
	    						//cout_suiv = lab_courant.getCout() + a.getLongueur(); 
	    						cout_suiv = a.getLongueur() + cout_cour; 
	    						if(lab_suiv.getCourant() == this.destination){
	    							lab_suiv.updateEstimation(0);
	    							cout_suiv = 0;
	    							stop = true;
	    						}else{
	    							lab_suiv.updateEstimation(Graphe.distance(suiv.getLon(), suiv.getLat(), dest.getLon(), dest.getLat()));
	    						}
	    					
	    						
	    						break;
	    					
	    					}
	    	    			//System.out.println(lab_suiv.getCout());
	    	    			if(lab_suiv.getCout() > cout_suiv){
	    	    				lab_suiv.updateCout(cout_suiv);
	    	    				lab_suiv.setPere(lab_courant.getCourant());
	    	    				lab_suiv.setArete(a);	    				
	    	    				this.carte.put(suiv, lab_suiv);
	    	    			}
	    	    			if(!this.tas.exist(lab_suiv)){
		    					//On insère le suiv dans le tas (vérifier si le tas gère si le label est déjà dedans) TODO
		    					this.tas.insert(lab_suiv);
		    					this.tas.update(lab_suiv);
		    				}else{
		    					this.tas.update(lab_suiv);
		    				}
	    	    			//System.out.println("Le cout du noeud " + lab_suiv.getCourant() + " : " + cout_suiv);
	    	    			//this.graphe.getDessin().drawPoint(noeuds.get(lab_courant.getCourant()).getLon(), noeuds.get(lab_courant.getCourant()).getLat(), 5);
	    	    		//this.tas.printSorted();
    				
    	    			
	    	    		}else{
	    	    		//	System.out.println("Des beugs ! "+this.graphe.New_get_arete(lab_courant.getCourant(),suiv.getId(),type));
    	    			//System.out.println("Pas de chemin du noeud "+lab_courant.getCourant()+ " vers le noeud "+lab_suiv.getCourant()+" en effet : "+this.graphe.New_get_arete(lab_suiv.getCourant(), lab_courant.getCourant(), type).getDescripteur().getNom());
	    	    		}
    				}
    	    		
    			}
    			//Les suivants ont été ajoutés
    			
    			
    		}else{
    			//System.out.println("Le noeud " + lab_courant.getCourant() + " a déjà été visité !");
    		}
    		int size = this.tas.size();
    		if(max_noeuds_in_tas < size){
    			max_noeuds_in_tas = size;
    		}

			this.graphe.getDessin().setColor(Color.BLUE);

    		this.graphe.getDessin().drawPoint(noeuds.get(lab_courant.getCourant()).getLon(), noeuds.get(lab_courant.getCourant()).getLat(), 2);
			
    		//this.tas.deleteMin();
    		
    	}
    	/*
    	 * Fin du parcours du graphe
    	 */
    	//System.out.println("Fin du parcours");
    	//On remplit le chemin
    	
    	if (stop == true) {
	    	lab_courant = this.carte.get(dest);
			itineraire.add_noeud(noeuds.get(lab_courant.getCourant()));
			
	
	    	do{
	    		itineraire.add_arete(lab_courant.getArete());
	    		lab_courant = this.carte.get(noeuds.get(lab_courant.getPere()));
	    		//System.out.println("coucou "+lab_courant.getCourant());
	    		itineraire.add_noeud(noeuds.get(lab_courant.getCourant()));
	    		
	    		
	    	}while(lab_courant.getCourant() != this.origine || !(lab_courant.is_fixed()));
	    	if(type == "Distance"){
    			this.graphe.getDessin().setColor(Color.MAGENTA);
    		}else{
    			this.graphe.getDessin().setColor(Color.RED);
    		}
	    	
	    	graphe.dessiner_chemin(itineraire);
	    	//On sort quand on est revenu au départ ou s'il y a une erreur...
	    	//Affichage du chemin...
	    	//System.out.println(itineraire.toString());
	    	//Affichage du cout...
	    	//System.out.println("Cout du chemin : \n en Distance -> " + itineraire.get_Longueur() + "  mètres \n en Temps ->" + itineraire.get_Temps() + "  minutes");
	    	//System.out.println("Vraie !!!!!!!!!!!!!!!!!!!!!!!!!!! Fin du parcours");
    	} 
    	return max_noeuds_in_tas;
    }
    
    /**
     * On surcharge pour mettre un "mode par défaut"
     * @param type "Distance" ou "Temps"
     * 
     */
   /* public int dijkstra(String type){
    	return dijkstra(type, false);
    }
    */
    public void run(Boolean star) {


    boolean go = false;
    boolean TEST = false;
   
    
	//System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;
	
	if (this.disttemps == -1) {
		Scanner entree = new  Scanner(System.in);
		System.out.println("En distance : 0 |ou| En temps : 1 ...");
		while(!go){
			if (entree.hasNextInt()){
				disttemps = entree.nextInt();
				if(disttemps == 0 || disttemps == 1){
					go = true;
				}
			}
			//On refait un tour...
			if (!go){
				System.out.println("L'entrée n'est pas bonne !");
				entree.next();
			}
			
	    }
		
	}
	else {
		TEST = true;
	}
	
	try{
		this.graphe.getNoeuds().get(this.origine);
		this.graphe.getNoeuds().get(this.destination);
	
	}catch(Exception e){
		System.out.println("Un des deux point donné n'existe pas... \n Hum, ou peut-être qu'aucun n'existe !");
		System.out.println("A bientôt !");
		System.exit(1);
		
	}
		long exec_time = 0;
		int max = 0;
		
		if (disttemps == 0){
			if (TEST== false) {
				exec_time = System.currentTimeMillis();
				max = dijkstra("Distance", star);
				exec_time = System.currentTimeMillis() - exec_time;
				System.out.println("Max noeuds dans le tas : " + max);
				System.out.println("Nombre de noeus visités : " + this.noeuds_explores);
				System.out.println("Temps d'execution de l'algo : " + exec_time + "ms");
				System.out.println("Cout du chemin : \n en Distance -> " + this.itineraire.get_Longueur() + "  mètres \n en Temps ->" + this.itineraire.get_Temps() + "  minutes");
			}
			else{
				exec_time = System.currentTimeMillis();
				this.max_noeud_tas = dijkstra("Distance", star);
				exec_time = System.currentTimeMillis() - exec_time;
								
			}
			
			this.temps_exec = exec_time;
		
		}else{
			
			if (TEST== false) {
				exec_time = System.currentTimeMillis();
				System.out.println("Max noeuds dans le tas : " + dijkstra("Temps", star));
				exec_time = System.currentTimeMillis() - exec_time;
				System.out.println("Nombre de noeus visités : " + this.noeuds_explores);
				System.out.println("Temps d'execution de l'algo : " + exec_time + "ms");
				System.out.println("Cout du chemin : \n en Distance -> " + this.itineraire.get_Longueur() + "  mètres \n en Temps ->" + this.itineraire.get_Temps() + "  minutes");
			}
			else{
				exec_time = System.currentTimeMillis();
				this.max_noeud_tas = dijkstra("Temps", star);
				exec_time = System.currentTimeMillis() - exec_time;
				
								
			}
			this.temps_exec = exec_time;
		
		}
    }
    
    public Chemin getchemin() { return this.itineraire ;}
    public long gettemps_exec() { return this.temps_exec;}
    public int getmax_noeuds_tas() {return this.max_noeud_tas;}
    
}
