/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import layout.TableLayout;

/**
 * Welcome страница запускается при старте, если пользователь запускает первый
 * раз. После установки, создается файл с настройками пользователя.
 * @author seraf
 */
public class Welcome extends JPanel {

    /**
     *
     */
    public Welcome() {
        TitledBorder title = BorderFactory.createTitledBorder("Добро пожаловать");
        setBorder(title);
        
        double[][] d = {
            {.5,.5}, {TableLayout.FILL}
        };
        setLayout(new TableLayout(d));
        
        add(new WelcomeFtpPanel(), "0,0");
        add(new WelcomeDustoragePanel(), "1,0");
    }
}
