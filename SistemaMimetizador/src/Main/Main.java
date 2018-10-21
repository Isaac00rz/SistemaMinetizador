
package Main;
import Interface.Interface;
import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
       Interface i = new Interface();
       i.setSize(400,500);
       i.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       i.setLocationRelativeTo(null);
       i.setVisible(true);
       i.setTitle("Sistema Mimetizador");
    }
    
}
