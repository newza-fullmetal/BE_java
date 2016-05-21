package core;

import java.util.*;

public class Noeud {
	
 
	/** 
	 * Un identifiant pour définir le noeud 
	 */
	private int id;
	
	/**Les coordonées du lieu */
	private float lat;
	private float lon;
	
	/** La liste des successeurs */
	private ArrayList<Noeud> suiv;
	
	private ArrayList<Arete> List_arete; 
	/**
	 * Le constructeur
	 * @param id l'id du noeud à créer
	 * @param lat la latitude du noeud
	 * @param lon la longitude du noeud
	 */
	public Noeud(int id, float lat, float lon){
		this.lat = lat;
		this.lon = lon;
		this.suiv = new ArrayList<Noeud>();
		this.id = id;
		this.List_arete= new ArrayList<Arete>(); 
		
	}
	
	/**
	 * Ajoute un noeud à la liste des suivants
	 * @param n le noeud à ajouter
	 */
	public void addSuiv(Noeud n){
		if(this.suiv.contains(n)){
			//System.out.println("Ce noeud ("+n.getId()+") est déjà dans les suivants du noeud "+this.id);
		}else{
			this.suiv.add(n);
		}
	}
	
	
	
	
	public void succToString(){
		System.out.println("Le Noeud " + this.id);
		for(Noeud succ : this.suiv){
			System.out.println("|" + succ.getId());
		}
		
		
	}
	
	@Override
	public String toString() {
		return "Noeud [id=" + this.id + ", lat=" + this.lat + ", lon=" + this.lon + "]";//suiv=" + this.suiv + "]";
	}

	/**
	 * Les getters et setters !
	 */
	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
	}

	public int getId() {
		return id;
	}

	public ArrayList<Noeud> getSuiv() {
		return suiv;
	}
	
	public int getNbSuiv(){
		return this.suiv.size();
	}
	
	public void addArete(Arete a) {this.List_arete.add(a);}
	
	public ArrayList<Arete> getList_arete() {return this.List_arete ;} 
	
	public Arete trouveArete(Noeud n){
		
		for(Arete a : this.List_arete){
			if(a.getDepart().getId() == this.id && a.getArrivee().getId() == n.getId()){
				return a;
			}
			if(a.getDepart().getId() == n.getId() && a.getArrivee().getId() == this.id){
				return a;
			}
		}
		return null;
	}
	
	
}
