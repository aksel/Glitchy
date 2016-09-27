package glitchy.core;

/**
 * Contains the main method, which initializes the program.
 * @author Aksel, Mikkel and Rasmus
 *
 */
public class Run {
	
	/**
	 * The main method of the program
	 * Initializes the CoreController
	 * @param args The run variables (Not used)
	 */
	public static void main(String[] args){
		System.out.println("Glitchy");
		new CoreController();
	}
}