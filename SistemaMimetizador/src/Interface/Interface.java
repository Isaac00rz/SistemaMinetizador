package Interface;

import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Interface extends JFrame implements ActionListener{
    
    private static PanamaHitek_Arduino ino;
    private JButton enviar,eliminar,anadir;
    private JTable mensajes;
    private DefaultTableModel model;
    
    public Interface (){
        // Construyendo la comunicacion al puerto serial
        ino = new PanamaHitek_Arduino();
        try {
            ino.arduinoTX("/dev/ttyUSB0", 9600); 
        } catch (ArduinoException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        model = new DefaultTableModel();
        model.addColumn("No. Mensaje");
        model.addColumn("Mensaje");
        mensajes = new JTable(model);
        JScrollPane scrollpane = new JScrollPane(mensajes);
        this.add(scrollpane,BorderLayout.CENTER);
        this.addButtons();
    }
    
    public void addButtons(){
        
        JPanel panelButtons = new JPanel();
        
        enviar = new JButton("Enviar");
        enviar.addActionListener(this);
        eliminar = new JButton("Eliminar");
        eliminar.addActionListener(this);
        anadir = new JButton("+");
        anadir.addActionListener(this);
        
        panelButtons.add(enviar);
        panelButtons.add(anadir);
        panelButtons.add(eliminar);
        this.add(panelButtons,BorderLayout.SOUTH);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
       if(e.getSource()== anadir){
           String [] datos = new String [2];
           datos[0] = "1";
           datos[1] = "Hola yo soy isaac";
           model.addRow(datos);
       }else if(e.getSource()== eliminar){
           
       }else if(e.getSource()== enviar){
           
       }
    }
    
}
