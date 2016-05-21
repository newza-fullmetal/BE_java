package core ;

/**
 *   Classe representant un graphe.
 *   A vous de completer selon vos choix de conception.
 */
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import base.Couleur;
import base.Descripteur;
import base.Dessin;
import base.Utils;

public class Graphe {

    // Nom de la carte utilisee pour construire ce graphe
    private final String nomCarte ;

    // Fenetre graphique
    private final Dessin dessin ;

    // Version du format MAP utilise'.
    private static final int version_map = 4 ;
    private static final int magic_number_map = 0xbacaff ;

    // Version du format PATH.
    private static final int version_path = 1 ;
    private static final int magic_number_path = 0xdecafe ;

    // Identifiant de la carte
    private int idcarte ;

    // Numero de zone de la carte
    private int numzone ;

    /*
     * Ces attributs constituent une structure ad-hoc pour stocker les informations du graphe.
     * Vous devez modifier et ameliorer ce choix de conception simpliste.
     * 
     */
    /*private float[] longitudes ;
    private float[] latitudes ;*/
    
    private ArrayList<Arete> routes;
    private Descripteur[] descripteurs ;
    
    /** Le nombre total de noeuds du graphe */
    private int nbNoeuds;
    
    /** L'ensemble des noeuds du graphe */
    private ArrayList<Noeud> noeuds;
    //private Noeud[] lesNoeuds;
    
    
    // Deux malheureux getters plus un.
    public Dessin getDessin() { return dessin ; }
    public int getZone() { return numzone ; }
    public ArrayList<Noeud> getNoeuds(){ return this.noeuds;}

