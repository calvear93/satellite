package satellitehomework.utils;

import java.util.LinkedList;
import java.util.List;

/** Representa un "estado" contenido en un nodo.
 * @author Cristopher Alvear Candia.
 * @version 2.1
 * @weight 35B x instance + 100B (matrix 5x5) + 175B (array 50) aprox.
 *      [int] 32b x 2.
 *      [LinkedList<Short>] 16b (wrapper) x node + 12b (pointer) x node + 40b x instance.
 *      [int[][]] 32b x rows x columns.
 *      [EmptySpace] 176b.
 */
public class NodeState {
	
	/** Distancia estimada. Longitud hasta la solucion mas proxima. */
	private int distance;
	/** Costo acumulado. Longitud total recorrida. */
	private int cost;
	/** Lista de movimientos acumulados. */
	public LinkedList<Short> moves;
	/** Matriz estado. */
	private int[][] state;
	/** Espacio en blanco. */
	private EmptySpace empty;
	
	/** Constructor.
	 * @param state : int[][] - Matriz estado.
	 * @param empty : {@link EmptySpace} - Objeto espacio en blanco.
	 * @param prevMoves : {@link LinkedList} - Movimientos acumulados anteriores.
	 * @param move : short - Movimiento actual.
	 * @param cumulativeCost : int - Costo acumulado anterior.
	 */
	@SuppressWarnings( "unchecked" )
	private NodeState( int[][] state, EmptySpace empty, LinkedList<Short> prevMoves, short move, int cumulativeCost ) {
		// Precondition: state, empty, prevMoves <> null; cumulativeCost > 0; move e {0, 1, 2, 3}.
		this.state = state;
		estimateDistanceToGoal( state );
		cost += cumulativeCost + 1;
		moves = ( LinkedList<Short> ) prevMoves.clone();
		moves.add( move );
		this.empty = empty;
	}
	
	/** Constructor de estado inicial. 
	 * @param state : int[][] - Matriz estado.
	 */
	public NodeState( int[][] state ) {
		// Precondition: state <> null.
		EmptySpace.setMatrixLength( state.length, state[0].length );
		short[] emptyPos = getEmptyPos( state );
		cost = 0;
		this.state = state;
		estimateDistanceToGoal( state );
		moves = new LinkedList<Short>();
		empty = new EmptySpace( emptyPos[0], emptyPos[1], emptyPos[0], emptyPos[1] );
	}
	
	/** Genera los hijos posibles del estado actual. 
	 * @return {@link List} - Lista con objetos {@link NodeState} de estados contiguos.
	 */
	public List<NodeState> generateChildren() {
		LinkedList<NodeState> childs = new LinkedList<>(); // Lista de hijos.
		List<Short> allowedMoves = empty.listAllowedMoves(); // Se obtiene la lista de movimientos permitidos.
		for ( short move : allowedMoves ) // Para cada movimiento permitido, se genera un hijo.
			childs.add( moveTo( move ) );
		return childs;
	}
	
	/** Genera un posible hijo aleatoriamente. 
	 * @return {@link NodeState} - Nuevo nodo estado aleatorio.
	 */
	public NodeState generateRandomChild() {
		List<Short> allowedMoves = empty.listAllowedMoves(); // Se obtiene la lista de movimientos permitidos.
		short move = allowedMoves.get( Randomizer.randomInt( 0, allowedMoves.size() - 1 ) ); // Obtiene movimiento aleatorio.
		return moveTo( move );
	}
	
	/** Genera un hijo respecto a un movimiento.
	 * @param short : move - Movimiento de empty space. move puede ser { 0, 1, 2, 3 }.
	 * @return {@link NodeState} - Nuevo nodo estado.
	 */
	private NodeState moveTo( short move ) {
		// Precondition: move e {0, 1, 2, 3}
		return new NodeState( empty.processMatrix( state, move ), empty.moveTo( move ), moves, move, cost );
	}
	
	/** Verifica si el estado actual es la solucion. 
	 * @return boolean - True si el costo individual es 0, False en caso contrario.
	 */
	public boolean isGoalState() {
		return distance == 0;
	}
	
	/** Retorna la distancia estimada hasta el estado objetivo. 
	 * @return int - Distancia estimada.
	 */
	public int getDistanceEstimatedToGoal() {
		return distance;
	}
	
	/** Retorna el costo acumulado de movimientos ( cada movimiento tiene costo 1 ). 
	 * @return int - Costo acumulado.
	 */
	public int getCumulativeCost() {
		return cost;
	}
	
	/** Retorna el costo estimado total dle estado ( distancia a objetivo + costo acumulado ). 
	 * @return int - Costo estimado.
	 */
	public int getEstimateTotalCost() {
		return distance + cost;
	}
	
	/** Retorna el costo del estado actual para llegar a la solucion.
	 * Calcula la posicion relativa de la casilla, el valor de esta y
	 * su diferencia para calcular el costo relativo. La suma de todas
	 * las distancias relativas por casilla dan el costo aproximado total.
	 * @param matrix : int[][] - Matriz estado.
	 * @return short[] - Vector { i, j }, donde i es la posicion vertical, y j la horizontal.
	 */
	private void estimateDistanceToGoal( int[][] matrix ) {
		// Precondition: matrix <> null.
		int[] relCoordinates;
		distance = 0;
		for( short i = 0; i < matrix.length; i++ )
			for( short j = 0; j < matrix[i].length; j++ ) {
				if ( matrix[i][j] == 0 ) continue;
				relCoordinates = calculateInMatrixPosition( matrix[i][j], matrix[i].length );
				distance += Math.abs( i - relCoordinates[0] ) + Math.abs( j - relCoordinates[1] );
			}
	}
	
	/** Imprime la matriz estado. */
	public void printMatrix() {
		for( int[] vector : state ) {
			for( int element : vector )
				System.out.print( element + " " );
			System.out.println();
		}
	}
	
	/** Retorna la posicion del espacio vacio o 0 dentro de la matriz estado. 
	 * @param matrix : int[][] - Matriz estado.
	 * @return short[] - Vector { i, j }, donde i es la posicion vertical, y j la horizontal.
	 */
	private static short[] getEmptyPos( int[][] matrix ) {
		// Precondition: matrix <> null.
		for( short i = 0; i < matrix.length; i++ )
			for( short j = 0; j < matrix[i].length; j++ )
				if ( matrix[i][j] == 0 )
					return new short[] { i, j };
		return new short[] { 0, 0 };
	}
	
	/** Calcula las coordenadas relativas en la matriz dada una posicion.
	 * @param p : int Posicion.
	 * @param rows : int - Filas.
	 * @param columns : int - Ccantidad de columnas.
	 * @return int[] - Coordenadas relativas. { i, j }.
	 */
	private static int[] calculateInMatrixPosition( int p, int columns ) {
		int i = ( int ) Math.ceil( ( float ) p / columns ) - 1; // Calcula la fila.
		int j = p - columns * i - 1; // Calcula la columna.
		return new int[]{ i, j };
	}
}