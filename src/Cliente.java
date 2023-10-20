import javax.swing.*;
import com.google.gson.Gson;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Cliente {
    private JTextField mensajeField;
    private JButton traducirButton;
    Gson gson = new Gson();
    private Socket socket;
    private PrintWriter out;

    public Cliente() {
        //Interfaz
        JFrame frame = new JFrame("Cliente de Chat");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(500, 400);
        JPanel panel = new JPanel(new GridBagLayout());
        mensajeField = new JTextField(20);
        GridBagConstraints mensajeConstraints = new GridBagConstraints();
        mensajeConstraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(mensajeField, mensajeConstraints);
        frame.getContentPane().add(panel);
        frame.setVisible(true);

        JLabel label = new JLabel("Este es un JLabel oculto");
        label.setVisible(false);
        // Agrega el JLabel al panel
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 9; // Coloca el JLabel en la primera columna
        labelConstraints.gridy = 28; // Cambia la fila según donde desees que aparezca
        panel.add(label, labelConstraints);

        //Para que sirva la X de cerrar la ventana
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    if (socket != null && !socket.isClosed()) {
                        socket.close();
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                System.exit(0);
            }
        });
//Boton que pasa los datos de la imagen seleccionada a texto y luego al servidor
        traducirButton = new JButton("Traducir Imagen e Enviar expresion");
        traducirButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                traducirImagen(); // Llama a la función para traducir la imagen
            }
        });
        GridBagConstraints traducirConstraints = new GridBagConstraints();
        traducirConstraints.gridx = 0;
        traducirConstraints.gridy = 2;
        panel.add(traducirButton, traducirConstraints);
        //bucle que escucha y recibe el cliente

        // Botón para enviar la expresión que esta en el txt al servidor
        JButton enviarButton = new JButton("Enviar al Servidor");
        enviarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enviardesdeTxt(); // Llama a la función para enviar la expresión al servidor
            }
        });
        GridBagConstraints enviarConstraints = new GridBagConstraints();
        enviarConstraints.gridx = 5; // Coloca el botón en la segunda columna
        enviarConstraints.gridy = 2; // Coloca el botón en la misma fila que el botón anterior
        panel.add(enviarButton, enviarConstraints);

        try {
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);

            // Agrega un oyente para recibir mensajes del servidor
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    String mensaje;
                    try {
                        while ((mensaje = in.readLine()) != null) {
                            Mensaje ObjetoMensaje = gson.fromJson(mensaje, Mensaje.class);
                            label.setText(ObjetoMensaje.getResultado());
                            label.setVisible(true);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //funcion de seleccionar imagen y convertir en string  para convert a json y al servidor
    private void traducirImagen() {
        String expresion = ""; // Valor por defecto
        try {
            File imageFile = new File("C:/Users/HP/Desktop/ImagenTeseract/Texto1.jpg");
            ITesseract instance = new Tesseract();
            instance.setDatapath("C:/Users/HP/Desktop/Tess4J/tessdata"); // Reemplaza con la ruta correcta
            expresion = instance.doOCR(imageFile);
            enviarAlcliente(expresion);   
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir detalles de la excepción para depuración
            // Manejo de la excepción aquí si es necesario
        }
    }

    public void enviardesdeTxt(){
        String textodeltxtx=mensajeField.getText();
        enviarAlcliente(textodeltxtx);
    }

    public void enviarAlcliente(String expresion){
        try {
            Mensaje mensajeCliente = new Mensaje("EVALUAR", expresion);
            String jsonMensajeCliente = gson.toJson(mensajeCliente);
            out.println(jsonMensajeCliente);
            System.out.println("Se envio  " + expresion);
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error al traducir o enviar el mensaje de la imagen.");
        }
    }
    //main que ejecuta el mismo cliente
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Cliente();
            }
        });
    }
}