    // Le constructeur cree le graphe en lisant les donnees depuis le DataInputStream
    public Graphe (String nomCarte, DataInputStream dis, Dessin dessin) {

	this.nomCarte = nomCarte ;
	this.dessin = dessin ;
	Utils.calibrer(nomCarte, dessin) ;
	this.routes = new ArrayList<Arete>();
	this.noeuds = new ArrayList<Noeud>();
	// Lecture du fichier MAP. 
	// Voir le fichier "FORMAT" pour le detail du format binaire.
	try {

	    // Nombre d'aretes du graphe
	    int edges = 0 ;

	    // Verification du magic number et de la version du format du fichier .map
	    int magic = dis.readInt () ;
	    int version = dis.readInt () ;
	    Utils.checkVersion(magic, magic_number_map, version, version_map, nomCarte, ".map") ;

	    // Lecture de l'identifiant de carte et du numero de zone, 
	    this.idcarte = dis.readInt () ;
	    this.numzone = dis.readInt () ;

	    // Lecture du nombre de descripteurs, nombre de noeuds.
	    int nb_descripteurs = dis.readInt () ;
	    int nb_nodes = dis.readInt () ;
	    this.nbNoeuds = nb_nodes;
	    // Nombre de successeurs enregistrÃ©s dans le fichier.
	    int[] nsuccesseurs_a_lire = new int[nb_nodes] ;
	    
	    // En fonction de vos choix de conception, vous devrez certainement adapter la suite.
	    /*this.longitudes = new float[nb_nodes] ;
	    this.latitudes = new float[nb_nodes] ;*/
	    this.descripteurs = new Descripteur[nb_descripteurs] ;
		
	    
	    float longitude = 0.0f;
	    float latitude = 0.0f;
	    int nbSuccesseurs;
	    // Lecture des noeuds
	    for (int num_node = 0 ; num_node < nb_nodes ; num_node++) {
			// Lecture du noeud numero num_node
			/*longitudes[num_node] = ((float)dis.readInt ()) / 1E6f ;
			latitudes[num_node] = ((float)dis.readInt ()) / 1E6f ;
			*/
			longitude = ((float)dis.readInt ()) / 1E6f ;
			latitude = ((float)dis.readInt ()) / 1E6f ;
			
			//On recup le nombre de successeurs dans le fichier
			nbSuccesseurs = dis.readUnsignedByte() ;
			
			//On cr�e le noeud num_node
			this.noeuds.add(new Noeud(num_node, latitude, longitude));
			//On rempli le nb de successeurs pour ce noeud
			nsuccesseurs_a_lire[num_node] = nbSuccesseurs;
	    }
	    
	    Utils.checkByte(255, dis) ;
	    
	    // Lecture des descripteurs du graphe
	    for (int num_descr = 0 ; num_descr < nb_descripteurs ; num_descr++) {
	    	// Lecture du descripteur numero num_descr
	    	descripteurs[num_descr] = new Descripteur(dis) ;

	    }
	    
	    Utils.checkByte(254, dis) ;
	    
//	    ArrayList<Float> deltalong = new ArrayList<Float>() ; 
//		ArrayList<Float> deltalat = new ArrayList<Float>() ; 
	    
	    // Lecture des successeurs
	    for (int num_node = 0 ; num_node < nb_nodes ; num_node++) {
	    	// Lecture de tous les successeurs du noeud num_node
	    	for (int num_succ = 0 ; num_succ < nsuccesseurs_a_lire[num_node] ; num_succ++) {
	    		// zone du successeur
	    		int succ_zone = dis.readUnsignedByte() ;

	    		// numero de noeud du successeur
	    		int dest_node = Utils.read24bits(dis) ;

	    		// descripteur de l'arete
	    		int descr_num = Utils.read24bits(dis) ;

	    		// longueur de l'arete en metres
	    		int longueur  = dis.readUnsignedShort() ;

	    		// Nombre de segments constituant l'arete
	    		int nb_segm   = dis.readUnsignedShort() ;
	    		
	    		
	    		edges++ ;
	    		//Noeud depart = noeuds.get(num_node);
	    		//Noeud arrive = noeuds.get(dest_node);
		    
	    		Couleur.set(dessin, descripteurs[descr_num].getType()) ;

	    		float current_long = noeuds.get(num_node).getLon() ;
	    		float current_lat  = noeuds.get(num_node).getLat() ;
	    		longitude = noeuds.get(dest_node).getLon() ;
	    		latitude  = noeuds.get(dest_node).getLat() ;
	    		
//	    		// free de mémoire
//	    		deltalong.clear();
//	    		deltalat.clear();
	    		
//	    		ArrayList<Float> deltalong = new ArrayList<Float>() ; 
//	    		ArrayList<Float> deltalat = new ArrayList<Float>() ; 
	    		 
	    		// Chaque segment est dessine'
	    		for (int i = 0 ; i < nb_segm ; i++) {
	    			float delta_lon = (dis.readShort()) / 2.0E5f ;
//	    			deltalong.add(delta_lon);
	    			float delta_lat = (dis.readShort()) / 2.0E5f ;
//	    			deltalat.add(delta_lat);
	    			dessin.drawLine(current_long, current_lat, (current_long + delta_lon), (current_lat + delta_lat)) ;
	    			current_long += delta_lon ;
	    			current_lat  += delta_lat ;
	    		}
		    
	    		// Le dernier trait rejoint le sommet destination.
	    		// On le dessine si le noeud destination est dans la zone du graphe courant.
	    		
	    		if (succ_zone == numzone) {
	    			dessin.drawLine(current_long, current_lat, longitude, latitude) ;
	    			
	    			//On ajoute l'arete � la liste des routes
	    			Arete a = new Arete(noeuds.get(num_node),noeuds.get(dest_node),descripteurs[descr_num],longueur,nb_segm,null,null,false);
		    		//routes.add(a);
	    			noeuds.get(num_node).addArete(a);
	    			if (!descripteurs[descr_num].isSensUnique()) { 
	    				Arete a_reverse = new Arete(noeuds.get(dest_node),noeuds.get(num_node),descripteurs[descr_num],longueur,nb_segm,null,null,true);
	    				//noeuds.get(num_node).addArete(new Arete(noeuds.get(dest_node),noeuds.get(num_node),descripteurs[descr_num],longueur,nb_segm,deltalong,deltalat)); 	
	    				noeuds.get(dest_node).addArete(a_reverse); 
	    				//noeuds.get(dest_node).addArete(a); 
	    				
	    			
	    			}
	    			
		    		//L'ajout des successeurs se fait � la cr�ation de l'arrete	
		    		//noeuds.get(num_node).succToString();
		    	}
	    		//deltalong.clear();
	    	}
	    	
	   }
	    
	    Utils.checkByte(253, dis) ;

	    System.out.println("Fichier lu : " + nb_nodes + " sommets, " + edges + " aretes, " 
			       + nb_descripteurs + " descripteurs.") ;

	} catch (IOException e) {
	    e.printStackTrace() ;
	    System.exit(1) ;
	}

    }

