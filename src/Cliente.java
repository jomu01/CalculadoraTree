import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import com.google.gson.Gson;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import java.awt.*;
import java.awt.event.*;
//import java.io.File;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private JTextField mensajeField;
    private JButton traducirButton;
    public  String nomarchivo;

    Gson gson = new Gson();
    private Socket socket;
    private PrintWriter out;

    public Cliente() {
        //Interfaz
        nomarchivo =JOptionPane.showInputDialog(this, "Enter image name");
        JFrame frame = new JFrame("Cliente de Chat");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(500, 200);
        JPanel panel = new JPanel(new GridBagLayout());
        mensajeField = new JTextField(10);
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

        
        // ...////////////////////////////////////
        // Botón para tomar una foto de la cámara y guardarla
        JButton capturaButton = new JButton("Tomar Foto de la Cámara");
        capturaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tomarFoto(); // Llama a la función para tomar la foto de la cámara
            }
        });
        GridBagConstraints capturaConstraints = new GridBagConstraints();
        capturaConstraints.gridx = 0; // Coloca el botón en la primera columna
        capturaConstraints.gridy = 3; // Ajusta la fila según sea necesario
        panel.add(capturaButton, capturaConstraints);
        // .../////////////////////////

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
        enviarConstraints.gridx = 9; // Coloca el botón en la segunda columna
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
                            System.out.print(ObjetoMensaje.getResultado());
                            SwingUtilities.invokeLater(() -> {
                                viewCSVFile("C:\\Users\\HP\\Documents\\ProyectoBueno\\lib\\"+nomarchivo+".csv");
                            });
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
            File imageFile = new File("C:/Users/HP/Desktop/ImagenTeseract/Texto2.jpg");
            ITesseract instance = new Tesseract();
            instance.setDatapath("C:/Users/HP/Desktop/Tess4J/tessdata"); // Reemplaza con la ruta correcta
            expresion = instance.doOCR(imageFile);
            enviarAlcliente(expresion);   
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir detalles de la excepción para depuración
            // Manejo de la excepción aquí si es necesario
        }
    }

    public static void viewCSVFile(String filePath) {
        JFrame frame = new JFrame("CSV Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 200);

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        List<String> headers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (firstLine) {
                    for (String value : values) {
                        headers.add(value);
                    }
                    model.setColumnIdentifiers(headers.toArray());
                    firstLine = false;
                } else {
                    model.addRow(values);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.setVisible(true);
    }

    public void enviardesdeTxt(){
        String textodeltxtx=mensajeField.getText();
        enviarAlcliente(textodeltxtx);
    }

    public void enviarAlcliente(String expresion){
        try {
            System.out.println(nomarchivo);
            Mensaje mensajeCliente = new Mensaje(nomarchivo, expresion);
            String jsonMensajeCliente = gson.toJson(mensajeCliente);
            out.println(jsonMensajeCliente);
            System.out.println("Se envio  " + expresion);
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error al traducir o enviar el mensaje de la imagen.");
        }
    }
    ///////////////////////////
    private static void tomarFoto() {
        VideoCapture camera = new VideoCapture(0); // La cámara por defecto
        Mat frame = new Mat();
        camera.read(frame);

        if (!frame.empty()) {
            guardarImagen(frame, "C:/Users/HP/Desktop/ImagenTeseract/Texto2.jpg");
            System.out.println("Foto tomada y guardada como captura.jpg");
        } else {
            System.err.println("No se pudo tomar la foto.");
        }

        camera.release();
    }

    private static void guardarImagen(Mat mat, String nombreArchivo) {
        Imgcodecs.imwrite(nombreArchivo, mat);
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
