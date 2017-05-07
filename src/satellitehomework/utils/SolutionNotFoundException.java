package satellitehomework.utils;

public class SolutionNotFoundException extends Exception {

	/** Version clase serializable. */
	private static final long serialVersionUID = 1l;
		
	/** Constructor por defecto. */
	public SolutionNotFoundException() { super(); }
		
	/** Constructor argumentado.
	 * @param msg : String - Mensaje que sera mostrado en la excepcion.
	 */
	public SolutionNotFoundException( String msg ) {
		// Precondition: msg <> null.
		super( msg );
	}	
}