/**
 * Clase que representa el objeto Flyweight para una Canción.
 * 
 * Aplicación del Patrón Flyweight:
 * Esta clase almacena el ESTADO INTRÍNSECO (información común e inmutable de la canción).
 * Al ser inmutable, una misma instancia de esta clase puede ser compartida de forma
 * segura por múltiples listas de reproducción y accederse concurrentemente desde varios hilos.
 */
public class CancionFlyweight {
    
    // ATRIBUTOS (Estado Intrínseco Inmutable)
    private final int duracion;
    private final int id;
    private final String titulo;
    private final String artista;
    private final String album;

    /**
     * Constructor para inicializar el objeto Flyweight con todos sus atributos intrínsecos.
     * 
     * @param titulo   Título de la canción
     * @param artista  Nombre del artista o banda
     * @param album    Álbum al que pertenece
     * @param duracion Duración en segundos
     * @param id       Identificador único de la canción
     */
    public CancionFlyweight(String titulo, String artista, String album, int duracion, int id) {
        this.album = album;
        this.titulo = titulo;
        this.artista = artista;
        this.duracion = duracion;
        this.id = id;
    }

    
    // MÉTODOS GETTER (Lectura de Estado Intrínseco)
    // No se incluyen métodos setters para garantizar la inmutabilidad de la instancia.
   

    public String getTitulo() {
        return titulo;
    }

    public String getArtista() {
        return artista;
    }

    public String getAlbum() {
        return album;
    }

    public int getDuracion() {
        return duracion;
    }

    public int getId() {
        return id;
    }

    
    // COMPARACIÓN Y REUTIILIZACIÓN DE INSTANCIAS
   

    /**
     * Compara si dos objetos CancionFlyweight representan la misma canción
     * basándose en su identificador único (ID).
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CancionFlyweight that = (CancionFlyweight) o;
        return id == that.id;
    }

    /**
     * Genera un código Hash único basado en el ID para colecciones optimizadas.
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    /**
     * Devuelve una representación en texto del estado intrínseco de la canción.
     */
    @Override
    public String toString() {
        return "[" + id + "] " + titulo + " - " + artista + " (" + album + ", " + duracion + "s)";
    }
}
