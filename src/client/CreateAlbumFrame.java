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

import com.alee.extended.list.CheckBoxListModel;
import com.alee.extended.list.WebCheckBoxList;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextField;
import dustorage.Photo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import layout.TableLayout;

/**
 * Окно для создания альбома пользователя.
 * @author seraf
 */
public class CreateAlbumFrame extends JFrame {
    
    ArrayList<Photo> photos;

    /**
     *
     */
    public CreateAlbumFrame() {
        setTitle("Создать альбом");
        setBounds(0, 0, 600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
        setLayout(new TableLayout(new double[][] {
            {TableLayout.FILL, TableLayout.FILL},
            {TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED}
        }));
        
        WebTextField albumName = new WebTextField();
        albumName.setInputPrompt("Отпуск, Прогулки в СПб, Пикник");
        
        
        WebCheckBoxList photosList = new WebCheckBoxList(model());
        WebScrollPane scroll = new WebScrollPane(photosList);
        WebLabel image = new WebLabel();
        
        WebLabel selectedLabel = new WebLabel("Выбирайте изображения");
        
        photosList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = photosList.getSelectedIndex();
                String thumbnail = photos.get(index).getThumbnail();
                image.setIcon(new ImageIcon(thumbnail));
                int checked = photosList.getCheckedValues().size();
                changeSelectedLabel(checked, selectedLabel);
            }
        });
        
        WebButton createAlbum = new WebButton("Создать альбом");
        createAlbum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                int album_id = LocalStorage.createAlbum(albumName.getText());
                
                List<Object> checked = photosList.getCheckedValues();
                for (Object x: checked){
                    for (Photo photo: photos){
                        if (x.toString().equals(photo.getFile().getName())){
                            LocalStorage.linkPhotoToAlbum(photo.getMd5(), album_id);
                        }
                    }
                }
                Overview.updateAlbumsList();
                setVisible(false); //you can't see me!
                dispose(); //Destroy the JFrame object
            }
        });
        
        add(new WebLabel("Название альбома"), "0,0");
        add(albumName, "1,0");
        add(scroll, "0,1");
        add(image, "1,1");
        add(selectedLabel, "0,2");
        add(createAlbum, "1,2");
    }
    
    private CheckBoxListModel model (){
        final CheckBoxListModel model = new CheckBoxListModel ();
        photos = LocalStorage.getPhotos();
        
        for (Photo photo: photos){
            model.addCheckBoxElement(photo.getFile().getName());
        }   
        return model;
    }
    
    private void changeSelectedLabel(int selected, WebLabel label){
        label.setText("Выбрано "+selected+" из "+photos.size());
    };
    
}
