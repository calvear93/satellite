package satellitehomework.hillclimbingsearch;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import satellitehomework.utils.NodeState;
import satellitehomework.utils.SolutionNotFoundException;

/** Busqueda informada Hill Climbing. Utiliza la heuristica de la suma de distancias relativas
 * hacia la posicion correcta de cada casilla.
 * @author Cristopher Alvear Candia.
 * @version 1.4
 */
public class HillClimbingSearch {
	
	/** Cola de prioridad. */
	private PriorityQueue<NodeState> queue;
	/** Tiempo limite de ejecucion si no encuentra solucion (ms). */
	private static long timeLimit = 60000; // 60 s limite.
	/** Tiempo de inicio (ms). */
	private static long initTime;
	
	
	/** Variables de rendimiento. */
	public long maxNodes; // Maximo de nodos almacenado en cola.
	public int cycles; // Cantidad de ciclos realizados.
	
	/** Busqueda Hill Climbing o Escalado de Colina.
	 * @param problem : int[][] - Matriz problema - Estado Inicial.
	 */
	public List<Short> search( int[][] problem ) throws SolutionNotFoundException {
		// Precondition: problem <> null; problem.length > 1.
		beginPerformanceTest(); // Inicia test de rendimiento.
		HillClimbingComparator comparator = new HillClimbingComparator();
		queue = new PriorityQueue<>( 1, comparator ); // Se inicializa la cola de prioridad.
		NodeState node; // Almacena el nodo revisado.
		queue.add( new NodeState( problem ) ); // Agrega el nodo con el estado inicial a la cola.
		while ( !queue.isEmpty() && verifyTime() ) {
			node = queue.poll(); // Obtiene el nodo con la menor distancia estimada asociada.
			if ( node.isGoalState() ) // Si encuentra un estado con distancia estimada 0 retorna la solucion.
				return node.moves;
			queue.addAll( node.generateChildren() ); // Agrega los hijos del nodo revisado.
			saveNodeStatistics( queue.size() ); // Guarda estadisticas de nodo y ciclos.
		}
		throw new SolutionNotFoundException( " [!] Solucion no encontrada!" );
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
	
	/** Comparador de Hill Climbing. Compara la distancia estimada para alcanzar
	 * el objetivo.
	 */
	private class HillClimbingComparator implements Comparator<NodeState> {
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