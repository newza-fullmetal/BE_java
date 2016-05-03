package core;

import java.util.ArrayList;

public class Chemin {
	private int magicnumber; 
	private int version; 
	private int ID_map; 
	private int Nb_noeud; 
	private ArrayList<Noeud> List_noeuds; 
	public ArrayList<Arete> List_Arete; 
	private double Longueur; // en mètre 
	private double Temps; // en min
	
		public Chemin(int version, int magicnumber, int ID_map, int NB_noeud ) {
			this.version = version ; 
			this.magicnumber = magicnumber;
			this.ID_map = ID_map ;
			this.Nb_noeud = NB_noeud;
			this.List_noeuds = new ArrayList<Noeud>();	
			this.Longueur = -1;
			this.List_Arete = new ArrayList<Arete>();
			this.Temps = -1; 
			
			
		}
		/*
		 * Calcule la longueur en distance du chemin
		 */
		public double cout_distance(){
			double dist=0;
			for(Arete a : this.List_Arete){
				dist += a.getLongueur(); 
			}
			return dist;
		}
		
		/**
		 * Calcule la durée en temps du chemin
		 */
		public double cout_temps(){
			double duree = 0;			
			if (this.List_Arete.isEmpty() ){
				System.out.println("la liste d'arrête est vide, pas de distance à calculer");
			}
			else {
				for(Arete a : this.List_Arete){
					//exprimer le temps en minutes
					System.out.println(a);
					if ( a!= null ) {					
						//duree += 1.0/(a.getDescripteur().vitesseMax()  * 1000.0 / 60.0 ) * Graphe.distance(a.getDepart().getLon(),a.getDepart().getLat(), a.getArrivee().getLon(),a.getArrivee().getLat());
						duree += 1.0/(a.getDescripteur().vitesseMax()  * 1000.0 / 60.0 ) * a.getLongueur() ; 
					}
				}
			}
			
			return duree;
		}

		
		public void add_noeud ( Noeud noeud ){
			this.List_noeuds.add(noeud);
			
		}
		
		public void add_arete ( Arete arete) { 
			this.List_Arete.add(arete);			
		}
		
		public ArrayList<Noeud> get_listnode (){
			return List_noeuds; 
		}
		
		public Noeud get_lastnode(){
			if (this.List_noeuds.isEmpty()) {
				return null;
			}
			return this.List_noeuds.get(this.get_listnode().size()-1);
		}
		public double get_Longueur() {
			if (this.Longueur == -1) {
				this.Longueur = this.cout_distance();
			}
			return this.Longueur; 
		}
		
		public double get_Temps() {
			if (this.Temps == -1){
				this.Temps = this.cout_temps();
			}
			return this.Temps;
		}
		
}
