package core;

/**
 * Label est la classe repr�sentant une �tiquette associ�e � chaque sommet.
 * 
 * 
 * 
 * 
 * @author florian
 *
 */
public class Label implements Comparable<Label>{

	/**
	 * L'�tat du sommet au sein de l'agorithme (vrai si fix� par l'algo)
	 * @see Label#is_fixed()
	 */
	private boolean marquage;
	
	/**
	 * Le cout du chemin le plus court depuis l'origine vers ce sommet
	 */
	private double cout;
	
	/**
	 * L'id du sommet pr�c�dent sur le plus court chemin courant
	 * @see Label#setPere()
	 * @see Label#getPere()
	 */
	
	private int id_pere;
	
	/**
	 * L'id du sommet li� � ce label
	 */
	private int id_sommet_courant;
	
	/**
	 * L'arete associ� entre ce noeud et le p�re. 
	 */
	
	private Arete arete; 
	
	

	/**
	 * Constructeur Label.
	 * Au d�part, le sommet n'est pas marqu�, le chemin pour y acc�der a un cout inifini,
	 * le sommet n'a pas de p�re et on r�cup�re l'id du sommet concern�.
	 * @param noeud le noeud associ� � ce label
	 */
	public Label(Noeud noeud){
		this.marquage = false;
		this.cout = Double.POSITIVE_INFINITY;
		this.id_pere = -1;
		this.id_sommet_courant = noeud.getId();
	}
	
	/**
	 * Change l'�tat du marquage du sommet
	 * @param etat true|false -> fix�|non fix�
	 */
	public void setMarquage(boolean etat){ this.marquage = etat;}
	
	/**
	 * V�rifie si le sommet est fix� par l'algo
	 * @return
	 */
	public boolean is_fixed(){
		return this.marquage;
	}
	
	/**
	 * Met � jour le cout min pour acc�der � ce sommet
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
	 * Met � jour l'estimation pour aller de ce sommet � la destination "� vol d'oiseau"
	 * @param est La nouvelle estimation
	 */
	public void updateEstimation(double dest){}
	
	/**
	 * Donne l'estimation de distance "� vol d'oiseau" de ce sommet vers la destination
	 * @return
	 */
	public double getEstimation(){return 0;}
	
	/**
	 * D�finit le sommet pr�c�dent sur le chemin � partir d'un noeud
	 * @param noeud le sommet pr�c�dent
	 */
	public void setPere(Noeud noeud){
		this.id_pere = noeud.getId();
	}
	/**
	 * D�finit le sommet pr�c�dent sur le chemin � partir d'un id
	 * @param id l'id du sommet pr�c�dent
	 */
	public void setPere(int id){
		this.id_pere = id;
	}
	/**
	 * Donne le sommet pr�c�dent sur le chemin
	 * @return le pere du sommet courant
	 */
	public int getPere(){return this.id_pere;}
	
	/**
	 * Donne l'id du sommet associ� � ce label
	 * @return l'id du sommet courant
	 */
	public int getCourant(){ return this.id_sommet_courant;}
	
	/**
	 * Permet de comparer deux labels
	 * @param lab le deuxi�me label � comparer
	 * @return 0 si les labels sont �gaux en cout
	 * 		   1 this plus grand que lab
	 *        -1 this plus petit que lab
	 */
	public int compareTo(Label lab){
		//on utilise la m�thode de comparaison des doubles
		Double d = new Double(this.getCout());
		//Ligne de d�beug
		//System.out.println("Comparaison de "+lab.getCout()+ " et de "+d +" donne "+d.compareTo(new Double(lab.getCout())));
		return d.compareTo(new Double(lab.getCout()));
		
	}
	public String toString(){
		return "Id : "+this.getCourant()+" cout : "+this.getCout()+" visit� : "+this.is_fixed();
	}
	
	/**
	 * D�finie l'ar�te associ� au PCC vers ce noeud
	 * @param a
	 */
	public void setArete(Arete a) {this.arete = a ;}
	
	/**
	 * Renvoie l'arr�te du PCC entre le p�re et ce noeud pour calcul
	 * @return
	 */
	
	public Arete getArete() {return this.arete;}
	
}
