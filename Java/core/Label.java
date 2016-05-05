package core;

/**
 * Label est la classe représentant une étiquette associée à chaque sommet.
 * 
 * 
 * 
 * 
 * @author florian
 *
 */
public class Label implements Comparable<Label>{

	/**
	 * L'état du sommet au sein de l'agorithme (vrai si fixé par l'algo)
	 * @see Label#is_fixed()
	 */
	private boolean marquage;
	
	/**
	 * Le cout du chemin le plus court depuis l'origine vers ce sommet
	 */
	private double cout;
	
	/**
	 * L'id du sommet précédent sur le plus court chemin courant
	 * @see Label#setPere()
	 * @see Label#getPere()
	 */
	private int id_pere;
	
	/**
	 * L'id du sommet lié à ce label
	 */
	private int id_sommet_courant;
	
	

	/**
	 * Constructeur Label.
	 * Au départ, le sommet n'est pas marqué, le chemin pour y accéder a un cout inifini,
	 * le sommet n'a pas de père et on récupère l'id du sommet concerné.
	 * @param noeud le noeud associé à ce label
	 */
	public Label(Noeud noeud){
		this.marquage = false;
		this.cout = Double.POSITIVE_INFINITY;
		this.id_pere = -1;
		this.id_sommet_courant = noeud.getId();
	}
	
	/**
	 * Change l'état du marquage du sommet
	 * @param etat true|false -> fixé|non fixé
	 */
	public void setMarquage(boolean etat){ this.marquage = etat;}
	
	/**
	 * Vérifie si le sommet est fixé par l'algo
	 * @return
	 */
	public boolean is_fixed(){
		return this.marquage;
	}
	
	/**
	 * Met à jour le cout min pour accéder à ce sommet
	 * @param new_cout le nouveau cout
	 */
	public void updateCout(double new_cout){
		this.cout = new_cout;				
	}
	
	/**
	 * Donne le cout minimal vers ce sommet
	 * @return le cout du chemin le plus court depuis l'origine vers ce sommet
	 */
	public double getCout(){ return this.cout;}
	
	/**
	 * Définit le sommet précédent sur le chemin à partir d'un noeud
	 * @param noeud le sommet précédent
	 */
	public void setPere(Noeud noeud){
		this.id_pere = noeud.getId();
	}
	/**
	 * Définit le sommet précédent sur le chemin à partir d'un id
	 * @param id l'id du sommet précédent
	 */
	public void setPere(int id){
		this.id_pere = id;
	}
	/**
	 * Donne le sommet précédent sur le chemin
	 * @return le pere du sommet courant
	 */
	public int getPere(){return this.id_pere;}
	
	/**
	 * Donne l'id du sommet associé à ce label
	 * @return l'id du sommet courant
	 */
	public int getCourant(){ return this.id_sommet_courant;}
	
	/**
	 * Permet de comparer deux labels
	 * @param lab le deuxième label à comparer
	 * @return 0 si les labels sont égaux en cout
	 * 
	 */
	public int compareTo(Label lab){
		//on utilise la méthode de comparaison des doubles
		Double d = new Double(this.getCout());
		//Ligne de débeug
		//System.out.println("Comparaison de "+lab.getCout()+ " et de "+d +" donne "+d.compareTo(new Double(lab.getCout())));
		return d.compareTo(new Double(lab.getCout()));
		
	}
	public String toString(){
		return "Id : "+this.getCourant()+" cout : "+this.getCout()+" visité : "+this.is_fixed();
	}
	
}
