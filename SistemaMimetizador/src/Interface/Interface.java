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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
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
    public final String[][] dat = {};//Utilizado para crear el DTM
    private int contador = 0, posicion = 0; //Contador es para la posicion de la tabla, y contador2 es para el timer
    private ArrayList<String> cadenas; //Contendra todos los menajes que esten en la tabla

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
            // Conexion con puerto serial ubuntu
            //ino.arduinoRXTX("/dev/ttyUSB0", 9600, manejadorArduino); 
            // Conexion con puerto serial windows
            ino.arduinoRXTX("COM4", 9600, manejadorArduino); 
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
        dtm = new DefaultTableModel(dat, COLUMNAS);
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
    // Envia el primer dato a Arduino indicando de esta manera que puede navegar entre mensajes
    private void enviarDatos() {
        cadenas = new ArrayList();
        posicion = 0;
        int filas = mensajes.getRowCount();
        for (int i = 0; i < filas; i++) {
            cadenas.add(dtm.getValueAt(i, 1).toString());
        }
        iniciarTimer();
        JOptionPane.showMessageDialog(Interface.this, "Mensajes enviados");
    }
    //Metodo que genera Fecha y hora en cual el mensaje es emitido
    public String getFechaHora(){
        Date date;
        DateFormat hora, fecha;

        date = new Date();
        hora = new SimpleDateFormat("HH:mm:ss");
        fecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaHora ="Fecha:"+String.valueOf(fecha.format(date))+" Hr:"+ String.valueOf(hora.format(date));
        return fechaHora;
    }
    
    private void iniciarTimer() {
        int datos = cadenas.size();
        try {
            //ino.sendData(cadenas.get(contadorTimer++)+" % "+fecha+" % "+hora); //La cadena final tendra el mesnaje + % + fecha+ % hora
             ino.sendData(cadenas.get(posicion)+" "+getFechaHora());
        } catch (ArduinoException | SerialPortException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // Metodo que muestra el siguiente mensaje
    private void siguienteMensaje() {
        if (cadenas != null) {
            if((posicion+1)==cadenas.size()){
                posicion=0;
            }else{
                posicion=posicion+1;
            }
            try {
                ino.sendData(cadenas.get(posicion)+" "+getFechaHora());
            } catch (ArduinoException | SerialPortException ex) {
                Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    // Metodo que muestra el mensaje anterior
    private void anteriorMensaje() {
        System.out.println("entra anterior");
        if (cadenas != null) {
            if(posicion==0){
                posicion=cadenas.size()-1;
                System.out.println("tamano de array: "+cadenas.size());
            }else{
                posicion=posicion-1;
            }
            try {
                ino.sendData(cadenas.get(posicion)+" "+getFechaHora());
            } catch (ArduinoException | SerialPortException ex) {
                Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    // Eventos de la interfaz
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
    // Eventos al escucha de Arduino
    private class ManejadorArduino implements SerialPortEventListener {

        @Override
        public synchronized void serialEvent(SerialPortEvent spe) {
            try {
                if (ino.isMessageAvailable()) {
                    String cadenaArdu = ino.printMessage();
                    if (cadenaArdu.equals("1")) { // Si arduino manda uno, se enviara el siguiente mensaje
                        siguienteMensaje();
                    }
                    if (cadenaArdu.equals("0")) {// si arduino manda cero, se enviara el mensaje anterior
                        anteriorMensaje();
                    }
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
