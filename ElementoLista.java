
import java.time.LocalDateTime;

/**
 * Clase que representa el contexto o enlace entre una canción compartida y una lista específica.
 * 
 * Aplicación del Patrón Flyweight:
 * Esta clase almacena y gestiona el ESTADO EXTRÍNSECO.
 * Guarda la información que depende del contexto particular de uso, como la posición de 
 * la canción dentro de una lista y la fecha/hora exacta en la que fue agregada.
 */
public class ElementoLista {

    
    // ATRIBUTOS (Estado Extrínseco y Referencia al Flyweight)
    
    
    // Referencia a la instancia compartida (Estado Intrínseco)
    private final CancionFlyweight cancion;
    
    // Posición dentro de la lista (Estado Extrínseco mutable)
    private int posicion;
    
    // Fecha y hora de incorporación a la lista (Estado Extrínseco inmutable)
    private final LocalDateTime fechaAgregacion;

    /**
     * Constructor para asociar una canción Flyweight con su contexto en una lista.
     * 
     *  cancion  Instancia compartida de la canción (Flyweight)
     *  posicion Posición ordinal en la que se coloca dentro de la lista
     */
    public ElementoLista(CancionFlyweight cancion, int posicion) {
        this.cancion = cancion;
        this.posicion = posicion;
        // Asignación automática del estado extrínseco temporal (fecha/hora actual)
        this.fechaAgregacion = LocalDateTime.now();
    }
    // MÉTODOS DE ACCESO (GETTERS Y SETTERS)

    /**
     * Obtiene la referencia a la canción Flyweight compartida.
    
     */
    public CancionFlyweight getCancion() {
        return cancion;
    }

    /**
     * Obtiene la posición actual del elemento en la lista.
     */
    public int getPosicion() {
        return posicion;
    }

    /**
     * Obtiene el registro de fecha y hora en que se agregó la canción.
     */
    public LocalDateTime getFechaAgregacion() {
        return fechaAgregacion;
    }

    /**
     * Modifica la posición del elemento.
     * Permite reordenar la lista cuando se eliminan o desplazan elementos.
     * @param posicion Nueva posición dentro de la lista.
     */
    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }


    /**
     * Muestra la combinación del estado extrínseco (posición, fecha) 
     * con la representación del estado intrínseco (canción).
     */
    @Override
    public String toString() {
        return "Posicion dentro de la lista " + posicion + " " + cancion.toString() + " Agregado: " + fechaAgregacion;
    }
}
