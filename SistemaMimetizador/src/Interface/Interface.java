package Interface;

import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class Interface extends JFrame {

    private PanamaHitek_Arduino ino;
    private JButton enviar, eliminar, anadir;
    private JTextField mensaje;
    private JTable mensajes;
    private JPanel centro,gruBoTabla,gruBoEnviar;
    private DefaultTableModel dtm;
    public final Cursor CURSOR = new Cursor(Cursor.HAND_CURSOR);
    private ManejadorAction manejadorBotones;
    private ManejadorArduino manejadorArduino;
    public final String[] COLUMNAS = {"No. Mensaje", "Mensaje"};
    public final String[][] datos = {};
    

    public Interface() {
        manejadorBotones = new ManejadorAction();
        manejadorArduino = new ManejadorArduino();
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
    
    private void paneles(){
        centro = new JPanel();
        centro.setLayout(new BorderLayout());
        gruBoTabla = new JPanel();
    }
    
    private void tablas() {
        dtm = new DefaultTableModel(datos, COLUMNAS);
        mensajes = new JTable(dtm);
        mensajes.setFont(new Font("Verdana", Font.PLAIN, 15));
        mensajes.setPreferredScrollableViewportSize(new Dimension(400, 25));
        mensajes.setAutoCreateRowSorter(true);
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
        mensaje = new JTextField();
        mensaje.setFont(new Font("Vernanda",Font.BOLD,20));
    }
    
    private void agregar(){
        gruBoTabla.add(anadir);
        gruBoTabla.add(eliminar);
        
        centro.add(mensaje,BorderLayout.CENTER);
        centro.add(gruBoTabla,BorderLayout.SOUTH);
        add(centro,BorderLayout.CENTER);
        
    }

    private JButton contruitBoton() {
        JButton boton = new JButton();
        boton.addActionListener(manejadorBotones);
        boton.setCursor(CURSOR);
        return boton;
    }

    private class ManejadorAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == anadir) {
                String[] datos = new String[2];
                datos[0] = "1";
                datos[1] = "Hola yo soy isaac";
                dtm.addRow(datos);
            }
            if (e.getSource() == eliminar) {

            }
            if (e.getSource() == enviar) {

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
}
