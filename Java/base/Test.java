package base;

public class Test extends Launch {

	public Test(String[] args) {
		super(args);
	}

	 public static void main(String[] args) {
			//Le code de d�part
		 	System.out.println("campagne de test"); 
			Test test = new Test(args) ;
			test.go () ;		    	
		    	
		    }

}
