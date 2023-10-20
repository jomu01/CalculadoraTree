import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class pruebaarchivos {
    public static void main(String[] args) {
        String FechaActual= fechaActual();
        String nombreArchivo = "productos.csv"; // Reemplaza con el nombre deseado
        String nuevaLinea = FechaActual+",pedo,orines"; // Nueva línea de datos a agregar
        String rutacarpeta = "lib";
        verificarArchivoEnCarpeta(nombreArchivo, rutacarpeta, nuevaLinea);
    }

    public static String fechaActual(){
        String formatoFecha = "dd/MM/yy"; // Define el formato de fecha que deseas
        Date fecha = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
        return sdf.format(fecha);
    }

    public static void verificarArchivoEnCarpeta(String nombreArchivo, String rutaCarpeta, String nuevaLinea) {
        // Concatena la ruta de la carpeta con el nombre del archivo
        String rutaCompleta = rutaCarpeta + "/" + nombreArchivo;

        // Verifica si el archivo existe
        File archivo = new File(rutaCompleta);
        if (archivo.exists()) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true));
                writer.newLine(); // Agregar una línea en blanco
                writer.write(nuevaLinea);
                writer.close();
                System.out.println("Nueva línea agregada al archivo CSV.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));
                writer.write("Fecha,Expresion,Resultado");
                writer.newLine();
                writer.write(nuevaLinea);
                writer.close();
                System.out.println("Archivo CSV creado con éxito y se agregó la primera línea de datos.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
