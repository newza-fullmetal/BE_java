package core;

import base.Descripteur;

public class Arete {
	
	/** Le noeud de départ */
	private Noeud depart;
	
	/** Le Noeud d'arrivée */
	private Noeud arrivee;
	
	/** La description de la route */
	private Descripteur desc;
	
	private float Longueur ; // longueur de l'arrête 
	
	private int nbseg; // nombre de segments que compose l'arrête 
	
	
	/** Le constructeur
	 * 
	 * @param a debut
	 * @param b	fin
	 * @param desc	description du chemin
	 */
	
	public Arete(Noeud a, Noeud b, Descripteur desc){
		this.depart = a;
		this.arrivee = b;
		this.desc = desc;
		this.depart.addSuiv(b);
		
		if(!desc.isSensUnique()){
			this.depart.addSuiv(b);
			this.arrivee.addSuiv(a);
		}
	}
	
	public Arete(Noeud a, Noeud b, Descripteur desc, float longueur, int nbseg){
		this.depart = a;
		this.arrivee = b;
		this.desc = desc;
		this.depart.addSuiv(b);
		this.Longueur= longueur; 
		this.nbseg = nbseg;
		
		if(!desc.isSensUnique()){
			this.depart.addSuiv(b);
			this.arrivee.addSuiv(a);
		}
	}
	
	
	/** Le constructeur 2 !!! A COMPLETER après avoir regardé un peu mieux les "DataInputStream"
	 * 
	 */
	
	/* public Arete(Noeud a, Noeud b, char type, boolean sensUnique, int vitMax, String nom){
		this.depart = a;
		this.arrivee = b;
		this.desc = new Descripteur(new DataInputStream());//Que mettre ici ? La question que les français se posent!
		this.depart.addSuiv(b);
		
		if(!desc.isSensUnique()){
			this.depart.addSuiv(b);
			this.arrivee.addSuiv(a);
		}
	}*/
	public String toString() {
		return "Arrête partant du noeud " + this.getDepart().getId() + " jusqu'à" + this.getArrivee().getId();
	}
	
	public Noeud getDepart(){return this.depart;}
	
	public Noeud getArrivee(){return this.arrivee;}
	
	public Descripteur getDescripteur(){return this.desc;}
	
	public float getLongueur() {return this.Longueur;} 
	
	public float getVitesse() { return this.desc.vitesseMax();};
}
