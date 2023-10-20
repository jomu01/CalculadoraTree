import java.io.*;
import java.net.*;
import java.util.*;
import com.google.gson.Gson;
import javax.swing.SwingUtilities;

import java.text.SimpleDateFormat;

public class Servidor {
    private static final int PUERTO = 12345;
    private static Set<PrintWriter> clientes = new HashSet<>();
    private static boolean serverRunning = false;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Servidor().StartServer();
            }
        });
    }

    private void StartServer() {
        if (!serverRunning) {
            try {
                serverSocket = new ServerSocket(PUERTO);
                serverRunning = true;
                System.out.println("Servidor listo para recibir conexiones...");
                while (serverRunning) {
                    Socket clienteSocket = serverSocket.accept();
                    System.out.println("Cliente conectado desde " + clienteSocket.getInetAddress());
                    PrintWriter out = new PrintWriter(clienteSocket.getOutputStream(), true);
                    clientes.add(out);
                    Thread hiloCliente = new Thread(new ManejadorCliente(clienteSocket, out));
                    hiloCliente.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String fechaActual() {
        String formatoFecha = "dd/MM/yy"; // Define el formato de fecha que deseas
        Date fecha = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(formatoFecha);
        return sdf.format(fecha);
    }

    private static class ManejadorCliente implements Runnable {
        private Socket socket;
        private PrintWriter out;
        private static String nombreArchivo = "productos.csv"; // Reemplaza con el nombre deseado
        private static String rutaCarpeta = "lib";
        private String nuevaLinea;

        //constructor 
        public ManejadorCliente(Socket socket, PrintWriter out) {
            this.socket = socket;
            this.out = out;
        }

        public String evaluarMensaje(String expresion) {
            if (expresion.equalsIgnoreCase("1+3")) {
                return "Si";
            } else {
                return "No";
            }
        }

        @Override
        public void run() {
            
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String mensaje;
                Gson gson = new Gson();
                while ((mensaje = in.readLine()) != null) {
                    //pasar de  Json a tipo Mensaje
                    Mensaje mensajeObj = gson.fromJson(mensaje, Mensaje.class);
                    // a la propiedad resultado le damos el valor que el arbol deberia dar
                    mensajeObj.setResultado(evaluarMensaje(mensajeObj.getExpresion()));
                    // convertimos denuevo a json
                    String jsonMensajeCliente = gson.toJson(mensajeObj);
                    //linea que se supone que voy a apend en el archivo 
                    nuevaLinea = fechaActual()+mensajeObj.getExpresion()+mensajeObj.getResultado();
                    // se crea si no existe el archivo y si existe se agregra la linea con los datos
                    verificarArchivoEnCarpeta(nombreArchivo, rutaCarpeta, nuevaLinea); // Primero verifica y agrega al archivo
                    //se envia el Json al cliente
                    out.println(jsonMensajeCliente);

                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                clientes.remove(out); // Elimina al cliente de la lista cuando se desconecta
            }
        }
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
