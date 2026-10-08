import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase que actúa como el Flyweight Factory (Fábrica Flyweight).
 * 
 * Aplicación del Patrón Flyweight y Concurrencia:
 * 1. Administra y centraliza la creación y reutilización de las instancias compartidas (CancionFlyweight).
 * 2. Garantiza que no se creen objetos duplicados para una misma canción.
 * 3. Incorpora sincronización (synchronized) en sus métodos para asegurar que el acceso 
 *    concurrente desde múltiples hilos no genere condiciones de carrera en el catálogo compartido.
 */
public class CancionFactory {


    // ATRIBUTOS (Recursos Compartidos)
    
    // Colección interna que actúa como el catálogo central de objetos Flyweight
    private ArrayList<CancionFlyweight> listaCanciones = new ArrayList<>();
    
    // Secuenciador automático para asignar un ID único a cada canción nueva
    private int contadorId = 1;

    // MÉTODOS PRINCIPALES (Regiones Críticas Sincronizadas)


    /**
     * Devuelve una instancia existente de CancionFlyweight si coincide en Título y Artista,
     * o crea una nueva si aún no está registrada en el catálogo.
     * 
     * Región Crítica: El método es 'synchronized' para evitar que dos hilos creen 
     * instancias duplicadas simultáneamente al solicitar la misma canción al mismo tiempo.
     * 
     *  titulo   Título de la canción
     *  artista  Artista de la canción
     *  album    Álbum de la canción
     *  duracion Duración en segundos
     */
    public synchronized CancionFlyweight getCancion(String titulo, String artista, String album, int duracion) {
        
        // 1. BÚSQUEDA Y REUTILIZACIÓN: Revisa si la canción ya existe en el catálogo compartido
        for (CancionFlyweight c : listaCanciones) {
            if (c.getTitulo().equalsIgnoreCase(titulo) && c.getArtista().equalsIgnoreCase(artista)) {
                System.out.println("[FACTORY] Instancia REUTILIZADA (ID " + c.getId() + "): " + titulo);
                return c; // Retorna la instancia existente (Flyweight compartido)
            }
        }

        // 2. CREACIÓN: Si no existe, genera el ID y crea el nuevo objeto Flyweight
        int nuevoId = contadorId++;
        CancionFlyweight nuevaCancion = new CancionFlyweight(titulo, artista, album, duracion, nuevoId);
        listaCanciones.add(nuevaCancion);
        
        System.out.println("[" + Thread.currentThread().getName() + "] Nueva instancia CREADA con ID " + nuevoId + ": " + titulo);
        return nuevaCancion;
    }

    /**
     * Carga de forma masiva canciones desde un archivo de texto externo (.txt)
     * e incorpora sus datos al catálogo utilizando el patrón Flyweight.
     * 
     * rutaArchivo Ruta relativa o absoluta del archivo .txt a leer
     */
    public synchronized void cargarDesdeArchivo(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                String[] datos = linea.split(",");
                
                // Formato esperado: ID, Título, Artista, Álbum, Duración
                if (datos.length >= 5) {
                    int id = Integer.parseInt(datos[0].trim());
                    String titulo = datos[1].trim();
                    String artista = datos[2].trim();
                    String album = datos[3].trim();          
                    int duracion = Integer.parseInt(datos[4].trim()); 

                    // Registro a través de la fábrica para asegurar reutilización
                    getCancion(titulo, artista, album, duracion);
                }
            }
            System.out.println("[FACTORY] Archivo cargado correctamente.");
        } catch (IOException e) {
            System.out.println("[FACTORY] Error al leer archivo: " + e.getMessage());
        }
    }

    /**
     * Consulta el número total de instancias únicas creadas en memoria.
     * @return Cantidad de objetos CancionFlyweight almacenados en el catálogo.
     */
    public synchronized int getCantidad() {
        return listaCanciones.size();
    }
}  

