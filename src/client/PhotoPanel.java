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

import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import dustorage.Photo;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import layout.TableLayout;

/**
 * Класс для страницы фотографии. Создается в единичном экземпляре. В шаблон
 * передаются данные для отображения запрошенной фото.
 * @author seraf
 */
public class PhotoPanel extends JPanel {
        
    static JLabel img;
    static JLabel info;
    static JButton other;
    static File file;
    
    PhotoPanel() {
        setLayout(new TableLayout(new double[][]{
            {TableLayout.FILL},
            {TableLayout.PREFERRED, TableLayout.PREFERRED}
        }));
        
        JPanel p = new JPanel();
        
        JButton back = new JButton("Назад к галерее");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gui.switchDisplay("overview");
            }
        });
                
        other = new JButton("Открыть иначе");
        other.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(file);
                } catch (IOException ex) {
                    Logger.getLogger(PhotoPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        p.add(back); p.add(other);
        
        
        add(p, "0,0");
        WebPanel card = new WebPanel();
        card.setMargin(3);
        card.setLayout(new TableLayout(new double[][]{
            {TableLayout.PREFERRED}, {TableLayout.PREFERRED, TableLayout.PREFERRED}
        }));
        card.setUndecorated(false);
        
        img = new JLabel();
        card.add(img, "0,0");
        info = new JLabel();
        card.add(info, "0,1");
        
        JScrollPane scroll = new JScrollPane(card);
//        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setPreferredSize(new Dimension(768, 535));
        scroll.setBorder(BorderFactory.createEmptyBorder());
        
        
        add(scroll, "0,1");
    }
    
    /**
     * Позволяет фото на шаблон
     * @param photo фотография и ее данные для установки 
     */
    static void setPhoto(Photo photo){
        try {
            BufferedImage kek = ImageIO.read(new File(photo.getOriginal()));
            int w = kek.getWidth();
            int h = kek.getHeight();
            float coef = (float) w/h;
            if (h>=w) {
                h = 500;
                w = Math.round(h*coef);
            } else {
                w = 720;
                h = Math.round(w/coef);
            }
            img.setIcon(new ImageIcon(kek.getScaledInstance(w, h, Image.SCALE_SMOOTH)));
            String syncStatus = photo.getSyncStatus() == 0 ? "<b>нет</b>" : "<b>да</b>";
            String s = "<html><br><b>"+photo.getOriginal()+"</b><br>"
                    + "Размер: "+((double)photo.getSize()/(1024))+" Кб<br>"
                    + "Синхронизировано: "+syncStatus+"</html>";
            info.setText(s);
            
            file = photo.getFile();
            
        } catch (IOException ex) {
            Logger.getLogger(PhotoPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
