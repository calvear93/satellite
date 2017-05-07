package satellitehomework;

import java.util.ArrayList;

import satellitehomework.utils.Randomizer;

/** Generador de problemas N-Puzzle.
 * @author Cristopher Alvear Candia.
 * @version 1.1
 */
class ProblemGenerator {
	
	/** Vector variacion para el calculo de movimientos. */
	private static final short[] var = { -1, 0, 1, 0, -1 };
	
	/** Genera una matriz problema para N-Puzzle.
	 * @param rows : int - Filas.
	 * @param columns : int - Columnas.
	 * @param steps : int - Complejidad. Pasos para desordenar la matriz.
	 * @return int[][] - La matriz desordenada, con solucion asegurada.
	 */
	static int[][] generateProblem( int rows, int columns, int steps ) {
		// Precondition: rows, columns > 0;
		// Se llena la matriz ordenadamente.
		int[][] problem = orderlyFillMatrix( new int[rows][columns] );
		// Ahora se desordena de forma que tenga solucion para N-Puzzle.
		processMatrix( problem, steps );
		return problem;
	}
	
	/** Desordena una matriz para N-Puzzle de forma que tenga solucion.
	 * @param matrix : int[][] - Matriz a desrondenar.
	 * @param steps : int - Pasos para generar desorden.
	 */
	private static void processMatrix( int[][] matrix, int steps ) {
		// Precondition: matrix <> null.
		int[] empty = { matrix.length - 1, matrix[0].length - 1 }; // Coordenadas espacio vacio.
		int[] prev = new int[] { -1, -1 }; // Coordenadas anteriores.
		int[] next; // Coordenadas futuras.
		ArrayList<Short> moves; // Lista de movimientos.
		short move; // Movimiento.
		while( steps-- > 0 ) {
			// Se genera la lista de movimientos permitidos.
			moves = listAllowedMoves( empty[0], empty[1], prev[0], prev[1], matrix.length, matrix[0].length );
			// Se obtiene un movimiento aleatorio.
			move = moves.get( Randomizer.randomInt( 0, moves.size() - 1 ) );
			next = new int[] { empty[0] + var[move], empty[1] + var[move + 1] };
			// Se mueve el especio vacio dentro de la matriz.
			emptyMoveInMatrix(matrix, empty[0], empty[1], next[0], next[1]);
			prev = empty;
			empty = next;
		}
	}
	
	/** Retorna una lista de movimientos permitidos.
	 * @param i : int - Coordenada vertical.
	 * @param j : int - Coordenada horizontal.
	 * @param previ : int - Coordenada vertical anterior.
	 * @param prevj : int - Coordenada horizontal anterior.
	 * @param rows : int - Filas.
	 * @param columns : int - Columnas.
	 * @return {@link ArrayList} - Lista de movimientos permitidos.
	 */
	private static ArrayList<Short> listAllowedMoves( int i, int j, int previ, int prevj, int rows, int columns ) {
		ArrayList<Short> moves = new ArrayList<>( 4 );
		for ( short k = 0; k < 4; k++ ) // Verifica los movimientos.
			if ( isAllowed( i + var[k], j + var[k + 1], previ, prevj ) && !isOutOfRange( i + var[k], j + var[k + 1], rows, columns ) )
				moves.add( k );
		return moves;
	}
	
	/** Verifica el rango de una coordenada
	 * @param i : int - Coordenada vertical.
	 * @param j : int - Coordenada horizontal.
	 * @param rows : int - Filas.
	 * @param columns : int - Columnas.
	 * @return boolean - True si la coordenada esta fuera de rango, False en caso contrario.
	 */
	private static boolean isOutOfRange( int i, int j, int rows, int columns ) {
		if ( ( i > ( rows - 1 ) ) || ( j > ( columns - 1 ) ) || ( i < 0 ) || ( j < 0 ) )
			return true;
		return false;
	}
	
	/** Verifica si la coordenada no es un retroceso.
	 * @param i : int - Coordenada vertical.
	 * @param j : int - Coordenada horizontal.
	 * @param previ : int - Coordenada vertical anterior.
	 * @param prevj : int - Coordenada horizontal anterior.
	 * @return boolean - True si la coordenada es vuelta atras, False en caso contrario.
	 */
	private static boolean isAllowed( int i, int j, int previ, int prevj ) {
		if ( ( i != previ ) || ( j != prevj ) )
			return true;
		return false;
	}
	
	/** Genera una matriz problema para N-Puzzle.
	 * @param rows : int - Filas.
	 * @param columns : int - Columnas.
	 * @param steps : int - Complejidad. Pasos para desordenar la matriz.
	 * @return int[][] - La matriz desordenada, con solucion asegurada.
	 */
	static void emptyMoveInMatrix( int[][] matrix, int i1, int j1, int i2, int j2 ) {
		// Precondition: matrix <> null; i1, i2 < matrix.length; j1, j2 < matrix[i].length.
		matrix[i1][j1] = matrix[i2][j2];
		matrix[i2][j2] = 0;
	}
	
	/** Llena de manera ordenada avanzando por columnas luego filas una matriz.
	 * @param matrix : int[][] - Matriz a llenar.
	 * @return int[][] - La matriz llenada.
	 */
	private static int[][] orderlyFillMatrix( int[][] matrix ) {
		// Precondition: matrix <> null.
		int n = 0;
		for( int i = 0; i < matrix.length; i++ )
			for( int j = 0; j < matrix[i].length; j++ )
				matrix[i][j] = ++n;
		matrix[matrix.length-1][matrix[0].length-1] = 0; // Se establece el espacio en blanco.
		return matrix;
	}
}