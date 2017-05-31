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

import com.alee.extended.panel.GroupPanel;
import com.alee.laf.panel.WebPanel;
import dustorage.Photo;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import layout.TableLayout;

/**
 * Объект с карточками фотографий.
 * @author seraf
 */
public class PhotoRoll extends JPanel {
    
    /**
     * Содержит в себе список из компонентов JLabel. Каждый компонент - одна
     * карточка с фото.
     */
    ArrayList<JLabel> photosList = new ArrayList<>();
    /**
     * Содержит в себе набор иконок - изображений из хранилища
     */
    ArrayList<ImageIcon> iconsList = new ArrayList<>();
    JLabel image;
    /**
     * Путь до изображений миниатюр фотографий.
     */
    private static File directory = new File(System.getProperty("user.home")+"/.dustorage/thumbnails");
        
    /**
     * Перестраивает отображение карточек
     * @param cols количество изображений в ряду
     */
    public void render(int cols){
        
        int width;
        
        switch (cols){
            case 6: {
                width = 128;
                break;
            }
            case 4: {
                width = 168;
                break;
            }
            case 3: {
                width = 228;
                break;
            }
            default: {
                width = 650/cols;
            }
        }
        
        File[] photoFiles = directory.listFiles();
        for(int i = 0; i < iconsList.size(); i++){
            ImageIcon image = new ImageIcon(iconsList.get(i).getImage().getScaledInstance(width, width, Image.SCALE_DEFAULT));
            photosList.get(i).setIcon(image);
        }
    }
    
    /**
     * Обновляет изображения на основе новых миниатюр и актуального состояния БД.
     */
    public void update() {
        photosList.clear();
        iconsList.clear();
        ArrayList<Photo> photos = LocalStorage.getPhotos();
        if (photos.isEmpty()){
            add(new JLabel("Пусто."));
        } else {
            setLayout(new WrapLayout());
            removeAll();
            for (Photo photo : photos) {
                WebPanel card = new WebPanel();
                card.setMargin(3);
                card.setLayout(new TableLayout(new double[][]{
                    {TableLayout.PREFERRED}, {TableLayout.PREFERRED, TableLayout.PREFERRED}
                }));
                card.setUndecorated(false);
                ImageIcon icon = new ImageIcon(photo.getThumbnail());
                image = new JLabel(icon);
                card.add(image, "0,0");
                
                String s = photo.getSyncStatus() == 0 ? "не сихр." : "синхр.";
                
                JLabel syncStatus = new JLabel(s);
                
                JButton moreButton = new JButton("инфо");
                card.add(new GroupPanel(syncStatus, moreButton), "0,1");
                photosList.add(image);
                iconsList.add(icon);
                add(card);
                
                moreButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PhotoPanel.setPhoto(photo);
                        Gui.switchDisplay("photo");
                    }
                });
                
            }
            photos.clear();
            updateUI();
        }
    }
    
    /**
     * Перегрузка метода, которая обновляет фотопоток для отображения фото из альбома
     * @param album_id альбом для отображения
     */
    public void update(int album_id) {
        photosList.clear();
        iconsList.clear();
        ArrayList<Photo> photos = LocalStorage.getPhotos(album_id);
        if (photos.isEmpty()){
            add(new JLabel("Пусто."));
        } else {
            setLayout(new WrapLayout());
            removeAll();
            for (Photo photo : photos) {
                WebPanel card = new WebPanel();
                card.setMargin(3);
                card.setLayout(new TableLayout(new double[][]{
                    {TableLayout.PREFERRED}, {TableLayout.PREFERRED, TableLayout.PREFERRED}
                }));
                card.setUndecorated(false);
                ImageIcon icon = new ImageIcon(photo.getThumbnail());
                image = new JLabel(icon);
                card.add(image, "0,0");
                
                String s = photo.getSyncStatus() == 0 ? "не сихр." : "синхр.";
                
                JLabel syncStatus = new JLabel(s);
                
                JButton moreButton = new JButton("инфо");
                card.add(new GroupPanel(syncStatus, moreButton), "0,1");
                photosList.add(image);
                iconsList.add(icon);
                add(card);
                
                moreButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PhotoPanel.setPhoto(photo);
                        Gui.switchDisplay("photo");
                    }
                });
                
            }
            photos.clear();
            updateUI();
        }
    }
    
}
