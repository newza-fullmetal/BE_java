package core;

import java.util.*;

public class Noeud {
	

	/** Un identifiant pour définir le noeud */
	private int id;
	
	/**Les coordonées du lieu */
	private float lat;
	private float lon;
	
	/** La liste des successeurs */
	private ArrayList<Noeud> suiv;
	
	/*
	 * Le constructeur
	 */
	public Noeud(int id, float lat, float lon){
		this.lat = lat;
		this.lon = lon;
		this.suiv = new ArrayList<Noeud>();
		this.id = id;
		
	}
	
	public void addSuiv(Noeud n){
		this.suiv.add(n);
	}
	
	
	
	@Override
	public String toString() {
		return "Noeud [id=" + id + ", lat=" + lat + ", lon=" + lon + ", suiv=" + suiv + "]";
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
	
	
}
