package client;

import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import layout.TableLayout;

/**
 *
 * @author seraf
 */
public class Gui {
    public static void main(String[] args) {
        
        JFrame frame = new JFrame("Окошко");
        frame.setVisible(true);
        frame.setBounds(100, 100, 1024, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        double[][] size = {
            {0.75, 0.25},
            {TableLayout.FILL}
        };
        frame.setLayout(new TableLayout(size));
        frame.setResizable(false);
        
        frame.add(new Welcome(), "0,0");
        frame.add(new Menu(), "1,0");
        frame.setVisible(true);
    }
}
