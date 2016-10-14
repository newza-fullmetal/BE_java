package core;

import java.util.ArrayList;

import base.Descripteur;

public class Arete {
	
	/** Le noeud de d�part */
	private Noeud depart;
	
	/** Le Noeud d'arriv�e */
	private Noeud arrivee;
	
	/** La description de la route */
	private Descripteur desc;
	
	private float Longueur ; // longueur de l'arete 
	
	private int nbseg; // nombre de segments que compose l'arete 
	
	public ArrayList<Float> List_deltalong; //valeurs des longitudes des aretes (pour le dessin) 
	
	private ArrayList<Float> List_deltalat; // pareil pour les latitudes
	
	private int num_zone; // num�ro de la zone de la carte. 
	
	private boolean reverse; // pour saoir si le chemin est en inversé #dessin
	
	
	
	
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
	
	public Arete(Noeud a, Noeud b, Descripteur desc, float longueur, int nbseg, ArrayList<Float> lon, ArrayList<Float> lat, boolean reverse){
		this.depart = a;
		this.arrivee = b;
		this.desc = desc;
		this.depart.addSuiv(b);
		this.Longueur= longueur; 
		this.nbseg = nbseg;
		this.List_deltalat = lat;
		this.List_deltalong = lon;
		this.num_zone = num_zone; 
		this.reverse= reverse;
		
		if(!desc.isSensUnique()){
			this.depart.addSuiv(b);
			this.arrivee.addSuiv(a);
		}
	}
	
	
	
	
	public String toString() {
		return "Arete partant du noeud " + this.getDepart().getId() + " jusqu'a" + this.getArrivee().getId();
	}
	
	public Noeud getDepart(){return this.depart;}
	
	public Noeud getArrivee(){return this.arrivee;}
	
	public Descripteur getDescripteur(){return this.desc;}
	
	public float getLongueur() {return this.Longueur;} 
	
	public float getVitesse() { return this.desc.vitesseMax();};
	
	public double getTemps() {return this.Longueur/(this.desc.vitesseMax()  * 1000.0 / 60.0 ) ; }
	
	public int getNbseg() { return this.nbseg; }
	
	public float getdeltalong(int a) {return this.List_deltalong.get(a); }
	
	public float getdeltalat(int a) {return this.List_deltalat.get(a); }
	
	public boolean isreverse() {return this.reverse;};
}
