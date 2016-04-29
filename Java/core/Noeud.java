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
		
	}
	
	/**
	 * Ajoute un noeud à la liste des suivants
	 * @param n le noeud à ajouter
	 */
	public void addSuiv(Noeud n){
		this.suiv.add(n);
	}
	
	
	
	
	public void succToString(){
		System.out.println("Le Noeud " + this.id);
		for(Noeud succ : this.suiv){
			System.out.println("|" + succ.getId());
		}
		
		
	}
	
	@Override
	public String toString() {
		return "Noeud [id=" + this.id + ", lat=" + this.lat + ", lon=" + this.lon + ", suiv=" + this.suiv + "]";
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
	
	
}
