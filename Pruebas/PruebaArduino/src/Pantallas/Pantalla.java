/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pantallas;

import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 *
 * @author Grapha
 */
public class Pantalla extends JFrame {

    private JPanel panel;
    private JButton encendido, apagado;
    private ManejadorClic clic;
    private static PanamaHitek_Arduino ino;
    private static final SerialPortEventListener listener = new SerialPortEventListener() {
        @Override
        public void serialEvent(SerialPortEvent spe) {
            try {
                if (ino.isMessageAvailable()) {
                    //Se imprime el mensaje recibido en la consola
                    System.out.println(ino.printMessage());
                }
            } catch (SerialPortException | ArduinoException ex) {
                Logger.getLogger(Pantalla.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    };

    public Pantalla() {
        super("Enciende y apaga");
        this.setSize(350, 100);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clic = new ManejadorClic();
    }

    public void contruir() {
        panel = new JPanel();
        encendido = new JButton("Encender");
        encendido.setCursor(new Cursor(Cursor.HAND_CURSOR));
        encendido.addActionListener(clic);
        apagado = new JButton("Apagar");
        apagado.setCursor(new Cursor(Cursor.HAND_CURSOR));
        apagado.addActionListener(clic);

        this.add(panel);
        panel.add(encendido);
        panel.add(apagado);
        preparar();
        this.setVisible(true);
    }

    private void preparar() {
        ino = new PanamaHitek_Arduino();
        try {
            ino.arduinoRXTX("COM5", 9600,listener); //     Mueve el puerto aqui
            //ino.arduinoRX("COM5", 9600, listener);
        } catch (ArduinoException ex) {
            Logger.getLogger(Pantalla.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class ManejadorClic implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == encendido) {
                try{
                    ino.sendData("1");
                    
                }catch(ArduinoException | SerialPortException e){
                    System.out.println(e);
                }
            }
            if (ae.getSource() == apagado) {
                try{
                    ino.sendData("0");
                }catch(ArduinoException | SerialPortException e){
                    System.out.println(e);
                    
                }
            }
        }

    }
}
