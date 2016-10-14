package base ;

/**
 * Ce programme propose de lancer divers algorithmes sur les graphes
 * a partir d'un menu texte, ou a partir de la ligne de commande (ou des deux).
 *
 * A chaque question posee par le programme (par exemple, le nom de la carte), 
 * la reponse est d'abord cherchee sur la ligne de commande.
 *
 * Pour executer en ligne de commande, ecrire les donnees dans l'ordre. Par exemple
 *   "java base.Launch insa 1 1 /tmp/sortie 0"
 * ce qui signifie : charge la carte "insa", calcule les composantes connexes avec une sortie graphique,
 * ecrit le resultat dans le fichier '/tmp/sortie', puis quitte le programme.
 */

import core.* ;
import java.util.Random;
import java.io.* ;

public class Launch {

    private final Readarg readarg ;

    public Launch(String[] args) {
	this.readarg = new Readarg(args) ;
    }

    public void afficherMenu () {
	System.out.println () ;
	System.out.println ("MENU") ;
	System.out.println () ;
	System.out.println ("0 - Quitter") ;
	System.out.println ("1 - Composantes Connexes") ;
	System.out.println ("2 - Plus court chemin standard") ;
	System.out.println ("3 - Plus court chemin A-star") ;
	System.out.println ("4 - Cliquer sur la carte pour obtenir un numero de sommet.") ;
	System.out.println ("5 - Charger un fichier de chemin (.path) et le verifier.") ;
	System.out.println ("6 - lancer procedure de test") ;
	System.out.println ("7 - Comparaison avec le groupe E") ;
	System.out.println ("8 - Faire un peu de covoit' (c'est bon pour la planete !)") ;
	System.out.println () ;
    }

    public static void main(String[] args) {
	//Le code de départ
	Launch launch = new Launch(args) ;
	launch.go () ;
	
    	
    	
    }

