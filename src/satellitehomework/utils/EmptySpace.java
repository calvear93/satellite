package satellitehomework.utils;

import java.util.ArrayList;
import java.util.Arrays;

/** Representa la casilla o espacio vacio del tablero.
 * @author Cristopher Alvear Candia.
 * @version 1.8
 * @weight 8B x instance + 14B base.
 *      [short] 16b x 4.
 *      [static short] 16b x 2.
 *      [static short[]] 16b x 5.
 */
class EmptySpace {
	
	/** Coordenadas vertical y horizontal respectivamente. */
	private short i, j;
	/** Coordenadas vetical y horizontal anteriores a el actual movimiento. */
	private short previ, prevj;
	/** Variables de clase. Filas y columnas de la matriz problema. */
	private static short rows = 0, columns = 0;
	/** Vector variacion para el calculo de movimientos. */
	static final short[] var = { -1, 0, 1, 0, -1 };
	
	/** Constructor. 
	 * @param i : short - Coordenada vertical.
	 * @param j : short - Coordenada horizontal.
	 * @param previ : short - Coordenada vertical previa.
	 * @param prevj : short - Coordenada horizontal previa.
	 */
	EmptySpace( short i, short j, short previ, short prevj ) {
		this.i = i;
		this.j = j;
		this.previ = previ;
		this.prevj = prevj;
	}
	
	/** Retorna una lista de movimientos permitidos.
	 * @return {@link ArrayList} - Lista de movimientos permitidos.
	 */
	ArrayList<Short> listAllowedMoves() {
		ArrayList<Short> moves = new ArrayList<>( 4 );
		for ( short k = 0; k < 4; k++ ) // Verifica los movimientos.
			if ( isAllowed( i + var[k], j + var[k + 1] ) && !isOutOfRange( i + var[k], j + var[k + 1] ) )
				moves.add( k );
		return moves;
	}
	
	/** Retorna un EmptySpace del movimiento respectivo.
	 * @param move : short - Movimiento [ 0, up; 1, left; 2, down; 3, right ]
	 * @return {@link EmptySpace} - Nuevo espacio en blanco luego del movimiento.
	 */
	EmptySpace moveTo( short move ) {
		// Precondition: move e {0, 1, 2, 3}.
		return new EmptySpace( ( short ) ( i + var[move] ), ( short ) ( j + var[move + 1] ), i, j );
	}
	
	/** Mueve dentro de la matriz el empty space.
	 * @param matrix : int[][] - Matriz estado.
	 * @param move : short - Movimiento [ 0, up; 1, left; 2, down; 3, right ]
	 * @return int[][] - Nueva matriz con movimiento realizado.
	 */
	int[][] processMatrix( int[][] matrix, short move ) {
		// Precondition: matrix <> null; move e {0, 1, 2, 3}.
		int[][] nwMatrix = matrixCopy( matrix );
		return emptyMoveInMatrix( nwMatrix, i, j, i + var[move], j + var[move + 1] );
	}
	
	/** Verifica el rango de una coordenada
	 * @param nwi : int - Coordenada vertical.
	 * @param nwj : int - Coordenada horizontal.
	 * @return boolean - True si la coordenada esta fuera de rango, False en caso contrario.
	 */
	private static boolean isOutOfRange( int nwi, int nwj ) {
		if ( ( nwi > ( rows - 1 ) ) || ( nwj > ( columns - 1 ) ) || ( nwi < 0 ) || ( nwj < 0 ) )
			return true;
		return false;
	}
	
	/** Verifica si la coordenada no es un retroceso.
	 * @param nwi : int - Coordenada vertical.
	 * @param nwj : int - Coordenada horizontal.
	 * @return boolean - True si la coordenada es vuelta atras, False en caso contrario.
	 */
	private boolean isAllowed( int nwi, int nwj ) {
		if ( ( nwi != previ ) || ( nwj != prevj ) )
			return true;
		return false;
	}
	
	/** Mueve un espacio vacio desde una coordenada hacia otra.
	 * @param matrix : int[][] - Matriz estado actual.
	 * @param i1 : short - Coordenada vertical del espacio vacio.
	 * @param j1 : short - Coordenada horizontal del espacio vacio.
	 * @param i2 : short - Coordenada vertical.
	 * @param j2 : short - Coordenada horizontal.
	 * @return int[][] - Matriz estado luego del intercambio.
	 */
	int[][] emptyMoveInMatrix( int[][] matrix, int i1, int j1, int i2, int j2 ) {
		// Precondition: matrix <> null; i1, i2 < matrix.length; j1, j2 < matrix[i].length.
		matrix[i1][j1] = matrix[i2][j2];
		matrix[i2][j2] = 0;
		return matrix;
	}
	
	/** Retorna una copia de la matriz parametizada.
	 * @param matrix : int[][] - Matriz a copiar.
	 * @return int[][] - Copia de la matriz.
	 */
	static int[][] matrixCopy( int[][] matrix ) {
		// Precondition: matrix <> null.
		int[][] nwMatrix = new int[ matrix.length ][];
		for( short k = 0; k < matrix.length; k++ )
			nwMatrix[k] = Arrays.copyOf( matrix[k], matrix[k].length );
		return nwMatrix;
	}
	
	/** Setea los valores de fila y columna de la matriz problema para la clase.
	 * @param nwRows : int - Cantidad de filas.
	 * @param nwColumns : int - Cantidad de columnas.
	 */
	static void setMatrixLength( int nwRows, int nwColumns ) {
		// Precondition: nrRows, nwColumns > 0.
		rows = ( short ) nwRows;
		columns = ( short ) nwColumns;
	}
}