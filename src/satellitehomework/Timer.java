package satellitehomework;

/** Clase Timer para probar la velocidad de ejecucion de los programas.
 * @author Cristopher Alvear Candia.
 * @version 2.0
 */
class Timer {

	/** Tiempo del sistema en nanosegundos en el que comenzo el timer. */
	private long startTime;
	/** Tiempo del sistema en nanosegundos en el que finalizo el timer. */
	private long endTime;

	/** Constructor por defecto. */
	Timer() {}
	

	/** Inicia el timer */
	void start() {
		startTime = System.nanoTime();
	}
	
	/** Finaliza el timer */
	void stop() {
		endTime = System.nanoTime();
	}
	
	/** Retorna el tiempo en nanosegundos */
	long getTimeInNanos() {
		return endTime - startTime;
	}
	
	/** Retorna el tiempo en ms (milisegundos) */
	double getTimeInMillis() {
		return ( double ) getTimeInNanos() / 1000000;
	}
	
	/** Retorna el tiempo en segundos */
	double getTimeInSeconds() {
		return ( double ) getTimeInMillis() / 1000;
	}
	
	/** Retorna el tiempo en minutos */
	double getTimeInMinutes() {
		return ( double ) getTimeInSeconds() / 60;
	}
}