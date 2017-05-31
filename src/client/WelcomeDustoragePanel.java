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
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;
import com.alibaba.fastjson.JSONObject;
import dustorage.User;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import layout.TableLayout;



/**
 * Панель для подключения по Dustorage протоколу. Содержит в себе компоненты
 * для подключения и сохранения параметров.
 * @author seraf
 */
public class WelcomeDustoragePanel extends JPanel {

    Dustorage dustorage;
    
    /**
     *
     */
    public WelcomeDustoragePanel() {
        setLayout(new TableLayout(new double[][]{
            {TableLayout.FILL},
            {TableLayout.PREFERRED,
             TableLayout.PREFERRED,
             TableLayout.PREFERRED,
             TableLayout.PREFERRED,
             TableLayout.PREFERRED,
            TableLayout.PREFERRED}
        }));
        
        JLabel t = new JLabel("<html>Вы так же можете подключиться к серверу dustorage."+
                " Тогда для Вас станет доступным расширенный функционал по "+
                "работе с фото (публичный доступ, комментирование и др.)"+
                "<h2>Сервер DUStorage</h2>"+
                "</html>");
        
        WebTextField host = new WebTextField();
        host.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        host.setInputPrompt("адрес сервера");
        host.setToolTipText("адрес сервера");
        
        WebTextField email = new WebTextField();
        email.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        email.setInputPrompt("email");
        email.setToolTipText("email");
        
        WebPasswordField pwd = new WebPasswordField();
        pwd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        pwd.setInputPrompt("пароль");
        pwd.setToolTipText("пароль");
        
        WebTextField username = new WebTextField();
        username.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        username.setInputPrompt("Имя пользователя*");
        username.setToolTipText("При авторизации можно оставить пустым");
        
        JPanel submit = new JPanel(new WrapLayout());
        
        WebButton doAuthButton = new WebButton("Авторизоваться и сохранить");
        
        dustorage = new Dustorage();
        
        doAuthButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dustorage.setHost(host.getText());
                JSONObject data = dustorage.auth(email.getText(), pwd.getText());
                String status = (String) data.get("status");
                if ("ok".equals(status)) {
                    JSONObject u = data.getJSONObject("data");
                    
                    User user = new User();
                    user.setEmail(email.getText());
                    user.setPassword(pwd.getText());
                    user.setUsername(u.getString("name"));
                    user.setApi_token(u.getString("api_token"));
                    user.setMethod(1);
                    user.setHost(host.getText());
                    
                    user.commit();
                    
                    Gui.switchDisplay("overview");
                    Overview.startIndexing();
                    Menu.updateFields();
                    
                }
            }
        });
        
        WebButton regButton = new WebButton("Регистрация");
        
        regButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dustorage.setHost(host.getText());
                dustorage.regiter(username.getText(), email.getText(), pwd.getText());
            }
        });
        
        submit.add(doAuthButton);
        submit.add(regButton);
        
        add(t, "0,0");
        add(host, "0,1");
        add(email, "0,2");
        add(pwd, "0,3");
        add(username, "0,4");
        add(submit, "0,5");
    }
}
