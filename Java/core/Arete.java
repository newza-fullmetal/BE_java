package core;

import base.Descripteur;

public class Arete {
	/** Le noeud de départ */
	private Noeud depart;
	
	/** Le Noeud d'arrivée */
	private Noeud arrivee;
	
	/** La description de la route */
	private Descripteur desc;
	
	
	/** Le constructeur */
	
	public Arete(Noeud a, Noeud b, Descripteur desc){
		this.depart = a;
		this.arrivee = b;
		this.desc = desc;
		
		if(desc.isSensUnique()){
			this.depart.addSuiv();
		}else{
			
		}
	}
	
	public Noeud getDepart(){return this.depart;}
	
	public Noeud getArrivee(){return this.arrivee;}
	
	public Descripteur getDescripteur(){return this.desc;}
}
