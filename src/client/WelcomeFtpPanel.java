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


import com.alee.laf.button.WebButton;
import com.alee.laf.progressbar.WebProgressBar;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;
import dustorage.User;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import layout.TableLayout;

/**
 * Панель для настройки подключения на FTP. Содержит в себе все необходимые поля
 * для проверки и установки параметров подключения.
 * @author seraf
 */
public class WelcomeFtpPanel extends JPanel {

    /**
     *
     */
    public WelcomeFtpPanel(){
        setLayout(new TableLayout(new double[][]{
            {TableLayout.FILL}, 
            {TableLayout.PREFERRED,
             TableLayout.PREFERRED,
             TableLayout.PREFERRED,
             TableLayout.PREFERRED,
             64}}));
        
        JLabel ftp_text = new JLabel(
                "<html><p>Программа независимо готова к использованию, если Вы "
              + "намерены хранить данные на личном FTP-сервере.<br/><br/></p>"
              + "<h2>Настроить на FTP</h2>"
              + "</html>");
        
        
        WebTextField host = new WebTextField();
        host.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        host.setInputPrompt("192.168.1.96");
        host.setToolTipText("адрес для подключения");
        
        WebTextField login = new WebTextField();
        login.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        login.setInputPrompt("логин");
        login.setToolTipText("имя пользователя");
        
        WebPasswordField password = new WebPasswordField();
        password.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        password.setInputPrompt("Пароль");
        password.setToolTipText("пароль");
        
        JPanel trio = new JPanel();
        
        WebButton TestFtpButton = new WebButton("Проверить соединение");
        
//        ImageIcon icon3 = new ImageIcon("save.png");
        WebButton SaveFtpButton = new WebButton("Сохранить");
        SaveFtpButton.setToolTipText("Сохранить настройки подключения и использовать FTP метод как основной");
        TestFtpButton.setToolTipText("Совершить попытку авторизации");
        JLabel TestFtpLabel = new JLabel();
        WebProgressBar progressBar = new WebProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setMaximumWidth(125);
        progressBar.setString("Подключение");
        progressBar.setVisible(false);
        
        
        TestFtpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ftp ftp = new Ftp();
                TestFtpLabel.setText("");
                progressBar.setVisible(true);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int s = ftp.testConnection(host.getText(), login.getText(), password.getText());
                        switch (s){
                            case 0: {
                                TestFtpLabel.setText("Сервер недоступен"); break;
                            }
                            case 530: {
                                TestFtpLabel.setText("Неверный логин/пароль"); break;
                            }
                            case 230: {
                                TestFtpLabel.setText("Успешно"); break;
                            }
                        }
                        progressBar.setVisible(false);
                    }
                });
                t.start();
            }
        });
        
        SaveFtpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User user = new User();
                user.setMethod(2);
                user.setHost(host.getText());
                user.setUsername(login.getText());
                user.setPassword(password.getText());
                user.commit();
                Menu.updateFields();
                Gui.switchDisplay("overview");
                Overview.startIndexing();
            }
        });
        
        trio.add(SaveFtpButton);
        trio.add(TestFtpButton);
        trio.add(progressBar);
        trio.add(TestFtpLabel);
        
        add(ftp_text,"0,0");
        add(host, "0,1");
        add(login, "0,2");
        add(password, "0,3");
        add(trio, "0,4");
    }
}
