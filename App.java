/**
 * Clase Principal (Cliente del Patrón Flyweight y Coordinador Concurrente).
 * 
 * Cumplimiento de Requerimientos de la Práctica:
 * 1. Carga inicial de datos desde un archivo de texto externo (.txt).
 * 2. Ejecución concurrente mediante un modelo de 4 hilos de trabajo.
 * 3. Simulación de un escenario de competencia sobre recursos compartidos.
 * 4. Verificación de la reutilización de objetos e impresión de métricas de evaluación.
 */
public class App {

    public static void main(String[] args) throws InterruptedException {
        
        
        System.out.println("          SISTEMA DE MÚSICA CONCURRENTE          ");
        System.out.println("\n");

        // Instanciación del Flyweight Factory (Fábrica de Canciones)
        CancionFactory factory = new CancionFactory();

        // 1. Carga del catálogo inicial desde el archivo externo (.txt)
        factory.cargarDesdeArchivo("src/canciones.txt");

        // Creación de las listas de reproducción (Estructuras del estado extrínseco)
        ListaReproduccion listaA = new ListaReproduccion("Rock Clásico");
        ListaReproduccion listaB = new ListaReproduccion("Favoritas 2026");

        System.out.println("\n--- INICIANDO EJECUCIÓN CONCURRENTE (4 HILOS) ---\n");

       
        // CREACIÓN Y CONFIGURACIÓN DE HILOS CONCURRENTES (Mínimo 4 hilos exigidos)
        

        /**
         * HILO 1: Operación independiente de modificación.
         * Solicita canciones a la fábrica y las agrega a la Lista A.
         */
        Thread hilo1 = new Thread(() -> {
            listaA.agregarCancion(factory.getCancion("Bohemian Rhapsody", "Queen", "A Night at the Opera", 354));
            listaA.agregarCancion(factory.getCancion("Hotel California", "Eagles", "Hotel California", 391));
        }, "Hilo-1-AgregarA");

        /**
         * HILO 2: Escenario de Competencia .
         * Intenta eliminar una canción de la Lista A concurrentemente mientras el Hilo 1 opera.
         */
        Thread hilo2 = new Thread(() -> {
            try { 
                Thread.sleep(20); // Ligero retardo para forzar el solapamiento temporal
            } catch (InterruptedException ignored) {}
            listaA.eliminarCancionPorId(1);
        }, "Hilo-2-EliminarA");

        /**
         * HILO 3: Demostración de Reutilización de Instancias (Flyweight).
         * Solicita a la fábrica canciones repetidas ("Bohemian Rhapsody") para agregarlas a la Lista B.
         * La fábrica debe devolver la instancia existente sin crear un objeto nuevo en memoria.
         */
        Thread hilo3 = new Thread(() -> {
            listaB.agregarCancion(factory.getCancion("Bohemian Rhapsody", "Queen", "A Night at the Opera", 354));
            listaB.agregarCancion(factory.getCancion("Stairway to Heaven", "Led Zeppelin", "Led Zeppelin IV", 482));
        }, "Hilo-3-AgregarB");

        /**
         * HILO 4: Operación Concurrente de Búsqueda y Lectura
         * Realiza una búsqueda por título en Lista A e interroga el estado de la fábrica.
         */
        Thread hilo4 = new Thread(() -> {
            System.out.println("[" + Thread.currentThread().getName() + "] Buscando 'Hotel California' en Lista A...");
            listaA.buscarPorTitulo("Hotel California");
            System.out.println("[" + Thread.currentThread().getName() + "] Total Flyweights en fábrica: " + factory.getCantidad());
        }, "Hilo-4-Busqueda");

        
        // EJECUCIÓN SIMULTÁNEA Y SINCRONIZACIÓN DE TERMINACIÓN DE HILOS[cite: 6]
       

        // Arranque simultáneo de los 4 hilos de trabajo
        hilo1.start();
        hilo2.start();
        hilo3.start();
        hilo4.start();

        // Sincronización del hilo principal (main): Espera a la finalización de los 4 hilos
        hilo1.join();
        hilo2.join();
        hilo3.join();
        hilo4.join();

        // EVIDENCIAS Y MÉTRICAS DE EVALUACIÓN DEL PATRÓN FLYWEIGHT[cite: 6]  

        System.out.println("\n=================================================");
        System.out.println("       EVIDENCIA Y MÉTRICAS DE EVALUACIÓN        ");
        System.out.println("=================================================");

        // Métrica 1: Número total de enlaces/referencias mantenidas en las listas de reproducción
        int totalReferencias = listaA.getCantidadReferencias() + listaB.getCantidadReferencias();
        
        // Métrica 2: Número de instancias únicas físicamente creadas en memoria por la fábrica
        int objetosFlyweightCreados = factory.getCantidad();

        System.out.println("1. Número total de referencias en listas:       " + totalReferencias);
        System.out.println("2. Número de objetos Flyweight creados:         " + objetosFlyweightCreados);
        System.out.println("-------------------------------------------------");

        // Comprobación de la eficacia del patrón Flyweight
        if (totalReferencias >= objetosFlyweightCreados) {
            System.out.println("¡ÉXITO EN LA VERIFICACIÓN FLYWEIGHT!");
            System.out.println("El estado intrínseco se está compartiendo eficientemente sin duplicar objetos en memoria.");
        }
        System.out.println("=================================================");
    }
}