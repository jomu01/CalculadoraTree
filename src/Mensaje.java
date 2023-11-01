
// Clase para representar un mensaje
import java.util.Date;
import java.text.SimpleDateFormat;

public class Mensaje {
    private String nomarchivo;
    private String expresion;
    private String resultado;

    // Constructor
    public Mensaje(String nomarchivo, String expresion) {
        this.nomarchivo = nomarchivo;
        this.expresion = expresion;
    }

    public String getComando() {
        return nomarchivo;
    }

    // Getter y Setter para el resultado
    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
    public String getExpresion() {
        return expresion;
    }

    public String fechaActual() {
        String formatoFecha = "dd/MM/yy"; // Define el formato de fecha que deseas
        Date fecha = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
        return sdf.format(fecha);
    }

  /*   public String impress(){
        return fechaActual()+","+this.getExpresion();
    } */
}
