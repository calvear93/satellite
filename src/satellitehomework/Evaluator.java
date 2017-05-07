package satellitehomework;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import satellitehomework.astarsearch.AStarSearch;
import satellitehomework.hillclimbingsearch.HillClimbingSearch;
import satellitehomework.simulatedannealingsearch.SimulatedAnnealingSearch;
import satellitehomework.utils.Randomizer;
import satellitehomework.utils.SolutionNotFoundException;

/** Evaluador. Mide velocidad de procesamiento y memoria usada por los algoritmos.
 * @author Cristopher Alvear Candia.
 * @version 1.1
 */
public class Evaluator {
	
	/** Lector de opciones. */
	private static Scanner read;
	
	/** Medidas promedio de rendimiento. */
	// A*
	private static double AStarAverageTime;
	private static double AStarAverageMemory;
	private static double AStarAverageCycles;
	private static int AStarSolutions;
	// Hill Climbing
	private static double HillClimbingAverageTime;
	private static double HillClimbingAverageMemory;
	private static double HillClimbingAverageCycles;
	private static int HillClimbingSolutions;
	// Simulated Annealing
	private static double SimAnnealingAverageTime;
	private static double SimAnnealingAverageMemory;
	private static double SimAnnealingAverageCycles;
	private static int SimAnnealingSolutions;
	
	/** Tiempo limite de ejecucion de los algoritmos. */
	private static short globalTime;
	
	public static void main( String[] args ) throws IOException {
		init();
		while ( true ) switch( showMenu() ) {
			case 1 :
				menu1(); // Menu generacion de N-Puzzles.
				break;
			case 2 :
				menu2(); // Menu de ejecucion de algoritmos.
				break;
			case 3 :
				finish(); // Salir del programa.
		}
	}
	
	/** Inicia las varaibles. */
	private static void init() {
		read = new Scanner( System.in );
		AStarAverageTime = 0;
		AStarAverageMemory = 0;
		AStarAverageCycles = 0;
		AStarSolutions = 0;
		HillClimbingAverageTime = 0;
		HillClimbingAverageMemory = 0;
		HillClimbingAverageCycles = 0;
		HillClimbingSolutions = 0;
		SimAnnealingAverageTime = 0;
		SimAnnealingAverageMemory = 0;
		SimAnnealingAverageCycles = 0;
		SimAnnealingSolutions = 0;
		globalTime = 60;
	}
	
	/** Finaliza el programa. */
	private static void finish() {
		read.close();
		System.out.println( "\tFIN DEL PROGRAMA");
		System.exit( 0 );
	}
	
	/** Muestra menu principal y lee opcion seleccionada. */
	private static int showMenu() {
		System.out.println( "\n\tINFORMED SEARCH MENU" );
		System.out.println( " [1] Generar N-Puzzles." );
		System.out.println( " [2] Ejecutar algoritmos." );
		System.out.print( " [3] Salir.\n[>] : " );
		return readNumber( 1, 3, " [!] Opcion incorrecta!\n[>] :" );
	}
	
	/** Muestra menu generacion de N-Puzzles y lee opcion seleccionada. */
	private static int showMenu1() {
		System.out.println( "\n [1] Generar fichero con N-Puzzles." );
		System.out.println( " [2] Generar y mostrar N-Puzzle." );
		System.out.print( " [3] Retroceder.\n[>] : " );
		return readNumber( 1, 3, " [!] Opcion incorrecta!\n[>] :" );
	}
	
	/** Muestra menu ejecucion algoritmos y lee opcion seleccionada. */
	private static int showMenu2() {
		System.out.println( "\n [1] Ejecutar A*." );
		System.out.println( " [2] Ejecutar Hill Climbing." );
		System.out.println( " [3] Ejecutar Simulated Annealing." );
		System.out.println( " [4] Imprimir sumario." );
		System.out.println( " [5] Cambiar tiempo limite de busqueda." );
		System.out.print( " [6] Retroceder.\n[>] : " );
		return readNumber( 1, 6, " [!] Opcion incorrecta!\n[>] :" );
	}
	
