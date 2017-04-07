/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import layout.TableLayout;

/**
 *
 * @author seraf
 */
public class Welcome extends JPanel {
    public Welcome() {
        TitledBorder title = BorderFactory.createTitledBorder("Добро пожаловать");
        setBorder(title);
        
        double[][] d = {
            {.5,.5}, {TableLayout.FILL}
        };
        setLayout(new TableLayout(d));
        
        JPanel ftp = new JPanel();
        ftp.setLayout(new BoxLayout(ftp, BoxLayout.Y_AXIS));
        
        JLabel ftp_text = new JLabel(
                "<html><p>Программа независимо готова к использованию, если Вы "
              + "намерены хранить данные на личном FTP-сервере.</p>"
              + "<h2>Настроить на FTP</h2>"
              + "</html>");
        
        
        JTextField host = new JTextField();
        host.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        host.setText("192.168.1.96");
        
        JTextField login = new JTextField();
        login.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        login.setText("Пользователь");
        
        JPasswordField password = new JPasswordField();
        password.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        password.setText("Пароль");
        
        JPanel trio = new JPanel();
        trio.setLayout(new FlowLayout());
        
        JButton TestFtpButton = new JButton("Проверить соединение");
        JLabel TestFtpLabel = new JLabel();
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        
        TestFtpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ftp ftp = new Ftp();
                TestFtpLabel.setText("Ожидайте...");
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
                                TestFtpLabel.setText("Успешная авторизация"); break;
                            }
                        }
                        progressBar.setVisible(false);
                    }
                });
                t.start();
            }
        });
        
        trio.add(new JButton("Connect"));
        trio.add(TestFtpButton);
        trio.add(progressBar);
        trio.add(TestFtpLabel);
        
        ftp.add(ftp_text);
        ftp.add(host);
        ftp.add(login);
        ftp.add(password);
        ftp.add(trio);
        
        
        
        add(ftp, "0,0");
    }
}
