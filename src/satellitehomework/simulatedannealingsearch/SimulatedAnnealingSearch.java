package satellitehomework.simulatedannealingsearch;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import satellitehomework.utils.NodeState;
import satellitehomework.utils.Randomizer;
import satellitehomework.utils.SolutionNotFoundException;

/** Busqueda informada Simulated Annealing. Utiliza un nivel de entropia.
 * @author Cristopher Alvear Candia.
 * @version 1.1
 */
public class SimulatedAnnealingSearch {
	
	/** Lista de nodos generados. */
	private List<NodeState> childs;
	/** Tiempo limite de ejecucion si no encuentra solucion (ms). */
	private static long timeLimit = 60000; // 60 s limite.
	/** Tiempo de inicio (ms). */
	private static long initTime;
	
	
	/** Variables de rendimiento. */
	public long maxNodes; // Maximo de nodos almacenado en cola.
	public int cycles; // Cantidad de ciclos realizados.
	
	/** Busqueda Simulated Annealing o Enfriamiento/Recocido Simulado.
	 * @param problem : int[][] - Matriz problema - Estado inicial.
	 */
	public List<Short> search( int[][] problem, double temperature, double resistance, double lowerLimit ) throws SolutionNotFoundException, IndexOutOfBoundsException {
		// Precondition: problem <> null.
		beginPerformanceTest(); // Inicia test de rendimiento.
		double entropy = Math.log( temperature ); // Entropia.
		SimulatedAnnealingComparator comparator = new SimulatedAnnealingComparator();
		int index; // Almacenara el indice calculado por ciclo.
		NodeState node; // Almacena el nodo revisado.
		childs = new LinkedList<>();
		childs.add( new NodeState( problem ) );

		while ( entropy > lowerLimit && verifyTime() ) {
			temperature = Math.exp( entropy ); // Calculo de la temperatura.
			entropy *= resistance; // Enfriamiento segun resistencia.
			index = childs.size() - 1 - Randomizer.randomInt( calculatePos( temperature, childs.size() ), childs.size() - 1 );
			node = childs.get( index ); // Se obtiene el candidato.
			childs.remove( node ); // Remueve el nodo revisado.
			if ( node.isGoalState() ) // Si encuentra un estado con distancia estimada 0 retorna la solucion.
				return node.moves;
			childs.addAll( node.generateChildren() ); // Agrega los hijos del nodo revisado.
			Collections.sort( childs, comparator ); // Ordena la lista (menor costo menor indice).
			saveNodeStatistics( childs.size() ); // Guarda estadisticas de nodo y ciclos.
		}
		throw new SolutionNotFoundException( " [!] Solucion no encontrada!" );
	}
	
	/** Metodo que calcula la posicion maxima dada una temperatura. 
	 * @param temperature : int - Temperatura actual.
	 * @param limit : int - Limite/Longitud de la lista.
	 * @return int - Numero aleatorio dentro del rango [a,b].
	 */
	private int calculatePos( double temperature, int limit ) {
		 return ( int ) ( ( 1 / temperature ) * limit );
	}
	
	/** Establece el tiempo limite maximo cuando no se encuentra una solucion.
	 * @param seconds : short - Tiempo limite maximo en segundos.
	 */
	public static void setTimeLimit( short seconds ) {
		timeLimit = seconds * 1000;
	}
	
	/** Verifica el limite de tiempo. */
	private boolean verifyTime() throws SolutionNotFoundException {
		if ( timeLimit == 0 ) return true;
		if ( System.currentTimeMillis() - initTime < timeLimit )
			return true;
		throw new SolutionNotFoundException( " [!] Solucion no encontrada en tiempo limite!" );
	}
	
	/** Comparador de Simulated Annealing. Compara la distancia estimada para alcanzar
	 * el objetivo.
	 */
	private class SimulatedAnnealingComparator implements Comparator<NodeState> {
		@Override
		public int compare( NodeState node1, NodeState node2 ) {
			if ( node1.getDistanceEstimatedToGoal() > node2.getDistanceEstimatedToGoal() )
				return 1;
			if( node1.getDistanceEstimatedToGoal() < node2.getDistanceEstimatedToGoal() )
				return -1;
			return 0;
		}
	}
	
	/** PERFORMANCE - Inicia el timer y las variables de rendimiento. */
	private void beginPerformanceTest() {
		maxNodes = 0;
		cycles = 0;	
		initTime = System.currentTimeMillis();
	}
	
	/** PERFORMANCE - Contabiliza los ciclos y calcula estadisticas de nodos. */
	private void saveNodeStatistics( long currentNodes ) {
		cycles++;
		maxNodes = maxNodes < currentNodes ? currentNodes : maxNodes;
	}
}