	/** Menu generacion de N-Puzzles. */
	private static void menu1() {
		while ( true ) switch( showMenu1() ) {
		case 1 : // Se genera un fichero con N-Puzzles aleatorios.
			try {
				generateRandomNPuzzle();
			} catch ( IOException e ) {
				System.err.println( " [!] No se pudo generar el fichero con los puzzles!" );
				finish();
			}
			break;
		case 2 : // Se genera un N-Puzzle y se muestra por consola.
			System.out.print( " [>] Ingrese cantidad filas : " );
			int rows = readNumber( 2, 99, " [!] Cantidad invalida!\n[>] :" );
			System.out.print( " [>] Ingrese cantidad columnas : " );
			int columns = readNumber( 2, 99, " [!] Cantidad invalida!\n[>] :" );
			System.out.print( " [>] Ingrese cantidad complejidad (pasos) : " );
			int steps = readNumber( 0, 99999, " [!] Cantidad invalida!\n[>] :" );
			printMatrix( ProblemGenerator.generateProblem( rows, columns, steps ) );
			break;
		case 3 : // Se retorna al menu principal.
			return;
		default :
			System.out.println( " [!] Opcion incorrecta!" );
			break;
		}
	}
	
	/** Menu ejecucion algoritmos y lee opcion seleccionada. */
	private static void menu2() {
		while ( true ) switch( showMenu2() ) {
		case 1 : // Se ejecuta el algoritmo A*.
			try {
				testAStar();
			} catch ( FileNotFoundException e ) {
				System.err.println( " [!] Fichero de problemas no encontrado!" );
				finish();
			}
			break;
		case 2 : // Se ejecuta el algoritmo Hill Climbing.
			try {
				testHillClimbing();
			} catch ( FileNotFoundException e ) {
				System.err.println( " [!] Fichero de problemas no encontrado!" );
				finish();
			}
			break;
		case 3 : // Se ejecuta el algoritmo Simulated Annealing.
			try {
				testSimAnnealing();
			} catch ( FileNotFoundException e ) {
				System.err.println( " [!] Fichero de problemas no encontrado!" );
				finish();
			}
			break;
		case 4 : // Muestra el sumario.
			printSummary();
			break;
		case 5 : // Cambia el tiempo limite de ejecucion de los algositmos.
			System.out.print( " [>] Ingrese tiempo en s (0 sin limite): " );
			short time = read.nextShort();
			globalTime = time;
			AStarSearch.setTimeLimit( time );
			HillClimbingSearch.setTimeLimit( time );
			SimulatedAnnealingSearch.setTimeLimit( time );
			System.out.println( " [i] Operacion exitosa!\n" );
			break;
		case 6 : // Vuelve a menu principal.
			return;
		}
	}
	
	/** Muestra el sumario de resultados promedios. */
	private static void printSummary() {
		System.out.println( "\n [>>] \tSUMMARY" );
		System.out.println( " [>] A*" );
		System.out.printf( "  %-26s %f\n", "Average Time (ms):", AStarAverageTime );
		System.out.printf( "  %-26s %f\n", "Average Memory (nodes):", AStarAverageMemory );
		System.out.printf( "  %-26s %f\n", "Estimated Memory (Bytes):", ( 310 * AStarAverageMemory ) );
		System.out.printf( "  %-26s %f\n", "Average Cycles:", AStarAverageCycles  );
		System.out.printf( "  %-26s %d\n", "Solutions:", AStarSolutions  );
		System.out.println( " [>] Hill Climbing" );
		System.out.printf( "  %-26s %f\n", "Average Time (ms):", HillClimbingAverageTime );
		System.out.printf( "  %-26s %f\n", "Average Memory (nodes):", HillClimbingAverageMemory );
		System.out.printf( "  %-26s %f\n", "Estimated Memory (Bytes):", ( 310 * HillClimbingAverageMemory ) );
		System.out.printf( "  %-26s %f\n", "Average Cycles:", HillClimbingAverageCycles  );
		System.out.printf( "  %-26s %d\n", "Solutions:", HillClimbingSolutions  );
		System.out.println( " [>] Simulated Annealing" );
		System.out.printf( "  %-26s %f\n", "Average Time (ms):", SimAnnealingAverageTime );
		System.out.printf( "  %-26s %f\n", "Average Memory (nodes):", SimAnnealingAverageMemory );
		System.out.printf( "  %-26s %f\n", "Estimated Memory (Bytes):", ( 310 * SimAnnealingAverageMemory ) );
		System.out.printf( "  %-26s %f\n", "Average Cycles:", SimAnnealingAverageCycles  );
		System.out.printf( "  %-26s %d\n", "Solutions:", SimAnnealingSolutions  );
		System.out.println();
	}
	
