/*
 * The MIT License
 *
 * Copyright 2017 seraf.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package client;

import dustorage.User;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import layout.TableLayout;

/**
 * Окно для настроек. Используется для редактирования данных о пользователе.
 * @author seraf
 */
public class SettingsFrame extends JDialog {

    private JPanel cards = new JPanel(new CardLayout());
    
    /**
     * При вызове отрисовывает окно.
     */
    public SettingsFrame() {
        setTitle("настройки");
        
        setBounds(0, 0, 300, 400);
        setLocationRelativeTo(null);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new TableLayout(new double[][]{
            {TableLayout.FILL},
            {TableLayout.PREFERRED, TableLayout.PREFERRED,
             TableLayout.PREFERRED}
        }));
        
        User user = new User();
        String[] methods = {"dustorage", "FTP"};
        JComboBox pickMethod = new JComboBox(methods);
        pickMethod.setSelectedIndex(user.getMethod()-1);
        
        JPanel method1 = new WelcomeDustoragePanel();
        JPanel method2 = new WelcomeFtpPanel();
        
        TitledBorder title = BorderFactory.createTitledBorder("Настройка метода");
        method1.setBorder(title); method2.setBorder(title);
        
        cards.add(method1, "1"); cards.add(method2, "2");
        
        switchMethodPanels(user.getMethod()+"");
        
        pickMethod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (pickMethod.getSelectedIndex()){
                    case 0:{
                        switchMethodPanels("1");
                        break;
                    }
                    case 1: {
                        switchMethodPanels("2");
                        break;
                    }
                }
                System.out.println("ACTION");
            }
        });
        String s = "<html>Пользователь: "+user.getUsername()+"<br>"
                 + "Объем хранилища: "+(float) LocalStorage.getStorageSize()/(1024*1024)+" Мб</html>";
        add(new JLabel(s), "0,0");
        add(pickMethod, "0,1");
        add(cards, "0,2");
        setVisible(true);
    }

    /**
     * Меняет панели местами для экономии пространства.
     * @param method
     */
    public void switchMethodPanels(String method){
        CardLayout cardLayout = (CardLayout) cards.getLayout();
        cardLayout.show(cards, method);
    }
}
