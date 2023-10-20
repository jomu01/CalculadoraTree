import java.io.File;
import net.sourceforge.tess4j.*;

public class Teseract {
    public static void main(String[] args) {
        File imageFile = new File("C:/Users/HP/Desktop/ImagenTeseract/Texto1.jpg");
        
        // Configura la ubicación de los archivos de datos de entrenamiento
        ITesseract instance = new Tesseract();
        instance.setDatapath("C:/Users/HP/Desktop/Tess4J/tessdata"); // Reemplaza con la ruta correcta
        
        try {
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    } 
}
/* 
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;

public class Teseract {

    public static void main(String[] args) {
        System.setProperty("java.library.path", "C:/Users/HP/Desktop/opencv/build/java/x64");

        // Cargar la biblioteca OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Inicializar la captura de video desde la cámara (cambiar el índice si es necesario)
        VideoCapture capture = new VideoCapture(0);

        // Verificar si la cámara se abrió correctamente
        if (!capture.isOpened()) {
            System.out.println("Error al abrir la cámara.");
            return;
        }

        // Capturar una imagen desde la cámara
        Mat frame = new Mat();
        capture.read(frame);

        // Guardar la imagen en un archivo
        String outputFile = "captured_image.jpg";
        Imgcodecs.imwrite(outputFile, frame);

        // Liberar la cámara
        capture.release();

        System.out.println("Imagen guardada como " + outputFile);
    }
}
 */