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
    
    /**
     * La carte qui permet d'associer un label � un noeud
     */
    protected HashMap<Noeud, Label> carte;
    protected HashMap<Noeud, Label> carte2;
    
    /**
     * Le tas qui contient les noeuds visit�s
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
     * Premi�re version de l'algorithme de Dijkstra
     * @param  param "Temps" ou "Distance"
     * @param star false par d�faut, true si on veut l'algo PccStar @see {@link Pcc#dijkstra(String)}
     * @return nombre max de noeuds dans le tas
     */
    public int dijkstra(String type, Boolean star){
    	
    	this.graphe.getDessin().setColor(Color.BLUE);
    	//Chemin(int version, int magicnumber, int ID_map, int NB_noeud )
    	Chemin itineraire = new Chemin(1, 1, 1, 0);
    	int max_noeuds_in_tas = 0;
    	//On r�cup�re les noeuds du graphe TODO optionnel mais pratique
    	ArrayList<Noeud> noeuds = new ArrayList<Noeud>();
    	noeuds = this.graphe.getNoeuds();
    	
    	// - >   this.remplirCarte(noeuds);
    	System.out.println("La HASHMAP est OK !!! ");
    	
    	/////// On remplit le premier noeud ///////////////////
    	Noeud N1 = noeuds.get(this.origine);
    	Label lab = (star) ? new Label_A_Star(N1) : new Label (N1);
    	this.carte.put(N1, lab);
    	System.out.println("type : " + lab.getClass());
    		
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
    		this.carte.put(suiv, lab_suiv);
    		double cout_suiv = 0;
			Arete a;
			
			a= this.graphe.New_get_arete(lab_courant.getCourant(),lab_suiv.getCourant(),type);
			
			if  (a != null) {
				switch(type){
				case "Temps" : 					
					cout_suiv = a.getTemps();
	    			break;
				case "Distance" :
					cout_suiv = a.getLongueur();
					break;
				
				}
				
			lab_suiv.updateCout(cout_suiv);
			lab_suiv.setPere(lab_courant.getCourant());
			lab_suiv.setArete(a);
    			
    			this.tas.insert(lab_suiv);
    			this.tas.update(lab_suiv);
    			
    		}else{
    			System.out.println("Des beugs ! "+this.graphe.New_get_arete(lab_courant.getCourant(),lab_suiv.getCourant(),type));
    			System.out.println("Pas de chemin du noeud "+lab_courant.getCourant()+ " vers le noeud "+lab_suiv.getCourant()+" en effet : "+this.graphe.New_get_arete(lab_suiv.getCourant(), lab_courant.getCourant(), type).getDescripteur().getNom());
    		}
    	}
    	
    	System.out.println("Les suivants de l'origine OK");
    	max_noeuds_in_tas = this.tas.size();
    	//System.out.println("Le tas tri� : \n");
    	//this.tas.printSorted();
    	
    	//Penser � sortir le noeud de d�part du tas TODO
    	
    	while((!this.tas.isEmpty()) && lab_courant.getCourant() != this.destination){ // ajouter la condition si on a trouv�, on arrete de chercher TODO
    		lab_courant = this.tas.findMin();
    		Noeud noeud_courant = noeuds.get(lab_courant.getCourant());
    		//� voir si on le vire l� TODO
    		
    		//Si ce sommet n'a pas �t� fix�...
    		if(!this.carte.get(noeud_courant).is_fixed()){
    			//System.out.println("noeud non visit�");
    			   			
    			//On marque le noeud visit�
    			lab_courant.setMarquage(true);
    			//On met � jour le noeud dans la HashMap (cout...)
    			
    			this.carte.put(noeud_courant, lab_courant);
    			
    			//On ajoute les suivants du noeud dans le tas
    			for(Noeud suiv : noeuds.get(lab_courant.getCourant()).getSuiv()){
    				Label lab_suiv = new Label(suiv);
    				//on v�rifie si le noeud est d�j� dans la carte
    				if (!this.carte.containsKey(suiv)){
    					this.carte.put(suiv, lab_suiv);
    				}
    	    		double cout_suiv = 0;
    	    		//On cherche le cout
    	    		 	    		

    	    		Arete a = this.graphe.New_get_arete(lab_courant.getCourant(),suiv.getId(),type);
    	    		
    	    		//System.out.println("Le New_get_arete dure : " + d + "ms.");
    	    		//Thread.sleep(2000);
    	    		if(a != null){
    	    			//System.out.println("\n" + this.graphe.New_get_arete(lab_courant.getCourant(),lab_suiv.getCourant()).toString() + "\n");
    	    			switch(type){
    					case "Temps" : 
    						cout_suiv = lab_courant.getCout() +a.getTemps(); 
    		    			break;
    					case "Distance" :
    						cout_suiv = lab_courant.getCout() + a.getLongueur(); 
    						break;
    					
    					}
    	    
    	    			lab_suiv.updateCout(cout_suiv);
    	    			lab_suiv.setPere(lab_courant.getCourant());
    	    			lab_suiv.setArete(a);
    	    			//On ins�re le suiv dans le tas (v�rifier si le tas g�re si le label est d�j� dedans) TODO
    	    			this.tas.insert(lab_suiv);
    	    			this.tas.update(lab_suiv);
    	    			
    	    			//this.graphe.getDessin().drawPoint(noeuds.get(lab_courant.getCourant()).getLon(), noeuds.get(lab_courant.getCourant()).getLat(), 5);
        				
    	    			
    	    		}else{
    	    			System.out.println("Des beugs ! "+this.graphe.New_get_arete(lab_courant.getCourant(),suiv.getId(),type));
    	    			//System.out.println("Pas de chemin du noeud "+lab_courant.getCourant()+ " vers le noeud "+lab_suiv.getCourant()+" en effet : "+this.graphe.New_get_arete(lab_suiv.getCourant(), lab_courant.getCourant(), type).getDescripteur().getNom());
    	    		}
    	    		
    			}
    			//Les suivants ont �t� ajout�s
    			//System.out.println("Les suivs");
    			
    		}else{
    			//System.out.println("Le noeud " + lab_courant.getCourant() + " a d�j� �t� visit� !");
    		}
    		if(max_noeuds_in_tas < this.tas.size()){
    			max_noeuds_in_tas = this.tas.size();
    		}
    		this.graphe.getDessin().setColor(Color.MAGENTA);
    		//On sort le min du tas
    		this.graphe.getDessin().drawPoint(noeuds.get(lab_courant.getCourant()).getLon(), noeuds.get(lab_courant.getCourant()).getLat(), 2);
			
    		this.tas.deleteMin();
    	}
    	/*
    	 * Fin du parcours du graphe
    	 */
    	System.out.println("Fin du parcours");
    	//On remplit le chemin
    	lab_courant = this.carte.get(noeuds.get(this.destination));
		itineraire.add_noeud(noeuds.get(lab_courant.getCourant()));
		

    	do{
    		itineraire.add_arete(lab_courant.getArete());
    		lab_courant = this.carte.get(noeuds.get(lab_courant.getPere()));
    		//System.out.println("coucou "+lab_courant.getCourant());
    		itineraire.add_noeud(noeuds.get(lab_courant.getCourant()));
    		
    		
    	}while(lab_courant.getCourant() != this.origine || !(lab_courant.is_fixed()));
    	this.graphe.getDessin().setColor(Color.GREEN);
    	graphe.dessiner_chemin(itineraire);
    	//On sort quand on est revenu au d�part ou s'il y a une erreur...
    	//Affichage du chemin...
    	//System.out.println(itineraire.toString());
    	//Affichage du cout... NOPE car manque les arretes !!! DONE
    	System.out.println("Cout du chemin : \n en Distance -> " + itineraire.get_Longueur() + "  m�tres \n en Temps ->" + itineraire.get_Temps() + "  minutes");
    	
    	return max_noeuds_in_tas;
    }
    
    /**
     * On surcharge pour mettre un "mode par d�faut"
     * @param type "Distance" ou "Temps"
     * 
     */
    public int dijkstra(String type){
    	return dijkstra(type, false);
    }
    
    public void run() {

    int disttemps = 0;
    Boolean go = false;
    Scanner entree = new  Scanner(System.in);
    
	System.out.println("Run PCC de " + zoneOrigine + ":" + origine + " vers " + zoneDestination + ":" + destination) ;
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
			System.out.println("L'entr�e n'est pas bonne !");
			entree.next();
		}
		
    }
	
	try{
		this.graphe.getNoeuds().get(this.origine);
		this.graphe.getNoeuds().get(this.destination);
	
	}catch(Exception e){
		System.out.println("Un des deux point donn� n'existe pas... \n Hum, ou peut-�tre qu'aucun n'existe !");
		System.out.println("A bient�t !");
		System.exit(1);
		
	}
		long exec_time = 0;
		if (disttemps == 0){
			
			exec_time = System.currentTimeMillis();
			System.out.println("Max noeuds dans le tas : " + dijkstra("Distance"));
			exec_time = System.currentTimeMillis() - exec_time;
			System.out.println("Temps d'ex�cution de l'algo : " + exec_time + "ms");
		}else{
			
			exec_time = System.currentTimeMillis();
			System.out.println("Max noeuds dans le tas : " + dijkstra("Temps"));
			exec_time = System.currentTimeMillis() - exec_time;
			System.out.println("Temps d'ex�cution de l'algo : " + exec_time + "ms");
		
		}
    }
    
}
