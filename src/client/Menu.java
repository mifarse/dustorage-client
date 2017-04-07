/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author seraf
 */
public class Menu extends JPanel {
    public Menu(){
        setBackground(Color.decode("#84BBFF"));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        JLabel username = new JLabel("@username");
        username.setBackground(Color.decode("#4A79B2"));
        username.setOpaque(true);
        username.setForeground(Color.decode("#FFD384"));
        username.setBorder(new EmptyBorder(10,10,10,10));
        username.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        add(username);
        
        JButton syncButton = new JButton("Синхронизировать");
        syncButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        JLabel lastSync = new JLabel("Последняя синхронизация выполнена ***");
        lastSync.setBorder(new EmptyBorder(0,10,0,10));
        
        JButton albumsButton = new JButton("Альбомы");
        albumsButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        JButton searchButton = new JButton("Поиск");
        searchButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        add(syncButton);
        add(lastSync);
        add(albumsButton);
        add(searchButton);
        
    }
}
