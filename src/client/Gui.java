package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 *
 * @author seraf
 */
public class Gui {
    public static void main(String[] args) {
        
        JFrame frame = new JFrame("Окошко");
        frame.setVisible(true);
        frame.setSize(500,600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JButton jButton = new JButton("Обработать фоточки");
        jButton.setSize(300,20);
        frame.add(jButton);
       
        JButton uploadButton = new JButton("Загрузить фотки");
        uploadButton.setSize(300,20);
        frame.add(uploadButton);
        
        
        
        uploadButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Ftp ftpManager = new Ftp();
                ftpManager.connect("192.168.1.96", "pi", "myraspbianromance");
                ftpManager.t();
            }
            
        });
        
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileManager.orderFiles();
                jButton.setText("Done! Storage Size: "+
                        FileManager.getOriginalsSize()+" Mb");
            }
        });
        
        frame.setVisible(true);
    }
}
