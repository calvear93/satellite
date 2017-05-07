package satellitehomework.utils;

/** Entrega enteros aleatorios.
 * @author Cristopher Alvear Candia.
 * @version 1.0
 */
public class Randomizer {
	
	/** Constructor privado (Clase util, estatica). */
	private Randomizer() {}
	
	/** Metodo que retorna un entero aleatorio dentro de un rango definido. 
	 * @param lower : int - Limite izquierdo/inferior inclusivo del rango.
	 * @param upper : int - Limite derecho/superior inclusivo del rango.
	 * @return int - Numero aleatorio dentro del rango [a,b].
	 */
	public static int randomInt( int lower, int upper ) {
		 return ( int ) ( Math.random() * ( upper - lower + 1 ) + lower );
	}
}