/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import dustorage.User;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Меню программы. Панель, отображающаяся слева.
 * @author seraf
 */
public class Menu extends JPanel {
    
    static WebLabel username;
    static JLabel lastSync;
    static User user;
    static JPanel stuff;
    
    /**
     *
     */
    public Menu(){
        
        user = new User();
        setBackground(Color.decode("#84BBFF"));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        stuff = new JPanel();
        
        stuff.setLayout(new BoxLayout(stuff, BoxLayout.PAGE_AXIS));
        
        username = new WebLabel("@username");
        username.setDrawShade(true);

        if (user.getUsername() != null) {
            username.setText(user.getUsername());
        }

        username.setBackground(Color.decode("#4A79B2"));
        username.setOpaque(true);
        username.setForeground(Color.decode("#FFD384"));
        username.setShadeColor(Color.decode("#000000"));
        username.setBorder(new EmptyBorder(10, 10, 10, 10));
        username.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        stuff.add(username);

        WebButton syncButton = new WebButton("Синхронизировать");
        syncButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        syncButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SyncFrame sf = new SyncFrame();
                Ftp ftp = new Ftp();
                
                SyncThread t = new SyncThread(ftp, sf);
                t.start();
                
            }
        });

        lastSync = new JLabel("<html>Синхронизированно " + user.getLast_sync() + "</html>");
        lastSync.setBorder(new EmptyBorder(0, 10, 0, 10));

        WebButton albumsButton = new WebButton("Создать альбом");
        albumsButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        albumsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateAlbumFrame();
            }
        });

        WebButton searchButton = new WebButton("Поиск");
        searchButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        WebButton settingsButton = new WebButton("Настройки");
        settingsButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SettingsFrame();
            }
        });

        stuff.add(syncButton);
        stuff.add(lastSync);
        stuff.add(albumsButton);
        //stuff.add(searchButton);
        stuff.add(settingsButton);
        
        stuff.setVisible(false);
        
        if (user.getMethod() != 0){
            stuff.setVisible(true);
        }
        
        add(stuff);
    }
    
    /**
     * Обновляет поля пользователя: имя, дата синхронизации.
     */
    public static void updateFields(){
        User u = new User();
        if (username != null && lastSync != null){
            username.setText(u.getUsername());
            lastSync.setText("<html><p align='center'>Синхронизированно<br>"+u.getLast_sync()+"</p></html>");
            stuff.setVisible(true);
        }
    }
}
