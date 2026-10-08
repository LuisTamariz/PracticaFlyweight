import java.util.ArrayList;

/**
 * Clase que gestiona una lista de reproducción específica dentro del sistema.
 * 
 * Aplicación del Patrón Flyweight y Concurrencia:
 * 1. Actúa como el contenedor del ESTADO EXTRÍNSECO al almacenar objetos ElementoLista,
 *    los cuales vinculan las canciones compartidas (Flyweight) con su contexto en esta lista.
 * 2. Protege todas las operaciones de modificación y lectura mediante sincronización (synchronized)
 *    para evitar condiciones de carrera cuando múltiples hilos intentan modificar la lista simultáneamente.
 */
public class ListaReproduccion {

    
    // ATRIBUTOS (Recursos Compartidos y Estado Extrínseco)
    
    
    private String nombre;
    
    // Colección de elementos que relacionan la canción Flyweight con su posición y fecha en esta lista
    private ArrayList<ElementoLista> elementos = new ArrayList<>();

    /**
     * Constructor para inicializar la lista de reproducción con su nombre.
     * 
     * nombre Nombre identificador de la lista de reproducción.
     */
    public ListaReproduccion(String nombre) {
        this.nombre = nombre;
    }

    // MÉTODOS SINCRONIZADOS (Regiones Críticas) 

    /**
     * Agrega una canción a la lista de reproducción asociándole su posición actual.
     * 
     * Región Crítica: Sincronizado para asegurar la consistencia del tamaño de la lista
     * y la asignación correcta de la posición cuando varios hilos agregan elementos concurrentemente.
     * 
     * cancion Instancia del objeto Flyweight (estado intrínseco compartido).
     */
    public synchronized void agregarCancion(CancionFlyweight cancion) {
        int nuevaPos = elementos.size() + 1;
        ElementoLista elem = new ElementoLista(cancion, nuevaPos);
        elementos.add(elem);
        
        System.out.println("[" + Thread.currentThread().getName() + "] Agregada '" 
                + cancion.getTitulo() + "' a la lista " + nombre + " en pos " + nuevaPos);
    }

    /**
     * Elimina una canción de la lista según su ID y reordena automáticamente 
     * las posiciones del estado extrínseco de las canciones restantes.
     * 
     * Región Crítica: Sincronizado para evitar lecturas/escrituras inconsistentes en escenarios
     * donde un hilo elimina mientras otro hilo agrega o consulta la misma lista.
     * 
     *id Identificador único de la canción a remover.
     * return true si la canción fue encontrada y eliminada; false en caso contrario.
     */
    public synchronized boolean eliminarCancionPorId(int id) {
        boolean eliminado = false;
        
        // Búsqueda del elemento dentro de la lista
        for (int i = 0; i < elementos.size(); i++) {
            if (elementos.get(i).getCancion().getId() == id) {
                elementos.remove(i);
                eliminado = true;
                System.out.println("[" + Thread.currentThread().getName() + "] ELIMINADA canción ID: " + id + " de lista " + nombre);
                break;
            }
        }

        // Reordenamiento del estado extrínseco (posición) tras la eliminación
        if (eliminado) {
            for (int i = 0; i < elementos.size(); i++) {
                elementos.get(i).setPosicion(i + 1);
            }
        }

        return eliminado;
    }

    /**
     * Realiza una búsqueda por título dentro de los elementos de la lista.
     * 
     *  titulo Título de la canción a buscar (no distingue mayúsculas/minúsculas).
     * return Colección de objetos ElementoLista que coinciden con el título buscado.
     */
    public synchronized ArrayList<ElementoLista> buscarPorTitulo(String titulo) {
        ArrayList<ElementoLista> resultados = new ArrayList<>();
        for (ElementoLista elem : elementos) {
            if (elem.getCancion().getTitulo().equalsIgnoreCase(titulo)) {
                resultados.add(elem);
            }
        }
        return resultados;
    }

    /**
     * Obtiene el número total de referencias/enlaces almacenados en esta lista.
     * Método clave para las métricas de evaluación del patrón Flyweight.
     * 
     *  Cantidad de elementos contenidos en la lista de reproducción.
     */
    public synchronized int getCantidadReferencias() {
        return elementos.size();
    }

  
    // MÉTODOS DE ACCESO (GETTERS)
    

    public String getNombre() {
        return nombre;
    }
}