    public void go() {

	try {
	    System.out.println ("**") ;
	    System.out.println ("** Programme de test des algorithmes de graphe.");
	    System.out.println ("**") ;
	    System.out.println () ;

	    // On obtient ici le nom de la carte a utiliser.
	    String nomcarte = this.readarg.lireString ("Nom du fichier .map a utiliser ? ") ;
	    DataInputStream mapdata = Openfile.open (nomcarte) ;

	    boolean display = (1 == this.readarg.lireInt ("Voulez-vous une sortie graphique (0 = non, 1 = oui) ? ")) ;	    
	    Dessin dessin = (display) ? new DessinVisible(800,600) : new DessinInvisible() ;

	    Graphe graphe = new Graphe(nomcarte, mapdata, dessin) ;

	    // Boucle principale : le menu est accessible 
	    // jusqu'a ce que l'on quitte.
	    boolean continuer = true ;
	    int choix ;
	    int NB_tests;
		boolean TEST = false;
		PrintStream stdout = System.out;
	    
	    while (continuer) {
			this.afficherMenu () ;
			choix = this.readarg.lireInt ("Votre choix ? ") ;
			
			// Algorithme a executer
			Algo algo = null ;
			
			// Le choix correspond au numero du menu.
			switch (choix) {
			case 0 : continuer = false ; break ;
	
			case 1 : algo = new Connexite(graphe, this.fichierSortie (), this.readarg) ; break ;
			
			case 2 : algo = new Pcc(graphe, this.fichierSortie (), this.readarg) ; break ;
			
			case 3 : algo = new PccStar(graphe, this.fichierSortie (), this.readarg) ; break ;
		
			case 4 : graphe.situerClick() ; break ;
	
			case 5 :
			    String nom_chemin = this.readarg.lireString ("Nom du fichier .path contenant le chemin ? ") ;
			    graphe.verifierChemin(Openfile.open (nom_chemin), nom_chemin) ;
			    break ;
			case 6 : NB_tests = this.readarg.lireInt ("Nombre de test? "); 
				TEST = true; 
				Algo PCC = null ; 
				
				Random randomGenerator = new Random();
				FileOutputStream f = new FileOutputStream("tests.txt"); //écriture dans le fichier
				System.setOut(new PrintStream(f));
				double distance_min = 200000; // distance minimale entre deux points pour lancer le test
				
				
				System.out.println("|------------------------------------------------------------------------------------------------------|");
				System.out.println(" Programme de tests lancé sur France !!");
				System.out.println(" Nombre de tests : " + NB_tests);
				System.out.println(" Bon voyage !!");
				System.out.println("|------------------------------------------------------------------------------------------------------|");
				
				Long[] tab_times_pcc = new Long[NB_tests*2];
				Long[] tab_times_star = new Long[NB_tests*2];
				
				
				for (int i =0;i<NB_tests; i++) {
					Thread.sleep(100);
					int depart = randomGenerator.nextInt(graphe.get_nbnoeuds());
					int arrivee = randomGenerator.nextInt(graphe.get_nbnoeuds());
					double distance = Graphe.distance(graphe.get_noeud(depart).getLon(), graphe.get_noeud(depart).getLat(), graphe.get_noeud(arrivee).getLon(), graphe.get_noeud(arrivee).getLat());
					if (distance > distance_min){
						System.out.println("|              Calcul d'itineraire entre "+ depart +" et " + arrivee +"!                                            |");
						PCC = new Pcc(graphe,depart,arrivee,0 ); 
						PCC.run(false);
						
					
						if (PCC.getchemin().get_Longueur() != 0 ) {
							System.out.println("|------------------------------------------------------------------------------------------------------|");
							System.out.println("| type     | dist en metres | temps en minutes  | temps d'exec en ms   | Nombres de noeuds dans le tas |");
							System.out.println("|          |                |                   |                      |                               |");
							System.out.println("| PCC      |-------------------------------------------------------------------------------------------|");
							System.out.println("| Distance |    "+PCC.getchemin().get_Longueur()+"    |"+ PCC.getchemin().get_Temps() + " |           "+ PCC.gettemps_exec() +"        |           "+ PCC.getmax_noeuds_tas() + "                 |");
							tab_times_pcc[i] = PCC.gettemps_exec();
							
							PCC = new Pcc(graphe,depart,arrivee,1 ); 
							PCC.run(false);
							System.out.println("| Temps    |    "+PCC.getchemin().get_Longueur()+"    |"+ PCC.getchemin().get_Temps() + " |           "+ PCC.gettemps_exec() +"        |           "+ PCC.getmax_noeuds_tas() + "                 |");
							tab_times_pcc[NB_tests+i] = PCC.gettemps_exec();
							
							
							
							
							PCC = new PccStar(graphe,depart,arrivee,0) ;	
							PCC.run(true);
							System.out.println("|          |                |                   |                      |                               |");
							System.out.println("| PCCStar  |-------------------------------------------------------------------------------------------|");
							System.out.println("| Distance |   "+PCC.getchemin().get_Longueur()+"     |"+ PCC.getchemin().get_Temps() + "  |            "+ PCC.gettemps_exec() +"       |            "+ PCC.getmax_noeuds_tas() + "                |");
							tab_times_star[i] = PCC.gettemps_exec();
							
							PCC = new PccStar(graphe,depart,arrivee,1) ;
							PCC.run(true);
							System.out.println("| Temps    |   "+PCC.getchemin().get_Longueur()+"     |"+ PCC.getchemin().get_Temps() + "  |            "+ PCC.gettemps_exec() +"       |            "+ PCC.getmax_noeuds_tas() + "                |");
							System.out.println("|          |                |                   |                      |                               |"); 
							System.out.println("|------------------------------------------------------------------------------------------------------|");
							System.out.println("");	
							tab_times_star[NB_tests+i] = PCC.gettemps_exec();
						}else { i--; }

						
					}
					/* on ne garde pas les valeurs en cas de chemin non existant*/
					else { i--; }
					
					System.setOut(stdout); 
					System.out.println("test " + i + " terminé ");
					System.setOut(new PrintStream(f));
				
				}
				
				
				/* calcul de la moyenne des temps d'éxecution pour PCC et PCC* */
				long somme = 0;
				long somme2 = 0; 
				for(int i=0; i < tab_times_pcc.length ; i++){
					somme = somme + tab_times_pcc[i];
					somme2 = somme2 + tab_times_star[i];
				}
				 
				//calculer la moyenne
				long moyenne = somme / tab_times_pcc.length;
				long moyenne2 = somme2 / tab_times_pcc.length;
				System.out.println("Temps moyen d'éxecution pour le PCC : " + moyenne +" ms");			
				
				System.out.println("Temps moyen d'éxecution pour le PCC * : " + moyenne2+ " ms");
				System.setOut(stdout);  
				break;
				
			case 7 : 
				TEST = true; 
				Algo PCC1 = null ; 
				
				FileOutputStream h = new FileOutputStream("testGR-E.txt"); //écriture dans le fichier
				System.setOut(new PrintStream(h));				
				System.out.println("|------------------------------------------------------------------------------------------------------|");
				System.out.println(" Programme de test pour comparer avec le groupe E !!");
				System.out.println(" Bon voyage !!");
				System.out.println("|------------------------------------------------------------------------------------------------------|");
				
							
				
				int depart =1174708;
				int arrivee =417811;
				PCC1 = new Pcc(graphe,depart,arrivee,1); 
				PCC1.run(false);
				System.out.println("|              Calcul d'itineraire entre "+ depart +" et " + arrivee +"!                                            |");
				System.out.println("|------------------------------------------------------------------------------------------------------|");
				System.out.println("| type     | dist en metres | temps en minutes  | temps d'exec en ms   | Nombres de noeuds dans le tas |");
				System.out.println("|          |                |                   |                      |                               |");
				System.out.println("| Pcc      |-------------------------------------------------------------------------------------------|");
				System.out.println("| Temps    |    "+PCC1.getchemin().get_Longueur()+"    |"+ PCC1.getchemin().get_Temps() + " |           "+ PCC1.gettemps_exec() +"        |           "+ PCC1.getmax_noeuds_tas() + "                 |");
				PCC1 = new Pcc(graphe,depart,arrivee,0 ); 
				PCC1.run(false);
				System.out.println("| Distance |    "+PCC1.getchemin().get_Longueur()+"    |"+ PCC1.getchemin().get_Temps() + " |           "+ PCC1.gettemps_exec() +"        |           "+ PCC1.getmax_noeuds_tas() + "                 |");
				System.out.println("|          |                |                   |                      |                               |");
				System.out.println("| PCCStar  |-------------------------------------------------------------------------------------------|");
				PCC1 = new PccStar(graphe,depart,arrivee,1) ;
				PCC1.run(true);
				System.out.println("| Temps    |   "+PCC1.getchemin().get_Longueur()+"     |"+ PCC1.getchemin().get_Temps() + "  |            "+ PCC1.gettemps_exec() +"       |            "+ PCC1.getmax_noeuds_tas() + "                |");
				PCC1 = new PccStar(graphe,depart,arrivee,0) ;
				PCC1.run(true);
				System.out.println("| Distance |   "+PCC1.getchemin().get_Longueur()+"     |"+ PCC1.getchemin().get_Temps() + "  |            "+ PCC1.gettemps_exec() +"       |            "+ PCC1.getmax_noeuds_tas() + "                |");
				System.out.println("|          |                |                   |                      |                               |"); 
				System.out.println("|------------------------------------------------------------------------------------------------------|");
				System.out.println("");
				


					
													
				System.setOut(stdout); 
		
				
				
				
			
			
			break; 
			
			case 8 : algo = new Covoit(graphe, this.fichierSortie (), this.readarg) ; break ;
	
			default :
			    System.out.println ("Choix de menu incorrect : " + choix) ;
			    System.exit(1) ;
			}
		  
			
			
			if (algo != null && TEST==false) { 
				
				if (PccStar.class.isInstance(algo)){
					System.out.println("C'est un AStar");
					algo.run(true) ; 
				}else if(Pcc.class.isInstance(algo)){
					System.out.println("C'est un Pcc");
					algo.run(false) ;
				}else if(Covoit.class.isInstance(algo)){
					System.out.println("C'est un Covoit");
					algo.run(true);
				}
			}
	    }
	    
	    System.out.println ("Programme terminé.") ;
	    System.exit(0) ;
	    
	    
	} catch (Throwable t) {
	    t.printStackTrace() ;
	    System.exit(1) ;
	}
    }

    // Ouvre un fichier de sortie pour ecrire les reponses
    public PrintStream fichierSortie () {
	PrintStream result = System.out ;

	String nom = this.readarg.lireString ("Nom du fichier de sortie ? ") ;

	if ("".equals(nom)) { nom = "/dev/null" ; }

	try { result = new PrintStream(nom) ; }
	catch (Exception e) {
	    System.err.println ("Erreur a l'ouverture du fichier " + nom) ;
	    System.exit(1) ;
	}

	return result ;
    }

}
