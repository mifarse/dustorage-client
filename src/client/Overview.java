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
import com.alee.laf.progressbar.WebProgressBar;
import com.alee.laf.toolbar.WebToolBar;
import dustorage.Album;
import dustorage.Photo;
import dustorage.User;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import layout.TableLayout;

/**
 * Ключевой класс, который контроллирует отображение фотографий.
 * @author seraf
 */
public class Overview extends JPanel {
    
    /**
     * Прогресс бар в верхнем баре (над фото)
     */
    private static WebProgressBar processing;
    /**
     * Фотопоток. Множество фотографий расположенных в виде сетки.
     */
    private static PhotoRoll pr;
    /**
     * Альбомы
     */
    private static ArrayList<Album> albums;
    /**
     * комбобокс для выбора альбома
     */
    private static JComboBox chooseAlbum;

    /**
     * Конструктор панели для страницы Overview.
     * На этой странице представлены миниатюры изображений, имеющиеся в хранили-
     * ще.
     */    
    public Overview() {
        setLayout(new TableLayout(new double[][]{
            {TableLayout.FILL},
            {TableLayout.PREFERRED,
            TableLayout.PREFERRED}
        }));
        
        WebToolBar bar = new WebToolBar();
        bar.setFloatable(false);
        
        String[] items = { "3", "4", "6" };
        JComboBox comboBox = new JComboBox( items );
        
        bar.add(new JLabel("Количество фото в ряду: "));
        bar.add(comboBox);
        
        
        processing = new WebProgressBar();
        processing.setStringPainted(true);
        processing.setIndeterminate(true);
        processing.setString("Сортировка фото");
        processing.setVisible(false);
        bar.add(processing);
        
        bar.add(new JLabel("Альбом:"));
        
        chooseAlbum = new JComboBox();
        updateAlbumsList();
        
        bar.add(chooseAlbum);
        
        pr = new PhotoRoll();
        
        if (new User().getUsername() != null) startIndexing();
        
        JScrollPane scroll = new JScrollPane(pr);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setPreferredSize(new Dimension(768, 535));
        scroll.setBorder(BorderFactory.createEmptyBorder());
        
        add(new GroupPanel(bar), "0,0");
        add(scroll, "0,1");
        
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = Integer.parseInt((String) comboBox.getSelectedItem());
                pr.render(i);
            }
        });
        
        chooseAlbum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                if (chooseAlbum.getSelectedIndex() == 0) {
                    pr.update();
                } else {
                    String selected = (String) chooseAlbum.getSelectedItem();
                    for (Album album: albums){
                        if (album.getName().equals(selected)) {
                            pr.update(album.getId());
                        }
                    }
                }
            }
        });
    }
    
    /**
     * Запуск индексирования фотографий. Порядок действий:
     * 1) Вытащить рекурсивно фото из всех папок в корневую папку хранилища
     * 2) Сохранить все изображения в базу данных
     * 3) Определить удаленные фото, удалить из из базы.
     */
    public static void startIndexing(){
        processing.setVisible(true);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                File path = new File(FileManager.getStorageDir());
                FileManager.orderFiles();
                processing.setIndeterminate(false);
                processing.setValue(0);
                processing.setString(null);
                File[] photos = path.listFiles();
                
                FileManager.savePhotos(photos, processing);
                
                processing.setIndeterminate(true);
                processing.setString("Очистка базы данных");
                
                ArrayList<Photo> sqlphotos = LocalStorage.getPhotos();
                for (Photo photo: sqlphotos){
                    if (!photo.getFile().exists()){
                        LocalStorage.deletePhoto(photo.getMd5());
                    }
                }

                processing.setVisible(false);
                pr.update();
            }
        });
        t.start();
    }

    /**
     * Получить объект PhotoRoll
     * @return PhotoRoll
     */
    public static PhotoRoll getPr() {
        return pr;
    }
    
    /**
     * Обновляет список альбомов в памяти программы и в списке в верхней панели
     */
    public static void updateAlbumsList(){
        albums = LocalStorage.getAlbums();
        chooseAlbum.removeAllItems();
        chooseAlbum.addItem("Общий");
        
        if (albums != null && albums.size() > 0){
            for (int i = 0; i < albums.size(); i++) {
                chooseAlbum.addItem(albums.get(i).getName());
            }
        }
    }
    
}