	/** Ejecuta la prueba de rendimiento para A*. */
	private static void testAStar() throws FileNotFoundException {
		Timer timer = new Timer();
		AStarSearch astar = new AStarSearch();
		Scanner reader = fileReader( "Problems" );
		int iter = 0, solved = 0;
		int[][] problem;
		List<Short> solution;
		System.out.println( "\n\tA* Search" );
		while ( reader.hasNextInt() ) {
			problem = readProblem( reader );
			try {
				timer.start();
				solution = astar.search( problem );
				timer.stop();
				printOutcomes( ++iter, problem, solution, timer.getTimeInMillis(), astar.maxNodes, astar.cycles );
				++solved;
			} catch ( SolutionNotFoundException e ) {
				System.out.println( ++iter + ". SIZE " + problem.length + "x" + problem[0].length );
				printMatrix( problem );
				System.out.println( " [!] Solucion no encontrada en tiempo limite! (" + globalTime + " s)" );
				continue;
			}
		}
		AStarAverageTime /= solved;
		AStarAverageMemory /= solved;
		AStarAverageCycles /= solved;
		AStarSolutions = solved;
	}

	/** Ejecuta la prueba de rendimiento para Hill Climbing. */
	private static void testHillClimbing() throws FileNotFoundException {
		Timer timer = new Timer();
		HillClimbingSearch hill = new HillClimbingSearch();
		Scanner reader = fileReader( "Problems" );
		int iter = 0, solved = 0;
		int[][] problem;
		List<Short> solution;
		System.out.println( "\n\tHill Climbing Search" );
		while ( reader.hasNextInt() ) {
			problem = readProblem( reader );
			try {
				timer.start();
				solution = hill.search( problem );
				timer.stop();
				printOutcomes( ++iter, problem, solution, timer.getTimeInMillis(), hill.maxNodes, hill.cycles );
				++solved;
			} catch ( SolutionNotFoundException e ) {
				System.out.println( ++iter + ". SIZE " + problem.length + "x" + problem[0].length );
				printMatrix( problem );
				System.out.println( " [!] Solucion no encontrada en tiempo limite! (" + globalTime + " s)" );
				continue;
			}
		}
		HillClimbingAverageTime /= solved;
		HillClimbingAverageMemory /= solved;
		HillClimbingAverageCycles /= solved;
		HillClimbingSolutions = solved;
	}
	
	/** Ejecuta la prueba de rendimiento para Simulated Annealing. */
	private static void testSimAnnealing() throws FileNotFoundException {
		Timer timer = new Timer();
		SimulatedAnnealingSearch sim = new SimulatedAnnealingSearch();
		Scanner reader = fileReader( "Problems" );
		int iter = 0, solved = 0;
		int[][] problem;
		List<Short> solution;
		System.out.println( "\n\tSimulated Annealing Search" );
		System.out.print( " [>] Ingrese Temperatura inicial (recomendada 20-160) : " );
		double temperature = readDouble( 0, 9999, " [!] Numero invalido!\n[>] :" );
		System.out.print( " [>] Ingrese Resistencia a enfriamiento (recomendada 0.2-0.99) : " );
		double resistance = readDouble( 0, 0.9999, " [!] Numero invalido!\n[>] :" );
		System.out.print( " [>] Ingrese Limite inferior (recomendado 0.001-1x10e-13) : " );
		double lowerLimit = readDouble( 0, 1, " [!] Numero invalido!\n[>] :" );
		while ( reader.hasNextInt() ) {
			problem = readProblem( reader );
			try {
				timer.start();
				// Valores admisibles : {100, 0.99, 0.001}, {100, 0.92, 0.0000000000001}, {300, 0.99, 0.0000000000001} entre otros.
				solution = sim.search( problem, temperature, resistance, lowerLimit );
				timer.stop();
				printOutcomes( ++iter, problem, solution, timer.getTimeInMillis(), sim.maxNodes, sim.cycles );
				SimAnnealingAverageTime += timer.getTimeInMillis();
				SimAnnealingAverageMemory += sim.maxNodes;
				SimAnnealingAverageCycles += sim.cycles;
				++solved;
			} catch ( IndexOutOfBoundsException | SolutionNotFoundException e ) {
				System.out.println( ++iter + ". SIZE " + problem.length + "x" + problem[0].length );
				printMatrix( problem );
				System.out.println( " [!] Solucion no encontrada en tiempo limite! (" + globalTime + " s)" );
				continue;
			}
		}
		SimAnnealingAverageTime /= solved;
		SimAnnealingAverageMemory /= solved;
		SimAnnealingAverageCycles /= solved;
		SimAnnealingSolutions = solved;
	}
	
