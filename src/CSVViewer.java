import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVViewer extends JFrame {
    private JTable table;
    private JComboBox<String> fileComboBox;
    private File selectedFile = null;

    public CSVViewer() {
        setTitle("CSV Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        fileComboBox = new JComboBox<>();
        loadFileList(); // Cargar la lista de archivos disponibles

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Seleccionar archivo CSV: "));
        topPanel.add(fileComboBox);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Agregar ActionListener para el JComboBox
        fileComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedFileName = (String) fileComboBox.getSelectedItem();
                if (selectedFileName != null) {
                    selectedFile = new File(selectedFileName);
                    loadCSVData(selectedFile);
                }
            }
        });
    }

    private void loadFileList() {
        // Obtener la lista de archivos CSV disponibles en una carpeta específica
        File folder = new File("C:\\Users\\HP\\Documents\\Proyecto2_Photomath\\Clientes_Servidores\\lib"); // Reemplaza con la ruta de tu carpeta
        File[] files = folder.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".csv");
            }
        });

        if (files != null) {
            for (File file : files) {
                fileComboBox.addItem(file.getAbsolutePath());
            }
        }
    }

    private void loadCSVData(File file) {
        DefaultTableModel model = new DefaultTableModel();
        table.setModel(model);

        String line;
        List<String> headers = new ArrayList<>();
        boolean firstLine = true;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                if (firstLine) {
                    for (String value : values) {
                        headers.add(value); // Agregar encabezados de columna
                    }
                    model.setColumnIdentifiers(headers.toArray());
                    firstLine = false;
                } else {
                    model.addRow(values); // Agregar filas de datos
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CSVViewer viewer = new CSVViewer();
            viewer.setVisible(true);
        });
    }
}
