package Interface;

import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Interface extends JFrame {

    private PanamaHitek_Arduino ino;
    private JButton enviar, eliminar, anadir;
    private JTextArea mensaje;
    private JTable mensajes;
    private JPanel centro, gruBoTabla, gruBoEnviar, sur;
    private DefaultTableModel dtm;
    public final Cursor CURSOR = new Cursor(Cursor.HAND_CURSOR);
    private ManejadorAction manejadorBotones;
    private ManejadorArduino manejadorArduino;
    private AdaptadorMouse manejadorMouse;
    public final String[] COLUMNAS = {"No. Mensaje", "Mensaje"};//Utilizado para crear el DTM
    public final String[][] datos = {};//Utilizado para crear el DTM
    private int contador = 0, contadorTimer = 0; //Contador es para la posicion de la tabla, y contador2 es para el timer
    private ArrayList<String> cadenas; //Contendra todos los menajes que esten en la tabla
    private Timer timer; //Objeto usado para mandar mensajes cada cierto tiempo a arduino
 
    public Interface() {
        manejadorBotones = new ManejadorAction();
        manejadorArduino = new ManejadorArduino();
        manejadorMouse = new AdaptadorMouse();
        paneles();
        buttons();
        campos();
        tablas();
        comArduino();
        agregar();
    }

    private void comArduino() {
        // Construyendo la comunicacion al puerto serial
        ino = new PanamaHitek_Arduino();
        try {
            ino.arduinoRXTX("/dev/ttyUSB0", 9600, manejadorArduino);
        } catch (ArduinoException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void paneles() {
        centro = new JPanel();
        centro.setLayout(new BorderLayout());
        gruBoTabla = new JPanel();
        gruBoEnviar = new JPanel();
        sur = new JPanel();
        sur.setLayout(new BorderLayout());
    }

    private void tablas() {
        dtm = new DefaultTableModel(datos, COLUMNAS);
        mensajes = new JTable(dtm);
        mensajes.setFont(new Font("Verdana", Font.PLAIN, 15));
        mensajes.setPreferredScrollableViewportSize(new Dimension(400, 300));
        mensajes.setAutoCreateRowSorter(true);
        mensajes.addMouseListener(manejadorMouse);
    }

    private void buttons() {
        enviar = contruitBoton();
        enviar.setText("Enviar");
        eliminar = contruitBoton();
        eliminar.setText("Eliminar");
        anadir = contruitBoton();
        anadir.setText("Agregar");
    }

    private void campos() {
        mensaje = new JTextArea();
        mensaje.setFont(new Font("Vernanda", Font.BOLD, 20));
    }

    private void agregar() {
        JScrollPane scrollTabla, scrollTextos;
        scrollTabla = new JScrollPane(mensajes);
        scrollTextos = new JScrollPane(mensaje);

        gruBoTabla.add(anadir);
        gruBoTabla.add(eliminar);
        gruBoEnviar.add(enviar);

        centro.add(scrollTextos, BorderLayout.CENTER);
        centro.add(gruBoTabla, BorderLayout.SOUTH);
        sur.add(scrollTabla, BorderLayout.CENTER);
        sur.add(gruBoEnviar, BorderLayout.SOUTH);

        add(centro, BorderLayout.CENTER);
        add(sur, BorderLayout.SOUTH);

    }

    private JButton contruitBoton() {
        JButton boton = new JButton();
        boton.addActionListener(manejadorBotones);
        boton.setCursor(CURSOR);
        return boton;
    }

    private void enviarDatos() {
        cadenas = new ArrayList();
        int filas = mensajes.getRowCount();
        for (int i = 0; i < filas; i++) {
            cadenas.add(dtm.getValueAt(i, 1).toString());
        }

        //Prepar timer
        if (timer != null) {
            timer.stop();
        }
        timer = new Timer(10000, new ActionListener() {// los mensajes se enviaran cada 10 segungos
            public void actionPerformed(ActionEvent e) {
                int datos = cadenas.size();
                if (contadorTimer < datos) {
                    System.out.println(cadenas.get(contadorTimer));
                    try {
                        ino.sendData(cadenas.get(contadorTimer++));
                        
                    } catch (ArduinoException | SerialPortException ex) {
                        Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    contadorTimer = 0;
                    System.out.println(cadenas.get(contadorTimer));
                    try {
                        ino.sendData(cadenas.get(contadorTimer++));
                        
                    } catch (ArduinoException | SerialPortException ex) {
                        Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        JOptionPane.showMessageDialog(Interface.this, "Mensajes enviados");
        timer.start();
    }

    private class ManejadorAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int fila = 0;
            int noFilas = 0;
            if (e.getSource() == anadir) {
                String[] datos = new String[2];
                datos[0] = String.valueOf(contador++);
                datos[1] = mensaje.getText();
                dtm.addRow(datos);
            }
            if (e.getSource() == eliminar) {
                fila = mensajes.getSelectedRow();
                dtm.removeRow(fila);
                contador--;
                noFilas = dtm.getRowCount();
                for (int i = 0; i < noFilas; i++) {
                    mensajes.setValueAt(i, i, 0);
                }

            }
            if (e.getSource() == enviar) {
                enviarDatos();
            }
        }

    }

    private class ManejadorArduino implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent spe) {
            try {
                if (ino.isMessageAvailable()) {
                    //Se imprime el mensaje recibido en la consola
                    System.out.println(ino.printMessage());
                }
            } catch (SerialPortException | ArduinoException ex) {
                Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private class AdaptadorMouse extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent me) {
            int fila = 0;
            String cadena = "";
            if (me.getSource() == mensajes) {
                fila = mensajes.getSelectedRow();
                cadena = mensajes.getValueAt(fila, 1).toString();
                mensaje.setText(cadena);
            }
        }
    }
}