	/** Lee y retorna un N-Puzzle desde el fichero de problemas. */
	private static int[][] readProblem( Scanner reader ) {
		int[][] problem = new int[reader.nextInt()][reader.nextInt()];
		for ( int i = 0; i < problem.length; i++ )
			for ( int j = 0; j < problem[i].length; j++ )
				problem[i][j] = reader.nextInt();
		return problem;
	}
	
	/** Imprime los resultados de las soluciones. */
	private static void printOutcomes( int n, int[][] problem, List<Short> solution, double millis, long nodes, int cycles ) {
		System.out.println( n + ". SIZE " + problem.length + "x" + problem[0].length );
		printMatrix( problem );
		System.out.println( " Sol: " + solution );
		System.out.println( " Time (ms): " + millis );
		System.out.println( " Memory (nodes): " + nodes );
		System.out.println( " Cycles: " + cycles );
	}
	
	/** Genera un fichero con problemas N-Puzzle aleatorios. */
	private static void generateRandomNPuzzle() throws IOException {
		PrintWriter writer = new PrintWriter( new FileWriter( "Problems.npuzzle" ) );
		int[][] problem;
		System.out.print( " [>] Ingrese orden maximo (recomendado 6) : " );
		int order = readNumber( 2, 99, " [!] Orden invalido!\n[>] :" );
		System.out.print( " [>] Ingrese complejidad minima (recomendada 10-40) : " );
		int minDifficulty = readNumber( 0, 999, " [!] Complejidad invalida!\n[>] :" );
		System.out.print( " [>] Ingrese complejidad maxima (recomendada 40-85) : " );
		int maxDifficulty = readNumber( minDifficulty, 99999, " [!] Complejidad invalida!\n[>] :" );
		System.out.print( " [>] ¿ Cuantas muestras de cada orden desea ? : " );
		int samples = readNumber( 1, 99, " [!] Cantidad de muestras invalida!\n[>] :" );
		
		for ( int level = 2; level < order; level++ ) {
			for ( int sample = 0; sample < samples; sample++ ) {
				problem = ProblemGenerator.generateProblem( level, Randomizer.randomInt( level, level + Randomizer.randomInt( 0, 4 ) ), Randomizer.randomInt( minDifficulty, maxDifficulty ) );
				writer.println( level + " " + problem[0].length );
				for( int[] vector : problem ) {
					for( int element : vector )
						writer.print( element + " " );
					writer.println();
				}
			}
		}
		writer.close();
	}
	
	/** Lee un entero desde teclado. */
	private static int readNumber( int a, int b, String msg ) {
		// a limite inferior, b limite superior, msg mensaje a mostrar si sale de rango.
		try {
			int n = read.nextInt();
			read.nextLine(); // Limpia el flujo de entrada.
			if ( n >= a && n <= b )
				return n;
			System.out.println( msg );
			return readNumber( a, b, msg );
		} catch ( InputMismatchException e ) {
			System.out.println( " [!] Dato incorrecto! Ingrese un numero!" );
			read.nextLine(); // Limpia el flujo de entrada.
			return readNumber( a, b, msg );
		}
	}
	
	/** Lee un decimal desde teclado. */
	private static double readDouble( double a, double b, String msg ) {
		// a limite inferior, b limite superior, msg mensaje a mostrar si sale de rango.
		while ( true ) try {
			double n = read.nextDouble();
			read.nextLine(); // Limpia el flujo de entrada.
			if ( n >= a && n <= b )
				return n;
			System.out.println( msg );
			return readDouble( a, b, msg );
		} catch ( InputMismatchException e ) {
			System.out.println( " [!] Dato incorrecto! Ingrese un numero!" );
			read.nextLine(); // Limpia el flujo de entrada.
			return readDouble( a, b, msg );
		}
	}
	
	/** Imprime una matriz. */
	private static void printMatrix( int[][] matrix ) {
		for( int[] vector : matrix ) {
			for( int element : vector )
				System.out.printf( "[%-2s]", element );
			System.out.println();
		}
	}
	
	/** Escritor a fichero. */
	static Scanner fileReader( String directory ) throws FileNotFoundException {
		return new Scanner( new FileReader( directory + ".npuzzle" ) );
	}
}