    // Rayon de la terre en metres
    private static final double rayon_terre = 6378137.0 ;

    /**
     *  Calcule de la distance orthodromique - plus court chemin entre deux points à la surface d'une sphère
     *  @param long1 longitude du premier point.
     *  @param lat1 latitude du premier point.
     *  @param long2 longitude du second point.
     *  @param lat2 latitude du second point.
     *  @return la distance entre les deux points en metres.
     *  Methode Ã©crite par Thomas Thiebaud, mai 2013
     */
    public static double distance(double long1, double lat1, double long2, double lat2) {
        double sinLat = Math.sin(Math.toRadians(lat1))*Math.sin(Math.toRadians(lat2));
        double cosLat = Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2));
        double cosLong = Math.cos(Math.toRadians(long2-long1));
        return rayon_terre*Math.acos(sinLat+cosLat*cosLong);
    }

    /**
     *  Attend un clic sur la carte et affiche le numero de sommet le plus proche du clic.
     *  A n'utiliser que pour faire du debug ou des tests ponctuels.
     *  Ne pas utiliser automatiquement a chaque invocation des algorithmes.
     */
    public void situerClick() {

	System.out.println("Allez-y, cliquez donc.") ;
	
	if (dessin.waitClick()) {
	    float lon = dessin.getClickLon() ;
	    float lat = dessin.getClickLat() ;
	    
	    System.out.println("Clic aux coordonnees lon = " + lon + "  lat = " + lat) ;

	    // On cherche le noeud le plus proche. O(n)
	    float minDist = Float.MAX_VALUE ;
	    int   noeud   = 0 ;
	    
	    for (int num_node = 0 ; num_node < noeuds.size() ; num_node++) {
		/*float londiff = (longitudes[num_node] - lon) ;
		float latdiff = (latitudes[num_node] - lat) ;
		float dist = londiff*londiff + latdiff*latdiff ;*/
	    	float londiff = (noeuds.get(num_node).getLon() - lon) ;
			float latdiff = (noeuds.get(num_node).getLat() - lat) ;
			float dist = londiff*londiff + latdiff*latdiff ;
		if (dist < minDist) {
		    noeud = num_node ;
		    minDist = dist ;
		}
	    }

	    System.out.println("Noeud le plus proche : " + noeud) ;
	    System.out.println() ;
	    dessin.setColor(java.awt.Color.red) ;
	    //dessin.drawPoint(longitudes[noeud], latitudes[noeud], 5) ;
	    dessin.drawPoint(noeuds.get(noeud).getLon(), noeuds.get(noeud).getLat(), 5) ;
	}
    }
    
    
    
    /**
     *  R�cup�re une ar�te avec deux noeuds en param�tre
     *  
     * @param a Noeud 1
     * @param b Noeud 2
     * @return L'arrete la plus courte ou null si elle n'existe pas
     */
    //retourne l'arr�te la plus courte en temps ou en distance 
    public Arete New_get_arete(int a, int b, String param) {
    	Arete best_arete = null;  
    	double temps_min = 10000f; 
    	float distance_min= 10000f;
    	for (Arete x : noeuds.get(a).getList_arete() )  {
    		if (  b== x.getArrivee().getId()) {
    			switch (param) {
    			case "Temps" : 
    				if (x.getTemps()< temps_min );
    				{
    					temps_min = x.getTemps(); 
    					best_arete =x; 
    				}
    				break;
    			case "Distance" :
    				if (x.getLongueur()< distance_min );
    				{
    					distance_min = x.getLongueur(); 
    					best_arete =x; 
    				}
    				break;
    				
    			default : 
    				best_arete =x; 
    				break;    				
    			}
    		
    		}
    	}
    	
    	return best_arete; 
    	
    }
    public Arete get_arete(int a, int b, String param) {
    	int ind=0; 
    	int id_arrete = -1;  
    	double temps_min = 10000f; 
    	float distance_min= 10000f;
    	for (ind =0 ; ind < this.routes.size() ; ind++ )  {
    		if ( (a == this.routes.get(ind).getDepart().getId()) && ( b== this.routes.get(ind).getArrivee().getId()) 
    			||(b == this.routes.get(ind).getDepart().getId()) && ( a== this.routes.get(ind).getArrivee().getId()) && (this.routes.get(ind).getDescripteur().isSensUnique())) {
    			switch (param) {
    			case "Temps" : 
    				if (this.routes.get(ind).getTemps()< temps_min );
    				{
    					temps_min = this.routes.get(ind).getTemps(); 
    					id_arrete = ind; 
    				}
    				break;
    			case "Distance" :
    				if (this.routes.get(ind).getLongueur()< distance_min );
    				{
    					distance_min = this.routes.get(ind).getLongueur(); 
    					id_arrete = ind; 
    				}
    				break;
    				
    			default : 
    				id_arrete = ind;
    				break;    				
    			}
    		
    		}
    	}
    	if (id_arrete == -1) {
    		// il n'existe aucune arrete liant les deux noeuds
    		return null; 
    	}
    	return this.routes.get(id_arrete); 
    	
    }
    
    
 
    // marche que pour les pi�tons 
    /**
     * 
     * @param a Noeud 1
     * @param b Noeud 2
     * @return L'arrete la plus courte ou null si elle n'existe pas
     */
    public Arete get_arete(int a, int b) {
    	int ind=0; 
    	
    	int id_arrete = -1;  
    	double temps_min = 10000f; 
    	for (ind =0 ; ind < this.routes.size() ; ind++ )  {
    		if ( (a == this.routes.get(ind).getDepart().getId()) && ( b== this.routes.get(ind).getArrivee().getId()) 
    			||(b == this.routes.get(ind).getDepart().getId()) && ( a== this.routes.get(ind).getArrivee().getId()) ) {
    			if (this.routes.get(ind).getTemps()<temps_min) {
    				id_arrete = ind;
    				temps_min = this.routes.get(ind).getTemps();
    			}
    		}
    	}
    	if (id_arrete == -1) {
    		return null; 
    	}
    	return this.routes.get(id_arrete); 
    	
    }

    /**
     *  Charge un chemin depuis un fichier .path (voir le fichier FORMAT_PATH qui decrit le format)
     *  Verifie que le chemin est empruntable et calcule le temps de trajet.
     */
    public void verifierChemin(DataInputStream dis, String nom_chemin) {
    	Chemin chemin; 
	
	try {
	    
	    // Verification du magic number et de la version du format du fichier .path
	    int magic = dis.readInt () ;
	    int version = dis.readInt () ;
	    Utils.checkVersion(magic, magic_number_path, version, version_path, nom_chemin, ".path") ;

	    // Lecture de l'identifiant de carte
	    int path_carte = dis.readInt () ;

	    if (path_carte != this.idcarte) {
		System.out.println("Le chemin du fichier " + nom_chemin + " n'appartient pas a la carte actuellement chargee." ) ;
		System.exit(1) ;
	    }

	    int nb_noeuds = dis.readInt () ;
	    chemin = new Chemin(version, magic, this.idcarte, nb_noeuds); 

	    // Origine du chemin
	    int first_zone = dis.readUnsignedByte() ;
	    int first_node = Utils.read24bits(dis) ;

	    // Destination du chemin
	    int last_zone  = dis.readUnsignedByte() ;
	    int last_node = Utils.read24bits(dis) ;

	    System.out.println("Chemin de " + first_zone + ":" + first_node + " vers " + last_zone + ":" + last_node) ;

	    int current_zone = 0 ;
	    int current_node = 0 ;

	    // Tous les noeuds du chemin
	    for (int i = 0 ; i < nb_noeuds ; i++) {
		current_zone = dis.readUnsignedByte() ;
		current_node = Utils.read24bits(dis) ;
		if (i!=0) { // on n'ajoute pas d'arr�tes au premier passage car pas de noeuds. 
			chemin.add_arete(this.New_get_arete(chemin.get_lastnode().getId(), current_node,"Temps"));
		}
		chemin.add_noeud(this.noeuds.get(current_node));
		
		System.out.println(" --> " + current_zone + ":" + current_node) ;
	    }

	    if ((current_zone != last_zone) || (current_node != last_node)) {
		    System.out.println("Le chemin " + nom_chemin + " ne termine pas sur le bon noeud.") ;
		    System.exit(1) ;
		}
	    
	    dessiner_chemin(chemin); //dessin du chemin 
	    System.out.println("le chemin a une distance de " + chemin.get_Longueur() + "m�tres");// calcul de la longueur en m�tres 
	    System.out.println("le chemin a une dur�e de " + chemin.get_Temps() + "minutes");
	    
	    
	} catch (IOException e) {
	    e.printStackTrace() ;
	    System.exit(1) ;
	}
	
	
	

    }
    
    public float nb_moyen_suiv(){
    	float moy = 0.0f;
    	
    	for (int num_node = 0 ; num_node < this.nbNoeuds ; num_node++){
    		moy+=noeuds.get(num_node).getNbSuiv();
    		
    	}
    	return moy/nbNoeuds;
    }
    //dessine une arrete de mani�re pr�cise, en ajoutant les segments 
    public void dessiner_arete(Arete a) {
    	dessin.drawLine(a.getDepart().getLon(),a.getDepart().getLat(),a.getArrivee().getLon(),a.getArrivee().getLat()) ;
    	
    }
    public void dessiner_chemin(Chemin ch){
    	
    	
    	dessin.setColor(java.awt.Color.green) ;
	   
    	for(Arete a : ch.List_Arete){
    		if (a!=null) {
    				//dessin.drawLine(a.getDepart().getLon(),a.getDepart().getLat(), a.getArrivee().getLon(),a.getArrivee().getLat()) ;
    				dessiner_arete(a); // <----- plus pr�cis !! 
    		}
    	}    	
    	
	}
    
    public int get_nbnoeuds() { return this.nbNoeuds;}
    public Noeud get_noeud(int id) { return this.noeuds.get(id);}


/////////////////////////////// Dans une classe abstraite Fonctions en static //////////////////////////////////
public static double dist_noeuds(Noeud n1, Noeud n2){
	return Math.sqrt(Math.pow(n1.getLat() - n2.getLat(), 2) + Math.pow(n2.getLon() - n1.getLon(), 2));
}
}
/*
* Questions
*?
*Si les noeuds ne sont pas reli�s (s'il n'existe pas de chemin entre les deux noeuds
*
*Le tas permet d'avoir les valeurs class�es (celle qui nous int�resse en haut)
*
*
*
*/