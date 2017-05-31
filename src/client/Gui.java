package client;

import com.alee.laf.WebLookAndFeel;
import dustorage.User;
import java.awt.CardLayout;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;
import layout.TableLayout;

/**
 * Ключевой класс, инициирует запуск графической оболочки и подключение всех 
 * остальных модулей.
 * @author seraf
 */
public class Gui {
    
    private static JFrame frame;
    private static final JPanel cards = new JPanel(new CardLayout());
    
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        
        new File(System.getProperty("user.home")+"/.dustorage/thumbnails").mkdirs();
        new File(System.getProperty("user.home")+"/dustorage").mkdirs();
        
        WebLookAndFeel.install();
        
        frame = new JFrame("Фотохранилище dustorage");
        
        
        frame.setBounds(0, 0, 1024, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        double[][] size = {
            {0.75, 0.25},
            {TableLayout.FILL}
        };
        frame.setLayout(new TableLayout(size));
        frame.setResizable(false);
        
        LocalStorage.init();
        User user = new User();
        
        cards.add(new Welcome(), "welcome");
        cards.add(new Overview(), "overview");
        cards.add(new PhotoPanel(), "photo");
        frame.add(cards, "0,0");
        
        if (user.getMethod() == 0){
            switchDisplay("welcome");
        } else {
            switchDisplay("overview");
        }
        frame.add(new Menu(), "1,0");
        frame.setVisible(true);
    }
    
    /**
     * Организует переключение между экранами.
     * @param method - кодовое слово экрана
     */
    public static void switchDisplay(String method){
        CardLayout cardLayout = (CardLayout) cards.getLayout();
        cardLayout.show(cards, method);
    }
}
